package Stack;

import Nodeclass.Node;

/**
 * 链栈
 */
public class LinkStack implements IStack {
    private Node top;//栈顶元素的引用

    @Override
    public void clear() {
        top = null;
    }

    @Override
    public Boolean isEmpty() {
        return top == null;
    }

    @Override
    public int length() {
        Node p = top;
        int length = 0;
        while (p != null) {
            p = p.next;
            ++length;
        }
        return length;
    }

    @Override
    public Object peek() {
        if (isEmpty()) {
            return null;
        } else {
            return top.data;
        }
    }

    /**
     * 压栈
     *
     * @param x 压栈的数据
     * @throws Exception
     */
    @Override
    public void push(Object x) throws Exception {
        Node p = new Node(x);//构建Node
        p.next = top;
        top = p;
    }

    /**
     * 出栈
     *
     * @return 返回栈顶的值
     */
    @Override
    public Object pop() {
        if (isEmpty()) {
            return null;
        } else {
            Node p = top;
            top = top.next;
            return p.data;
        }
    }
}
