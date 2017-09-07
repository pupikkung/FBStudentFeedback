package com.stc;

import java.util.*;

public class STCClustering {

    private Node r;
    private float con = 0.75f;
    private List suffix, ap_doc;
    private List label_cluster, doc_cluster;
    private List have;
    private int max;
    private StringBuilder stcOutputbuilder;

    public STCClustering(Tree tree) {
        r = tree.getRoot();
        suffix = new ArrayList();
        ap_doc = new ArrayList();
        //sb = new ArrayList();
        label_cluster = new ArrayList();
        doc_cluster = new ArrayList();
        have = new ArrayList();
    }

    public Node getRoot() {
        return r;
    }

    public int getNumOfCluster() {
        return label_cluster.size();
    }

    public List getLabel_Cluster() {
        return label_cluster;
    }

    public List getDoc_Cluster() {
        return doc_cluster;
    }

    private List getDocFrequency(Node child) {
        List stack = new ArrayList();
        List link_node = new ArrayList();
        List apDoc = new ArrayList();
        boolean check = true;
        Node trav;
        trav = child;
        int linknum;
        //int num; 
        String pos;
        int visit;
        try {
            if (trav.link.size() == 0) {
                if (trav.doc.size() > 0) {
                    for (int i = 0; i < trav.doc.size(); i++) {
                        apDoc.add((String) trav.doc.get(i));
                    }
                }//
            } else if (trav.link.size() != 0) {
                visit = 1;
                //trav = child;
                // num = 1;
                stack.add(child);
                link_node.add("0");
                if (trav.doc.size() > 0) {
                    for (int i = 0; i < trav.doc.size(); i++) {
                        if (!apDoc.contains((String) trav.doc.get(i))) {
                            apDoc.add((String) trav.doc.get(i));
                        }
                    }
                }//

                trav = (Node) trav.link.get(0);
                if (trav.doc.size() > 0) {
                    for (int i = 0; i < trav.doc.size(); i++) {
                        if (!apDoc.contains((String) trav.doc.get(i))) {
                            apDoc.add((String) trav.doc.get(i));
                        }
                    }
                    if ((trav.link.size() == 0) && (child.link.size() == 1)) {
                        check = false;
                    }
                }//

                while (check == true) {
                    if (trav.link.size() > 1) {
                        if (stack.contains(trav)) {
                            pos = (String) link_node.get(link_node.size() - 1);
                            linknum = new Integer(pos).intValue();
                            if (linknum < trav.link.size() - 1) {
                                linknum++;
                                link_node.set(link_node.size() - 1, new Integer(linknum).toString());
                                trav = (Node) trav.link.get(linknum);
                                //num++;
                                //System.out.print(num+". "+trav.dat+" ");  printDoc(trav);
                                if (trav.doc.size() > 0) {
                                    for (int i = 0; i < trav.doc.size(); i++) {
                                        if (!apDoc.contains((String) trav.doc.get(i))) {
                                            apDoc.add((String) trav.doc.get(i));
                                        }
                                    }
                                }//
                            } else {
                                stack.remove(stack.size() - 1);
                                link_node.remove(link_node.size() - 1);  // ź top,low stack
                                if (stack.size() != 0) {
                                    trav = (Node) stack.get(stack.size() - 1);
                                }

                            }
                        } else {
                            stack.add(trav);
                            link_node.add("0");
                            trav = (Node) trav.link.get(0);
                            //num++;
                            //System.out.print(num+". "+trav.dat+" ");  printDoc(trav);
                            if (trav.doc.size() > 0) {
                                for (int i = 0; i < trav.doc.size(); i++) {
                                    if (!apDoc.contains((String) trav.doc.get(i))) {
                                        apDoc.add((String) trav.doc.get(i));
                                    }
                                }
                            }//
                        }
                    }//  link>1
                    else if (trav.link.size() == 1) {
                        if (visit > child.link.size()) {
                            check = false;
                        } else {
                            trav = (Node) trav.link.get(0);
                            //num++;
                            //System.out.print(num+". "+trav.dat+" ");  printDoc(trav);
                            if (trav.doc.size() > 0) {
                                for (int i = 0; i < trav.doc.size(); i++) {
                                    if (!apDoc.contains((String) trav.doc.get(i))) {
                                        apDoc.add((String) trav.doc.get(i));
                                    }
                                }
                            }//
                        }
                    }// link == 1
                    else if (trav.link.size() == 0) {
                        trav = (Node) stack.get(stack.size() - 1);
                    }// link ==0
                    /*------------------------------------------------------*/
                    if (stack.size() == 0) {
                        check = false;
                    }
                    if (trav.equals(child)) {
                        visit++;
                    }
                }// while
            }
        } catch (Exception e) {
            System.out.println("In getDocfrequency  ERROR  = ");
            e.printStackTrace();
        }
        return apDoc;
    }

