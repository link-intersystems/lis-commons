The lis-commons-test-db library provides support for setting up test databases
and also includes the sakila sample databases that is ready to use.

# Sakila Sample Database

The sakila sample database is provided by oracle's mysql database
and published under the [BSD license](https://en.wikipedia.org/wiki/BSD_licenses).

![Sakila Sample Database ERM](sakila.png)

The sakila sample database is provided as the JUnit extension `SakilaTestDBExtension`.
This extension is a `ParameterResolver` so that you can inject either a jdbc `Connection`
or a `H2Database` into your tests by simply specifying them as parameters to a test method.

    @ExtendWith(SakilaTestDBExtension.class)
    class DatabaseTest {
    
        // Inject in before methods
    
        @BeforeEach
        void setUp(Connection connection) {
        }
    
        // or inject in test methods.
    
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

> You don't need to close or reset the `Connection` the `SakilaTestDBExtension` will provide you a clean
> database for every test method and closes the Connection after all tests were executed.