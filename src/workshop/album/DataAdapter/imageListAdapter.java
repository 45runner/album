package workshop.album.DataAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import workshop.album.Views.MyImageView;
import workshop.album.data.Beans.ImageBean;
import workshop.album.data.Beans.ImageGroupBean;
import workshop.album.helper.Tools.SyncImageLoader;
import workshop.album.ui.Activitys.R;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import workshop.album.Widgets.loadImageWorkThread;

public class imageListAdapter extends BaseAdapter implements loadImageWorkThread.onBitmapLoaded {
	/*************************变量声明****************************************/
	private ArrayList<ImageGroupBean> images;
	private Context context;
	private int width;
	private int ImagePerRow=3;
	private int marginBetweenImage = 4;
	private SyncImageLoader syncImageLoader;
	private ListView mListView;
	/**************************************************************************/
	public imageListAdapter(ArrayList<ImageGroupBean> data,Context c,ListView list,int width) {
		this.width = width;
		this.images = data;
		this.context = c;
		syncImageLoader  = new SyncImageLoader(new Handler());
		mListView = list;
	}
	@Override
	public int getCount() {
		return images.size();
	}
	@Override
	public ImageGroupBean getItem(int index) {
		return images.get(index);
	}
	@Override
	public long getItemId(int index) {
		return 0;
	}
	@Override
	public View getView(int index, View contentView, ViewGroup root) {
		long s = System.currentTimeMillis();
		viewHolder holder=null;
		ImageGroupBean bean = getItem(index);
		if (contentView==null) {
			holder = new viewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			contentView = inflater.inflate(R.layout.item_piclist_normal, null);
			holder.lable = (TextView) contentView.findViewById(R.id.lable);
			holder.table = (TableLayout) contentView.findViewById(R.id.itemTable);
			holder.timeTips = (TextView) contentView.findViewById(R.id.timetips);
			contentView.setTag(holder);
		}else {
			holder = (viewHolder) contentView.getTag();
		}
		holder.table.removeAllViews();
		holder.lable.setText(bean.lable);
		setTimeTips(holder,bean);
		//holder.lable.setText(bean.lable+"count"+bean.images.size());
		drawImageTableRow(index,bean,holder.table);
		Log.e("view time", System.currentTimeMillis()-s+"mm");
		return contentView;
	}
	//
	private void setTimeTips(viewHolder holder,ImageGroupBean bean){
		SimpleDateFormat format = new SimpleDateFormat("M");
		long Now = System.currentTimeMillis();
		long creatTime = bean.creatTime;
		String monthString = format.format(new Date(creatTime));
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int offset = (hour*60*60+minute*60+second)*1000;
		Now = Now - offset;//今天０点
		float dayScap = (float)((Now - creatTime+0.01)/(1000*60*60*24));
		
		if (dayScap <= 0) {
			//今天
			holder.timeTips.setText("今");
		}else if (dayScap<=1) {
			//昨天
			holder.timeTips.setText("昨");
		}else if (dayScap<=2) {
			//前天
			holder.timeTips.setText("前");
		}else {
			holder.timeTips.setText(monthString);
		}
	}
	//添加行
	private void drawImageTableRow(int index ,ImageGroupBean bean,TableLayout table){
		int top = mListView.getFirstVisiblePosition();
		int bottom= mListView.getLastVisiblePosition();
		int[] region = new int[]{top,bottom};
		Looper looper = context.getMainLooper();
		LayoutParams trParams = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		
		if (bean==null) {
			return ;
		}else {
			List<ImageBean> imageBeans = bean.images;
			if (imageBeans == null) {
				return ;
			}else {
				int count = imageBeans.size();
				LayoutParams params = new LayoutParams();
				int totalMargin = (ImagePerRow+1)*marginBetweenImage;
				int sigleMargin = marginBetweenImage/2;
				int imageWidth = params.width = (width-totalMargin)/ImagePerRow;
				params.height = params.width;
				params.setMargins(sigleMargin,sigleMargin,sigleMargin,sigleMargin);
				TableRow row = null;
				for (int i = 0; i < count; i++) {
					if (i%ImagePerRow == 0) {
						row = new TableRow(context);
						table.addView(row, trParams);
					}
					ImageBean image = imageBeans.get(i);
					//图片全路径
					String path = image.imagePath;
					//自定义的ImageView对象
					int[] ivPoaition = new int[]{index,i};
					MyImageView iv = new MyImageView(context,imageWidth,ivPoaition);
					syncImageLoader.registerImageViewLoad(true,path, index, region,iv, imageWidth,this,looper);
					row.addView(iv, params);
				}
			}
		}
	}
	//视图辅助类
	static class viewHolder{
		public TextView lable;
		public TableLayout table;
		public TextView timeTips;
	}
	//加载图片
	@Override
	public void loadBitmapToImageView(MyImageView iv, Bitmap bm) {
		iv.setImageViewBitmap(bm);
	}
}
