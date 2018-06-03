package com.yzs.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.yzs.view.MainFrame;

public class BufferPool {
	public static final int MAXSIZE=8;
	public static boolean stepflag=true;
	private volatile LinkedList<Integer> list = new LinkedList<>();//装载资源的容器
	private static Random prRandom = new Random();
	
	private Lock lock = new ReentrantLock();
	private Condition pro = lock.newCondition();
	private Condition con = lock.newCondition();
	
	public static String t_info=null;
	public static String p_info=null;
	
	public void add(String flag)throws InterruptedException{//生产方法
		lock.lock();//拿到锁（即互斥访问信号量）
		try {
			String name = Thread.currentThread().getName();
			int s = prRandom.nextInt(100);
			while(getSize()>=MAXSIZE){//判断缓冲池是否已满
				t_info = "缓冲池已满！线程"+name+"阻塞！";
				p_info = "缓冲池资源为："+Arrays.toString(list.toArray())+",总数："+getSize();
				MainFrame.getMessage(t_info,p_info);//回调方法，传递线程信息
				Thread.sleep(100);
				if("step".equals(flag)){//单步方式判断
					stepflag=false;
					while (!stepflag) {
						if(Thread.currentThread().isInterrupted()){
							break;
						}
					}
				}else {
					Thread.sleep(1900);
				}	
				pro.await();//缓冲池已满，生产者线程阻塞
			}
			/**
			 * 生产资源
			 */
			list.add(s);
			t_info = "生产者线程 "+name+" 生产资源 "+s;
			p_info = "缓冲池资源为："+Arrays.toString(list.toArray())+",总数："+getSize();
			MainFrame.getMessage(t_info,p_info);
			if("step".equals(flag)){
				stepflag=false;
				while(!stepflag){
					if(Thread.currentThread().isInterrupted()){
						break;
					}
				}
				Thread.sleep(100);
			}else {
				Thread.sleep(1900);
			}
			con.signal();//唤醒阻塞的消费者线程
		} finally {
			lock.unlock();//释放锁
		}
		
	}
	public void use(String flag)throws InterruptedException{//消费方法
		lock.lock();//拿到锁
		try {
			String name = Thread.currentThread().getName();
			while(getSize()<=0){//判断缓冲池是否为空
				t_info = "缓冲池为空！线程"+name+"阻塞！";
				p_info = "缓冲池资源为："+Arrays.toString(list.toArray())+",总数："+getSize();
				MainFrame.getMessage(t_info,p_info);
				Thread.sleep(100);
				if("step".equals(flag)){
					stepflag=false;
					while (!stepflag) {
						if(Thread.currentThread().isInterrupted()){
							break;
						}
					}
				}else {
					Thread.sleep(1900);
				}	
				con.await();//消费者阻塞
			}
			/**
			 * 消费资源
			 */
			Integer result =  (Integer) list.removeFirst();
			t_info = "消费者线程 "+name+" 消费资源 "+result;
			p_info = "缓冲池资源为："+Arrays.toString(list.toArray())+",总数："+getSize();
			MainFrame.getMessage(t_info,p_info);
			if("step".equals(flag)){
				Thread.sleep(100);
				stepflag=false;
				while(!stepflag){
					if(Thread.currentThread().isInterrupted()){
						break;
					}
				}
			}else {
				Thread.sleep(1900);
			}
			pro.signal();//唤醒阻塞的生产者线程
		} finally {
			lock.unlock();//释放锁
		}
	}
	public static void changeStepFlag(){
		stepflag=!stepflag;
	}
	public int getSize(){
		return list.size();
	}
	public LinkedList<Integer> getList() {
		return list;
	}
}
