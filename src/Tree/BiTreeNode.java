package Tree;

/**
 * 二叉树链式存储结构的节点类描述
 */
public class BiTreeNode {
    public Object data;
    BiTreeNode lChild;
    BiTreeNode rChild;
//1.数据域空节点
    public BiTreeNode() {
        this(null);
    }
//2.左右孩子为空的节点
    public BiTreeNode(Object data) {
        this(data, null, null);
    }
//左右孩子都不为空的节点
    public BiTreeNode(Object data, BiTreeNode lChild, BiTreeNode rChild) {
        this.data = data;

    }
}
