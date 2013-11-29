/**
 * 
 */

package com.example.i2at.tc.test;

import android.test.ApplicationTestCase;

import com.example.i2at.tc.TemperatureConverterApplication;

/**
 * @author brldmila
 */
public class TemperatureConverterApplicationTests extends
        ApplicationTestCase<TemperatureConverterApplication> {

    private TemperatureConverterApplication mApplication;

    public TemperatureConverterApplicationTests() {
        this("TemperatureConverterApplicationTests");
    }

    public TemperatureConverterApplicationTests(String name) {
        super(TemperatureConverterApplication.class);
        setName(name);
    }

    /*
     * (non-Javadoc)
     * @see android.test.ApplicationTestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        final RenamingMockContext mockContext = new RenamingMockContext(getSystemContext());
        setContext(mockContext);
        createApplication();
        mApplication = getApplication();
    }

    /*
     * (non-Javadoc)
     * @see android.test.ApplicationTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testPreconditions() {
        assertNotNull(mApplication);
    }

    public final void testSetDecimalPlaces() {
        final int expected = 3;
        mApplication.setDecimalPlaces(expected);
        assertEquals(expected, mApplication.getDecimalPlaces());
    }
}
