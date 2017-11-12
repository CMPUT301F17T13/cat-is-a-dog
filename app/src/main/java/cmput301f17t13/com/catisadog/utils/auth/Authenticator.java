/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.auth;


import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cmput301f17t13.com.catisadog.models.user.CurrentUser;

/**
 * Abstract handler for all Firebase authentication requests
 */
public abstract class Authenticator {

    /**
     * Listener for authentication success or failure
     */
    public interface OnResultListener {
        /**
         * Called if authentication succeeds
         */
        void onAuthSuccess();

        /**
         * Called if authentication fails
         */
        void onAuthFailed();
    }

    Activity mContext;
    FirebaseAuth mAuth;

    private OnResultListener mAuthListener;

    Authenticator(Activity context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Initiate a new sign in request
     */
    public abstract void signIn();

    /**
     * If the user is already signed in (non-null), return success
     */
    public void signInIfAuthenticated() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() != null) {
            onSuccess();
        }
    }

    /**
     * Handle onActivity result callbacks from the parent context
     * @see Activity
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // By default empty
    }

    /**
     * Set result callback listener
     * @param authListener result callback listener
     * @see OnResultListener
     */
    public void setAuthListener(OnResultListener authListener) {
        this.mAuthListener = authListener;
    }

    /**
     * Authenticate the user and call listener onSuccess callback
     */
    void onSuccess() {
        // Sign in the authenticated user
        FirebaseUser user = mAuth.getCurrentUser();
        CurrentUser.signIn(user);

        if(mAuthListener != null) {
            mAuthListener.onAuthSuccess();
        }
    }

    /**
     * Call listener onFailed callback
     */
    void onFailed() {
        if(mAuthListener != null) {
            mAuthListener.onAuthFailed();
        }
    }
}
