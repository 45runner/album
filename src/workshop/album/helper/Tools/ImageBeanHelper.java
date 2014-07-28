package workshop.album.helper.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.R.integer;
import android.util.Log;
import workshop.album.data.Beans.ImageBean;
import workshop.album.data.Beans.ImageGroupBean;
import workshop.album.global.Configs.commConfig;

public class ImageBeanHelper {
	private String[] images;
	
	public static final int ORG_DAY = 0;
	public static final int ORG_WEEK = 1;
	public static final int ORG_MONTH = 2;
	public static final int ORG_SEASON=3;
	public static final int ORG_YEAR = 4;
	
	private ArrayList<ImageGroupBean> imageGroupList;
	private ArrayList<ImageBean> imageList;
	public ImageBeanHelper (String[] images){
		imageGroupList = new ArrayList<ImageGroupBean>();
		this.images = images;
	}
	public ArrayList<ImageGroupBean> orgInDay(int type){
		imageList = new ArrayList<ImageBean>();
		for (String imagePath : images) {
			String imageFileName = commConfig.getPhotoPath()+imagePath;
			ImageBean image = new ImageBean(imageFileName);
			imageList.add(image);
		}
		//排序
		long s = System.currentTimeMillis();
		Collections.sort(imageList);
		Log.v("sort---------------------------------", System.currentTimeMillis()-s+"mm");
		switch (type) {
		case ORG_DAY:
			return groupImagesInDay(imageList,ORG_DAY);
		case ORG_MONTH :
			return groupImagesInDay(imageList,ORG_MONTH);
		case ORG_YEAR:
				return groupImagesInDay(imageList, ORG_YEAR);
		default:
			return groupImagesInDay(imageList,ORG_DAY);
		}
		
	}
	//
	public List<ImageBean> getMatrixDatas(){
		if (imageList!=null) {
			return imageList;
		}
		List<ImageBean> imageList = new LinkedList<ImageBean>();
		for (String imagePath : images) {
			String imageFileName = commConfig.getPhotoPath()+imagePath;
			ImageBean image = new ImageBean(imageFileName);
			imageList.add(image);
		}
		//排序
		Collections.sort(imageList);
		return imageList;
	}
	//按天组织图片
	private ArrayList<ImageGroupBean> groupImagesInDay(ArrayList<ImageBean> datas,int type){
		ArrayList<ImageGroupBean> gbs = new ArrayList<ImageGroupBean>();
		int currentIndex =0;
		//索引和时间对应
		Map<Integer, Integer> keySet = new HashMap<Integer, Integer>();
		//当前日期
		SimpleDateFormat format=null;
		switch (type) {
		case ORG_DAY:
			format=new SimpleDateFormat("yyyyMMdd");
		break;
		case ORG_MONTH:
			format=new SimpleDateFormat("yyyyMM");
		break;
		case ORG_YEAR:
			format=new SimpleDateFormat("yyyy");
		break;
		default:
			break;
		}
		Date dateCurrent = new Date();
		int current = Integer.valueOf(format.format(dateCurrent));
		for (ImageBean image : datas) {
			long dateTime = image.creatTime;
			Date dateCreat = new Date(dateTime);
			int creatDate = Integer.valueOf(format.format(dateCreat));
			//计算索引
			int index = current - creatDate;
			if (keySet.containsKey(index)) {
				ImageGroupBean igb = gbs.get(keySet.get(index));
				igb.addImage(image);
			}else {
				ImageGroupBean igb = new ImageGroupBean();
				igb.lable = formatLable(creatDate);
				keySet.put(index, currentIndex);
				igb.addImage(image);
				gbs.add(currentIndex, igb);
				currentIndex++;
			}
		}
		return gbs;
	}
	//
	private String formatLable(int date){
		//20140101
		String sDate = String.valueOf(date);
		String year = sDate.substring(0, 4);
		String month = sDate.substring(4, 6);
		int day = Integer.valueOf(sDate.substring(6, 8));
		return day+" · "+year;
	}
}
