package workshop.album.data.Beans;

import java.io.File;

public class ImageBean implements Comparable<ImageBean> {

	public String imageName;
	public String imagePath;
	public long  creatTime;
	public int    imageSize;
	public int    imageWidth;
	public int    imageHeight;
	public int    loveLevel;
	public String[] imageTags;
	public int	  date;//yyyymmddhhmm
	public int    color;
	public int    whom;
	public int    album;
	public int    valueable;//显示的价值，过小、不清楚的不显示
	public String thumbImageView;//缩略图
	public ImageBean(String path) {
		setBasicInfo(path);
	}
	@Override
	public int compareTo(ImageBean image) {
		if (this.creatTime < image.creatTime) {
			return 1;
		}else {
			return -1;
		}
	}
	private void setBasicInfo(String path){
		File f = new File(path);
		imagePath = path;
		creatTime = f.lastModified();
	}
	class imageAddtionalInfo extends Thread{
		public imageAddtionalInfo(String path){
			
		}
		@Override
		public void run(){
			
		}
	}

}
