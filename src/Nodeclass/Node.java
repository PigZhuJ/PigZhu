package Nodeclass;

/**
 * 节点类
 */
public class Node {
    public Object data;//存放节点值
    public Node next;//存放节点的引用
    public Node(){
        this(null);
    }
    public Node(Object data){
        this(data,null);
    }
    public Node(Object data,Node next){
        this.data=data;
        this.next=next;
    }

}
