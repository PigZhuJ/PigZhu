package Test;

import org.junit.Test;

class AsonTest {
    public int finddesignIDa(int path[][],int src,int dst)
    {

//        int i=0;
//
//        for(i=0;i<30;i++)
//        {
//            int flag=0;
//            for(int k=7;k<19;k++)
//            {
//                if(path[i][k]==src & path[i][k+1]==dst)
//                {
//                    flag=1;
//                    break;
//                }
//                else if(path[i][k]==dst & path[i][k+1]==src)
//                {
//                    flag=1;
//                    break;
//                }
//                else{
//                    continue;
//                }
//            }
//            if(flag==1)
//            {
//                System.out.println("找到该行"+i);
//                break;
//            }
//
//        }
        int row=5;
        for (int i = 0; i <path.length ; i++) {
            for (int j=7;j<19;j++){
                if(path[i][j]==src){
                    if (path[i][j+1]==dst){
                        row=i;
                        System.out.println(row);
                        System.out.println("here");
                        break;
                    }
                }else if(path[i][j]==dst){
                    if(path[i][j+1]==src){
                        row=i;
                        break;
                    }
                }else {
                    continue;
                }
            }
        }
        return row;
    }


    public static void main(String[] args) {
        AsonTest a=new AsonTest();
        int[][] p={{1,1,1,1,1,1,1,1,55,6,1,1,1,1,1,1,1,1,1,1,1,11,1,1,1,1,1,1,1,1,1,1,11,1,1,1,1,1,1,1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1,5,6,1,1,1,1,1,1,1,1,1,1,1,11,1,1,1,1,1,1,1,1,1,1,11,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};
        int b=a.finddesignIDa(p,5,6);
        System.out.println(b);
    }
}
