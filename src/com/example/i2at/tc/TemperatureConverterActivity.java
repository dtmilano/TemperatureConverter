package com.example.i2at.tc;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

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
}