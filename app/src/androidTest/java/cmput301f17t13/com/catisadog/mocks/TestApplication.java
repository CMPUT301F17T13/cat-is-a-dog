/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.mocks;

import android.app.Application;
import android.os.Handler;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import cmput301f17t13.com.catisadog.R;

import static org.junit.Assert.*;

/**
 * Sets the test application environment
 */
@RunWith(AndroidJUnit4.class)
public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setProjectId(getString(R.string.mock_project_id))
                .setApplicationId(getString(R.string.mock_google_app_id))
                .setDatabaseUrl(getString(R.string.mock_firebase_database_url))
                .setGcmSenderId(getString(R.string.mock_gcm_defaultSenderId))
                .setApiKey(getString(R.string.mock_google_api_key))
                .setStorageBucket(getString(R.string.mock_google_storage_bucket))
                .build();

        FirebaseApp.initializeApp(this, firebaseOptions);
        FirebaseAuth.getInstance().signOut();
    }

    @Test
    public void useMockConfig() throws Exception {
        // Check that we are using the right configuration
        String databaseUrl = FirebaseApp.getInstance().getOptions().getDatabaseUrl();
        assertEquals("Not using mock Firebase database",
                "ws://10.0.2.2:5000", databaseUrl);

        assertEquals("Database not using mock application",
                FirebaseApp.getInstance(), FirebaseDatabase.getInstance().getApp());
    }

    @Test
    public void hitFirebaseServer() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        FirebaseDatabase.getInstance().getReference("test").setValue("test");

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                assertTrue(dataSnapshot.getValue() != null);
                Log.i("TestApplication", "Success!!");
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        boolean didNotTimeout = latch.await(10, TimeUnit.SECONDS);
        assertTrue(didNotTimeout);
    }
}
