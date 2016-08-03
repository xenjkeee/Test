package test.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.R;
import test.fragments.ProjectManagerFragment;
import test.service.JsonParser;

public class ItemAdapter extends BaseAdapter {
    private final List<String> objects;
    private final Context context;
    private final ProjectManagerFragment.DeleteProjectOnClickListener listener;

    public ItemAdapter(Context context, List<String> objects, ProjectManagerFragment.DeleteProjectOnClickListener listener) {
        this.context = context;
        this.objects = objects;
        this.listener = listener;
    }

    public void remove(String object) {
        objects.remove(object);
    }
    @Override
    public int getCount() {
        return objects.size();
    }
    @Override
    public String getItem(int position) {
        return objects.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.project_item, parent, false);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageView popupMenu = (ImageView) convertView.findViewById(R.id.project_item_menu);
        listener.setValue(objects.get(position));
        popupMenu.setOnClickListener(listener);
        viewHolder.textView.setText(JsonParser.getProjectName(objects.get(position)));
        return convertView;
    }
    protected static class ViewHolder {
        @Bind(R.id.project_title)
        public TextView textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }

}
