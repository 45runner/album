package workshop.album.Widgets;

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

import workshop.album.Views.MyImageView;
import workshop.album.global.Configs.commConfig;
import workshop.album.helper.Tools.Bitmap2ByteHelper;
import workshop.album.helper.Tools.BitmapSerializHelper;
import workshop.album.helper.Tools.SyncImageLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class loadImageWorkThread extends Thread {

	String imagePath;
	Handler mainHandler;
	MyImageView imageView;
	public boolean exceted = false;
	public static Integer runningThreadCount = 0;
	private static HashMap<String, String> cache = null;
	private static int count = 0;
	private int width;
	public int groupIndex;
	private onBitmapLoaded listener;
	
	private Handler handler;
	private Bitmap bm;
	private MyImageView iv;
	
	public interface onBitmapLoaded{
		public void loadBitmapToImageView(MyImageView iv,Bitmap bm);
	}
	public void setBitmapLoadLIstener(onBitmapLoaded l,Looper looper){
		listener = l;
		handler = new Handler(looper){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					listener.loadBitmapToImageView(imageView, bm);
				}
			}
		};
	}
	public loadImageWorkThread(int groupIndex,String path,Handler mainHandler,MyImageView imageView,int width){
		this.width = width;
		this.imagePath = path;
		this.mainHandler = mainHandler;
		this.imageView = imageView;
		this.groupIndex = groupIndex;
		
		//映射缓存文件夹中的缓存文件
		if (cache == null) {
			cache = new  HashMap<String, String>();
			String cachePath = commConfig.getCachePath();
			File file = new File(cachePath);
			if (file.exists()) {
				String[] cacheFiles = file.list();
				String cachePathDir = commConfig.getCachePath();
				for (String kv : cacheFiles) {
					cache.put(kv, cachePathDir+kv);
				}
			}
		}
	}
	@Override
	public void run(){
		if (!exceted) {
			runningThreadCount++;
			//加载图片数据                                                                                                     
			Bitmap bm = getBitmapFromSDCard(imagePath);
			Message message = Message.obtain();
			message.what = 1;
			this.bm = bm;
			handler.sendEmptyMessage(message.what);
			//内存缓存
			String key = imagePath.substring(imagePath.lastIndexOf('/')+1, imagePath.lastIndexOf('.'));
			
			if (!SyncImageLoader.cacheInMemory.containsKey(key)) {
				SyncImageLoader.cacheInMemory.put(key, new SoftReference<Bitmap>(bm));
			}
			exceted = true;
			runningThreadCount --;
		}else {
			Log.e("Destory by app", "destoryed");
		}
	}
	private int getSampleSize(int width,String patth){
		int sampleSize = 1;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(patth, options);
		int outWidth = options.outWidth;//1836  12
		int outHeight = options.outHeight;//3264 21
		int standerSize = (outHeight + outWidth)/2;
		sampleSize = standerSize/width;
		//sampleSize = outWidth/width < outHeight /width?outWidth/width:outHeight /width;
		Log.v("inSampleSize", "size: "+sampleSize);
		return sampleSize;
	}
	//从SD卡上加载图片
	private Bitmap getBitmapFromSDCard(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = getSampleSize(width, path);
		options.inJustDecodeBounds = false;
		String key = path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
		//Step 1 先从内存缓存中读
		if (SyncImageLoader.cacheInMemory.containsKey(key)) {
			Log.v("loadFrom", "Memory");
			Bitmap bm =SyncImageLoader.cacheInMemory.get(key).get();
			if (bm!=null) {
				return bm;
			}
		}
		//Step2 从本地序列化对象中读
		if (cache.containsKey(key)) {
			Bitmap bm = null;
			String cachePath = cache.get(key);
			Log.v("readobj", cachePath);
			InputStream input;
			try {
				input = new FileInputStream(cachePath);
				ObjectInputStream oin = new ObjectInputStream(input);
				BitmapSerializHelper bsh = (BitmapSerializHelper) oin.readObject();
				bm= Bitmap2ByteHelper.bytes2Bitmap(bsh.getByteBitmap());
				bsh=null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return bm;
		}else {
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm!=null && !bm.isRecycled()) {
				new cacheImage(path, bm).start();
			}
			return bm;
		}
	}
	//缓存文件
	class cacheImage extends Thread{
		private String key;
		private String value;
		private Bitmap bm;
		public cacheImage(String path,Bitmap bm){
			key = path;
			key = key.substring(key.lastIndexOf('/')+1, key.lastIndexOf('.'));
			value = path;
			this.bm = bm;
		}
		@Override
		public void run(){
			FileOutputStream os = null;
			try {
				String cachePath = commConfig.getCachePath();
				String cacheFileName = (cachePath+value.substring(value.lastIndexOf('/')+1, value.indexOf('.')));
				Log.v("cachef", cacheFileName);
				os = new FileOutputStream(cacheFileName);
				ObjectOutputStream oos = new ObjectOutputStream(os);
				if (bm == null || bm.isRecycled() ) {
					return;
				}
				oos.writeObject(new BitmapSerializHelper(Bitmap2ByteHelper.bitmap2Bytes(bm)));
				cache.put(key, cacheFileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();	
			}
		}
	}
	//
	class loadBitmapRinnable implements Runnable{
		private Bitmap bitmap;
		private MyImageView imageView;
		public loadBitmapRinnable(MyImageView imageView,Bitmap bm){
			bitmap = bm;
			this.imageView = imageView;
		}
		@Override
		public void run() {
			Log.v("count", Thread.currentThread().getName()+(count++));
			imageView.setImageViewBitmap(bitmap);
		}
		
	}

}
