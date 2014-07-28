package workshop.album.DataAdapter;

import java.util.List;

import workshop.album.Views.MyImageView;
import workshop.album.Widgets.loadImageWorkThread;
import workshop.album.data.Beans.ImageBean;
import workshop.album.helper.Tools.SyncImageLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class imageGridAdapter extends BaseAdapter implements loadImageWorkThread.onBitmapLoaded {

	private List<ImageBean> datas;
	private Context context;
	private SyncImageLoader syncImageLoader;
	private int width;
	private GridView gv;
	public imageGridAdapter(GridView gv,List<ImageBean> data,Context context,int width){
		this.context = context;
		this.datas = data;
		this.width = width;
		this.gv = gv;
		this.syncImageLoader = new SyncImageLoader(new Handler());
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ImageBean getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageBean image = getItem(position);
		MyImageView view =new MyImageView(context, width, position);
		String path = image.imagePath;
		int startIndex = gv.getFirstVisiblePosition();
		int endIndex = gv.getLastVisiblePosition();
		syncImageLoader.registerImageViewLoad(false,path,position,new int[]{startIndex,endIndex},view,width,this,context.getMainLooper());
		return view;
	}
	@Override
	public void loadBitmapToImageView(MyImageView iv, Bitmap bm) {
		iv.setImageViewBitmap(bm);
	}
}
