package workshop.album.Views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageTextButton extends LinearLayout {

	ImageView icon;
	TextView text;
	Context context;
	public ImageTextButton(Context context) {
		super(context);
		this.context = context;
		initial();
		// TODO Auto-generated constructor stub
	}

	public ImageTextButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial();
		// TODO Auto-generated constructor stub
	}
	private void initial(){
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		this.setBackgroundColor(0xffffff);
		icon = new ImageView(context);
		LayoutParams iconParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.addView(icon, iconParams);
		LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		textParams.topMargin = 5;
		text = new TextView(context);
		this.addView(text, textParams);
	}
	//
	public void setICON(Drawable drawable){
		icon.setImageDrawable(drawable);
	}
	//
	public void setText(String text){
		this.text.setText(text);
	}
	//
	public void setImageSize(int width,int height){
		LayoutParams params = (LayoutParams) icon.getLayoutParams();
		params.width = width;
		params.height = height;
		
		icon.setLayoutParams(params);
		icon.invalidate();
	}
	//
	public void setLayoutSize(int width,int height){
		LayoutParams params = (LayoutParams) this.getLayoutParams();
		params.width = width;
		params.height = height;
		this.setLayoutParams(params);
		this.invalidate();
	}

}
