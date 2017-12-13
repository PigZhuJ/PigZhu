package Stack;

/**
 * 顺序栈
 */
public class Sqstack implements IStack{
    private Object[] stackElem;//对象数组
    private int top;//在非空栈中，top始终指向栈顶元素的下一个存储位置，当栈为空时，top值为0；
    //构造函数
    public Sqstack(int maxSize){
        top=0;
        stackElem=new Object[maxSize];//为栈分配maxSize个存储单元
    }

    @Override
    public void clear() {
        top=0;
    }

    @Override
    public Boolean isEmpty() {

        return top==0;
    }

    @Override
    public int length() {

        return top;//返回栈的长度
    }

    @Override
    public Object peek() {
        if (isEmpty()) {//如果栈非空，那么就返回栈顶元素
            return stackElem[top - 1];
        } else {
            return null;//如果为空就返回null;
        }
    }

    @Override
    public void push(Object x) throws Exception {
        if(top==stackElem.length){
            throw new Exception("栈已满");
        }else{
            stackElem[top++]=x;//将x压入栈顶，在top再自增1
        }
    }

    @Override
    public Object pop() {

        if(isEmpty()){
            return null;
        }else{
            return stackElem[--top];//先top再减1；然后再返回top的值
        }
    }

    public void display(){
        for (int i=top-1;i>=0;i--){
            System.out.print(stackElem[i].toString()+"   ");//打印出所有的元素
        }
    }
}



/*所有关于栈的算法的时间复杂度都是O(1)*/