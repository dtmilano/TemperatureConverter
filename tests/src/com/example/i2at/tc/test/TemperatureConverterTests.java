/**
 * 
 */
package com.example.i2at.tc.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.example.i2at.tc.TemperatureConverter;

import junit.framework.TestCase;

/**
 * @author diego
 *
 */
public class TemperatureConverterTests extends TestCase {

	private static final HashMap<Double, Double> sConversionTableDouble =
			new HashMap<Double, Double>();
	
	static {
		sConversionTableDouble.put(0.0, 32.0);
		sConversionTableDouble.put(100.0, 212.0);
		sConversionTableDouble.put(-1.0, 30.20);
		sConversionTableDouble.put(-100.0, -148.0);
		sConversionTableDouble.put(32.0, 89.60);
		sConversionTableDouble.put(-40.0, -40.0);
		sConversionTableDouble.put(-273.0, -459.40);
	}

	/**
	 * @param name
	 */
	public TemperatureConverterTests(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.example.i2at.tc.TemperatureConverter#fahrenheitToCelsius(double)}.
	 */
	public void testFahrenheitToCelsius() {
		for (double c: sConversionTableDouble.keySet()) {
			final double f = sConversionTableDouble.get(c);
			final double ca = TemperatureConverter.fahrenheitToCelsius(f);
			final double delta = Math.abs(ca - c);
			assertTrue("delta=" + delta + " for c=" + c + " ca=" + ca, delta < 0.005);
		}
	}


	/**
	 * Test method for {@link com.example.i2at.tc.TemperatureConverter#fahrenheitToCelsius(double)}.
	 */
	public void testCelsiusToFahrenheit() {
		for (double c: sConversionTableDouble.keySet()) {
			final double f = sConversionTableDouble.get(c);
			final double fa = TemperatureConverter.celsiusToFahrenheit(c);
			final double delta = Math.abs(fa - f);
			assertTrue("delta=" + delta + " for f=" + f + " fa=" + fa, delta < 0.005);
		}
	}
	
	public final void testPrivateConstructor() throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Constructor<TemperatureConverter> ctor =
			TemperatureConverter.class.getDeclaredConstructor();
		ctor.setAccessible(true);
		TemperatureConverter tc = ctor.newInstance((Object[])null);
		assertNotNull(tc);
	}
}
