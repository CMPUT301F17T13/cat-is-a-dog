/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;

import cmput301f17t13.com.catisadog.R;
import cmput301f17t13.com.catisadog.activities.summary.HabitSummaryActivity;
import cmput301f17t13.com.catisadog.utils.auth.Authenticator;
import cmput301f17t13.com.catisadog.utils.auth.GoogleAuthenticator;
import cmput301f17t13.com.catisadog.utils.testing.SimpleIdlingResource;

/**
 * View to sign in a user.
 * Currently only supports Google OAuth authentication.
 */
public class LoginActivity extends Activity implements
        View.OnClickListener,
        Authenticator.OnResultListener {

    private static final String TAG = "LoginActivity";

    // The Idling Resource which will be null in production.
    @Nullable private SimpleIdlingResource mIdlingResource;

    private ProgressBar loginProgressBar;

    private Authenticator mAuthenticator;

    /**
     * Setup firebase and get ready to log in user
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the dimensions of the Google sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress);

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
                loginProgressBar.setVisibility(View.VISIBLE);
                mAuthenticator.signIn();

                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }

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
        loginProgressBar.setVisibility(View.GONE);
        Toast.makeText(LoginActivity.this, "Signed in!", Toast.LENGTH_SHORT).show();

        Intent transition = new Intent(LoginActivity.this, HabitSummaryActivity.class);
        startActivity(transition);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }

        finish();
    }

    /**
     * Handler if authentication fails
     */
    @Override
    public void onAuthFailed() {
        loginProgressBar.setVisibility(View.GONE);
        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
