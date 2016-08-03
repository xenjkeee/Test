package test.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.Constants;
import test.service.JsonParser;
import test.R;

import java.util.ArrayList;
import java.util.HashSet;

public class SendActivity extends AppCompatActivity {

    @Bind(R.id.spinnerProjects)
    protected Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);

        SharedPreferences preferences = getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        ArrayList<String> projectList = new ArrayList<>();
        for(String project : preferences.getStringSet(Constants.PROJECTS,new HashSet<String>())) {
            projectList.add(JsonParser.getProjectName(project));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,projectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);

    }
    @OnClick(R.id.btn_send)
    public void send(View view) {
        finish();
    }

    @OnClick(R.id.btn_cancel)
    public void cancel(View view) {
        finish();
    }
}
