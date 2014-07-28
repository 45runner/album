package workshop.album.data.Beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageGroupBean{

	public List<ImageBean> images;
	public String lable;
	public long creatTime;
	public ImageGroupBean() {
		images = new LinkedList<ImageBean>();
	}
	public void addImage(ImageBean imageBean){
		images.add(imageBean);
		creatTime = imageBean.creatTime;
		if (images.size()>1) {
			Collections.sort(images);
		}
	}
}
