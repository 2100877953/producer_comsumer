package com.yzs.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import com.yzs.view.MainFrame;

public class BufferPool {
	public static final int MAXSIZE=5;//缓冲池最大容量
	public static boolean stepflag=true;
	private LinkedList<Integer> list = new LinkedList<>();//装载资源的容器
	private static Random prRandom = new Random();
	private Semaphore semaphore = new Semaphore(1);
	public static String t_info=null;
	public static String p_info=null;
	
	public void add(String flag)throws InterruptedException{//生产方法
		String name = Thread.currentThread().getName();
		int s = prRandom.nextInt(100);
		semaphore.acquire();
		try {
			if(getSize()>=MAXSIZE){
				return;
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
		} finally {
			semaphore.release();
		}
	}
	public void use(String flag)throws InterruptedException{//消费方法
		String name = Thread.currentThread().getName();
		semaphore.acquire();
		try {
			if(getSize()<=0){
				return ;
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
		} finally {
			semaphore.release();
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
