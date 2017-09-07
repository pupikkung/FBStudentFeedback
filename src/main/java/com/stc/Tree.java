package com.stc;
import java.util.*;

public class Tree {
    protected Node root;
    /** Creates a new instance of Tree */
    
    public Tree(){
        root = new Node("root");
    }
    
    public Node getRoot(){
        return root;
    }
    
    public void printTree(){
        List stack =  new ArrayList();
        List link_node = new ArrayList();
        boolean check = true;
        Node trav,r;
        r = getRoot();
        trav = r;
        int linknum,num; String pos;  int visit;
        try{
            if(trav.link.size()==0){
                System.out.println("No Link Node");    // throw Exception
                System.exit(0);System.exit(0);
            } else{
                visit =1;
                trav = r;
                num = 1;
                stack.add(r); link_node.add("0"); trav = (Node)trav.link.get(0); System.out.print(num+". "+trav.dat+"     ");  printDoc(trav);
                while (check==true){
                    if (trav.link.size()>1){
                        if(stack.contains(trav)){  
                            pos = (String)link_node.get(link_node.size()-1);
                            linknum = new Integer(pos).intValue();
                            if (linknum < trav.link.size()-1){  
                                linknum++;
                                link_node.set(link_node.size()-1, new Integer(linknum).toString());
                                trav = (Node)trav.link.get(linknum);
                                num++;
                                System.out.print(num+". "+trav.dat+"     ");  printDoc(trav);
                            } else {
                                stack.remove(stack.size()-1);
                                link_node.remove(link_node.size()-1);  // ź top,low stack
                                if (stack.size()!=0){
                                    trav = (Node)stack.get(stack.size()-1);
                                }
                                
                            }
                        } else{ 
                            stack.add(trav);
                            link_node.add("0");
                            trav = (Node)trav.link.get(0);
                            num++;
                            System.out.print(num+". "+trav.dat+"     ");  printDoc(trav);
                        }
                    }//  link>1
                    else if (trav.link.size()==1){
                        if (visit > r.link.size()){
                            check = false;
                        } else {
                            trav = (Node)trav.link.get(0);
                            num++;
                            System.out.print(num+". "+trav.dat+"     ");  printDoc(trav);
                        }
                    }// link == 1
                    else if (trav.link.size()==0){
                        trav = (Node)stack.get(stack.size()-1);
                    }// link ==0
                    /*------------------------------------------------------*/
                    if (stack.size()==0){ 
                        check = false;
                    }
                    if (trav.equals(r)){
                        visit++;
                    }
                    
                }// while
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    } // optimized
    
    private void printDoc(Node t){
        if (t.doc.size()>0){
            for(int i = 0; i < t.doc.size();i++){
                System.out.print(t.doc.get(i)+" ");
            }
            System.out.println();
        } else {
            System.out.println("--");
        }
    }// printDoc
}

