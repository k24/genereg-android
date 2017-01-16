package com.github.k24.genereg.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.github.k24.genereg.primitive.Primitive;
import com.github.k24.genereg.primitive.PrimitiveStore;

/**
 * Created by k24 on 2017/01/15.
 */

public class SharedPreferencesStore implements PrimitiveStore {
    private final SharedPreferences prefs;

    public SharedPreferencesStore(@NonNull Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public SharedPreferencesStore(@NonNull Context context, String name) {
        if (name == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        } else {
            prefs = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        }
    }

    public SharedPreferencesStore(@NonNull SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public void put(String s, Primitive primitive) {
        editor().begin()
                .put(s, primitive)
                .apply();
    }

    @Override
    public Primitive get(String s) {
        if (!prefs.contains(s)) return null;
        Object value = prefs.getAll().get(s);
        return Primitive.valueOf(value.getClass(), value);
    }

    @Override
    public void remove(String s) {
        editor().begin().remove(s).apply();
    }

    public Editor editor() {
        return new CompatEditor();
    }

    private class CompatEditor implements Editor {
        private SharedPreferences.Editor edit;

        @Override
        public Editor begin() {
            edit = prefs.edit();
            return this;
        }

        @Override
        public Editor put(String s, Primitive primitive) {
            Object value = primitive.value();
            if (value == null) {
                remove(s);
            } else {
                switch (primitive.valueClass().getSimpleName()) {
                    case "boolean":
                    case "Boolean":
                        edit.putBoolean(s, (boolean) value);
                        break;
                    case "float":
                    case "Float":
                        edit.putFloat(s, (float) value);
                        break;
                    case "int":
                    case "Integer":
                        edit.putInt(s, (int) value);
                        break;
                    case "long":
                    case "Long":
                        edit.putLong(s, (long) value);
                        break;
                    case "String":
                        edit.putString(s, (String) value);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            return this;
        }

        @Override
        public Editor remove(String s) {
            edit.remove(s);
            return this;
        }

        @Override
        public void clear() {
            edit.clear().apply();
            edit = null;
        }

        @Override
        public void apply() {
            edit.apply();
            edit = null;
        }
    }
}
