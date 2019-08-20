package source.util;

public class ToolUtil {


    public static void countTimer(long second){
        final long temp = second;
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < (int) temp; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("i=" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }



}
