/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.data;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Represents a data source for an entity
 * @param <TEntity> The entity type
 */
public abstract class DataSource<TEntity> extends Observable {

    /**
     * Open the data source connection
     */
    public abstract void open();

    /**
     * Get te underlying data source
     * This value should not be changed or connection will be lost
     * @return the underlying data source
     */
    public abstract ArrayList<TEntity> getSource();

    /**
     * Close the data source connection
     */
    public abstract void close();

    /**
     * Notify observers to update with new data
     */
    protected void datasetChanged() {
        setChanged();
        notifyObservers();
    }
}
