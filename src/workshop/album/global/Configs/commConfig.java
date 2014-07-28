package workshop.album.global.Configs;

import java.io.File;

import android.R.anim;
import android.os.Environment;

public class commConfig {

	public final static String SDCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String DCIMPath = android.os.Environment.DIRECTORY_DCIM;
	public final static String CACHEPath = "MyAlbum/Cache/";
	/**
	 * 获取图片文件夹路径
	 * @return
	 */
	public  static String getAlbumPath(){
		String albumPath = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			albumPath = SDCardPath+"/"+DCIMPath+"/";
		}
		return albumPath;
	}
	public static String getCachePath(){
		String cachePath = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			cachePath = SDCardPath+"/"+CACHEPath;
			File cache = new File(cachePath);
			if (!cache.exists()) {
				cache.mkdirs();
			}
		}
		return cachePath;
	}
	/**
	 * 获取系统相册路径
	 * @return
	 */
	public static String getPhotoPath() {
		String photoPath = null;
		String albumPath = getAlbumPath();
		if (albumPath!=null) {
			File album = new File(albumPath);
			
			String[] temp = album.list();
			
			for (String string : temp) {
				if (string.trim().toLowerCase().contains("camera")) {
					photoPath = albumPath+string+"/";
				}
			}
		}
		return photoPath;
	}
	/**
	 * 获取截屏路径
	 * @return
	 */
	public static String getScreenShotPath(){
		return null;
	}
	
}
