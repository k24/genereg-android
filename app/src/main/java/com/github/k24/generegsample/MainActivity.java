package com.github.k24.generegsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.github.k24.genereg.Genereg;
import com.github.k24.genereg.prefs.GeneregPrefs;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bool_value)
    CheckBox boolView;

    @BindView(R.id.float_value)
    EditText floatView;

    @BindView(R.id.int_value)
    EditText intView;

    @BindView(R.id.long_value)
    EditText longView;

    @BindView(R.id.string_value)
    EditText stringView;

    @BindViews({R.id.reset, R.id.apply})
    List<Button> buttons;

    private SampleRegistrar registrar;
    private Genereg genereg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        genereg = new GeneregPrefs(this).newGenereg();
        registrar = genereg.newRegistrar(SampleRegistrar.class);

        updateViews();

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.reset:
                        genereg.clear();
                        updateViews();
                        break;
                    case R.id.apply:
                        genereg.put(registrar.boolReg(), boolView.isChecked());
                        if (floatView.length() > 0)
                            genereg.put(registrar.floatReg(), Float.valueOf(floatView.getText().toString()));
                        else
                            genereg.remove(registrar.floatReg());
                        if (intView.length() > 0)
                            genereg.put(registrar.intReg(), Integer.valueOf(intView.getText().toString()));
                        else
                            genereg.remove(registrar.intReg());
                        if (longView.length() > 0)
                            genereg.put(registrar.longReg(), Long.valueOf(longView.getText().toString()));
                        else
                            genereg.remove(registrar.longReg());
                        if (stringView.length() > 0)
                            genereg.put(registrar.stringReg(), stringView.getText().toString());
                        else
                            genereg.remove(registrar.stringReg());
                        genereg.apply();
                        break;
                }
            }
        };
        ButterKnife.apply(buttons, new ButterKnife.Action<Button>() {
            @Override
            public void apply(@NonNull Button view, int index) {
                view.setOnClickListener(onClickListener);
            }
        });
    }

    private void updateViews() {
        boolView.setChecked(Boolean.TRUE.equals(registrar.boolReg().get()));
        floatView.setText(toString(registrar.floatReg().get()));
        intView.setText(toString(registrar.intReg().get()));
        longView.setText(toString(registrar.longReg().get()));
        stringView.setText(toString(registrar.stringReg().get()));
    }

    private static String toString(Object object) {
        return object == null ? null : String.valueOf(object);
    }
}
