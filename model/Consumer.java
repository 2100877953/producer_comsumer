package com.yzs.model;

public class Consumer implements Runnable{
	private BufferPool pool ;
	private String flag=null;
	public Consumer(String flag,BufferPool pool) {
		this.flag=flag;
		this.pool=pool;
	}
	public void consume(String flag) throws InterruptedException{
		pool.use(flag);
	}
	@Override
	public void run() {
			while (true) {
				if(Thread.currentThread().isInterrupted()){
					break;
				}
				try {
					consume(flag);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
				
			}
	}
	
}
