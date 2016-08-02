package test.fragments;

/**
 * Created by Стас on 02.08.2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;


import test.service.JsonParser;
import test.activities.MainActivity;
import test.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProjectManagerFragment extends Fragment {
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private SharedPreferences preferences;
    private SharedPreferences.Editor preferencesEditor;
    protected List<String> projectsList;
    private ArrayAdapter<String> adapter;
    private RelativeLayout projectsNotFound;
    private RelativeLayout projectsFound;

    public void applyChangesPreferencesToList() {
        projectsList.clear();
        projectsList.addAll(preferences.getStringSet("projects",null));
        adapter.notifyDataSetChanged();
        switchLayouts();
    }

    private void switchLayouts() {
        if(projectsList.isEmpty()) {
            projectsNotFound.setVisibility(View.VISIBLE);
            projectsFound.setVisibility(View.INVISIBLE);
        }else{
            projectsNotFound.setVisibility(View.INVISIBLE);
            projectsFound.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.project_manager_fragment, container, false);

        projectsNotFound = (RelativeLayout) rootView.findViewById(R.id.no_projects);
        projectsFound = (RelativeLayout) rootView.findViewById(R.id.projects_found);

        preferences = getActivity().getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        projectsList = new ArrayList<>();
        if(preferences.getStringSet("projects",null) != null)
            projectsList.addAll(preferences.getStringSet("projects",new HashSet<String>()));
        switchLayouts();
        adapter = new ItemAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                projectsList,
                preferences,preferencesEditor,projectsNotFound,projectsFound);

        ListView listView = (ListView) rootView.findViewById(R.id.projectsList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ImageButton button = (ImageButton) rootView.findViewById(R.id.addProject);
        button.setOnClickListener(new AddProject());
        ImageButton button1 = (ImageButton) rootView.findViewById(R.id.addProject1);
        button1.setOnClickListener(new AddProject());


        return rootView;
    }

    class AddProject implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException anfe) {
                showDialog(getActivity(), "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }
    }




    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message,
                                          CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                if(!preferences.contains("projects")) {
                    Set<String> projects = new HashSet<String>();
                    projects.add(intent.getStringExtra("SCAN_RESULT"));
                    preferencesEditor.putStringSet("projects", projects).apply();
                }else{
                    Set<String> newSet = preferences.getStringSet("projects",null);
                    newSet.add(intent.getStringExtra("SCAN_RESULT"));
                    preferencesEditor.putStringSet("projects",newSet).apply();
                }
                applyChangesPreferencesToList();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        preferencesEditor.apply();
    }


    /**
     * Created by Стас on 28.07.2016.
     */
    private static class ItemAdapter extends ArrayAdapter<String> {
        private List<String> objects;
        private Context context;
        private SharedPreferences preferences;
        private SharedPreferences.Editor preferencesEditor;
        private RelativeLayout projectsNotFound; RelativeLayout projectsFound;



        public ItemAdapter(Context context, int textViewResourceId, List<String> objects,
                           SharedPreferences preferences, SharedPreferences.Editor preferencesEditor,
                           RelativeLayout projectsNotFound, RelativeLayout projectsFound) {
            super(context, textViewResourceId);
            this.context = context;
            this.objects = objects;
            this.preferences = preferences;
            this.preferencesEditor = preferencesEditor;
            this.projectsFound = projectsFound;
            this.projectsNotFound = projectsNotFound;

        }

        public void applyChangesListToPreferences() {
            Set<String> stringSet = preferences.getStringSet("projects",null);
            if(stringSet != null) {
                stringSet.clear();
            }
            if(!objects.isEmpty())
                stringSet.addAll(objects);
            preferencesEditor.putStringSet("projects",stringSet).apply();
        }
        @Override
        public void add(String object) {
            if(getCount() == 0) {
                projectsNotFound.setVisibility(View.INVISIBLE);
                projectsFound.setVisibility(View.VISIBLE);
            }
            objects.add(object);
        }
        @Override
        public void remove(String object) {
            objects.remove(object);
            if(getCount()==0) {
                projectsNotFound.setVisibility(View.VISIBLE);
                projectsFound.setVisibility(View.INVISIBLE);
            }
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
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.project_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.project_title);
            textView.append( JsonParser.getProjectName(objects.get(position)));

            ImageView popupMenu = (ImageView) rowView.findViewById(R.id.project_item_menu);

            final String value = objects.get(position);
            popupMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        showPopupMenu(view,value);
                }
            });
            return rowView;
        }
        private void showPopupMenu(View view, final String value) {
            PopupMenu popupMenu = new PopupMenu(context,view);
            popupMenu.inflate(R.menu.project_popup_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.deleteProject: remove(value);
                            applyChangesListToPreferences();
                            notifyDataSetChanged();
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }

    }
}