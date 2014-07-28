package workshop.album.Views;

import workshop.album.ui.Activitys.MainActivity;
import workshop.album.ui.Activitys.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnClickListener;

public class MyImageView extends ImageView implements OnClickListener {
	private Bitmap bitmap;
	private int width;
	private Paint paint;
	private Context context;
	private int [] position;
	private int index;
	public MyImageView(Context context,int width,int[] position) {
		super(context);
		this.context = context;
		this.width = width;
		this.position = position;
		initialPaint();
		this.setOnClickListener(this);
	}
	//
	public MyImageView(Context context,int width,int index) {
		super(context);
		this.context = context;
		this.width = width;
		this.index = index;
		initialPaint();
		this.setOnClickListener(this);
	}
	//
	@Override
	public void draw(Canvas canvas) {
		if (bitmap!= null) {
			int bw = bitmap.getWidth();
			int bh = bitmap.getHeight();
			int bcx = bw/2;
			int bcy = bh/2;
			int halfw= width/2;
			Rect src = new Rect(bcx-halfw, bcy-halfw, bcx+halfw, bcy+halfw);
			Rect dst = new Rect(0,0,width,width);
			//Ä£·ÂscaleType£½centreCrop
			canvas.drawBitmap(bitmap, src, dst, null);
			//»­±ß½ç
			canvas.drawRect(dst, paint);
		}
	}
	//
	public void setImageViewBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
		this.invalidate();
	}
	//
	private void initialPaint(){
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(6);
		paint.setStyle(Style.STROKE);
	}
	@Override
	public void onClick(View v) {
		ImageViewerFragment fragment = ImageViewerFragment.newInstance(position);
		FragmentTransaction transaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.Container, fragment);
		transaction.commit();
	}
}
