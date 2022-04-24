lis-commons
=============

![Java CI with Maven](https://github.com/link-intersystems/lis-commons/workflows/Java%20CI%20with%20Maven/badge.svg) [![Coverage Status](https://coveralls.io/repos/github/link-intersystems/lis-commons/badge.svg?branch=master)](https://coveralls.io/github/link-intersystems/lis-commons?branch=master)

A collection of reusable Java components. In order to make them reusable as possible they don't have any dependency to
other libraries. I work hard to ensure that the libraries only depend on pure Java and have a few dependencies between
each other. Take a look at the overview of the module dependencies below.

Overview of the module dependencies:

    lis-commons-util (0 deps)

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

# lis-commons-beans

Provides a more easy api to handle Java bean related issues. The lis-commons-beans is shipped with
support for the Java beans specification, but can be extended to any kind of beans.

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

     Average<BigDecimal> average = new BigIncrementalAvarage();

     Random r = new Random();
     int samples = 10000000;
  
     while(samples-- > 0){
         average.addValue(r.nextInt(100));
     }
  
     System.out.println(average.getValue());






