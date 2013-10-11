/**
 * 
 */
package com.example.i2at.tc.test;

import com.example.i2at.tc.EditNumber;

import android.test.AndroidTestCase;

/**
 * @author diego
 *
 */
public class EditNumberTests extends AndroidTestCase {

	private EditNumber mEditNumber;

	/**
	 * @param name
	 */
	public EditNumberTests(String name) {
		setName(name);
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		mEditNumber = new EditNumber(mContext);
		mEditNumber.setFocusable(true);
	}

	/* (non-Javadoc)
	 * @see android.test.AndroidTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.example.i2at.tc.EditNumber#EditNumber(android.content.Context)}.
	 */
	public final void testEditNumberContext() {
		assertNotNull(mEditNumber);
	}

	/**
	 * Test method for {@link com.example.i2at.tc.EditNumber#clear()}.
	 */
	public final void testClear() {
		final String value = "123.45";
		mEditNumber.setText(value);
		mEditNumber.clear();
		final String expected = "";
		final String actual = mEditNumber.getText().toString();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.example.i2at.tc.EditNumber#setNumber(double)}.
	 */
	public final void testSetNumber() {
		final double d = 123.45;
		mEditNumber.setNumber(d);
		final String expected = Double.toString(d);
		final String actual = mEditNumber.getText().toString();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link com.example.i2at.tc.EditNumber#getNumber()}.
	 */
	public final void testGetNumber() {
		final double expected = 123.45;
		mEditNumber.setNumber(expected);
		final double actual = mEditNumber.getNumber();
		assertEquals(expected, actual);
	}

}
