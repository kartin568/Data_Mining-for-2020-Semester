package com.gbdpcloud.TestTool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class TestDirectorP implements Runnable{

    private Queue<TestFiles> testbed = new LinkedList<TestFiles>();
    private static   TestDirectorP testDirector = new TestDirectorP();

    @Override
    public void run() {
        while(true){
            if(testbed.size()>0){

            }
        }
    }


    /*
    public static void main(String[] args) throws Exception {
        // 在 Java8 中，推荐使用 Lambda 来替代匿名 Supplier 实现类
        CompletableFuture<ArrayList<String>> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            ArrayList<String> r = new ArrayList();
            r.add("11");
            return r;//"I have completed";
        });
        System.out.print("hhhhh");

        System.out.println(future.get());

    }
     */
}
