
package com.example.i2at.tc;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.InstrumentationInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <em>TemperatureConverterActivity</em> is a demonstration Activity used to
 * show some testing features.
 * 
 * @author diego
 */
public class TemperatureConverterActivity extends Activity {
    public static final String FAHRENHEIT_KEY = "com.example.i2at.tc.Fahrenheit";

    public static final String CELSIUS_KEY = "com.example.i2at.tc.Celsius";

    @SuppressWarnings("unused")
    private static final String TAG = "TemperatureConverterActivity";

    private static final int MENU_ITEM_RUN_TESTS = 1;

    private static final boolean DEBUG = true;

    public abstract class TemperatureChangeWatcher implements TextWatcher {
        private static final String TAG = "TemperatureChangeWatcher";
        
        private EditNumber mSource;
        private EditNumber mDest;

        /**
         * @param source
         * @param dest
         */
        public TemperatureChangeWatcher(EditNumber source, EditNumber dest) {
            this.mSource = source;
            this.mDest = dest;
        }

        /*
         * (non-Javadoc)
         * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
         */
        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        /*
         * (non-Javadoc)
         * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence,
         * int, int, int)
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            if (DEBUG) {
                Log.d(TAG, "onTextChanged(" + s + ", " + start + ", " + before + ") ###############");
            }
            if (!mDest.hasWindowFocus() || mDest.hasFocus() || s == null) {
                return;
            }
            final String str = s.toString();
            if ("".equals(str)) {
                mDest.setText("");
                return;
            }
            try {
                android.util.Log.v(TAG, "converting temp=" + str + "{"
                        + Double.parseDouble(str) + "}");
                final double result = convert(Double.parseDouble(str));
                android.util.Log.v("TemperatureChangeWatcher", "result=" + result);
                // final String resutlStr = String.format("%.2f", result);
                mDest.setNumber(result);
            } catch (NumberFormatException e) {
                // WARNING:
                // this is thrown while a number is entered
                // for example just a '-'
            } catch (Exception e) {
                Log.e(TAG,  "ERROR", e);
                mSource.setError("ERROR: " + e.getLocalizedMessage());
            }
        }

        protected abstract double convert(double temp);

        /*
         * (non-Javadoc)
         * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence,
         * int, int, int)
         */
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                int arg3) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * Handles focus changes between the entry fields and sets the value of the
     * filed receiving focus accordingly.
     */
    private OnFocusChangeListener mTemperatureEntryFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View dest, boolean hasFocus) {
            if (!hasFocus) {
                // nothing to do if we're loosing focus
                return;
            }
            
            final double f = mFahrenheit.getNumber();
            final double c = mCelsius.getNumber();
            
            if (dest == mCelsius && ! Double.isNaN(f)) {
                mCelsius.setNumber(TemperatureConverter.fahrenheitToCelsius(f));
            }
            else if (dest == mFahrenheit && !Double.isNaN(c)) {
                mFahrenheit.setNumber(TemperatureConverter.celsiusToFahrenheit(c));
            }
        }
    };
    
    private EditNumber mCelsius;
    private EditNumber mFahrenheit;

    private TextView mDebug;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) {
            Log.d(TAG, "onCreate(" + savedInstanceState + ")");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCelsius = (EditNumber) findViewById(R.id.celsius);
        mFahrenheit = (EditNumber) findViewById(R.id.fahrenheit);

        mCelsius.addTextChangedListener(new TemperatureChangeWatcher(mCelsius, mFahrenheit) {

            @Override
            protected double convert(double temp) {
                return TemperatureConverter.celsiusToFahrenheit(temp);
            }

        });

        mFahrenheit.addTextChangedListener(new TemperatureChangeWatcher(mFahrenheit, mCelsius) {

            @Override
            protected double convert(double temp) {
                return TemperatureConverter.fahrenheitToCelsius(temp);
            }

        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CELSIUS_KEY)) {
                final double c = savedInstanceState.getDouble(CELSIUS_KEY);
                final double f = TemperatureConverter.celsiusToFahrenheit(c);
                if (DEBUG) {
                    Log.d(TAG, "onCreate: restoring celsius: " + c);
                }
                mCelsius.setNumber(c);
                mFahrenheit.setNumber(f);
            }
            else if (savedInstanceState.containsKey(FAHRENHEIT_KEY)) {
                final double f = savedInstanceState.getDouble(FAHRENHEIT_KEY);
                final double c = TemperatureConverter.fahrenheitToCelsius(f);
                if (DEBUG) {
                    Log.d(TAG, "onCreate: restoring fahrenheit: " + f);
                }
                mFahrenheit.setNumber(f);
                mCelsius.setNumber(c);
            }
        }
        
        mCelsius.setOnFocusChangeListener(mTemperatureEntryFocusChangeListener);
        mFahrenheit.setOnFocusChangeListener(mTemperatureEntryFocusChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_RUN_TESTS, Menu.NONE, "Run tests").setIcon(
                android.R.drawable.ic_menu_manage);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_RUN_TESTS:
                runTests();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void runTests() {
        final String packageName = getPackageName();
        final List<InstrumentationInfo> list = getPackageManager().queryInstrumentation(
                packageName, 0);
        if (list.isEmpty()) {
            Toast.makeText(this, "Cannot find instrumentation for " + packageName,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        InstrumentationInfo instrumentationInfo = null;
        for (InstrumentationInfo ii : list) {
            if ((packageName + ".test").equals(ii.packageName)) {
                instrumentationInfo = ii;
                break;
            }
        }
        final ComponentName componentName = new ComponentName(instrumentationInfo.packageName,
                instrumentationInfo.name);
        if (!startInstrumentation(componentName, null, null)) {
            Toast.makeText(this, "Cannot run instrumentation for " + packageName,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public double getCelsius() {
        return mCelsius.getNumber();
    }

    public double getFahrenheit() {
        return mFahrenheit.getNumber();
    }

}
