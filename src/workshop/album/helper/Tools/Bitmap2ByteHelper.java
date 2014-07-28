/**
 * Project Name:ZhenTiBen
 * File Name:Bitmap2ByteHelper.java
 * Package Name:com.xhrd.notebook.util
 * Date:2014�?�?1日上�?:30:22
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved.
 *
*/

package workshop.album.helper.Tools;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bitmap2ByteHelper {

	public Bitmap2ByteHelper() {

		// TODO Auto-generated constructor stub

	}
	/**
	 * 
	 * bitmap2Bytes:位图转byte[]. <br/>
	 * 用�?：Bitmap对象不能直接序列化，�?��转为byte[]才能实现序列�?
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		//实例化字节数组输出流  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);//压缩位图  
	    return baos.toByteArray();//创建分配字节数组  
	}
	/**
	 * 
	 * bytes2Bitmap:byte[]转Bitmap，将SD卡中的序列化对象还原为Bitmap <br/>
	 */
	public static Bitmap bytes2Bitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);//从字节数组解码位�?
	}
}

