/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.utils.auth.Authenticator;
import cmput301f17t13.com.catisadog.utils.auth.GoogleAuthenticator;

/**
 * View to sign in a user.
 * Currently only supports Google OAuth authentication.
 */
public class LoginActivity extends Activity implements
        View.OnClickListener,
        Authenticator.OnResultListener {

    private static final String TAG = "LoginActivity";

    private Authenticator mAuthenticator;

    /**
     * Setup firebase and get ready to log in user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        // Set the dimensions of the Google sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        mAuthenticator = new GoogleAuthenticator(this);
        mAuthenticator.setAuthListener(this);
    }

    /**
     * Try to authenticate user on start
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuthenticator.signInIfAuthenticated();
    }

    /**
     * onClick handler for nested views such as sign in button
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                mAuthenticator.signIn();
                break;
        }
    }

    /**
     * Handle activity results
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthenticator.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Once the user is authenticated go to the HabitSummary activity
     * @see HabitSummaryActivity
     */
    @Override
    public void onAuthSuccess() {
        Toast.makeText(LoginActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();

        Intent transition = new Intent(LoginActivity.this, HabitSummaryActivity.class);
        startActivity(transition);
        finish();
    }

    /**
     * Handler if authentication fails
     */
    @Override
    public void onAuthFailed() {
        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }
}
