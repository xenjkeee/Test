package test.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import test.R;

public class ImageAdapter extends BaseAdapter {

    private ArrayList<String> images;

    public ImageAdapter(ArrayList<String> images, ArrayList<String> allShown) {
        this.images = images;
        this.images.addAll(allShown);
    }


    public int getCount() {
        return images.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView( int position, View convertView,
                        ViewGroup parent) {
        ImageView picturesView;
        if (convertView == null) {
            picturesView = new ImageView(parent.getContext());
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView
                    .setLayoutParams(new GridView.LayoutParams(270, 270));
        } else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(parent.getContext()).load(images.get(position))
                .placeholder(R.drawable.ic_attachment).centerCrop()
                .into(picturesView);
        return picturesView;
    }

}
