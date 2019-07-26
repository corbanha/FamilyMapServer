package com.corbanha.familymapserver.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.ui.MapsFragment;

public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "Event";

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.event_activity_frag_container);

        model = Model.getInstance();

        String eventID = getIntent().getStringExtra(EventActivity.EXTRA_EVENT_ID);

        if(fragment == null) {
            if(eventID != null){
                fragment = MapsFragment.newInstance(eventID);
            }else{
                fragment = MapsFragment.newInstance();
            }


            fm.beginTransaction().add(R.id.event_activity_frag_container, fragment).commit();
        }


    }
}
