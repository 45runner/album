package workshop.album.Widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

public class dotVerticalLine extends View {

	int l,r,b,t;
	private Canvas c;
	public dotVerticalLine(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public dotVerticalLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public dotVerticalLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		drawLine(l,t,r,b,canvas);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		l = left;
		r = right;
		t = top;
		b= bottom;
		
		this.invalidate();
	}
	
	private void drawLine(int l,int t,int r,int b,Canvas canvas){
		/*
		Paint clear = new Paint();
		clear.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(clear);
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		int startX = (r-l)/2;
		float radio = 0.5f;
		int startY = t;
		int distance = b-t;
		for (int i = startY; i < distance;i+=4*radio) {
			canvas.drawCircle(startX, i, radio, paint);
		}
		*/
		///*
		  Paint paint = new Paint();
	      paint.setStyle(Paint.Style.STROKE);
	      paint.setColor(Color.DKGRAY);
	      Path path = new Path();     
	      path.moveTo((r-l)/2, t);
	      path.lineTo((r-l)/2, b);      
	      PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},2);
	      paint.setPathEffect(effects);
	      canvas.drawPath(path, paint);
	     // */
	}

}
