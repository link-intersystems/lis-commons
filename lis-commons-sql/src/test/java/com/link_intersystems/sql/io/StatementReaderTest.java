package com.link_intersystems.sql.io;

import com.link_intersystems.test.io.ResetableReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class StatementReaderTest {

    private ResetableReader resetableReader;
    private StatementReader statementReader;

    @BeforeEach
    void setUp() {
        resetableReader = new ResetableReader();
        statementReader = new StatementReader(new BufferedReader(resetableReader));
    }

    @Test
    void readStatement() throws IOException {
        resetableReader.setCharSequence("-- some comment\n    select * \n\t from actor where actor_id = 1;-- other comment\n");

        assertTrue(statementReader.hasNext());
        assertEquals("select * from actor where actor_id = 1", statementReader.next());
        assertFalse(statementReader.hasNext());
    }

    @Test
    void statementFilter() throws IOException {
        resetableReader.setCharSequence("-- some comment\n    select * \n\t from actor where actor_id = 1;-- other comment\n" +
                "\tselect count(*) from film\n join language on \t\r\nfilm.original_language_id = language.language.id\n" +
                "where film.film_id = 12");

        statementReader.setStatementFilter(s -> s.contains("film"));

        assertTrue(statementReader.hasNext());
        assertEquals("select count(*) from film join language " +
                "on film.original_language_id = language.language.id where film.film_id = 12", statementReader.next());
        assertFalse(statementReader.hasNext());
    }

    @Test
    void close() throws IOException {
        resetableReader.setCharSequence("select * from actor where actor_id = 1");
        statementReader.close();

        assertThrows(IOException.class, () -> statementReader.hasNext());
    }

}