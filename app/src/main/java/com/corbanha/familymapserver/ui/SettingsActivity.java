package com.corbanha.familymapserver.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Filter;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.model.Settings;
import com.corbanha.familymapserver.model.User;
import com.corbanha.familymapserver.net.ServerProxy;
import com.corbanha.familymapserver.net.SignInTask;

public class SettingsActivity extends AppCompatActivity {

    private Spinner lifeStorySpinner;
    private Switch lifeStorySwitch;
    private Spinner familyTreeSpinner;
    private Switch familyTreeSwitch;
    private Spinner spouseSpinner;
    private Switch spouseSwitch;
    private Spinner mapSpinner;
    private Button resyncButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lifeStorySpinner = findViewById(R.id.settings_show_life_story_lines_color_spinner);
        lifeStorySwitch = findViewById(R.id.settings_show_life_story_lines_switch);
        familyTreeSpinner = findViewById(R.id.settings_show_fam_tree_lines_color_spinner);
        familyTreeSwitch = findViewById(R.id.settings_show_fam_tree_lines_switch);
        spouseSpinner = findViewById(R.id.settings_show_spouse_lines_color_spinner);
        spouseSwitch = findViewById(R.id.settings_show_spouse_lines_switch);
        mapSpinner = findViewById(R.id.settings_map_type_spinner);
        resyncButton = findViewById(R.id.settings_resync_button);
        logoutButton = findViewById(R.id.settings_logout_button);

        final Model model = Model.getInstance();
        final Settings settings = model.getSettings();
        final Filter filter = model.getFilter();

        lifeStorySwitch.setChecked(settings.isLifeStoryLinesEnabled());
        familyTreeSwitch.setChecked(settings.isFamilyTreeLinesEnabled());
        spouseSwitch.setChecked(settings.isSpouseLinesEnabled());

        lifeStorySpinner.setSelection(settings.getLifeStoryLinesSelection());
        familyTreeSpinner.setSelection(settings.getFamilyTreeLinesSelection());
        spouseSpinner.setSelection(settings.getSpouseLinesSelection());

        mapSpinner.setSelection(settings.getMapTypeSelection());

        //Add all of the listeners
        lifeStorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setLifeStoryLinesEnabled(isChecked);
            }
        });

        familyTreeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFamilyTreeLinesEnabled(isChecked);
            }
        });

        spouseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setSpouseLinesEnabled(isChecked);
            }
        });

        lifeStorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setLifeStoryLinesSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        familyTreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setFamilyTreeLinesSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setSpouseLinesSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mapSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settings.setMapTypeSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        resyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Model.SignInDetails signInDetails = model.getSignInDetails();
                Model.resetInstance().setSettings(settings);

                SignInTask sit = new SignInTask(new SignInTask.SignInListener() {
                    @Override
                    public void onError(Error e) {
                        Toast.makeText(SettingsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(boolean success, String message) {
                        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();

                        if(success){
                            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }
                }, signInDetails.serverHost, signInDetails.serverPort);

                sit.execute(new User(signInDetails.userName, signInDetails.password, null, null, null, null, null));

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.getInstance().logUserOut();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
