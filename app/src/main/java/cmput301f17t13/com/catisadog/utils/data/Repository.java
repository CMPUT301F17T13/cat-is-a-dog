/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.data;

/**
 * Indicates a CRUD Repository service
 * @param <TEntity> The entity type
 */
public interface Repository<TEntity> {
    void add(TEntity entity);
    void update(String key, TEntity entity);
    void delete(String key);
    void get(String key, OnResultListener<TEntity> resultListener);

    /**
     * Query callback interface for a repository container
     * @param <TResult> the entity type of the result
     */
    interface OnResultListener<TResult> {
        /**
         * Callback function passing back the parsed entity
         * @param result The result, null if no value found
         */
        void onResult(TResult result);
    }
}
