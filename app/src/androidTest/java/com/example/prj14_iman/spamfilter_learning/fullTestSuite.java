package com.example.prj14_iman.spamfilter_learning;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Prj14_IMAN on 8/8/2015.
 */
public class fullTestSuite extends TestSuite {
    public static Test suit(){
        return new TestSuiteBuilder(fullTestSuite.class).includeAllPackagesUnderHere().build();
    }
    public fullTestSuite(){
        super();
    }
}
