package Log4j2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * log4j2 jar 包测试
 */
public class log4j2_1 {
    private static Logger logger= LogManager.getLogger(log4j2_1.class.getName());
    public static void main(String[] args) {
        for (int i=0;i<10;i++){
            System.out.println("黑夜问白天！");
            logger.trace("logger test!");
            logger.debug("debug");
            logger.info("info");
            logger.warn("warn");
            logger.error("error");
            logger.fatal("fatal");
        }
    }
    @Test
    public void loggerTest(){
        System.out.println("this is Junit Test");
    }
}
