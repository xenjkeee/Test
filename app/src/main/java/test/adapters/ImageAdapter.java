package test.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
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

    static class ViewHolder {
        @Bind(R.id.image_item)
        ImageView imageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView( int position, View convertView,
                        ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.image_item,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(parent.getContext()).load(images.get(position))
                .placeholder(R.drawable.ic_attachment).centerCrop()
                .into(viewHolder.imageView);
        return convertView;
    }

}
