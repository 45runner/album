package workshop.album.helper.Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import workshop.album.DataAdapter.imageListAdapter;
import workshop.album.Views.MyImageView;
import workshop.album.Widgets.loadImageWorkThread;
import workshop.album.global.Configs.commConfig;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.System;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

public class SyncImageLoader {
	public static HashMap<String, SoftReference<Bitmap>> cacheInMemory = new HashMap<String, SoftReference<Bitmap>>();
	private ImageThreadPools threadPools;
	private String imagePath;
	private Handler mainHandler;
	private MyImageView imageView;
	private int width;
	private int index;
	//
	private int top;
	private int bottom;
	@SuppressLint("NewApi")
	public SyncImageLoader(Handler handler){
		 threadPools = new ImageThreadPools();
		 //int maxMemory = (int) Runtime.getRuntime().maxMemory();
		this.mainHandler = handler;
	}
	public void registerImageViewLoad(Boolean isListview,String path,int index,int[]visiableRegion,MyImageView imageView,int imageViewWidth,loadImageWorkThread.onBitmapLoaded l,Looper looper){
		this.width = imageViewWidth;
		this.imagePath = path;
		this.imageView = imageView;
		this.index = index;
		this.top = visiableRegion[0];
		this.bottom = visiableRegion[1];
		loadImageWorkThread T = new loadImageWorkThread(index,imagePath,mainHandler,imageView,width);
		T.setBitmapLoadLIstener(l,looper);
		threadPools.submitRunnable(T);
		
		if (isListview) {
			noticeToLoad();
		}else {
			threadPools.setLoadRegion(0, 20);
		}
		
	}
	//
	public void noticeToLoad(){
		int top = index-2<0?0:index-2;
		int bottom = index+2;
		threadPools.setLoadRegion(top, bottom);
	}
}