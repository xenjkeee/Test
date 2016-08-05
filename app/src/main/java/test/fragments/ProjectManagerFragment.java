package test.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;


import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.Constants;
import test.adapters.ItemAdapter;
import test.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ProjectManagerFragment extends Fragment {
    private SharedPreferences preferences;
    private List<String> projectsList;
    private ItemAdapter adapter;

    @Bind(R.id.no_projects)
    protected RelativeLayout projectsNotFound;
    @Bind(R.id.projects_found)
    protected RelativeLayout projectsFound;
    @Bind(R.id.projectsList)
    protected ListView listView;

    @BindString(R.string.no_scanner)
    protected String noScanner;
    @BindString(R.string.download_scanner_activity)
    protected  String downloadScanner;
    @BindString(R.string.yes)
    protected String yesDownload;
    @BindString(R.string.no)
    protected String noDownload;


    private void applyChangesPreferencesToList() {
        projectsList.clear();
        projectsList.addAll(preferences.getStringSet(Constants.PROJECTS,new HashSet<String>()));
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

        ButterKnife.bind(this,rootView);

        preferences = getActivity().getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        projectsList = new ArrayList<>();
        if(preferences.getStringSet(Constants.PROJECTS,null) != null)
            projectsList.addAll(preferences.getStringSet(Constants.PROJECTS,new HashSet<String>()));
        switchLayouts();
        adapter = new ItemAdapter(projectsList,new DeleteProjectOnClickListener());

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return rootView;
    }

    @OnClick({R.id.addProject,R.id.addProject1})
    public void addProject(View view) {
        try {
            Intent intent = new Intent(Constants.ACTION_SCAN);
            intent.putExtra(Constants.SCAN_MODE, Constants.QR_CODE_MODE);
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            showDialog(getActivity(), noScanner,downloadScanner,yesDownload,noDownload)
                    .show();
        }
    }





    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message,
                                          CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse(Constants.MARKET_URI);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(Constants.APP_LOG_TAG, "exception", e);
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
                if(!preferences.contains(Constants.PROJECTS)) {
                    Set<String> projects = new HashSet<>();
                    projects.add(intent.getStringExtra(Constants.SCAN_RESULT));
                    preferences.edit().putStringSet(Constants.PROJECTS, projects).apply();
                }else{
                    Set<String> newSet = preferences.getStringSet(Constants.PROJECTS,null);
                    if (newSet != null) {
                        newSet.add(intent.getStringExtra(Constants.SCAN_RESULT));
                    }
                    preferences.edit().putStringSet(Constants.PROJECTS,newSet).apply();
                }
                applyChangesPreferencesToList();
            }
        }
    }

    private void applyChangesListToPreferences() {
        Set<String> stringSet = preferences.getStringSet(Constants.PROJECTS,null);
        if(stringSet != null) {
            stringSet.clear();
        }
        if(!projectsList.isEmpty())
            if (stringSet != null) {
                stringSet.addAll(projectsList);
            }
        preferences.edit().putStringSet(Constants.PROJECTS,stringSet).apply();
        if (stringSet != null && stringSet.isEmpty()) {
            projectsFound.setVisibility(View.INVISIBLE);
            projectsNotFound.setVisibility(View.VISIBLE);
        }
    }
    private void showPopupMenu(View view, final String value) {
        PopupMenu popupMenu = new PopupMenu(getActivity(),view);
        popupMenu.inflate(R.menu.project_popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.deleteProject: adapter.remove(value);
                        applyChangesListToPreferences();
                        adapter.notifyDataSetChanged();
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
    public class DeleteProjectOnClickListener implements View.OnClickListener {
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view,value);
        }
    }

}