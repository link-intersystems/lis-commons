lis-commons
=============

![Java CI with Maven](https://github.com/link-intersystems/lis-commons/workflows/Java%20CI%20with%20Maven/badge.svg) 
[![Coverage Status](https://coveralls.io/repos/github/link-intersystems/lis-commons/badge.svg?branch=master)](https://coveralls.io/github/link-intersystems/lis-commons?branch=master)

A collection of reusable Java components for library and framework developers.

In order to make them as reusable as possible I try to minimize the dependencies the
modules have down to zero. I work hard to ensure that the libraries only depend on pure Java 
and also minimize the dependencies between the modules themselves.

Here is an overview of the module dependencies:

    lis-commons-util (0 deps)

    lis-commons-jdbc (0 deps)

    lis-commons-events (0 deps)

    lis-commons-graph (0 deps)

    lis-commons-math (0 deps)

    lis-commons-lang (0 dep)

    lis-commons-beans (0 deps)

    lis-commons-lang-criteria (3 deps)
    +- lis-commons-lang
    +- lis-commons-util
    +- lis-commons-graph

    

The modules are available in the [central maven repository](https://repo.maven.apache.org/maven2/com/link-intersystems/commons/).
Maven project sites are linked at [link-intersystems.github.io/lis-commons/](https://link-intersystems.github.io/lis-commons/)

# lis-commons-beans

Provides a more easy api to handle Java bean related issues. The lis-commons-beans is shipped with
support for the Java beans specification, but can be extended to any kind of beans (like JPA beans).

    BeansFactory factory = BeansFactory.getDefault();
    Bean<DefaultButtonModel> buttonModelBean = factory.createBean(new DefaultButtonModel());

    ChangeListener changeListener = ce -> System.out.println("button model changed.");
    ActionListener actionListener = ae -> System.out.println("action performed.");
  
    buttonModelBean.addListener(changeListener);
    buttonModelBean.addListener(actionListener);

    DefaultButtonModel buttonModel = buttonModelBean.getBeanObject();

    buttonModel.setPressed(true);
    buttonModel.setArmed(true);
    buttonModel.setPressed(false);

    PropertyList properties = buttonModelBean.getProperties();
    Property armedProperty = properties.getByName("armed");
    System.out.println("button model armed: " + armedProperty.getValue());

will output

    button model changed.
    button model changed.
    action performed.
    button model changed.
    button model armed: true

# lis-commons-jdbc

Provides a convenient api to access the meta-data of a jdbc connection


    Connection jdbcConnection = ...;
    ConnectionMetaData metaData = new ConnectionMetaData(jdbcConnection);
    
    ForeignKeyList foreignKeys = metaData.getImportedKeys("film_actor");
    
    ColumnMetaDataList filmActorColumns = metaData.getColumnMetaDataList("film_actor");
    ForeignKey foreignKey = foreignKeys.getByFkColumnDescription(filmActorColumns.getByName("actor_id"));
    assertNotNull(foreignKey);
    assertEquals("fk_film_actor_actor", foreignKey.getName());
    
    ColumnMetaDataList actorColumns = metaData.getColumnMetaDataList("actor");
    foreignKey = foreignKeys.getByPkColumnDescription(actorColumns.getByName("actor_id"));
    assertNotNull(foreignKey);
    assertEquals("fk_film_actor_actor", foreignKey.getName());

# [lis-commons-events](lis-commons-events/README.md)

Provides support for Java event handling, e.g. it provides easy listener adapter creation using method references.

    ListSelectionListener selectionListener = ListSelectionEventMethod.VALUE_CHANGED.listener(this::printEventFired);

    private void printEventFired(ListSelectionEvent e) {
        int firstIndex = e.getFirstIndex();
        int lastIndex = e.getLastIndex();
        boolean isAdjusting = e.getValueIsAdjusting();
        System.out.println("Selection model event fired:" +
                            " firstIndex= " + firstIndex +
                            " lastIndex= " + lastIndex +
                            " isAdjusting= " + isAdjusting);
    }

    ListSelectionModel selectionModel = new DefaultListSelectionModel();
    selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    selectionModel.addListSelectionListener(selectionListener);

    selectionModel.setSelection(3); // fires the selection event and it is delegates to 
                                    // the method reference this::printEventFired


# lis-commons-util

Provides utility classes that I sometimes miss in standard Java. The library started when Java 1.8 was the current version.
Nowadays, some utility classes might already be supported by the Java version you use.

# lis-commons-math

A library that contains utility classes for simple common math issues like aggregating values or working with linear
functions. This library is not expected to be a sophisticated math library. It is only for simple use cases when high
accuracy is not an issue.

It contains support for simple linear equations.

    // E.g. when you want to calculate the progress of operations
    // you map one coordinate (the tasks) into another coordinate (the progress).
    // The following example shows how to provide progress in values of
    // 0 to 100 for 25 tasks.

    TwoPointLinearEquation linearEquation1 = new TwoPointLinearEquation(50, 100);

    double y = linearEquation1.fX(5); // 5 tasks are 10%
    System.out.println(y);

There is also support for aggregate functions. E.g. the BigIncrementalAverage
can be used when you have to constantly calculate the average of a values, because
it applies the next value to the previous average value. Can be useful for
time measurements of service calls, etc.

    Average<BigDecimal> average = new BigIncrementalAverage();

    Random r = new Random();
    int samples = 10000000;
  
    while(samples-- > 0){
        average.addValue(r.nextInt(100));
    }
  
    System.out.println(average.getValue());






