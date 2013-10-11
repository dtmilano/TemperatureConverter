package com.example.i2at.tc.test;

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;

import com.example.i2at.tc.TemperatureConverter;
import com.example.i2at.tc.TemperatureConverterActivity;

public class TemperatureConverterActivityUnitTests extends
        ActivityUnitTestCase<TemperatureConverterActivity> {

    public TemperatureConverterActivityUnitTests(String name) {
        super(TemperatureConverterActivity.class);
        setName(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    
    /* (non-Javadoc)
     * @see android.test.ActivityUnitTestCase#startActivity(android.content.Intent, android.os.Bundle, java.lang.Object)
     */
    @Override
    protected TemperatureConverterActivity startActivity(Intent intent,
            Bundle savedInstanceState, Object lastNonConfigurationInstance) {
        if ( intent == null ) {
            intent = new Intent(getInstrumentation().getTargetContext(), TemperatureConverterActivity.class);
        }
        if ( savedInstanceState == null ) {
            savedInstanceState = new Bundle();
            savedInstanceState.putDouble(TemperatureConverterActivity.CELSIUS_KEY, 123.4);
        }
        return super.startActivity(intent, savedInstanceState, lastNonConfigurationInstance);
    }
    
    public final void testOnCreateBundle() {
        startActivity(null, null, null);
        TemperatureConverterActivity activity = getActivity();
        assertNotNull(activity);
        assertEquals(123.4, activity.getCelsius());
        assertEquals(TemperatureConverter.celsiusToFahrenheit(123.4), activity.getFahrenheit());
    }

}
