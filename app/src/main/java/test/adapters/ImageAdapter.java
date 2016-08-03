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

    private final Activity context;
    private ArrayList<String> images;

    public ImageAdapter(Activity localContext, ArrayList<String> images, ArrayList<String> allShown) {
        this(localContext);
        this.images = images;
        this.images.addAll(allShown);
    }

    private ImageAdapter(Activity localContext) {
        context = localContext;
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

    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ImageView picturesView;
        if (convertView == null) {
            picturesView = new ImageView(context);
            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            picturesView
                    .setLayoutParams(new GridView.LayoutParams(270, 270));

        } else {
            picturesView = (ImageView) convertView;
        }

        Glide.with(context).load(images.get(position))
                .placeholder(R.drawable.ic_attachment).centerCrop()
                .into(picturesView);
        return picturesView;
    }

}
