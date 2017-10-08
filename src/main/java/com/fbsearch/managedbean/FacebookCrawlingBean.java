package com.fbsearch.managedbean;

import com.fbsearch.domains.Comments;
import com.fbsearch.domains.Data;
import com.fbsearch.domains.FBComment;
import com.fbsearch.domains.FBFirstLevel;
import com.fbsearch.domains.FBGroup;
import com.fbsearch.domains.FBPost;
import com.fbsearch.domains.FBSecondLevel;
import com.fbsearch.domains.Feed;
import com.fbsearch.domains.Likes;
import com.fbsearch.utils.GSonBinding;
import com.fbsearch.utils.ReadFromAnExcelFile;
import com.fbsearch.utils.ThaiWordbreak;
import com.google.common.collect.Lists;
import com.ibm.icu.text.BreakIterator;
import com.stc.CompactTree;
import com.stc.STCClustering;
import com.stc.SuffixTree;
import com.stc.stemmer;
import com.stc.stopword;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
public class FacebookCrawlingBean implements Serializable {

    /*Replace your confix here*/
    private int recursiveLoop = 10; //maximun cawling is 100 activities.
    /*Ended*/
    private String accessToken;
    private String groupId;
    private Date startDate = new Date();
    private Date endDate = new Date();
    private PieChartModel pieModel1;
    private String resultDetail;
    private static final String DESTINATION_NM = "https://graph.facebook.com/v2.4/";
    private static final String GET_POST_ID_LIST_CMD = "%s?fields=id,name,description,feed.since(%s).until(%s){id}";
    private static final String GET_POST_COMMENT_LIST_CMD = "%s?fields=message,comments{message},likes.summary(true)";
    private static final String GET_GROUP_MEMBER_CMD = "%s?499083853497753/members?limit=200";
    private FBGroup fbGroup = new FBGroup("-", "-");
    private List<String> symbolList = Lists.newArrayList();
    private List<String> positiveList = Lists.newArrayList();
    private List<String> negativeList = Lists.newArrayList();
    private List<String> inactionList = Lists.newArrayList();
    private PrintStream out;
    private int totalPost;
    private int goodFeedback;
    private int badFeedback;
    private int normalfeedback;
    private String requestUrl;
    private int totalSnippet;
    private StringBuilder stcOutputbuilder;

    public void clearAll() {
        accessToken = null;
        groupId = null;
        startDate = new Date();
        endDate = new Date();
        createPieModel(0, 0, 0);
        resultDetail = "";
        fbGroup = new FBGroup("-", "-");
        totalPost = 0;
        goodFeedback = 0;
        badFeedback = 0;
        normalfeedback = 0;
        requestUrl = "";
        totalSnippet = 0;
        stcOutputbuilder = null;
    }

