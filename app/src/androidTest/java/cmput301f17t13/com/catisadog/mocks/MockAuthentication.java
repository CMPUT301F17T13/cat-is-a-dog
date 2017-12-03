/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.mocks;


import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import cmput301f17t13.com.catisadog.models.user.CurrentUser;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MockAuthentication {

    public static void signIn() {
        if(CurrentUser.isAuthenticated()) {
            CurrentUser.signOut();
        }

        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        Mockito.when(mockUser.getEmail()).thenReturn("mockuser@ualberta.ca");
        Mockito.when(mockUser.getUid()).thenReturn("mockUserId");
        Mockito.when(mockUser.getDisplayName()).thenReturn("Mock User");
        Mockito.when(mockUser.getPhotoUrl()).thenReturn(Uri.EMPTY);

        CurrentUser.signIn(mockUser);
    }

    @Test
    public void verifyMockAuthentication() {
        signIn();
        assertEquals(CurrentUser.getInstance().getUserId(), "mockUserId");
        assertEquals(CurrentUser.getInstance().getEmail(), "mockuser@ualberta.ca");
        assertEquals(CurrentUser.getInstance().getDisplayName(), "Mock User");
    }
}
