package com.link_intersystems.swing.text;

import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;

class TextTableTest {

    @Test
    void defaultTextTable() throws IOException {
        TextTable textTable = new TextTable();
        assertThrows(IllegalArgumentException.class, () -> textTable.setColumnWidth(2));

        String table = renderTable(textTable);

        assertEquals("", table);

    }

    private String renderTable(TextTable textTable) throws IOException {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        textTable.print(pw);
        pw.flush();

        String table = sw.toString();
        return table;
    }

    @Test
    void setColumnWidth() {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);
        textTable.setColumnWidth(4);
        assertThrows(IllegalArgumentException.class, () -> textTable.setColumnWidth(3));

    }

    @Test
    void emptyTable() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);
        textTable.setHeaderEnabled(false);

        String table = renderTable(textTable);

        assertEquals(
                "", table);

    }

    @Test
    void tableWithTitle() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);
        textTable.setTitle("Title");

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------+\n" +
                        "| Title                          |\n" +
                        "+--------------------------------+\n" +
                        "| A                              |\n" +
                        "+--------------------------------+", table);

    }

    @Test
    void tableWithHeaderWithoutTitle() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------+\n" +
                        "| A                              |\n" +
                        "+--------------------------------+", table);

    }

    @Test
    void tableWithTitleWithoutHeader() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);
        textTable.setHeaderEnabled(false);
        textTable.setTitle("Title");

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------+\n" +
                        "| Title                          |\n" +
                        "+--------------------------------+", table);

    }

    @Test
    void onlyRows() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);
        textTable.setHeaderEnabled(false);
        defaultTableModel.addRow(new Object[]{"A1"});
        defaultTableModel.addRow(new Object[]{"A2"});

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------+\n" +
                        "| A1                             |\n" +
                        "+--------------------------------+\n" +
                        "| A2                             |\n" +
                        "+--------------------------------+", table);

    }

    @Test
    void abbreviateColumnName() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(new Object[]{"A very long column name that should be abbreviated"}, 0);
        TextTable textTable = new TextTable(defaultTableModel);

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------+\n" +
                        "| A very long column name tha... |\n" +
                        "+--------------------------------+", table);

    }

    @Test
    void abbreviateRowValue() throws IOException {
        DefaultTableModel defaultTableModel = new DefaultTableModel(0, 1);
        TextTable textTable = new TextTable(defaultTableModel);
        textTable.setHeaderEnabled(false);
        defaultTableModel.addRow(new Object[]{"A very long row value that should be abbreviated"});

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------+\n" +
                        "| A very long row value that ... |\n" +
                        "+--------------------------------+", table);

    }

    @Test
    void tableWith3ColumnsDefaultColumnWidth() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel(0, 3);
        TextTable textTable = new TextTable(tableModel);
        textTable.setTitle("Title");

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------------------------------------------------------------------------+\n" +
                        "| Title                                                                                            |\n" +
                        "+--------------------------------+--------------------------------+--------------------------------+\n" +
                        "| A                              | B                              | C                              |\n" +
                        "+--------------------------------+--------------------------------+--------------------------------+", table);

    }

    @Test
    void tableWith3ColumnsWithoutHeaderAndColumnWidth10() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel(0, 3);
        TextTable textTable = new TextTable(tableModel);
        textTable.setColumnWidth(10);
        textTable.setTitle("Title");

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------------+\n" +
                        "| Title                                |\n" +
                        "+------------+------------+------------+\n" +
                        "| A          | B          | C          |\n" +
                        "+------------+------------+------------+", table);

    }

    @Test
    void tableWith3Columns() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Col1", "Col2", "Col3"}, 0);
        TextTable textTable = new TextTable(tableModel);
        textTable.setColumnWidth(10);
        textTable.setTitle("Title");

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------------+\n" +
                        "| Title                                |\n" +
                        "+------------+------------+------------+\n" +
                        "| Col1       | Col2       | Col3       |\n" +
                        "+------------+------------+------------+", table);

    }


    @Test
    void tableWith3ColumnsAnd2Rows() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"A", "B", "C"}, 0);
        TextTable textTable = new TextTable(tableModel);
        textTable.setColumnWidth(10);
        textTable.setTitle("Title");
        textTable.setTableModel(tableModel);
        tableModel.addRow(new Object[]{"A1", "B1", "C1"});
        tableModel.addRow(new Object[]{"A2", "B2", "C2"});

        int[] columnWidths = textTable.getColumnWidths();
        assertArrayEquals(new int[]{10, 10, 10}, columnWidths);
        assertEquals(10, textTable.getColumnWidth(0));
        assertEquals(10, textTable.getColumnWidth(1));
        assertEquals(10, textTable.getColumnWidth(2));

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------------+\n" +
                        "| Title                                |\n" +
                        "+------------+------------+------------+\n" +
                        "| A          | B          | C          |\n" +
                        "+------------+------------+------------+\n" +
                        "| A1         | B1         | C1         |\n" +
                        "+------------+------------+------------+\n" +
                        "| A2         | B2         | C2         |\n" +
                        "+------------+------------+------------+", table);

    }

    @Test
    void tableWith3ColumnsDifferentWidthAnd2Rows() throws IOException {
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"A", "B", "C"}, 0);
        TextTable textTable = new TextTable(tableModel);
        textTable.setColumnWidth(0, 10);
        textTable.setColumnWidth(1, 15);
        textTable.setColumnWidth(2, 5);
        textTable.setTitle("Title");
        textTable.setTableModel(tableModel);
        tableModel.addRow(new Object[]{"A1", "B1", "C1"});
        tableModel.addRow(new Object[]{"A2", "B2", "C2"});

        int[] columnWidths = textTable.getColumnWidths();
        assertArrayEquals(new int[]{10, 15, 5}, columnWidths);
        assertEquals(10, textTable.getColumnWidth(0));
        assertEquals(15, textTable.getColumnWidth(1));
        assertEquals(5, textTable.getColumnWidth(2));

        String table = renderTable(textTable);

        assertEquals(
                "" +
                        "+--------------------------------------+\n" +
                        "| Title                                |\n" +
                        "+------------+-----------------+-------+\n" +
                        "| A          | B               | C     |\n" +
                        "+------------+-----------------+-------+\n" +
                        "| A1         | B1              | C1    |\n" +
                        "+------------+-----------------+-------+\n" +
                        "| A2         | B2              | C2    |\n" +
                        "+------------+-----------------+-------+", table);

    }
}