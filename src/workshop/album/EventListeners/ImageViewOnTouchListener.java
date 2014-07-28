package workshop.album.EventListeners;

import workshop.album.Presenter.MainActivityPresenter;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ImageViewOnTouchListener implements OnTouchListener {

	private boolean zoom = true;
	private int zoomRegion = 30;
	public static final int ZOOMIN = -1;
	public static final int ZOOMOUT = 1;
	private int distance = 0;
	private MainActivityPresenter presenter;
	
	public ImageViewOnTouchListener(MainActivityPresenter presenter){
		this.presenter = presenter;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
		break;
		case MotionEvent.ACTION_MOVE:
			if (zoom) {
				int pointerCount = event.getPointerCount();
				if (pointerCount == 2) {
					int newDistance = (int) Math.sqrt((event.getX(0)-event.getX(1))*(event.getX(0)-event.getX(1))+(event.getY(0)-event.getY(1))*(event.getY(0)-event.getY(1)));
					if (distance == 0) {
						distance = newDistance;
					}
					if (newDistance > distance && newDistance - distance >= zoomRegion ) {
						presenter.transformToTimeLine(newDistance);
					}else if(newDistance < distance && distance - newDistance >= zoomRegion) {
						presenter.transformToMatrix(newDistance);
					}
				}
			}
		break;
		case MotionEvent.ACTION_POINTER_UP:
			distance= 0;
		break;
		case MotionEvent.ACTION_UP:
			distance =0;
		break;
		default:
			break;
		}
		return false;
	}


}
