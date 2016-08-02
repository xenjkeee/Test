package test.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import test.service.JsonParser;
import test.R;

import java.util.ArrayList;
import java.util.HashSet;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        SharedPreferences preferences = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        ArrayList<String> projectList = new ArrayList<String>();
        for(String project : preferences.getStringSet("projects",new HashSet<String>())) {
            projectList.add(JsonParser.getProjectName(project));
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinnerProjects);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,projectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Title");
        spinner.setSelection(0);


        Button cancel = (Button) findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Button send= (Button) findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
