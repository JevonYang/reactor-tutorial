package com.yang.tutorial.callback;

/**
 * @author yangzijing
 */
public class Boss implements Callback<String> {

    private Worker worker;

    public Boss(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void apply(String s) {
        System.out.println("boss got the feedback from worker: " + s);
    }

    public void makeBigDeals(final String someDetail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                worker.work(Boss.this, someDetail);
            }
        }).start();
    }

}
