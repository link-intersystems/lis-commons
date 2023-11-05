lis-commons
=============

![Java CI with Maven](https://github.com/link-intersystems/lis-commons/workflows/Java%20CI%20with%20Maven/badge.svg) 
[![Coverage Status](https://coveralls.io/repos/github/link-intersystems/lis-commons/badge.svg?branch=master)](https://coveralls.io/github/link-intersystems/lis-commons?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/com.link-intersystems.commons/lis-commons)](https://mvnrepository.com/artifact/com.link-intersystems.commons)

A collection of reusable Java components for library and framework developers.

In order to make them as reusable as possible I try to minimize the dependencies the
modules have down to zero. I task hard to ensure that the libraries only depend on pure Java 
and also minimize the dependencies between the modules themselves.

Here is an overview of the module dependencies:

    lis-commons-util (0 deps)

    lis-commons-jdbc (0 deps)

    lis-commons-sql (0 deps)

    lis-commons-sql-hibernate (1 deps)

    lis-commons-net (0 deps)

    lis-commons-events (0 deps)

    lis-commons-graph (0 deps)

    lis-commons-math (0 deps)

    lis-commons-lang (0 dep)

    lis-commons-test (1 deps)
    +- org.junit.jupiter:junit-jupiter-api

    lis-commons-beans (0 deps)

    lis-commons-beans-record (1 deps)
    +- lis-commons-beans

    lis-commons-lang-criteria (3 deps)
    +- lis-commons-lang
    +- lis-commons-util
    +- lis-commons-graph

# Maven Dependencies

You can browse all libs and versions under [mvnrepository.com](https://mvnrepository.com/artifact/com.link-intersystems.commons)
or in the [central maven repository](https://repo.maven.apache.org/maven2/com/link-intersystems/commons/).

Maven project sites are available at [link-intersystems.github.io/lis-commons/](https://link-intersystems.github.io/lis-commons/)

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

# [lis-commons-beans-records](lis-commons-beans-records/README.md)

Beans support for Java records.

    public record PersonRecord(String firstname, String lastname) {}

       
    BeansFactory beansFactory = BeansFactory.getInstance("record");
    Bean<Person> bean = beansFactory.createBean(new PersonRecord("René", "Link"));
    PropertyList properties = bean.getProperties();

    assertEquals("René", properties.getByName("firstname").getValue());
    assertEquals("Link", properties.getByName("lastname").getValue());

Details at [lis-commons-beans-records](lis-commons-beans-records/README.md).


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

# lis-commons-sql

Provides support for sql related tasks like statement creation. This module provides
a simple support for creating sql statements. At the moment it only supports insert statements.
It is primarily made for the H2 dialect, but might also task for some other dialects. You
can extend the DefaultSqlDialect to adapt the module to your needs. If you do so please make
a pull request to let me integrate the dialect into this library.

    SqlDialect sqlDialect = new DefaultSqlDialect();
    
    InsertSql insertSql = sqlDialect.createInsertSql("actor");
    
    LiteralFormat idFormat = sqlDialect.getLiteralFormat(Types.BIGINT);
    insertSql.addColumn("id", idFormat.format(1L));
    
    LiteralFormat firstNameFormat = sqlDialect.getLiteralFormat(Types.VARCHAR);
    insertSql.addColumn("first_name", firstNameFormat.format("PENELOPE"));
    
    LiteralFormat lastNameFormat = sqlDialect.getLiteralFormat(Types.VARCHAR);
    insertSql.addColumn("last_name", lastNameFormat.format("GUINESS"));
    
    LiteralFormat lastUpdateFormat = sqlDialect.getLiteralFormat(Types.TIMESTAMP);
    insertSql.addColumn("last_update", lastUpdateFormat.format(new Date(106, 1, 15, 4, 34, 33)));
    
    String sql = insertSql.toSqlString();
    assertEquals("insert into actor (id, first_name, last_name, last_update) values (1, 'PENELOPE', 'GUINESS', '2006-02-15 04:34:33')", sql);

> If you want support for more dialects you might want to take a look at the `lis-commons-sql-hibernate`
module that uses hibernates capabilities.

    H2Dialect dialect = new H2Dialect();
    SqlDialect sqlDialect = new HibernateSqlDialect(dialect);
    
    InsertSql insertSql = sqlDialect.createInsertSql("actor");
    // ...

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


# lis-commons-test-db
The lis-commons-test-db library provides support for setting up test databases
and also includes the sakila sample databases that is ready to use.

## Sakila Sample Database

The sakila sample database is provided by oracle's mysql database
and published under the [BSD license](https://en.wikipedia.org/wiki/BSD_licenses).

For details take a look at [lis-commons-test-db](lis-commons-test-db/README.md)

    @ExtendWith(SakilaTestDBExtension.class)
    class DatabaseTest { 
        @Test
        void selectSakilaDatabase(Connection connection) {
            try (Statement stmt = connection.createStatement()) {
                if (stmt.execute("select * from actor where actor_id = 1")) {
                    ResultSet rs = stmt.getResultSet();
    
                    assertTrue(rs.next(), "result set should not be empty");
    
                    assertEquals(1L, rs.getLong("actor_id"), "actor_id");
                    assertEquals("PENELOPE", rs.getString("first_name"), "first_name");
                    assertEquals("GUINESS", rs.getString("last_name"), "last_name");
                }
            }
        }
    }

# lis-commons-net

Networking tools based on pure Java.

    JavaHttpRequestFactory requestFactory = new JavaHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.of(5, ChronoUnit.SECONDS));
    requestFactory.setReadTimeout(Duration.of(10, ChronoUnit.SECONDS));

    // GET google.com
    HttpRequest httpRequest = requestFactory.get(new URL("https://www.google.com"));
    httpRequest.addHeader("Accept", "text/html");
    HttpResponse httpResponse = httpRequest.prepare().close();
    System.out.println(httpResponse.getContentAsString(StandardCharsets.UTF_8));

    HttpRequest httpRequest = requestFactory.post(new URL("http://localhost:8080));
    PreparedRequest preparedRequest = httpRequest.prepare();
    OutputStream postOut = preparedRequest.getOutputStream()
    postOut.write("Hello World".getBytes(StandardCharsets.UTF_8));
    HttpResponse httpResponse = preparedRequest.close();
    System.out.println(httpResponse.getResponseCode());

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








