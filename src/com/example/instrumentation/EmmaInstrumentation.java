package com.example.instrumentation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.example.i2at.tc.TemperatureConverterActivity;
//import com.vladium.emma.rt.RT;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

public class EmmaInstrumentation extends Instrumentation implements FinishListener {

    private static final String TAG = "EmmaInstrumentation";

    private static final boolean LOGD = true;

    private static final String DEFAULT_COVERAGE_FILE_PATH = "/mnt/sdcard/coverage.ec";

    private final Bundle mResults = new Bundle();

    private Intent mIntent;

    private boolean mCoverage = true;

    private String mCoverageFilePath;

    /**
     * Extends the AUT to provide the necessary behavior to invoke the
     * {@link FinishListener} that may have been provided using
     * {@link #setFinishListener(FinishListener)}.
     * 
     * It's important to note that the original Activity has not been modified.
     * Also, the Activity must be declared in the
     * <code>AndroidManifest.xml</code> because it is started by an intent in
     * {@link EmmaInstrumentation#onStart()}. This turns more difficult to use
     * other methods like using template classes. This latter method could be
     * viable, but all Activity methods should be re-written to invoke the
     * template parameter class corresponding methods.
     * 
     * @author diego
     * 
     */
    public static class InstrumentedActivity extends
    TemperatureConverterActivity {
        private FinishListener mListener;

        public void setFinishListener(FinishListener listener) {
            mListener = listener;
        }

        @Override
        public void finish() {
            if (LOGD)
                Log.d(TAG + ".InstrumentedActivity", "finish()");
            super.finish();
            if (mListener != null) {
                mListener.onActivityFinished();
            }
        }

    }

    /**
     * Constructor
     */
    public EmmaInstrumentation() {

    }

    @Override
    public void onCreate(Bundle arguments) {
        if (LOGD)
            Log.d(TAG, "onCreate(" + arguments + ")");
        super.onCreate(arguments);

        if (arguments != null) {
            mCoverage = getBooleanArgument(arguments, "coverage");
            mCoverageFilePath = arguments.getString("coverageFile");
        }

        mIntent = new Intent(getTargetContext(), InstrumentedActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start();
    }

    @Override
    public void onStart() {
        if (LOGD)
            Log.d(TAG, "onStart()");
        super.onStart();

        Looper.prepare();
        InstrumentedActivity activity = (InstrumentedActivity) startActivitySync(mIntent);
        activity.setFinishListener(this);
    }

    private boolean getBooleanArgument(Bundle arguments, String tag) {
        String tagString = arguments.getString(tag);
        return tagString != null && Boolean.parseBoolean(tagString);
    }

    private void generateCoverageReport() {
        if (LOGD)
            Log.d(TAG, "generateCoverageReport()");

        java.io.File coverageFile = new java.io.File(getCoverageFilePath());

        // We may use this if we want to avoid reflection and we include
        // emma.jar
        // RT.dumpCoverageData(coverageFile, false, false);

        // Use reflection to call emma dump coverage method, to avoid
        // always statically compiling against emma jar
        try {
            Class<?> emmaRTClass = Class.forName("com.vladium.emma.rt.RT");
            Method dumpCoverageMethod = emmaRTClass.getMethod(
                    "dumpCoverageData", coverageFile.getClass(), boolean.class,
                    boolean.class);
            dumpCoverageMethod.invoke(null, coverageFile, false, false);
        } catch (ClassNotFoundException e) {
            reportEmmaError("Is emma jar on classpath?", e);
        } catch (SecurityException e) {
            reportEmmaError(e);
        } catch (NoSuchMethodException e) {
            reportEmmaError(e);
        } catch (IllegalArgumentException e) {
            reportEmmaError(e);
        } catch (IllegalAccessException e) {
            reportEmmaError(e);
        } catch (InvocationTargetException e) {
            reportEmmaError(e);
        }
    }

    private String getCoverageFilePath() {
        if (mCoverageFilePath == null) {
            return DEFAULT_COVERAGE_FILE_PATH;
        } else {
            return mCoverageFilePath;
        }
    }

    private void reportEmmaError(Exception e) {
        reportEmmaError("", e);
    }

    private void reportEmmaError(String hint, Exception e) {
        String msg = "Failed to generate emma coverage. " + hint;
        Log.e(TAG, msg, e);
        mResults.putString(Instrumentation.REPORT_KEY_STREAMRESULT, "\nError: "
                + msg);
    }

    /* (non-Javadoc)
     * @see com.example.instrumentation.FinishListener#onActivityFinished()
     */
    @Override
    public void onActivityFinished() {
        if (LOGD)
            Log.d(TAG, "onActivityFinished()");
        if (mCoverage) {
            generateCoverageReport();
        }
        finish(Activity.RESULT_OK, mResults);
    }

}
