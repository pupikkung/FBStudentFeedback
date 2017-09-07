package com.stc;

import java.util.*;


public class Calculation {

    /**
     * Creates a new instance of Calculation
     */
    public Calculation() {
    }
    
    public void calculatePoint(List docList){
        for(Object obj: docList){
            
        }
    }

    public List Calc_overlab(List doc_list, int total_snippet) {
        List dd = new ArrayList();
        String doc = new String();
        String doc_num = new String();
        List d_10 = new ArrayList();
        if (doc_list.size() >= 10) {
            for (int t = 0; t <= 9; t++) {//number of cluster display 10 cluster
                d_10.add(doc_list.get(t));
            }
        } else {
            d_10 = doc_list;
        }
        doc = d_10.toString();
        StringTokenizer d = new StringTokenizer(doc, ", [ ] ");
        while (d.hasMoreTokens()) {
            dd.add(d.nextToken());
        }//end while         
        System.out.println("dd == " + dd);
        System.out.println("dd.size == " + dd.size());
        System.out.println("total-snippet == " + total_snippet);
        float total_doc_cluster = dd.size();
        float overlap = ((total_doc_cluster / total_snippet) - 1) * 100;
        System.out.println("overlap == " + overlap);
        return dd;
    }//end of method Calc_overlab

    public List Calc_coverage(List doc_list, int total_snippet) {
        List over = new ArrayList();
        List dd = new ArrayList();
        List d_10 = new ArrayList();
        String doc = new String();
        if (doc_list.size() >= 10) {
            for (int t = 0; t <= 9; t++) {//number of cluster display
                d_10.add(doc_list.get(t));
            }
        } else {
            d_10 = doc_list;
        }
        doc = d_10.toString();
        for (int n = 1; n <= total_snippet; n++) {
            over.add(n);
        }
        StringTokenizer d = new StringTokenizer(doc, " , [ ]  ");
        List dd_filter = new ArrayList();
        while (d.hasMoreTokens()) {
            dd.add(d.nextToken());
        }//end of while        
        for (int m = 0; m < dd.size(); m++) {
            if (!dd_filter.contains(dd.get(m))) {
                dd_filter.add(dd.get(m));
            }
        }//end of for        
        // System.out.println("dd_filter.size == "+dd_filter.size());
        //System.out.println("over.size == "+over.size());
        float numdoc_nocluster = over.size() - dd_filter.size();
        //  System.out.println("numdoc_nocluster  "+numdoc_nocluster);       
        //   System.out.println("dd== "+dd);   
        //   System.out.println("dd_filter = "+dd_filter);  
        //   System.out.println("over== "+over);              
        float coverage = ((total_snippet - numdoc_nocluster) / total_snippet) * 100;
        System.out.println("coverage == " + coverage);

        return over;
    }//end of method Calc_coverage
}//end of class
