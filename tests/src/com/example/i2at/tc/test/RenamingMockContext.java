/**
 * 
 */

package com.example.i2at.tc.test;

import android.content.Context;
import android.test.RenamingDelegatingContext;

/**
 * @author brldmila
 */
public class RenamingMockContext extends RenamingDelegatingContext {

    private static final String PREFIX = "test.";

    public RenamingMockContext(Context context) {
        super(context, PREFIX);
    }

    /**
     * @param context
     * @param filePrefix
     */
    public RenamingMockContext(Context context, String filePrefix) {
        super(context, filePrefix);
    }

    /**
     * @param context
     * @param fileContext
     * @param filePrefix
     */
    public RenamingMockContext(Context context, Context fileContext, String filePrefix) {
        super(context, fileContext, filePrefix);
    }

}
