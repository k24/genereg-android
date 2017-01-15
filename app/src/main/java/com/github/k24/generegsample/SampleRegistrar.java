package com.github.k24.generegsample;

import com.github.k24.genereg.registry.BooleanRegistry;
import com.github.k24.genereg.registry.FloatRegistry;
import com.github.k24.genereg.registry.IntRegistry;
import com.github.k24.genereg.registry.LongRegistry;
import com.github.k24.genereg.registry.StringRegistry;

/**
 * Created by k24 on 2017/01/16.
 */

public interface SampleRegistrar {
    BooleanRegistry boolReg();

    FloatRegistry floatReg();

    IntRegistry intReg();

    LongRegistry longReg();

    StringRegistry stringReg();
}
