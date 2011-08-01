/**
 * 
 */
package com.example.i2at.tc;

/**
 * @author diego
 *
 */
public class TemperatureConverter {
	private TemperatureConverter() {
		// do nothing
	}
	public static double fahrenheitToCelsius(double f) {
		return (f-32) * 5/9.0;
	}

	public static double celsiusToFahrenheit(double c) {
		return 9/5.0 * c + 32;
	}

}










