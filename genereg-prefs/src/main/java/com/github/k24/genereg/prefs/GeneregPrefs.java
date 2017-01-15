package com.github.k24.genereg.prefs;

import android.content.Context;

import com.github.k24.genereg.Genereg;

/**
 * Created by k24 on 2017/01/15.
 */

public class GeneregPrefs {
    private final Context context;

    public GeneregPrefs(Context context) {
        this.context = context;
    }

    public Genereg newGenereg() {
        SharePreferencesStore store = new SharePreferencesStore(context);

        return new Genereg.Builder()
                .primitiveStore(store)
                .primitiveEditor(store.editor())
                .build();
    }
}
