package workshop.album.helper.Tools;

import java.io.Serializable;

public class BitmapSerializHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	byte[] byteBitmap = null;
	public BitmapSerializHelper(byte[] data) {
		this.byteBitmap =data;
	}
	//
	public byte[] getByteBitmap(){
		if (byteBitmap!=null) {
			return this.byteBitmap;
		}else {
			return null;
		}
	}

}
