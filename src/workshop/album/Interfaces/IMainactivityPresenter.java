package workshop.album.Interfaces;

import java.util.ArrayList;

import workshop.album.data.Beans.ImageGroupBean;
import android.view.View;

public interface IMainactivityPresenter {
	void loadImageView(View v,ArrayList<ImageGroupBean> data);
	void transitionAnimation(int distance);
}
