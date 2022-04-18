package com.link_intersystems;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.link_intersystems")
@IncludeTags({"ComponentTest"})
public class ComponentTestSuite {

}