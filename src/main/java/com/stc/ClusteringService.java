package com.stc;

import com.google.common.collect.Lists;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author mac
 */
public class ClusteringService {

    List<String> likeList = Lists.newArrayList();
    List<String> unlikeList = Lists.newArrayList();
    List<String> normalList = Lists.newArrayList();

    public static void main(String[] args) {
        String input = "His hand closed automatically around the fake Horcrux, but in spite of\n"
                + "everything, in spite of the dark and twisting path he saw stretching ahead for\n"
                + "himself, in spite of the final meet-ing with Voldemort he knew must come,\n"
                + "whether in a month, in a year, or in ten, he felt his heart lift at the thought\n"
                + "that there was still one last golden day of peace left to enjoy with Ron and\n"
                + "Hermione.";
        try {
            StringTokenizer firststr = new StringTokenizer(input, " ");
            String ss;
            StringBuilder snip = new StringBuilder();
            String snippet;
            int total_snippet = 0;
            while (firststr.hasMoreTokens()) {
                ss = firststr.nextToken();
                snip.append(ss);
                snip.append("\r\n");
                total_snippet++;
            }//end while            

            snippet = snip.toString();
            System.out.println("total_snippets == " + total_snippet);

            //---------stop word -----------------------//
            snippet = removeSign(snippet);
            //  snippet = removeStopword(snippet);
            //--------stemming  word----------------//
            List stem = new ArrayList();
            StringTokenizer stt = new StringTokenizer(snippet, "\r\n");

            String lines = "";
            String ress = "";
            while (stt.hasMoreTokens()) {
                lines = stt.nextToken();
                String words = "";
                StringTokenizer stt2 = new StringTokenizer(lines, " \t\r\n,;:.");
                List stem2 = new ArrayList();
                while (stt2.hasMoreTokens()) {
                    words = stt2.nextToken();
                    ress = make_Stem(words);
                    if (!ress.equals("")) {
                        stem2.add(ress);
                    }
                }//while in                                      
                stem.add(stem2);
            }//while out

            //---------------------------------------------------//
            SuffixTree sf = new SuffixTree();
            int docnum = 0;
            for (int dc = 0; dc < stem.size(); dc++) {
                checkLike(String.valueOf(stem.get(dc)));
                add((ArrayList) stem.get(dc), new Integer(docnum).toString(), sf);
                docnum++;
            }

            //----------------------------------------------------------------//
            CompactTree tm = new CompactTree(sf);
            tm.compact();
            //---------------------------------------------------------------//
            STCClustering s = new STCClustering(tm);
            s.clustering();

            System.out.println("---------------------num of CLUSTER = " + s.getNumOfCluster());
            //----------------------------------------------------------------//
            List phrase_suffix = new ArrayList();
            List doc_list = new ArrayList();
            phrase_suffix = (ArrayList) s.getLabel_Cluster();
            doc_list = (ArrayList) s.getDoc_Cluster();
            Calculation c = new Calculation();
            c.Calc_overlab(doc_list, total_snippet);
            c.Calc_coverage(doc_list, total_snippet);
            int count = sf.getSize();
            System.out.println("size: " + count);

        } catch (Exception e) {
            System.out.println("in create data = " + e);
        }

    }//end method main   

    static String removeSign(String st) {
        String g;
        g = st;
        g = g.replace("'s", " ");
        g = g.replace("'", " ");
        g = g.replace("\"", " ");
        g = g.replace("'ve", " ");
        g = g.replace("'re", " ");
        g = g.replace("'t", " ");
        g = g.replace("&middot;", " ");   //.
        g = g.replace("&amp;", " ");  // &
        g = g.replace("&#39;", " ");
        g = g.replace("&gt;", " ");
        g = g.replace("&lt;", " ");
        g = g.replace("&#x3C;", " ");
        g = g.replace("&quot;", " ");
        g = g.replace("&#x3E;", " ");
        g = g.replace("&#x22;", " ");
        g = g.replace("(", " ");
        g = g.replace(")", " ");
        g = g.replace("{", " ");
        g = g.replace("}", " ");
        g = g.replace("[", " ");
        g = g.replace("]", " ");
        g = g.replace("A", " ");
        return g;
    }//end method removeSign

    static String removeStopword(String snp) {
        StringTokenizer stt = new StringTokenizer(snp, "\r\n");
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

    static String make_Stem(String orsnippet) {
        String returns = "";
        List allLine = new ArrayList();
        StringBuffer fst = new StringBuffer("");
        StringTokenizer tk = new StringTokenizer(orsnippet, "\r\n");
        String uu = "";
        String u = "";
        char[] w = new char[501];
        stemmer s = new stemmer();
        int nkk = 1;
        try {

            while (tk.hasMoreTokens()) {
                List line = new ArrayList();
                StringReader in = new StringReader(tk.nextToken());
                StringBuffer uall = new StringBuffer("");
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
            List one = new ArrayList();
            for (int m = 0; m < allLine.size(); m++) {
                one = (ArrayList) allLine.get(m);
                if (one.size() == 1) {
                    returns = (String) one.get(0);
                } else if (one.size() > 1) {
                    StringBuffer bnm = new StringBuffer("");
                    for (int n = 0; n < one.size(); n++) {
                        bnm.append((String) one.get(n));
                        bnm.append(",");  //bnm.append(",");
                    }//for
                    returns = bnm.toString();
                }//else
            }//for out

        } catch (Exception e) {
            System.out.println("in make stem file  not found");

        }
        return returns;
    }//end method  makeStem     

    static void add(List stem, String docc, SuffixTree sf) {
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

    private static void checkLike(String word) {
        boolean found = false;
        if (word != null) {
        }
    }
}
