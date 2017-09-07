package com.stc;

import java.util.*;

/**
 *
 * @author Administrator
 */
public class SuffixTree extends Tree {

    private int size;
    private Node cn;

    public SuffixTree() {//initial value  of  tree=
        size = 0;
        root = new Node("root");
        cn = root;
    }

    public int getSize() {
        return size;
    }

    private void addNode(Node on, Node nn) {
        try {
            on.link.add(nn);
            size++;
            cn = nn;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Node getCurrentNode() {
        return cn;
    }

    private Node setCurrentNode(Node bn) {
        cn = bn;
        return cn;
    }

    private boolean checkDup(Node on, String data) {
        int siz = on.link.size();
        boolean boo = false;
        Node bn;
        try {
            for (int i = 0; i < siz; i++) {
                bn = (Node) on.link.get(i);
                //System.out.println("nnn = "+cn.dat);
                if ((bn.dat).equals(data)) {
                    boo = true;
                    //System.out.println("in TRUE");
                    setCurrentNode(bn);
                    break;
                }//if
            }//for
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boo;
    }

    public int numLink(Node count) { 
        return count.link.size();
    }

    public void buildSTC(List st, String docnum) {
        //StringTokenizer tok = new StringTokenizer(st," ,");
        //int s_tok = tok.countTokens(); // size
        int s_tok = st.size();
        String[] arr = new String[s_tok];
        for (int i = 0; i < s_tok; i++) {
            //arr[i] = tok.nextToken();
            arr[i] = (String) st.get(i);
            //   System.out.println(i+" "+arr[i]);
        }//for
        if (size != 0) {
            if (checkDup(getRoot(), arr[0])) {
                Node buff;
                int nlink = 0;
                boolean check = true;

                if (s_tok > 1) {
                    int is_word = 1;
                    while (check == true) {
                        nlink = numLink(getCurrentNode()); 
                        if (nlink == 0) {
                            for (int i = is_word; i < s_tok; i++) {
                                Node newn = new Node(arr[i]);
                                addNode(cn, newn);
                            }
                            if (!getCurrentNode().doc.contains(docnum)) {
                                getCurrentNode().doc.add(docnum);
                            }

                            //addLineNode(cn, is_word, s_tok, arr);
                            check = false;
                        } else if (nlink == 1) {
                            buff = (Node) getCurrentNode().link.get(0);
                            if (arr[is_word].equals(buff.dat)) {  
                                if (is_word == s_tok - 1) {  
                                    cn = buff;
                                    if (!getCurrentNode().doc.contains(docnum)) {
                                        getCurrentNode().doc.add(docnum);

                                    }
                                    check = false;
                                }
                                cn = buff;
                                is_word++;
                            } else { 
                                for (int i = is_word; i < s_tok; i++) {
                                    Node newn = new Node(arr[i]);
                                    addNode(cn, newn);
                                }
                                if (!getCurrentNode().doc.contains(docnum)) {
                                    getCurrentNode().doc.add(docnum);
                                }
                                check = false;
                            }//
                        }// nlink = 1
                        else if (nlink > 1) {
                            if (checkDup(getCurrentNode(), arr[is_word])) { 
                                //
                                if (is_word == s_tok - 1) {
                                    //set doc
                                    if (!getCurrentNode().doc.contains(docnum)) {
                                        getCurrentNode().doc.add(docnum);
                                    }
                                    check = false;
                                }
                                is_word++;
                            } else {  
                                for (int i = is_word; i < s_tok; i++) {
                                    Node newn = new Node(arr[i]);
                                    addNode(cn, newn);
                                }//for
                                if (!getCurrentNode().doc.contains(docnum)) {
                                    getCurrentNode().doc.add(docnum);
                                }
                                check = false;
                            }//
                        } // nlink >1
                    } // for  
                }//if
                else if (s_tok == 1) {
                    // set doc
                    if (!getCurrentNode().doc.contains(docnum)) {
                        getCurrentNode().doc.add(docnum);
                    }
                }
            } //
            else {
                cn = getRoot();
                for (int i = 0; i < s_tok; i++) {
                    Node newn = new Node(arr[i]);
                    addNode(cn, newn);
                }
                if (!getCurrentNode().doc.contains(docnum)) {
                    getCurrentNode().doc.add(docnum);
                }
            }
        } else {  
            cn = getRoot();
            for (int i = 0; i < s_tok; i++) {
                Node newn = new Node(arr[i]);
                addNode(cn, newn);
            }
            if (!getCurrentNode().doc.contains(docnum)) {
                getCurrentNode().doc.add(docnum);
            }
        }
    }  // buildSTC
}
