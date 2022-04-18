package com.link_intersystems.beans;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
class MainSuite {

    @Suite
    @SelectPackages("com.link_intersystems")
    @IncludeTags({"ComponentTest"})
    static class ComponentTestSuite {

    }

    @Suite
    @SelectPackages("com.link_intersystems")
    @IncludeTags({"UnitTest"})
    static class UnitTestSuite {

    }
}