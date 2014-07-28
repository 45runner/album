package workshop.album.Presenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import workshop.album.DataAdapter.imageGridAdapter;
import workshop.album.DataAdapter.imageListAdapter;
import workshop.album.Interfaces.IMainactivityPresenter;
import workshop.album.data.Beans.ImageBean;
import workshop.album.data.Beans.ImageGroupBean;
import workshop.album.helper.Tools.ImageBeanHelper;
import workshop.album.helper.Tools.scanImageFromLocalDisk;

public class MainActivityPresenter implements scanImageFromLocalDisk.OnImageFileScan{
	private IMainactivityPresenter presenter;
	private Context context;
	private int width= 0;
	private boolean isTransforming = false;
	private ImageBeanHelper helper;
	public MainActivityPresenter(Context context){
		this.context = context;
	}
	public void setPresenter(IMainactivityPresenter presenter){
		this.presenter = presenter;
	}
	//
	public void setViewWidth(int width){
		this.width = width;
	}
	public void startPresenter(){
		scanImageFromLocalDisk scan =  new scanImageFromLocalDisk();
		scan.setScanListener(this);
		scan.getAllImagesInCameralFolder();
	}
	//
	public void transformToTimeLine(int distance){
		if (!isTransforming) {
			isTransforming = true;
		}
	}
	//
	public void transformToMatrix(int distance){
		presenter.transitionAnimation(distance);
	}
	@Override
	public void onScanFinish(int type, String[] datas) {
		switch (type) {
		case scanImageFromLocalDisk.CAMERALIMAGES:
			helper = new ImageBeanHelper(datas);
			ArrayList<ImageGroupBean> maps = helper.orgInDay(ImageBeanHelper.ORG_DAY);
			//presenter.loadImageView(getTimeLineView(maps),maps);
			presenter.loadImageView(getMatrixView(),maps);
			
		break;
		default:
		break;
		}
	
	}
	@Override
	public void onScaning(int type, String data) {
		
	}
	//
	private View getTimeLineView( ArrayList<ImageGroupBean> data){
		ListView list = new ListView(context);
		imageListAdapter adapter = new imageListAdapter(data, context,list,width);
		list.setAdapter(adapter);
		list.setDivider(null);//没有分割线
		return list;
	}
	//
	private View getMatrixView(){
		GridView gv = new GridView(context);
		List<ImageBean> data = helper.getMatrixDatas();
		imageGridAdapter adapter = new imageGridAdapter(gv, data, context, width);
		gv.setNumColumns(3);
		gv.setAdapter(adapter);
		return gv;
	}

}
