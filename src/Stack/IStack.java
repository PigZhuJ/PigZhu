package Stack;

/**
 * 栈的接口类 zhujian
 */
public interface IStack {

    public void clear();//清空

    public Boolean isEmpty();//判断是否为空

    public int length();//长度

    public Object peek();//取出栈顶

    public void push(Object x) throws Exception;

    public Object pop();//弹栈
}
