package workshop.album.helper.Tools;

import java.util.Stack;

import workshop.album.Widgets.loadImageWorkThread;
import android.util.Log;
/**
 * ����
 * 1�����ȼ��ص�ǰ�ɼ���ͼƬ
 * 2��������ز��ɼ���ͼƬ
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
	//����̵߳������
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
		//�ܺ�ʱ��������������
		Log.e("thread start time costed", System.currentTimeMillis()-s+"mm");
	}
	//���ؿɼ�����
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
		//ֹͣ���ɼ�������߳�
		empty();
		//��ʼ���߳�
		loadController.onResume();
	}
	//��մ�ִ������
	private void empty(){
		while (!runningThread.isEmpty()) {
			loadImageWorkThread t = runningThread.pop();
			if (t== null) {
				return;
			}
			//ͣ�����ɼ�������߳�
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
		//��ͣ
		public void onPause(){
			synchronized (locker) {
				isLocked = true;
			}
		}
		//�ָ�
		public void onResume(){
			synchronized (locker) {
				isLocked = false;
				locker.notifyAll();
			}
		}
		//��ͣλ��
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
						//�ܺ�ʱ��������������
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
