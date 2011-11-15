package com.example.i2at.tc;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.InstrumentationInfo;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TemperatureConverterActivity extends Activity {
    /**
	 * @author diego
	 *
	 */
	public abstract class TemperatureChangeWatcher implements TextWatcher {
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

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		 */
		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if ( !mDest.hasWindowFocus() || mDest.hasFocus() || s == null ) {
				return;
			}
			final String str = s.toString();
			if ( "".equals(str) ) {
				mDest.setText("");
				return;
			}
			try {
				android.util.Log.v("TemperatureChangeWatcher", "converting temp=" + str + "{" + Double.parseDouble(str) + "}");
				final double result = convert(Double.parseDouble(str));
				android.util.Log.v("TemperatureChangeWatcher", "result=" + result);
				//final String resutlStr = String.format("%.2f", result);
				mDest.setNumber(result);
			}
			catch (NumberFormatException e) {
				// WARNING:
				// this is thrown while a number is entered
				// for example just a '-'
			}
			catch (Exception e) {
				mSource.setError("ERROR: " + e.getLocalizedMessage());
			}
		}

		protected abstract double convert(double temp);

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

	}

	private static final int MENU_ITEM_RUN_TESTS = 1;
	

	private EditNumber mCelsius;
	private EditNumber mFahrenheit;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_ITEM_RUN_TESTS, Menu.NONE, "Run tests");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch ( item.getItemId() ) {
		case MENU_ITEM_RUN_TESTS:
			runTests();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void runTests() {
		final String packageName = getPackageName();
		final List<InstrumentationInfo> list = getPackageManager().queryInstrumentation(packageName, 0);
		if ( list.isEmpty() ) {
			Toast.makeText(this, "Cannot find instrumentation for " + packageName, Toast.LENGTH_SHORT).show();
			return;
		}
		final InstrumentationInfo instrumentationInfo = list.get(0);
		final ComponentName componentName = new ComponentName(instrumentationInfo.packageName, instrumentationInfo.name);
		if ( !startInstrumentation(componentName, null, null) ) {
			Toast.makeText(this, "Cannot run instrumentation for " + packageName, Toast.LENGTH_SHORT).show();
		}
	}
    
    
}