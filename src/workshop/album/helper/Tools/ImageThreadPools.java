package workshop.album.helper.Tools;

import java.util.Stack;

import workshop.album.Widgets.loadImageWorkThread;
import android.util.Log;
/**
 * 功能
 * 1、优先加载当前可见的图片
 * 2、逆序加载不可见的图片
 * 
 *
 */
public class ImageThreadPools {
	private imageLoadController loadController;
	private static Stack<loadImageWorkThread> runables;
	private Stack<loadImageWorkThread> runningThread;
	private final static int MAXTHREADCOUNT = 15;
	private Stack<loadImageWorkThread> runnableSeq;
	private int visiableTop = 0;
	private int visiableBottom =0;
	public ImageThreadPools() {
		runables = new Stack<loadImageWorkThread>();
		runnableSeq= new Stack<loadImageWorkThread>();
		runningThread = new Stack<loadImageWorkThread>();
		loadController= new imageLoadController();
		loadController.start();
	}
	//添加线程到缓存池
	public void submitRunnable(loadImageWorkThread r) {
		runables.add(r);
	}
	//
	public void start(){
		long s = System.currentTimeMillis();
		while (!runnableSeq.isEmpty()) {
			int threadCount = loadImageWorkThread.runningThreadCount;
			if (threadCount <= MAXTHREADCOUNT) {
				loadImageWorkThread r = runnableSeq.pop();
				runningThread.add(r);
				if (r== null) {
					return;
				}
				r.start();
			}else {
				Log.v("Threat Busy", threadCount+"threads Running");
			}
		}
		//总耗时２１２／２４７
		Log.e("thread start time costed", System.currentTimeMillis()-s+"mm");
	}
	//加载可见区域
	public void setLoadRegion(int startIndex,int endIndex){
		visiableTop = startIndex;
		visiableBottom = endIndex;
		while (!runables.empty()) {
			loadImageWorkThread t = runables.pop();
			int index = t.groupIndex;
			if (index >= startIndex && index <= endIndex) {
				runnableSeq.add(t);
			}
		}
		//停止不可见区域的线程
		empty();
		//开始新线程
		loadController.onResume();
	}
	//清空待执行序列
	private void empty(){
		while (!runningThread.isEmpty()) {
			loadImageWorkThread t = runningThread.pop();
			if (t== null) {
				return;
			}
			//停掉不可见区域的线程
			if (t.isAlive() &&( t.groupIndex < visiableTop || t.groupIndex >visiableBottom)) {
				t.exceted = true;
			}
		}
	}
	class imageLoadController extends Thread{
		private Object locker;
		private boolean isLocked;
		public imageLoadController(){
			locker = new Object();
			isLocked = false;
		}
		//暂停
		public void onPause(){
			synchronized (locker) {
				isLocked = true;
			}
		}
		//恢复
		public void onResume(){
			synchronized (locker) {
				isLocked = false;
				locker.notifyAll();
			}
		}
		//暂停位置
		private void pauseHooker(){
			synchronized (locker) {
				if (isLocked) {
					try {
						locker.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		@Override
		public void run() {
			long s = System.currentTimeMillis();
			while (true) {
				int threadCount = loadImageWorkThread.runningThreadCount;
				if (threadCount <= MAXTHREADCOUNT) {
					if (runnableSeq.isEmpty()) {
						continue;
					}
					loadImageWorkThread r = runnableSeq.pop();
					runningThread.add(r);
					if (r== null) {
						//总耗时２１２／２４７
						Log.e("thread start time costed", System.currentTimeMillis()-s+"mm");
						onPause();
						pauseHooker();
					}
					r.start();
				}else {
					Log.v("Threat Busy", threadCount+"threads Running");
				}
			}
		}
		
	}
}