    public List getDocList(List f) {
        List m = new ArrayList();
        for (int y = 0; y < f.size(); y++) {
            m.add((String) f.get(y));
        }
        return m;
    }

    private void getSuffixs() {
        //suffix.clear(); ap_doc.clear(); 
        List frq = new ArrayList();
        Node rr, trav, head;
        List stack = new ArrayList();
        List link_node = new ArrayList();
        //List suffix = new ArrayList();
        //List ap_doc = new ArrayList();
        List t_phrase = new ArrayList();
        int visit;
        int linknum;
        String pos;
        try {
            rr = getRoot();
            String phrase = "";
            boolean check, check2, check3;
            if (rr.link.size() != 0) {
                for (int i = 0; i < rr.link.size(); i++) { //rr.link.size()
                    check = true;
                    check2 = false;
                    check3 = false;
                    stack.clear();
                    link_node.clear();
                    t_phrase.clear();   // clear 
                    phrase = "";
                    head = (Node) rr.link.get(i);
                    trav = head;
                    frq.clear();
                    frq = getDocFrequency(trav);
                    if (frq.size() > 1) {
                        phrase = trav.dat;
                        //check = true;
                        if (trav.link.size() == 1) {
                            stack.add(trav);
                            link_node.add("0");
                            suffix.add(phrase);
                            ap_doc.add(getDocList(frq));
                            t_phrase.add(phrase);
                        } else if (trav.link.size() > 1) {
                            stack.add(trav);
                            link_node.add("0");
                            suffix.add(phrase);
                            ap_doc.add(getDocList(frq));
                            t_phrase.add(phrase);
                            check2 = true;
                            check3 = true;
                        } else if (trav.link.size() == 0) {
                            suffix.add(phrase);
                            ap_doc.add(getDocList(frq));
                            check = false;
                        }
                        /*.........*/
                        visit = 1;
                        while (check == true) {
                            if (trav.link.size() > 1) {
                                if (stack.contains(trav)) {
                                    pos = (String) link_node.get(link_node.size() - 1);
                                    linknum = new Integer(pos).intValue();
                                    if (linknum < trav.link.size() - 1) {
                                        if (check2 == false) {
                                            linknum++;
                                        }
                                        check2 = false;
                                        link_node.set(link_node.size() - 1, new Integer(linknum).toString());
                                        trav = (Node) trav.link.get(linknum);
                                        frq.clear();
                                        frq = getDocFrequency(trav);
                                        if (frq.size() > 1) {
                                            phrase = phrase + " " + trav.dat;
                                            suffix.add(phrase);
                                            ap_doc.add(getDocList(frq));  // create 1,2,3,
                                        } else if (frq.size() <= 1) {
                                            if (stack.size() != 0) {
                                                trav = (Node) stack.get(stack.size() - 1);
                                                phrase = (String) t_phrase.get(t_phrase.size() - 1);
                                            }
                                        }
                                    } else {//
                                        stack.remove(stack.size() - 1);
                                        link_node.remove(link_node.size() - 1);
                                        t_phrase.remove(t_phrase.size() - 1);
                                        if (stack.size() != 0) {
                                            trav = (Node) stack.get(stack.size() - 1);
                                            phrase = (String) t_phrase.get(t_phrase.size() - 1);
                                        }
                                    }
                                } else {
                                    stack.add(trav);
                                    link_node.add("0");
                                    t_phrase.add(phrase);
                                    trav = (Node) trav.link.get(0);
                                    frq.clear();
                                    frq = getDocFrequency(trav);
                                    if (frq.size() > 1) {
                                        phrase = phrase + " " + trav.dat;
                                        suffix.add(phrase);
                                        ap_doc.add(getDocList(frq));
                                    } else if (frq.size() <= 1) {
                                        trav = (Node) stack.get(stack.size() - 1);
                                        phrase = (String) t_phrase.get(t_phrase.size() - 1);
                                    }
                                }
                            }// trav link >1
                            else if (trav.link.size() == 1) {
                                if (visit > 1) {   //
                                    check = false;
                                } else {
                                    trav = (Node) trav.link.get(0);
                                    frq.clear();
                                    frq = getDocFrequency(trav);
                                    if (frq.size() > 1) {  // frq >1
                                        phrase = phrase + " " + trav.dat;
                                        suffix.add(phrase);
                                        ap_doc.add(getDocList(frq));
                                    } else if (frq.size() <= 1) { // frq<1
                                        trav = (Node) stack.get(stack.size() - 1);
                                        phrase = (String) t_phrase.get(t_phrase.size() - 1);
                                    }
                                    //num++;
                                    //System.out.print(num+". "+trav.dat+" ");  printDoc(trav);
                                }
                            }// trav link = 1
                            else if (trav.link.size() == 0) {
                                trav = (Node) stack.get(stack.size() - 1);
                                phrase = (String) t_phrase.get(t_phrase.size() - 1);
                            }// trav link = 0

                            if (stack.size() == 0) {
                                check = false;
                            }
                            if (trav.equals(head)) {
                                if (check3 == false) {
                                    visit++;
                                }
                                check3 = false;
                            }
                        }//while
                    }
                }//for

            } else {
            }
            List ssb = new ArrayList();
            ssb = getSb();
            System.out.println("----------SUFFIX-----------");
            stcOutputbuilder.append("<br />").append("----------SUFFIX-----------").append("<br />");
            for (int i = 0; i < suffix.size(); i++) {
                System.out.print(i + ". " + suffix.get(i) + "       ");
                stcOutputbuilder.append(i).append(". ").append(suffix.get(i)).append("       ");
                List pp = (ArrayList) ap_doc.get(i);
                for (int y = 0; y < pp.size(); y++) {
                    System.out.print(pp.get(y));
                    stcOutputbuilder.append(pp.get(y));
                    System.out.print(",");
                    stcOutputbuilder.append(",");
                }
                System.out.print("          s(B) = " + ssb.get(i));
                stcOutputbuilder.append("          s(B) = ").append(ssb.get(i)).append("<br />");
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("in GetSuffix... ");
            e.printStackTrace();
        }
    }//getSuffix

    public List getSuffix() {
        return suffix;
    }

    public List getAp_doc() {
        return ap_doc;
    }

    private List getSb() {
        List sb = new ArrayList();
        List suf = new ArrayList();
        List ap = new ArrayList();
        suf = getSuffix();
        ap = getAp_doc();
        int B = 0;
        int P = 0;
        int fP = 0;

        for (int ib = 0; ib < suf.size(); ib++) {
            B = ((ArrayList) ap.get(ib)).size();
            StringTokenizer h2 = new StringTokenizer((String) suf.get(ib), " ");
            List Nh2 = new ArrayList();
            while (h2.hasMoreTokens()) {
                List h22 = new ArrayList();
                h22.add(h2.nextToken().toString());
                String sw = (String) h22.get(0);
                // System.out.print(sw+" == ");
                //sw = removeStopword(sw);
                // System.out.println(ib+" "+h22+"  "+sw);
                if (sw.length() <= 2) {
                    //  System.out.println(" sw is emtry");
                } else {
                    Nh2.add(sw.toString());

                } //end of if
                h22.clear();
                // System.out.println(Nh2.toString()+"          "+Nh2.size());
                P = Nh2.size();
            }//end of while

            fP = P;
            // System.out.print(fP+" "+P+"   ==>");
            if (fP <= 1) {//single word is phrase
                fP = 0;
            } else if ((fP >= 2) && (fP <= 6)) {//phrase are two to six words
                int k = fP;
                fP = k;
            } else {//it is longer phrase
                fP = 999;
            }

            sb.add(new Integer(B * fP));
        }//for

        return sb;
    }

//    static String removeStopword(String snp) {
//        StringTokenizer stt = new StringTokenizer(snp, "\r\n");
//        StringBuffer sbb = new StringBuffer();
//        String temp = "";
//        String temp2 = "";
//        while (stt.hasMoreTokens()) {
//            temp = stt.nextToken();
//            StringTokenizer stt2 = new StringTokenizer(temp, " \t\r\n,;:.");
//            while (stt2.hasMoreTokens()) {
//                temp2 = stt2.nextToken();
//                if (!stopword.isStopword(temp2)) {
//                    sbb.append(temp2);
//                    sbb.append(" ");
//                }
//            }//while in
//            sbb.append("\r\n");
//        }//while out
//        return sbb.toString();
//    } // end method removeStopword
    private int count_Intersection(List mmm, List nnn) {
        int count = 0;
        String m, n;
        for (int i = 0; i < mmm.size(); i++) {
            m = (String) mmm.get(i);
            for (int y = 0; y < nnn.size(); y++) {
                n = (String) nnn.get(y);
                if (m.equals(n)) {
                    count++;
                }
            }
        } //for
        return count;
    } // 

    private List addSim(List mmm, int i) {
        float d1, d2;
        boolean b1, b2;
        int c;
        List nnn = new ArrayList();
        List simm = new ArrayList();
        for (int y = i + 1; y < ap_doc.size(); y++) {
            nnn = (ArrayList) ap_doc.get(y);
            d1 = (float) count_Intersection(mmm, nnn) / mmm.size();
            if (d1 >= con) {
                b1 = true;
            } else {
                b1 = false;
            }
            d2 = (float) count_Intersection(mmm, nnn) / nnn.size();
            if (d2 >= con) {
                b2 = true;
            } else {
                b2 = false;
            }
            /*----------------------------------*/
            if (b1 && b2) {
                simm.add(new Integer(y));
            }
        }
        //System.out.println(" simm= "+simm);
        return simm;
    }//

    private List getSimilarity() {
        List sim = new ArrayList();
        List mmm = new ArrayList();
        for (int i = 0; i < ap_doc.size(); i++) {
            mmm = (ArrayList) getAp_doc().get(i);
            sim.add(addSim(mmm, i));
        }//for out

        return sim;
    }

    private List union_su(List su_u, int index_su, List uni) {
        List g = new ArrayList();
        List b = new ArrayList();
        g = (ArrayList) su_u.get(index_su);
        for (int i = 0; i < g.size(); i++) {
            b.add((Integer) g.get(i));
        }
        for (int y = 0; y < uni.size(); y++) {
            int v = ((Integer) uni.get(y)).intValue();
            if (!b.contains(new Integer(v))) {
                b.add(new Integer(v));
            }
        }
        return b;
    }// union   

    private int haveInList(int h, List t, List su) {
        List g = new ArrayList();
        int pos = 0;
        boolean check = true;
        for (int i = 0; i < su.size(); i++) {
            g = (ArrayList) su.get(i);
            int k;
            if (g.contains(new Integer(h))) {
                pos = i;
                check = false;
                break;
            }
            for (int y = 0; y < t.size(); y++) {
                if (g.contains((Integer) t.get(y))) {
                    pos = i;
                    check = false;
                    break;
                }
            }//for
            if (check == false) {
                break;
            }
        }//for
        if (check == true) {
            pos = 99999;
        }
        return pos;
    }//    

    private List union_List(int h, int hill, List t, List su) {
        List unn = new ArrayList();
        List g = (ArrayList) su.get(hill);
        for (int n = 0; n < g.size(); n++) {
            unn.add((Integer) g.get(n));
        }
        if (!g.contains(new Integer(h))) {
            unn.add(new Integer(h));
            have.add(new Integer(h));
        }
        for (int i = 0; i < t.size(); i++) {
            if (!g.contains((Integer) t.get(i))) {
                unn.add((Integer) t.get(i));
                have.add((Integer) t.get(i));
            }
        }//for
        return unn;
    }

    public void clustering() {
        stcOutputbuilder = new StringBuilder();
        label_cluster.clear();
        doc_cluster.clear();
        List su_union = new ArrayList();
        List suff = new ArrayList();
        List apd = new ArrayList();   // String
        List sbb = new ArrayList();
        List simm = new ArrayList();
        //------------------------------------------
        getSuffixs();
        //------------------------------------------
        suff = getSuffix();
        //------------------------------------------
        apd = ap_doc;
        sbb = getSb();
        simm = getSimilarity();
        List temp = new ArrayList();
        //List have = new ArrayList();
        try {//old
            for (int i = 0; i < simm.size(); i++) {
                temp = (ArrayList) simm.get(i);
                if (i == 0) {
                    su_union.add(createU(i, temp));
                    List t = (ArrayList) su_union.get(su_union.size() - 1);
                    for (int ii = 0; ii < t.size(); ii++) {    // add  have
                        have.add((Integer) t.get(ii));
                    }
                } else if (temp.size() == 0) {
                    if (!have.contains(new Integer(i))) {
                        su_union.add(createU1node(i));
                    }
                } else {
                    int hil = haveInList(i, temp, su_union);
                    if (hil != 99999) {
                        su_union.set(hil, union_List(i, hil, temp, su_union));
                    } else if (hil == 99999) {
                        su_union.add(createU(i, temp));
                        List tt = (ArrayList) su_union.get(su_union.size() - 1);
                        for (int ii = 0; ii < tt.size(); ii++) {    // add  have
                            have.add((Integer) tt.get(ii));
                        }
                    }// hil == 99999
                }//else//else  
            }//for
        }//old
        catch (Exception e) {
            System.out.println("in Clustering");
            e.printStackTrace();
        }
        List ou = new ArrayList();
        int p;
        List copy_label = new ArrayList();
        List copy_doc = new ArrayList();
        for (int m = 0; m < su_union.size(); m++) {
            ou = (ArrayList) su_union.get(m);
            copy_label.add(makeLabel_Cluster(ou));
            copy_doc.add(makeDoc_Cluster(ou));
        }//for 

        //----------------------Befor Rank------------------------
        // System.out.println("------------------------------Print------------------------");
        //printSuffix();
        //printSb(sbb);
        //printSim(simm);
        //printBeforeRank(copy_label, copy_doc);
        //printUnionDoc(su_union);
        //------------------------------------------------------------------------------
        List sc = new ArrayList();
        int sum = 0;
        int inum = 0;
        List tempsu = new ArrayList();
        for (int z = 0; z < su_union.size(); z++) {
            tempsu = (ArrayList) su_union.get(z);
            sum = 0;
            for (int zz = 0; zz < tempsu.size(); zz++) {
                inum = ((Integer) tempsu.get(zz)).intValue();
                inum = ((Integer) sbb.get(inum)).intValue();
                sum = sum + inum;
            }
            sc.add(new Integer(sum));
        }// for
        // System.out.println("-----------SC--Print-------");
        //printSc(sc);
        //------------------------------------------------
        int mark = 0;
        int iii = 0;
        while (sc.size() != 0) {
            mark = searchSC_MAX(sc);
            label_cluster.add((String) copy_label.get(mark));
            doc_cluster.add(copy_doc.get(mark));
            copy_label.remove(mark);
            copy_doc.remove(mark);
            sc.remove(mark);
        }
        printLabel_Doc_Cluster();

    }//clustering 

    private String makeLabel_Cluster(List ou) {
        List sbb = new ArrayList();
        List suf = new ArrayList();
        sbb = getSb();
        suf = getSuffix();
        int max_sb;
        int pos = 0;
        int value = 0;
        max = 0;
        pos = ((Integer) ou.get(0)).intValue();
        max_sb = ((Integer) sbb.get(pos)).intValue();
        max = pos;
        for (int i = 1; i < ou.size(); i++) {
            pos = ((Integer) ou.get(i)).intValue();
            value = ((Integer) sbb.get(pos)).intValue();
            if (value > max_sb) {
                max_sb = value;
                max = pos;
            }
        }//

        return (String) suf.get(max);
    }

    private int searchSC_MAX(List sc) {
        int max = ((Integer) sc.get(0)).intValue();
        int g;
        int pos = 0;
        for (int i = 1; i < sc.size(); i++) {
            g = ((Integer) sc.get(i)).intValue();
            if (g > max) {
                max = g;
                pos = i;
            }
        }
        return pos;
    }

    private List makeDoc_Cluster(List ou) {
        List ap = new ArrayList();
        List apb = new ArrayList();
        List union = new ArrayList();
        ap = getAp_doc();
        int pos = 0;
        //int pos = ((Integer)ou.get(0)).intValue();
        apb = (ArrayList) ap.get(max);
        for (int f = 0; f < apb.size(); f++) {
            union.add((String) apb.get(f));
        }
        String dd;
        for (int i = 0; i < ou.size(); i++) {
            pos = ((Integer) ou.get(i)).intValue();
            apb = (ArrayList) ap.get(pos);
            for (int ii = 0; ii < apb.size(); ii++) {
                dd = (String) apb.get(ii);
                if (!union.contains(dd)) {
                    union.add(dd);
                }
            }// for in
        }// for out
        return union;
    }// makeDoc_Cluster

    private List createU(int h, List un) {// copy
        List copy = new ArrayList();
        copy.add(new Integer(h));
        for (int i = 0; i < un.size(); i++) {
            copy.add((Integer) un.get(i));
        }
        return copy;
    }

    private List createU1node(int i) {
        List c = new ArrayList();
        c.add(new Integer(i));
        return c;
    }

    //PRINT Method
    private void printSuffix() {
        System.out.println("----------SUFFIX-----------");
        for (int i = 0; i < suffix.size(); i++) {
            System.out.print(i + ". " + suffix.get(i) + "       ");
            List pp = (ArrayList) ap_doc.get(i);
            for (int y = 0; y < pp.size(); y++) {
                System.out.print(pp.get(y));
                System.out.print(",");
            }
            System.out.print("--------------" + pp.size());
            System.out.println();
        }
    }

    private void printSb(List sbb) {
        System.out.println("----------SB-----------");
        for (int i = 0; i < sbb.size(); i++) {
            System.out.println(i + ".   sb = " + sbb.get(i));
        }
    }

    private void printBeforeRank(List copy_label, List copy_doc) {
        System.out.println("----------Befor Rank-----------");
        for (int i = 0; i < copy_label.size(); i++) {
            System.out.print(i + ". " + copy_label.get(i) + "             ");
            List g = (ArrayList) copy_doc.get(i);
            for (int y = 0; y < g.size(); y++) {
                System.out.print(g.get(y) + ",");
            }
            System.out.println();
        }
    }

    private void printUnionDoc(List su_union) {
        System.out.println("----------Union Doc----------");
        for (int h = 0; h < su_union.size(); h++) {
            List bh = new ArrayList();
            bh = (ArrayList) su_union.get(h);
            System.out.println(h + ".... ");
            for (int k = 0; k < bh.size(); k++) {
                System.out.println("     " + bh.get(k));
            }
        }// for print*/
    }

    private void printSc(List sc) {
        System.out.println("-----------------------SC-----------------------");
        for (int i = 0; i < sc.size(); i++) {
            System.out.println(sc.get(i));
        }
    }

    private void printLabel_Doc_Cluster() {
        System.out.println("-----------------------Label _ DOC _ Cluster-----------------------");
        stcOutputbuilder.append("------Label document cluster------").append("<br />");
        List e = new ArrayList();
        for (int kk = 0; kk < doc_cluster.size(); kk++) {
            e = (ArrayList) doc_cluster.get(kk);
            System.out.print(label_cluster.get(kk));
            stcOutputbuilder.append(label_cluster.get(kk)).append("<br />");
            for (int jj = 0; jj < e.size(); jj++) {
                System.out.print(" ," + e.get(jj));
                stcOutputbuilder.append(" ," + e.get(jj)).append("<br />");
                if (jj == e.size() - 1) {
                    stcOutputbuilder.append("<br />");
                    System.out.println();
                }
            }
        }
    }

    public String getStcOutputTree() {
        String result = "";
        if (stcOutputbuilder != null) {
            result = stcOutputbuilder.toString();
        }
        return result;
    }

    private void printSim(List simm) {
        System.out.println("--------------------SIMM--------------------");
        for (int i = 0; i < simm.size(); i++) {
            List bg = (ArrayList) simm.get(i);
            System.out.println(i + ".");
            for (int y = 0; y < bg.size(); y++) {
                System.out.println("       " + bg.get(y));
            }
        }
    }
}
