package com.corbanha.familymapserver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.corbanha.familymapserver.R;
import com.corbanha.familymapserver.model.User;
import com.corbanha.familymapserver.net.RegisterTask;
import com.corbanha.familymapserver.net.SignInTask;


public class LoginFragment extends Fragment {

    private Button signInButton;
    private Button registerButton;
    private EditText serverHostEditText;
    private EditText serverPortEditText;
    private EditText userNameEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private OnLoginListener parentListener;

    public interface OnLoginListener{
        void onPlayerLoggedIn();
    }

    public static LoginFragment newInstance(OnLoginListener listener) {
        LoginFragment fragment = new LoginFragment();
        fragment.parentListener = listener;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        signInButton = (Button) v.findViewById(R.id.signInButton);
        registerButton = (Button) v.findViewById(R.id.registerButton);

        serverHostEditText = (EditText) v.findViewById(R.id.serverHostEditText);
        serverPortEditText = (EditText) v.findViewById(R.id.serverPortEditText);
        userNameEditText = (EditText) v.findViewById(R.id.userNameEditText);
        passwordEditText = (EditText) v.findViewById(R.id.passwordEditText);
        firstNameEditText = (EditText) v.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) v.findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) v.findViewById(R.id.emailEditText);
        maleRadioButton = (RadioButton) v.findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton) v.findViewById(R.id.femaleRadioButton);

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int serverPort;

                try{
                    serverPort = Integer.parseInt(serverPortEditText.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Please enter a valid Server Port", Toast.LENGTH_SHORT).show();
                    return;
                }

                SignInTask sit = new SignInTask(new SignInTask.SignInListener() {
                    @Override
                    public void onError(Error e) {
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(boolean success, String message) {
                        if(success){
                            parentListener.onPlayerLoggedIn();
                        }
                    }
                }, serverHostEditText.getText().toString(), serverPort);


                String gender = "m";
                if(femaleRadioButton.isChecked())
                    gender = "f";


                Toast.makeText(getContext(), "Logging you in...", Toast.LENGTH_SHORT).show();
                sit.execute(new User(
                        userNameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        gender, null
                ));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Register the new User!!

                int serverPort = 0;

                try{
                    serverPort = Integer.parseInt(serverPortEditText.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Please enter a valid Server Port", Toast.LENGTH_SHORT).show();
                    return;
                }

                RegisterTask rt = new RegisterTask(new RegisterTask.RegisterListener() {
                    @Override
                    public void onError(Error e) {
                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(boolean success, String message) {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        if(success){
                            parentListener.onPlayerLoggedIn();
                        }
                    }
                }, serverHostEditText.getText().toString(), serverPort);


                String gender = "m";
                if(femaleRadioButton.isChecked())
                    gender = "f";

                rt.execute(new User(
                        userNameEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        gender, null
                ));
            }
        });

        TextWatcher editTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updateButtonsDisabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        serverHostEditText.addTextChangedListener(editTextWatcher);
        serverPortEditText.addTextChangedListener(editTextWatcher);
        userNameEditText.addTextChangedListener(editTextWatcher);
        passwordEditText.addTextChangedListener(editTextWatcher);
        firstNameEditText.addTextChangedListener(editTextWatcher);
        lastNameEditText.addTextChangedListener(editTextWatcher);
        emailEditText.addTextChangedListener(editTextWatcher);

        maleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateButtonsDisabled();
            }
        });

        femaleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateButtonsDisabled();
            }
        });

        updateButtonsDisabled();

        return v;
    }

    /**
     * Whenever the user updates an editText, will update the buttons to be either
     * enabled or disabled if the cooresponding edittexts are not filled in
     */
    private void updateButtonsDisabled(){

        if(serverHostEditText.getText().toString().equals("") ||
                serverPortEditText.getText().toString().equals("") ||
                userNameEditText.getText().toString().equals("") ||
                passwordEditText.getText().toString().equals("")){

            signInButton.setEnabled(false);
            registerButton.setEnabled(false);
            return;
        }

        signInButton.setEnabled(true);
        registerButton.setEnabled(true);

        if(firstNameEditText.getText().toString().equals("") ||
                lastNameEditText.getText().toString().equals("") ||
                emailEditText.getText().toString().equals("") ||
                (!maleRadioButton.isChecked() && !femaleRadioButton.isChecked())){
            registerButton.setEnabled(false);
        }
    }
}
