/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.data;

import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Represents a data source for an entity
 * @param <TEntity> The entity type
 */
public abstract class DataSource<TEntity> extends Observable
       implements ChildEventListener {

    public abstract ArrayList<TEntity> getSource();

}
