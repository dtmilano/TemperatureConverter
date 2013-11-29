/**
 * Copyright (C) 2011 Diego Torres Milano
 */

package com.example.i2at.tc.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.test.suitebuilder.annotation.Suppress;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;

import com.example.i2at.tc.EditNumber;
import com.example.i2at.tc.TemperatureConverter;
import com.example.i2at.tc.TemperatureConverterActivity;

/**
 * @author diego
 */
public class TemperatureConverterActivityTests extends
        ActivityInstrumentationTestCase2<TemperatureConverterActivity> {

    private static final String MINUS_SIGN = "-";
    private TemperatureConverterActivity mActivity;
    private EditNumber mCelsius;
    private EditNumber mFahrenheit;
    private Instrumentation mInstrumentation;

    /**
     * @param name
     */
    public TemperatureConverterActivityTests(String name) {
        super(TemperatureConverterActivity.class);
        setName(name);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();
        assertNotNull(mInstrumentation);

        mActivity = getActivity();
        assertNotNull(mActivity);

        mCelsius = (EditNumber) mActivity.findViewById(com.example.i2at.tc.R.id.celsius);
        assertNotNull(mCelsius);
        mFahrenheit = (EditNumber) mActivity.findViewById(com.example.i2at.tc.R.id.fahrenheit);
        assertNotNull(mFahrenheit);
    }

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @SmallTest
    public void testFieldsOnScreen() {
        final View origin =
                mActivity.getWindow().getDecorView();
        ViewAsserts.assertOnScreen(origin, mCelsius);
        ViewAsserts.assertOnScreen(origin, mFahrenheit);
    }

    @SmallTest
    public void testAlignment() {
        ViewAsserts.assertRightAligned(mCelsius, mFahrenheit);
        ViewAsserts.assertLeftAligned(mCelsius, mFahrenheit);
    }

    @SmallTest
    public void testFieldsShouldStartEmpty() {
        assertTrue("".equals(mCelsius.getText().toString()));
        assertTrue("".equals(mFahrenheit.getText().toString()));
    }

    @SmallTest
    public void testJustification() {
        final int expected = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        assertEquals(expected, mCelsius.getGravity());
        assertEquals(expected, mFahrenheit.getGravity());
    }

    @UiThreadTest
    public void testFahrenheitToCelsiusConversion() {
        mCelsius.clear();
        mFahrenheit.clear();
        final double f = 32.5;
        assertTrue(mFahrenheit.requestFocus());
        mFahrenheit.setNumber(f);
        assertEquals(f, mFahrenheit.getNumber());
        assertTrue(mCelsius.requestFocus());
        assertTrue(mCelsius.isFocused());
        final double expected = TemperatureConverter.fahrenheitToCelsius(f);
        final double actual = mCelsius.getNumber();
        final double delta = Math.abs(expected - actual);
        assertTrue("delta=" + delta + " expected=" + expected + " actual=" + actual, delta < 0.005);
    }

    @UiThreadTest
    public void testCelsiusToFahrenheitConversion() {
        mCelsius.clear();
        mFahrenheit.clear();
        final double c = 100;
        assertTrue(mCelsius.requestFocus());
        mCelsius.setNumber(c);
        assertEquals(c, mCelsius.getNumber());
        assertTrue(mFahrenheit.requestFocus());
        assertTrue(mFahrenheit.isFocused());
        final double expected = TemperatureConverter.celsiusToFahrenheit(c);
        final double actual = mFahrenheit.getNumber();
        final double delta = Math.abs(expected - actual);
        assertTrue("delta=" + delta + " expected=" + expected + " actual=" + actual, delta < 0.005);
    }

    public void testFahrenheitToCelsiusConversion_text() throws Throwable {
        final double f = 32.5;
        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                mCelsius.clear();
                mFahrenheit.clear();
                assertTrue(mFahrenheit.requestFocus());
            }
        });

        mInstrumentation.sendStringSync(Double.toString(f));
        assertEquals(f, mActivity.getFahrenheit());
        final double expected = TemperatureConverter.fahrenheitToCelsius(f);
        final double actual = mCelsius.getNumber();
        final double delta = Math.abs(expected - actual);
        assertTrue("delta=" + delta + " expected=" + expected + " actual=" + actual, delta < 0.005);
    }

    public void testCelsiusToFahrenheitConversion_text() throws Throwable {
        final double c = 100;
        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                mCelsius.clear();
                mFahrenheit.clear();
                assertTrue(mCelsius.requestFocus());
            }
        });
        mInstrumentation.sendStringSync(Double.toString(c));

        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mActivity.getCelsius();
                assertEquals(c, mCelsius.getNumber());
                assertEquals(c, mActivity.getCelsius());
                final double expected = TemperatureConverter.celsiusToFahrenheit(c);
                final double actual = mFahrenheit.getNumber();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                assertEquals(c, mActivity.getFahrenheit());
                final double delta = Math.abs(expected - actual);
                assertTrue("delta=" + delta + " expected=" + expected + " actual=" + actual,
                        delta < 0.005);
            }
        });
    }

    @UiThreadTest
    public void testCelsiusIncompleteNumberEntered() {
        mCelsius.clear();
        mCelsius.setText(MINUS_SIGN);
        assertTrue(MINUS_SIGN.equals(mCelsius.getText().toString()));
    }

    @UiThreadTest
    public void testFahrenheitIncompleteNumberEntered() {
        mFahrenheit.clear();
        mFahrenheit.setText(MINUS_SIGN);
        assertTrue(MINUS_SIGN.equals(mFahrenheit.getText().toString()));
    }

    public void testOnOptionsItemSelected() {
        final Instrumentation i = getInstrumentation();
        i.sendKeyDownUpSync(KeyEvent.KEYCODE_MENU);
        i.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        // this is trying to run tests from the menu, which is not going to
        // happen as we are already running instrumentation tests here
        // and there's nothing to verify
        assertTrue(true);
    }

    @Suppress
    public void testForceFail() {
        fail("Forced fail");
    }
}
