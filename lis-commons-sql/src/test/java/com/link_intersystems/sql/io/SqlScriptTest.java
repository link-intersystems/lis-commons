package com.link_intersystems.sql.io;

import com.link_intersystems.test.io.ResetableReader;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class SqlScriptTest {

    @Test
    void executeCallback() throws SQLException {
        ResetableReader resetableReader = new ResetableReader();
        resetableReader.setCharSequence("-- some comment\n    select * \n\t from actor where actor_id = 1;-- other comment\n");

        SqlScript.ScriptResource scriptResource = () -> resetableReader;
        SqlScript sqlScript = new SqlScript(scriptResource);

        SqlScript.StatementCallback statementCallback = mock(SqlScript.StatementCallback.class);
        sqlScript.execute(statementCallback);

        verify(statementCallback, times(1))
                .doWithStatement("select * from actor where actor_id = 1");
    }

    @Test
    void executeConnection() throws SQLException {
        ResetableReader resetableReader = new ResetableReader();
        resetableReader.setCharSequence("-- some comment\n    select * \n\t from actor where actor_id = 1;-- other comment\n");

        SqlScript.ScriptResource scriptResource = () -> resetableReader;
        SqlScript sqlScript = new SqlScript(scriptResource);

        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        sqlScript.execute(connection);

        verify(statement, times(1))
                .addBatch("select * from actor where actor_id = 1");
    }

    @Test
    void executeWithFilter() throws SQLException {
        ResetableReader resetableReader = new ResetableReader();
        resetableReader.setCharSequence("-- some comment\n    select * \n\t from actor where actor_id = 1;-- other comment\n" +
                "\tselect count(*) from film\n join language on \t\r\nfilm.original_language_id = language.language.id\n" +
                "where film.film_id = 12");

        SqlScript.ScriptResource scriptResource = () -> resetableReader;

        SqlScript sqlScript = new SqlScript(scriptResource);
        sqlScript.setStatementFilter(s -> s.contains("film"));

        SqlScript.StatementCallback statementCallback = mock(SqlScript.StatementCallback.class);
        sqlScript.execute(statementCallback);

        verify(statementCallback, times(1))
                .doWithStatement("select count(*) from film join language " +
                        "on film.original_language_id = language.language.id where film.film_id = 12");
    }
}