    @PostConstruct
    public void init() {
        createPieModel(0, 0, 0);
        try {
            generateListFromExcelFile();
            this.out = new PrintStream(System.out, true, "TIS-620");
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
            Logger.getLogger(FacebookCrawlingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void generateListFromExcelFile() throws IOException {
        ReadFromAnExcelFile readExcel = new ReadFromAnExcelFile();
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        ServletContext sc = (ServletContext) externalContext.getContext();
        String symbolFilePath = sc.getRealPath("/WEB-INF/files/symbol.xlsx");
        String keywordsFilePath = sc.getRealPath("/WEB-INF/files/keywords.xlsx");

        symbolList = readExcel.readDataFromExcelFile(symbolFilePath, "symbol");
        positiveList = readExcel.readDataFromExcelFile(keywordsFilePath, "positive");
        negativeList = readExcel.readDataFromExcelFile(keywordsFilePath, "negative");
        inactionList = readExcel.readDataFromExcelFile(keywordsFilePath, "inaction");
    }

    public void crawlingData() {
        totalPost = 0;
        requestUrl = "";
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd,hh:mm:ss").withZone(ZoneId.of("America/Los_Angeles"));
            StringBuilder fbRquestUrl = new StringBuilder();
            fbRquestUrl.append(DESTINATION_NM);
            fbRquestUrl.append(groupId);
            fbRquestUrl.append("?fields=id,name,description,feed.since(");
            fbRquestUrl.append(fmt.format(startDate.toInstant()));
            fbRquestUrl.append(").until(");
            fbRquestUrl.append(fmt.format(endDate.toInstant()));
            fbRquestUrl.append("){id}&access_token=");
            fbRquestUrl.append(accessToken);
            out.print(fbRquestUrl);
            requestUrl = fbRquestUrl.toString();
            clientServiceGet(requestUrl);

            doTextCleaning();
            doClustering();
            doSTCClustering();

        } catch (IOException | JSONException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
            Logger.getLogger(FacebookCrawlingBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void clientServiceGet(String requestUrl) throws IOException, JSONException {

        JSONObject json = readJsonFromUrl(requestUrl);
        out.println(json.toString());
        resultDetail = json.toString();
        System.out.println("query1: " + resultDetail);
        FBFirstLevel firstLevel = GSonBinding.convertToFirstLevelObject(resultDetail);
        FBGroup group = this.fbGroup;
        group.setGroupId(firstLevel.getId());
        group.setGroupName(firstLevel.getName());
        group.setGroupDesc(firstLevel.getDescription());
        Feed firstLevelFeed = firstLevel.getFeed();
        System.out.println("Number of first page feed :" + firstLevelFeed.getData().size());
        totalPost = firstLevelFeed.getData().size();

        for (int i = 1; i <= recursiveLoop; i++) {
            firstLevelFeed = recursiveFeedData(i, firstLevelFeed);
        }

        List<FBPost> postList = Lists.newArrayList();
        for (Data data : firstLevelFeed.getData()) {
            String id = data.getId();
            String updateTime = data.getUpdated_time();
            String secRequestUrl = DESTINATION_NM + id + "?fields=message,comments{message},likes.summary(true)&access_token=" + accessToken;
            json = readJsonFromUrl(secRequestUrl);
            out.println(json.toString());
            resultDetail = json.toString();
            FBSecondLevel secondLevel = GSonBinding.convertToSecondLevelObject(resultDetail);

            FBPost post = new FBPost();
            post.setPostMessage(secondLevel.getMessage());
            if (updateTime != null) {
                post.setUpdateTime(updateTime);
            }
            Likes like = secondLevel.getLikes();
            post.setTotalLike(like.getSummary().getTotal_count());
            Comments comment = secondLevel.getComments();
            if (comment != null) {
                List<Data> commentData = comment.getData();
                List<FBComment> fBCommentList = Lists.newArrayList();
                for (Data cData : commentData) {
                    FBComment fbComment = new FBComment();
                    fbComment.setCommentId(cData.getId());
                    fbComment.setCommentDetail(cData.getMessage());
                    fBCommentList.add(fbComment);
                }
                post.setCommentList(fBCommentList);
            }
            postList.add(post);
            group.setPostList(postList);
        }
        this.fbGroup = group;
    }

    private Feed recursiveFeedData(int count, Feed feed) throws IOException, JSONException {
        if (feed.getPaging() != null && feed.getPaging().getNext() != null) {
            String nextPageUrl = feed.getPaging().getNext();
            JSONObject json = readJsonFromUrl(nextPageUrl);
            Feed pagingData = GSonBinding.convertToNextPageLevelObject(json.toString());
            List<Data> pagingDataList = pagingData.getData();
            feed.getData().addAll(pagingDataList);
            System.out.println("Number of " + count + " page feed :" + feed.getData().size());
            totalPost = feed.getData().size();
        }
        return feed;
    }

    private void doTextCleaning() {
        for (FBPost post : this.fbGroup.getPostList()) {
            if (post.getPostMessage() != null) {
                String postAfterClean = textCleaning(post.getPostMessage());
                String postAfterBreak = doThaiWordBreak(postAfterClean);
                post.setPostMessage(postAfterBreak);
            }
            for (FBComment comment : post.getCommentList()) {
                if (comment.getCommentDetail() != null) {
                    String textAfterClean = textCleaning(comment.getCommentDetail());
                    String textAfterBreak = doThaiWordBreak(textAfterClean);
                    comment.setCommentDetail(textAfterBreak);
                }
            }
        }
    }

    public int getTotalLike() {
        int total = 0;
        for (FBPost post : this.getFbGroup().getPostList()) {
            total = total + post.getTotalLike();
        }
        return total;
    }

    private String doThaiWordBreak(String text) {
        String s = "wrong input!";
        Locale thaiLocale = new Locale("th");
        BreakIterator boundary = BreakIterator.getWordInstance(thaiLocale);
        boundary.setText(text);
        s = ThaiWordbreak.printEachForward(boundary, text);
        return s;
    }

    private void doClustering() {
        goodFeedback = 0;
        badFeedback = 0;
        normalfeedback = 0;
        totalSnippet = 0;
        StringBuilder snip = new StringBuilder();
        String snippet;

        for (FBPost post : this.fbGroup.getPostList()) {
            snip.append(post.getPostMessage());
            totalSnippet++;
            for (FBComment comment : post.getCommentList()) {
                snip.append(comment.getCommentDetail());
                totalSnippet++;
            }
        }
        snippet = snip.toString();
        System.out.println("total_snippets == " + totalSnippet);

        StringTokenizer stt = new StringTokenizer(snippet, "\r\n");

        String lines = "";
        while (stt.hasMoreTokens()) {
            lines = stt.nextToken();
            String words = "";
            StringTokenizer stt2 = new StringTokenizer(lines, "-");
            while (stt2.hasMoreTokens()) {
                words = stt2.nextToken();
                checkfeedback(words);
            }

        }
        createPieModel(goodFeedback + getTotalLike(), badFeedback, normalfeedback);
    }

    private void doSTCClustering() {
        stcOutputbuilder = new StringBuilder();
        StringBuilder snip = new StringBuilder();
        String snippet;
        for (FBPost post : this.fbGroup.getPostList()) {
            snip.append(post.getPostMessage());
            for (FBComment comment : post.getCommentList()) {
                snip.append(comment.getCommentDetail());
            }
        }
        snippet = snip.toString();

        List stem = new ArrayList();
        StringTokenizer stt = new StringTokenizer(snippet, "\r\n");

        String lines = "";
        String ress = "";
        while (stt.hasMoreTokens()) {
            lines = stt.nextToken();
            String words = "";
            StringTokenizer stt2 = new StringTokenizer(lines, "-");
            List stem2 = new ArrayList();
            while (stt2.hasMoreTokens()) {
                words = stt2.nextToken();
                if (words.trim().length() > 0) {
                    ress = makeStem(words);
                    if (!ress.equals("")) {
                        stem2.add(words);
                        stem2.add(ress);
                    }
                }
            }
            stem.add(stem2);
        }
        SuffixTree sf = new SuffixTree();
        int docnum = 0;
        for (int dc = 0; dc < stem.size(); dc++) {
            addTree((ArrayList) stem.get(dc), new Integer(docnum).toString(), sf);
            docnum++;
        }
        //----------------------------------------------------------------//
        CompactTree tm = new CompactTree(sf);
        tm.compact();
        //---------------------------------------------------------------//
        STCClustering s = new STCClustering(tm);
        s.clustering();
        stcOutputbuilder.append(s.getStcOutputTree()).append("<br />");
        stcOutputbuilder.append("Number of Cluster is ").append(s.getNumOfCluster()).append("<br />");
        System.out.println("---------------------num of CLUSTER = " + s.getNumOfCluster());
        int count = sf.getSize();
        stcOutputbuilder.append("Suffix size is ").append(count).append("<br />");
        System.out.println("suffix size: " + count);
    }

    public String getStcResult() {
        String result = "";
        if (stcOutputbuilder != null) {
            result = stcOutputbuilder.toString();
        }
        return result;
    }

    private String makeStem(String orsnippet) {
        String returns = "";
        List allLine = new ArrayList();
        StringTokenizer tk = new StringTokenizer(orsnippet, "\r\n");
        String u = "";
        char[] w = new char[501];
        stemmer s = new stemmer();
        try {
            while (tk.hasMoreTokens()) {
                List line = new ArrayList();
                StringReader in = new StringReader(tk.nextToken());
                while (true) {
                    int ch = in.read();
                    if (Character.isLetter((char) ch)) {
                        int j = 0;
                        while (true) {
                            ch = Character.toLowerCase((char) ch);
                            w[j] = (char) ch;
                            if (j < 500) {
                                j++;
                            }
                            ch = in.read();
                            if (!Character.isLetter((char) ch)) {
                                for (int c = 0; c < j; c++) {
                                    s.adds(w[c]);
                                }
                                s.stem();
                                {
                                    u = s.toString();
                                    line.add(u);
                                }
                                break;
                            }
                        }//while
                    }//if
                    if (ch < 0) {
                        break;
                    }

                }//while in loop
                allLine.add(line);
                in.close();
            }//while out loop
            List one;
            for (Object allLine1 : allLine) {
                one = (ArrayList) allLine1;
                if (one.size() == 1) {
                    returns = (String) one.get(0);
                } else if (one.size() > 1) {
                    StringBuilder bnm = new StringBuilder("");
                    for (Object one1 : one) {
                        bnm.append((String) one1);
                        bnm.append(",");
                    }
                    returns = bnm.toString();
                }
            }
        } catch (IOException e) {
            System.out.println("in make stem file  not found");
        }
        return returns;
    }//end method  makeStem

    private void addTree(List stem, String docc, SuffixTree sf) {
        int block = stem.size();
        int si = stem.size();
        int start = 0;
        int last = 0;
        while (start <= si - 1) {
            List bb = new ArrayList();
            last = start + block - 1;
            if (last > si - 1) {
                last = si - 1;
            }//if
            for (int y = start; y <= last; y++) {
                bb.add((String) stem.get(y));

            }//for
            sf.buildSTC(bb, docc);
            start++;

        }//while
    }// end method 

    private String textCleaning(String text) {
        if (text != null && text.trim().length() != 0) {
            text = removeSign(text);
            text = removeStopword(text);
            text = removeWordFromExcel(text);
        }
        return text;
    }

    private String removeWordFromExcel(String text) {
        if (text != null && text.trim().length() != 0) {
            for (String sym : symbolList) {
                text = text.replace(sym, "");
            }
        }
        return text;
    }

    private void checkfeedback(String text) {
        if (text != null && text.trim().length() != 0) {
            if (positiveList.contains(text)) {
                goodFeedback++;
            } else if (negativeList.contains(text)) {
                badFeedback++;
            } else if (inactionList.contains(text)) {
                normalfeedback++;
            }
        }
    }

    private String removeSign(String st) {
        String g;
        g = st;
        g = g.replace("'s", "");
        g = g.replace("'", "");
        g = g.replace("\"", "");
        g = g.replace("\n", "");
        g = g.replace("'ve", "");
        g = g.replace("'re", "");
        g = g.replace("'t", "");
        g = g.replace("&middot;", "");   //.
        g = g.replace("&amp;", "");  // &
        g = g.replace("&#39;", "");
        g = g.replace("&gt;", "");
        g = g.replace("&lt;", "");
        g = g.replace("&#x3C;", "");
        g = g.replace("&quot;", "");
        g = g.replace("&#x3E;", "");
        g = g.replace("&#x22;", "");
        g = g.replace("(", "");
        g = g.replace(")", "");
        g = g.replace("{", "");
        g = g.replace("}", "");
        g = g.replace("[", "");
        g = g.replace("]", "");
        g = g.replace("A", "");
        return g;
    }//end method removeSign

    private String removeStopword(String snp) {
        StringTokenizer stt = new StringTokenizer(snp, "<br />");
        StringBuffer sbb = new StringBuffer();
        String temp = "";
        String temp2 = "";
        while (stt.hasMoreTokens()) {
            temp = stt.nextToken();
            StringTokenizer stt2 = new StringTokenizer(temp, " \t\r\n,;:.");
            while (stt2.hasMoreTokens()) {
                temp2 = stt2.nextToken();
                if (!stopword.isStopword(temp2)) {
                    sbb.append(temp2);
                    sbb.append(" ");
                }
            }//while in
            sbb.append("\r\n");
        }//while out
        return sbb.toString();
    } // end method removeStopword

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private void createPieModel(int noOfPositive, int noOfnagative, int noOfNormal) {
        int total = noOfNormal + noOfPositive + noOfnagative;
        pieModel1 = new PieChartModel();
        int positivePercentage = (int) calculatePercentage(noOfPositive, total);
        int nagativePercentage = (int) calculatePercentage(noOfnagative, total);
        int inactivePercentage = (int) calculatePercentage(noOfNormal, total);
        pieModel1.set("LIKE : " + positivePercentage + "%", positivePercentage);
        pieModel1.set("UNLIKE : " + nagativePercentage + "%", nagativePercentage);
        pieModel1.set("INACTION: " + inactivePercentage + "%", inactivePercentage);
        pieModel1.setSeriesColors("00FF00,FF0000,FFFF00");
        pieModel1.setTitle("Facebook like chart");
//        pieModel1.setShowDataLabels(true);
        pieModel1.setLegendPosition("w");
    }

    private double calculatePercentage(double number, double total) {
        return (number / total) * 100;
    }

    public PieChartModel getPieModel1() {
        return pieModel1;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

    public FBGroup getFbGroup() {
        return fbGroup;
    }

    public void setFbGroup(FBGroup fbGroup) {
        this.fbGroup = fbGroup;
    }

    public int getTotalPost() {
        return totalPost;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public int getTotalSnippet() {
        return totalSnippet;
    }

    public void setTotalSnippet(int totalSnippet) {
        this.totalSnippet = totalSnippet;
    }
}
