package com.stc;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class Node {

    protected List link, doc, item, fre;   //
    protected String dat;

    public Node(String data) {
        link = new ArrayList();
        doc = new ArrayList();
        this.dat = data;

    }
}
