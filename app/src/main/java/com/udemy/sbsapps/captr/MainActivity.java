package com.udemy.sbsapps.captr;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    EditText usernameET, passET;
    TextView loginText;

    ImageView logoImageView;
    ConstraintLayout constraintLayout;

    Button signUpButton;

    boolean signUpModeActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = findViewById(R.id.nameEditText);
        passET = findViewById(R.id.passEditText);
        loginText = findViewById(R.id.loginTextView);
        signUpButton = findViewById(R.id.signUpButton);
        logoImageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.backgroundLayout);

        loginText.setOnClickListener(this);
        passET.setOnKeyListener(this);
        logoImageView.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signUpClicked(View view) {
        if( usernameET.getText().toString().matches("") || passET.getText().toString().matches("")) {
            Toast.makeText(this, "A username and password are required.",Toast.LENGTH_SHORT).show();
        } else {
            if(signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameET.getText().toString());
                user.setPassword(passET.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("Signup", "Success");
                        } else {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Login
                ParseUser.logInInBackground(usernameET.getText().toString(), passET.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("Login", "OK!");
                        } else {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginTextView) {
            if(signUpModeActive) {
                signUpModeActive = false;
                String msg = "Login";
                signUpButton.setText(msg);
                msg = "or, Signup";
                loginText.setText(msg);
            } else {
                signUpModeActive = true;
                String msg = "Sign Up";
                signUpButton.setText(msg);
                msg = "or, Login";
                loginText.setText(msg);
            }
        }else {
            if(v.getId() == R.id.imageView || v.getId() == R.id.backgroundLayout) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            signUpClicked(v);
        }
        return false;
    }
}