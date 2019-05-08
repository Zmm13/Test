package com.example.administrator.test.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FankeTest {

    static class haha implements Runnable {

        public haha(List<String> list) {
            // TODO Auto-generated constructor stub
            this.list = (ArrayList<String>) list;
        }

        @Override
        public  void run() {
                while (log != null) {
                    synchronized (this) {
                    if (list.size() > 0) {
                            try {
//                                Thread.currentThread().sleep(1);
                                log = list.remove(0); //报下标错误
                                System.out.println(Thread.currentThread().getName() + ":" + log);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    } else {
//                        System.out.println(Thread.currentThread().getName() );
                        break;
                    }
                }
            }


        }

        private List<String> list;
        private String log = "";
    }

    public static void main(String[] args) {
        String[] strs = new String[]{"aa", "bb", "cc"};
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(strs));
        haha h = new haha(list);
//        Thread t1 = new Thread(new haha(list));
//        Thread t2 = new Thread(new haha(list));
        Thread t1 = new Thread(h);
        Thread t2 = new Thread(h);
        t1.start();
        t2.start();
    }

}

