package com.yzs.model;

public class Producer implements Runnable{
	private BufferPool pool;
	private String flag=null;
	public Producer(String flag,BufferPool pool) {
		this.flag=flag;
		this.pool=pool;
	}
	public void produce(String flag) throws InterruptedException{
		pool.add(flag);
	}
	@Override
	public void run() {
			while (true) {
				if(Thread.currentThread().isInterrupted()){
					break;
				}
				try {
					produce(flag);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
	}
	

}
