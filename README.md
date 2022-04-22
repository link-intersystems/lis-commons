lis-commons
=============

![Java CI with Maven](https://github.com/link-intersystems/lis-commons/workflows/Java%20CI%20with%20Maven/badge.svg)

A collection of reusable Java components. In order to make them reusable as possible they don't have
any dependency to other libraries. They only are dependent on pure Java and some modules depend on other
lis-commons modules.

Here is an overview of the module dependencies:

    lis-commons-util (0 deps)

    lis-commons-events (0 deps)

    lis-commons-graph (0 deps)

    lis-commons-math (0 deps)

    lis-commons-lang (1 dep)
    +- lis-commons-util

    lis-commons-lang-criteria (3 deps)
    +- lis-commons-lang
    |  +- lis-commons-util
    +- lis-commons-graph

    lis-commons-beans (2 deps)
    +- lis-commons-lang
       +- lis-commons-util





Some time ago (in 2007) I started to aggregate reusable Java components that were not included in J2SE or that provide an easier api for common programming tasks in one library.
In 2011 I published the library on www.link-intersystems.com and a few weeks ago (Dec. 2014) I decided to migrate this library from svn to git and make it available under the apache license 2.0 on github.

Most of the components are the result of projects that I worked on.

# [lis-commons-events](lis-commons-events/README.md)


Provides support for Java event handling.




