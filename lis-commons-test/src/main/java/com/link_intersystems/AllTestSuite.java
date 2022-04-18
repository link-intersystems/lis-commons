package com.link_intersystems;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({UnitTestSuite.class, ComponentTestSuite.class})
public class AllTestSuite {

}