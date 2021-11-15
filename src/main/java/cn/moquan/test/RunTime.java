package cn.moquan.test;

import lombok.SneakyThrows;

/**
 * describe
 *
 * @author wangyuanhong
 * @date 2021/11/12
 */
public class RunTime extends Thread{

    private static int timeNum = 0;
    private boolean startService = false;
    private static RunTime runTime = new RunTime();

    public static RunTime getRunTime(){
        return runTime;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (startService){
            timeNum++;
            Thread.sleep(1000);
        }
    }

    public void startService(){
        if(!startService){
            startService = true;
            this.start();
        }
    }

    public void stopService(){
        startService = false;
    }

    public int getTime(){
        return timeNum;
    }

}
