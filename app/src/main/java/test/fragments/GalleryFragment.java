package test.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.R;
import test.activities.SendActivity;
import test.adapters.ImageAdapter;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private ArrayList<String> images;

    @Bind(R.id.galleryGridView)
    protected GridView gallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.gallery_fragment, container, false);
        ButterKnife.bind(this,rootView);
        images = new ArrayList<>();
        gallery.setAdapter(new ImageAdapter(getActivity(),images, getAllShownImagesPath(getActivity())));

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (null != images && !images.isEmpty() ) {
                    Intent SendIntent = new Intent(getActivity(), SendActivity.class);
                    startActivity(SendIntent);
                }


            }
        });
        return rootView;
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.MediaColumns.DATE_MODIFIED};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, "3 desc");

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            cursor.close();
        }

        return listOfAllImages;
    }
}
