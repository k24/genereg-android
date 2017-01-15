package com.github.k24.genereg.prefs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.k24.genereg.Genereg;
import com.github.k24.genereg.registry.BooleanRegistry;
import com.github.k24.genereg.registry.FloatRegistry;
import com.github.k24.genereg.registry.IntRegistry;
import com.github.k24.genereg.registry.LongRegistry;
import com.github.k24.genereg.registry.StringRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class GeneregPrefsTest {

    private GeneregPrefs generegPrefs;
    private SharedPreferences prefs;

    @Before
    public void setUp() {
        generegPrefs = new GeneregPrefs(RuntimeEnvironment.application);
        prefs = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application);
        prefs.edit().clear().apply();
    }

    @Test
    public void newGenereg_newRegistry() throws Exception {
        Genereg genereg = generegPrefs.newGenereg();

        StringRegistry stringRegistry = genereg.newRegistry(StringRegistry.class, "string");
        IntRegistry intRegistry = genereg.newRegistry(IntRegistry.class, "int", 123);

        // Verify nothing stored
        assertThat(stringRegistry.get()).isNull();
        assertThat(intRegistry.get()).isEqualTo(123);

        assertThat(prefs.contains("string")).isFalse();
        assertThat(prefs.contains("int")).isFalse();

        // Store them
        stringRegistry.put("value");
        intRegistry.put(456);

        // Verify stored
        assertThat(prefs.getString("string", null)).isEqualTo("value");
        assertThat(prefs.getInt("int", 0)).isEqualTo(456);
    }

    @Test
    public void newGenereg_editor() throws Exception {
        Genereg genereg = generegPrefs.newGenereg();

        StringRegistry stringRegistry = genereg.newRegistry(StringRegistry.class, "string");
        IntRegistry intRegistry = genereg.newRegistry(IntRegistry.class, "int", 123);

        // Store them
        genereg.put(stringRegistry, "value")
                .put(intRegistry, 456)
                .apply();

        // Verify stored
        assertThat(prefs.getString("string", null)).isEqualTo("value");
        assertThat(prefs.getInt("int", 0)).isEqualTo(456);

        // Clear all
        genereg.clear();

        // Verify cleared
        assertThat(stringRegistry.get()).isNull();
        assertThat(intRegistry.get()).isEqualTo(123);

        assertThat(prefs.contains("string")).isFalse();
        assertThat(prefs.contains("int")).isFalse();
    }

    @Test
    public void newGenereg_registrar() throws Exception {
        Genereg genereg = generegPrefs.newGenereg();

        TestRegistrar registrar = genereg.newRegistrar(TestRegistrar.class);

        // Verify nothing stored
        assertThat(prefs.contains("stringValue")).isFalse();
        assertThat(prefs.contains("intValue")).isFalse();
        assertThat(prefs.contains("boolReg")).isFalse();
        assertThat(prefs.contains("floatReg")).isFalse();
        assertThat(prefs.contains("longReg")).isFalse();

        // Verify default value
        assertThat(registrar.stringValue("foo")).isEqualTo("foo");
        assertThat(registrar.intValue(12)).isEqualTo(12);

        // Store them
        registrar.setStringValue("bar");
        registrar.setIntValue(21);
        registrar.boolReg().put(true);
        registrar.floatReg().put(34f);
        registrar.longReg().put(56L);

        // Verify stored
        assertThat(prefs.getString("stringValue", null)).isEqualTo("bar");
        assertThat(prefs.getInt("intValue", 0)).isEqualTo(21);
        assertThat(prefs.getBoolean("boolReg", false)).isTrue();
        assertThat(prefs.getFloat("floatReg", 0)).isEqualTo(34f);
        assertThat(prefs.getLong("longReg", 0)).isEqualTo(56L);

        // Verify getting value
        assertThat(registrar.stringValue(null)).isEqualTo("bar");
        assertThat(registrar.intValue(0)).isEqualTo(21);
        assertThat(registrar.boolReg().get()).isTrue();
        assertThat(registrar.floatReg().get()).isEqualTo(34f);
        assertThat(registrar.longReg().get()).isEqualTo(56L);
    }

    public interface TestRegistrar {
        String stringValue(String defaultValue);

        int intValue(int defaultValue);

        void setStringValue(String defaultValue);

        void setIntValue(int defaultValue);

        BooleanRegistry boolReg();

        FloatRegistry floatReg();

        LongRegistry longReg();
    }
}