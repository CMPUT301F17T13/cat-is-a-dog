/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.activities;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

import cmput301f17t13.com.catisadog.R;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setProjectId(getString(R.string.project_id))
                .setApplicationId(getString(R.string.google_app_id))
                .setDatabaseUrl(getString(R.string.firebase_database_url))
                .setGcmSenderId(getString(R.string.gcm_defaultSenderId))
                .setApiKey(getString(R.string.google_api_key))
                .setStorageBucket(getString(R.string.google_storage_bucket))
                .build();

        FirebaseApp.initializeApp(this, firebaseOptions);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        String databaseUrl = FirebaseApp.getInstance().getOptions().getDatabaseUrl();
        Log.i("BaseApplication", "DatabaseUrl: " + databaseUrl);
    }

}
