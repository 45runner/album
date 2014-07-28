package workshop.album.Views;

import java.util.ArrayList;

import workshop.album.data.Beans.ImageBean;
import workshop.album.data.Beans.ImageGroupBean;
import workshop.album.ui.Activitys.MainActivity;
import workshop.album.ui.Activitys.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class ImageViewerFragment extends Fragment implements OnTouchListener,OnKeyListener {

	private int[]position;
	private ArrayList<ImageGroupBean> data;
	private viewHolder holder;
	public static ImageViewerFragment newInstance(int[] position){
		ImageViewerFragment fragment = new ImageViewerFragment();
        Bundle bundle = new Bundle();
        bundle.putIntArray( "index", position);
        fragment.setArguments(bundle);
        return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		data= ((MainActivity) getActivity()).data;
		position =getArguments().getIntArray("index");
		View contentView = inflater.inflate(R.layout.fragment_imageviewer, null);
		holder = new viewHolder(contentView);
		contentView.setOnTouchListener(this);
		contentView.setOnKeyListener(this);
		getImageByPosition();
		return contentView;
	}
	//
	private void getImageByPosition(){
		int groupIndex = position[0];
		int index = position[1];
		if (data==null) {
			return;
		}
		ImageGroupBean groupBean = data.get(groupIndex);
		ImageBean image = groupBean.images.get(index);
		
		Bitmap bm = BitmapFactory.decodeFile(image.imagePath);
		holder.image.setImageBitmap(bm);
		
	}
	
	class viewHolder{
		public ImageView image;
		public viewHolder(View root){
			image = (ImageView) root.findViewById(R.id.imageView);
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			
		break;
		case MotionEvent.ACTION_MOVE:
			
		break;

		default:
			break;
		}
		return true;
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			Toast.makeText(getActivity(), "back", 1000).show();
			getFragmentManager().beginTransaction().remove(this).commit();
		}
		return false;
	}
	
}
