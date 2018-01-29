package Test;


import org.junit.Test;

import java.util.Arrays;

public class SpiltTest {
    @Test
    public void Splittest(){
        String s="116|124|119";
        String[] sp=s.split("\\|");
        System.out.println(Arrays.toString(sp));
    }

}
