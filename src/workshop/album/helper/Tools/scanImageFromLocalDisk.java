package workshop.album.helper.Tools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import workshop.album.global.Configs.commConfig;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path.Direction;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class scanImageFromLocalDisk {
	
	private Handler myHandler;
	private OnImageFileScan scan;
	public final static int CAMERALIMAGES = 0;
	public final static int SINGLEPERSON =1;
	
	public scanImageFromLocalDisk() {
		this(null);
		//new orderImage().start();
	}
	public scanImageFromLocalDisk(Looper looper){
		if (looper == null) {
			myHandler = new myHandler();
		}else {
			myHandler = new myHandler(looper);
		}
		//初始化目录
	}
	class myHandler extends Handler{
		public myHandler(){
			super();
			
		}
		public myHandler(Looper looper){
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case CAMERALIMAGES:
				scan.onScanFinish(CAMERALIMAGES, msg.getData().getStringArray("data"));
			break;
			case SINGLEPERSON:
				scan.onScanFinish(SINGLEPERSON, msg.getData().getStringArray("data"));
			break;
			default:
			break;
			}
		}
		
	}
	public interface OnImageFileScan{
		public void onScanFinish(int type,String[] datas);
		public void onScaning(int type ,String data);
	}
	//
	public void setScanListener(OnImageFileScan scan){
		this.scan = scan;
	}
	public void getAllImagesInCameralFolder(){
		new Thread(){
			@Override
			public void run(){
				String CamrealPath = commConfig.getPhotoPath();
				if (CamrealPath!=null) {
					File cameral = new File(CamrealPath);
					if (cameral.exists()) {
						//文件过滤器，过滤jpg、jpeg、png、gif
						FilenameFilter filter = new FilenameFilter() {
							@Override
							public boolean accept(File file, String fileName) {
								fileName= fileName.toLowerCase().trim();
								if (fileName.endsWith(".jpg")
										||fileName.endsWith(".jpeg")
										||fileName.endsWith(".png")
										||fileName.endsWith(".gif")) {
									return true;
								}
								return false;
							}
						};
						//过滤图片
						String[] pics = cameral.list(filter);
						//发送通知
						Message message = Message.obtain();
						message.what = CAMERALIMAGES;
						Bundle data = new Bundle();
						data.putStringArray("data", pics);
						message.setData(data);
						myHandler.sendMessage(message);
					}
				}
			
			}
		}.start();
	}
	//获取图库下的所有图片
	private HashMap<String, String[]> getImagesFromDCIM(){
		HashMap<String, String[]> imageFileMap = new HashMap<String, String[]>();
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			//获取图库路径
			String sdCard = android.os.Environment.getExternalStorageDirectory().toString();
			final String DCIMpath = android.os.Environment.DIRECTORY_DCIM;
			//图库全路径
			String imageFullPath = sdCard+"/"+DCIMpath;
			//文件过滤器，过滤jpg、jpeg、png、gif
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File file, String fileName) {
					fileName= fileName.toLowerCase().trim();
					if (fileName.endsWith(".jpg")
							||fileName.endsWith(".jpeg")
							||fileName.endsWith(".png")
							||fileName.endsWith(".gif")) {
						return true;
					}
					return false;
				}
			};
			//加载图库下的所有文件、文件夹
			File file = new File(imageFullPath);
			if (file.isDirectory()) {
				File[] imageFolders = file.listFiles();
				if (imageFolders==null) {
					return null;
				}
				for (File folder : imageFolders) {
					if (folder.isDirectory()) {
						String directoryNameString = folder.getName();
						if (directoryNameString.startsWith(".")) {
							continue;
						}
						String directory = imageFullPath+"/"+folder.getName()+"/";
						String[] temp = folder.list(filter);
						imageFileMap.put(directory, temp);
					}else {
						//外部零散文件
						folder.getName();
					}
				}
			
			
			}
		}else {
			return null;
		}
		return imageFileMap;
	}
	//
	public void filterSinglePerson(){
		//可以改成5个线程同步
		final int MAXFACE =1;
		new Thread(){
			@Override
			public void run(){
				ArrayList<File> files = null;
				ArrayList<String> datas = null;
				for (File file : files) {
					Face[] faces = new Face[MAXFACE];
					String fileName = file.getAbsolutePath();
					///*
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					//options.inSampleSize = 50;
					//*/
					Bitmap bm = BitmapFactory.decodeFile(fileName,options);
					int width = options.outWidth;
					int height = options.outHeight;
					int bw = width/4;
					int bh = height/4;
					options.inSampleSize = 4;
					options.outWidth = bw;
					options.outHeight = bh;
					options.inJustDecodeBounds = false;
					//int width = bm.getWidth();
					//int height = bm.getHeight();
					bm = BitmapFactory.decodeFile(fileName, options);
					try {
						FaceDetector detector = new FaceDetector(bw, bh, MAXFACE);
						
						int count = detector.findFaces(bm, faces);
						Log.v("--------------", "face"+count+fileName);
						bm.recycle();
						if (count >0) {
							datas.add(fileName);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
				int size = datas.size();
				if (datas!=null && size>0) {
					String[] data = new String[size];
					for (int i = 0; i < size; i++) {
						data[i] = datas.get(i);
					}
					Message message = Message.obtain();
					message.what = SINGLEPERSON;
					Bundle bdata = new Bundle();
					bdata.putStringArray("data", data);
					message.setData(bdata);
					myHandler.sendMessage(message);
				}
				
				
			}
		}.start();
	}
	//
	public void filterGroupphoto(){
		
	}

}
