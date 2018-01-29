package Ason.CodeTest;

public class splitString {
    public static void main(String[] args) {
        String s="1_20";
        int node1=Integer.valueOf(s.split("_")[0]);
        int node2=Integer.valueOf(s.split("_")[1]);
        System.out.println(node1);
        System.out.println(node2);
    }


}
