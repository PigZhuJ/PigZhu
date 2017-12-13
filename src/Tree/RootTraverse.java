package Tree;

/**
 * 遍历的递归调用
 */
public class RootTraverse {
    /**
     * 1.先根遍历
     *
     * @param T 二叉树节点
     */
    public void preRootTraverse(BiTreeNode T) {
        if (T != null) {
            System.out.println(T.data);//输出数据域
            preRootTraverse(T.lChild);//先根遍历左子树
            preRootTraverse(T.rChild);//先根遍历右子树
        } else {
            System.out.println("error!");
        }
    }

    /**
     * 2.中根遍历
     *
     * @param T 二叉树节点
     */
    public void inRootTraverse(BiTreeNode T) {
        if (T != null) {
            inRootTraverse(T.lChild);//中根左子树
            System.out.println(T.data);//中根遍历
            inRootTraverse(T.rChild);//中根遍历右子树
        } else {
            System.out.println("error!");
        }

    }

    /**
     * 3.后根遍历
     *
     * @Param T 二叉树节点
     */
    public void postRootTraverse(BiTreeNode T) {
        if (T != null) {
            postRootTraverse(T.lChild);//先遍历左子树
            postRootTraverse(T.rChild);//再遍历右子树
            System.out.println(T.data);//最后后根遍历根节点

        }

    }
}
