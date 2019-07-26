package com.corbanha.familymapserver.ui;

import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.Toast;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.Model;
import com.corbanha.familymapserver.model.User;
import com.corbanha.familymapserver.net.RegisterTask;
import com.corbanha.familymapserver.net.ServerProxy;
import com.corbanha.familymapserver.net.SignInTask;
import com.google.android.gms.maps.MapFragment;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Model model = Model.getInstance();

        if(model.userLoggedIn()){
            showMapFragment();
        }else{
            showLoginFragment();
        }
    }

    private void showLoginFragment(){
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.main_activity_frag_container);

        if(fragment == null){
            fragment = LoginFragment.newInstance(this);
            fm.beginTransaction().add(R.id.main_activity_frag_container, fragment).commit();
        }else if(!(fragment instanceof LoginFragment)){
            fragment = LoginFragment.newInstance(this);
            fm.beginTransaction().replace(R.id.main_activity_frag_container, fragment).commit();
        }
    }

    private void showMapFragment(){
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.main_activity_frag_container);

        if(fragment == null){
            fragment = MapsFragment.newInstance();
            fm.beginTransaction().add(R.id.main_activity_frag_container, fragment).commit();
        }else if(!(fragment instanceof MapsFragment)){
            fragment = MapsFragment.newInstance();
            fm.beginTransaction().replace(R.id.main_activity_frag_container, fragment).commit();
        }
    }

    @Override
    public void onPlayerLoggedIn() {
        //Switch to the map fragment
        showMapFragment();
    }
}
