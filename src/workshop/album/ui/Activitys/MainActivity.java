package workshop.album.ui.Activitys;

import java.util.ArrayList;

import workshop.album.EventListeners.ImageViewOnTouchListener;
import workshop.album.Interfaces.IMainactivityPresenter;
import workshop.album.Presenter.MainActivityPresenter;
import workshop.album.Widgets.ImageRelativeLayout;
import workshop.album.data.Beans.ImageGroupBean;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends FragmentActivity implements IMainactivityPresenter {
	private viewHolder holder;
	private boolean isImageLoaded = false;
	private MainActivityPresenter presenter;
	//
	public ArrayList<ImageGroupBean> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		holder = new viewHolder();
	}
	@Override
	protected void onStart() {
		super.onStart();
		//获取最新照片
		if (!isImageLoaded) {
			presenter = new MainActivityPresenter(MainActivity.this);
			presenter.setPresenter(this);
		}
	}
	@Override
	public void loadImageView(View v,ArrayList<ImageGroupBean> data) {
		this.data = data;
		android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		holder.container.addView(v, params);
	}
	@Override
	public void transitionAnimation(int distance) {
		
	}
	//
	class viewHolder  {
		public boolean zoom = false;
		public ImageRelativeLayout container;
		
		public viewHolder() {
			container = (ImageRelativeLayout) findViewById(R.id.mainContainer);
			//父容器加载完毕后，开始事务。
			container.post(new Runnable() {
				@Override
				public void run() {
					presenter.startPresenter();
					presenter.setViewWidth(container.getWidth());
					container.setOnTouchListener(new ImageViewOnTouchListener(presenter));
					isImageLoaded = true;
				}
			});
		}
	}
}
