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
    
    public final void testOnCreateBundle_celsiusKey() {
        final Bundle savedInstanceState = new Bundle();
        savedInstanceState.putDouble(TemperatureConverterActivity.CELSIUS_KEY, 123.4);
        startActivity(null, savedInstanceState, null);
        final TemperatureConverterActivity activity = getActivity();
        assertNotNull(activity);
        final double c = activity.getCelsius();
        assertEquals(123.4, c);
        final double f = activity.getFahrenheit();
        assertEquals(TemperatureConverter.celsiusToFahrenheit(c), f);
    }
    
    public final void testOnCreateBundle_fahrenheitKey() {
        final Bundle savedInstanceState = new Bundle();
        savedInstanceState.putDouble(TemperatureConverterActivity.FAHRENHEIT_KEY, 32.0);
        startActivity(null, savedInstanceState, null);
        final TemperatureConverterActivity activity = getActivity();
        assertNotNull(activity);
        final double f = activity.getFahrenheit();
        assertEquals(32.0, f);
        final double c = activity.getCelsius();
        assertEquals(TemperatureConverter.fahrenheitToCelsius(f), c);
    }
}
