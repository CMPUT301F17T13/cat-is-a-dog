/*
 * Copyright 2017 James Hryniw, Leland Jansen, Nathan Liebrecht, Abid Rahman, Kevin Wang - All Rights Reserved.
 * Licensed under the Apache License, Version 2.0. Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package cmput301f17t13.com.catisadog.utils.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManagedArray<TKey, TValue> {

    private HashMap<TKey, Integer> mMap;
    private ArrayList<TValue> mArray;

    public ManagedArray() {
        mMap = new HashMap<>();
        mArray = new ArrayList<>();
    }

    public ArrayList<TValue> getArray() {
        return mArray;
    }

    public void add(TKey key, TValue value) {
        mMap.put(key, mArray.size());
        mArray.add(value);
    }

    public void update(TKey key, TValue newValue) {
        int index = mMap.get(key);
        mArray.set(index, newValue);
    }

    public void delete(TKey key) {
        // swap with the last element
        int lastIndex = mArray.size() - 1;
        TValue lastValue = mArray.get(lastIndex);


        // pop from list

        mArray.remove(lastIndex);
    }
}
