package com.stc;

import java.util.*;

public class CompactTree extends Tree {

    public CompactTree(Tree tree) {
        root = tree.getRoot();
    }

    public void compact() {
        List stack = new ArrayList();
        List link_node = new ArrayList();
        boolean check = true;
        int linknum = 0;
        Node t1, t2;
        int visit;
        String pos;
        try {
            t1 = root;
            visit = 1;
            stack.add(root);
            link_node.add("0");
            t1 = (Node) t1.link.get(0);
            t2 = t1;
            while (check == true) {
                if (t1.link.size() == 1) {
                    if (visit > root.link.size()) {
                        check = false;
                    } else {
                        t2 = (Node) t1.link.get(0);
                        if (t1.doc.size() > 0) {
                            t1 = t2;
                        } else if (t1.doc.size() == 0) {
                            //merge
                            t1.link.clear();
                            if ((t2.link.size() > 0) && (t2.doc.size() > 0)) {
                                for (int i = 0; i < t2.link.size(); i++) {
                                    t1.link.add((Node) t2.link.get(i));
                                } // set link t1
                                for (int i = 0; i < t2.doc.size(); i++) {
                                    t1.doc.add((String) t2.doc.get(i));
                                } // set doc t2 t1
                                t2.link.clear();
                                t2.doc.clear();
                                t1.dat = t1.dat + " " + t2.dat;
                                t2 = t1;
                            } else if ((t2.link.size() > 0) && (t2.doc.size() == 0)) {
                                for (int i = 0; i < t2.link.size(); i++) {
                                    t1.link.add((Node) t2.link.get(i));
                                }  // set link t2 t1
                                t2.link.clear();
                                t1.dat = t1.dat + " " + t2.dat;
                                t2 = t1;
                            } else if (t2.link.size() == 0) {
                                for (int i = 0; i < t2.doc.size(); i++) {
                                    t1.doc.add((String) t2.doc.get(i));
                                }
                                t2.doc.clear();
                                t1.dat = t1.dat + " " + t2.dat;
                                t2 = t1;
                            }
                        }
                    }// visit <
                }// t1 = 1
                else if (t1.link.size() > 1) {
                    if (stack.contains(t1)) {
                        pos = (String) link_node.get(link_node.size() - 1);
                        linknum = new Integer(pos).intValue();
                        if (linknum < t1.link.size() - 1) {
                            linknum++;
                            link_node.set(link_node.size() - 1, new Integer(linknum).toString());
                            t2 = (Node) t1.link.get(linknum);
                            t1 = t2;
                        } else {// ź top,low stack
                            stack.remove(stack.size() - 1);
                            link_node.remove(link_node.size() - 1);  // ź top,low stack
                            if (stack.size() != 0) {
                                t1 = (Node) stack.get(stack.size() - 1);
                                t2 = t1;
                            }
                        }
                    } else {
                        stack.add(t1);
                        link_node.add("0");
                        t2 = (Node) t1.link.get(0);
                        t1 = t2;
                    }

                }//t1 >1
                else if (t1.link.size() == 0) {
                    // pop stack
                    t1 = (Node) stack.get(stack.size() - 1);
                    t2 = t1;
                }// t1 = 0

                if (stack.size() == 0) {
                    check = false;
                }
                if (t1.equals(root)) {
                    visit++;
                }
            }//while
        } catch (Exception e) {
            System.out.println("in Compact = ");
            e.printStackTrace();
        }
    }// optimized
    //private Node
}
