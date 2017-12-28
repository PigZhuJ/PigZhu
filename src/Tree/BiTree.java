package Tree;

import Stack.LinkStack;

/**
 * 二叉树遍历操作的非递归实现
 */
public class BiTree {
    private BiTreeNode root;

    public BiTree() {
        this.root = null;//构建一棵空二叉树
    }

    public BiTree(BiTreeNode root) {
        this.root = root;
    }

    public void preRootTraverse() throws Exception {
        BiTreeNode T=root;
        LinkStack S=new LinkStack();//构造栈
        S.push(T);//根节点入栈
        while(!S.isEmpty()){
            T=(BiTreeNode)S.pop();
            System.out.println(T.data);//移除栈顶节点并返回其值
            while(T!=null){
                if (T.lChild!=null){
                    System.out.println(T.lChild.data);
                }
                if(T.rChild!=null){
                    S.push(T.rChild);
                }
                T=T.lChild;
            }

        }
    }

}
