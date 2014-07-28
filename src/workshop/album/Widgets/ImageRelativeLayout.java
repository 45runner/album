package workshop.album.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageRelativeLayout extends RelativeLayout {

	public ImageRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.e("Real-----------------", "onInterceptTouchEvent");
		int action = ev.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_POINTER_DOWN:
			return true;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
