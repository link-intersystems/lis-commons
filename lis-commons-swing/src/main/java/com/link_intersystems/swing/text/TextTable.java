package com.link_intersystems.swing.text;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;

/**
 * Prints a table based on a {@link javax.swing.table.TableModel}. In addition to a {@link javax.swing.table.TableModel}
 * you can set an optional title {@link #setTitle(String)}.
 *
 * <pre>
 * +--------------------------------------+
 * | Title                                |
 * +------------+------------+------------+
 * | A          | B          | C          |
 * +------------+------------+------------+
 * | A1         | B1         | C1         |
 * +------------+------------+------------+
 * | A2         | B2         | C2         |
 * +------------+------------+------------+
 * </pre>
 *
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class TextTable {

    private int[] columnWidths = null;
    private String title;
    private boolean headerEnabled = true;

    private TableStringConverter tableStringConverter = new TableStringConverter() {
        @Override
        public String toString(TableModel model, int row, int column) {
            Object valueAt = model.getValueAt(row, column);
            return String.valueOf(valueAt);
        }
    };

    private TableModel tableModel = new DefaultTableModel();

    public TextTable() {
    }

    public TextTable(TableModel tableModel) {
        this.tableModel = tableModel;
    }

    public TableModel getTableModel() {
        return tableModel;
    }

    public void setTableModel(TableModel tableModel) {
        if (this.tableModel == tableModel) {
            return;
        }

        this.tableModel = tableModel;
        columnWidths = null;
    }

    private int[] getColumnWidthInternal() {
        if (columnWidths == null) {
            int columnCount = getTableModel().getColumnCount();
            columnWidths = new int[columnCount];
            Arrays.fill(columnWidths, 30);
        }
        return columnWidths;
    }

    private int getSumColumnWidths() {
        int[] columnWidths = getColumnWidthInternal();
        return Arrays.stream(columnWidths).reduce(0, Integer::sum);
    }

    public void setTableStringConverter(TableStringConverter tableStringConverter) {
        this.tableStringConverter = Objects.requireNonNull(tableStringConverter);
    }

    public void setHeaderEnabled(boolean headerEnabled) {
        this.headerEnabled = headerEnabled;
    }

    public boolean isHeaderEnabled() {
        return headerEnabled;
    }

    private int getWidth() {
        int columnCount = tableModel.getColumnCount();
        int sumColumnWidths = getSumColumnWidths();
        return sumColumnWidths + (3 * columnCount) + 1;
    }

    public void setColumnWidth(int columnWidthForEachColumn) {
        if (columnWidthForEachColumn < 4) {
            throw new IllegalArgumentException("A column width must be at least 4, but was " + columnWidthForEachColumn);
        }
        int[] columnWidths = getColumnWidthInternal();
        Arrays.fill(columnWidths, columnWidthForEachColumn);
    }

    public int[] getColumnWidths() {
        return columnWidths.clone();
    }

    public int getColumnWidth(int colum) {
        return getColumnWidths()[colum];
    }

    public void setColumnWidth(int column, int width) {
        if (width < 4) {
            throw new IllegalArgumentException("A column width must be at least 4, but was " + width);
        }
        int[] columnWidths = getColumnWidthInternal();
        columnWidths[column] = width;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void print(Writer writer) throws IOException {
        if (tableModel.getColumnCount() == 0) {
            return;
        }

        if (title != null) {
            printTitle(writer);
        } else if (isHeaderEnabled()) {
            char[] line = new char[getWidth()];
            fillRowSeparator(line);
            writer.write(line);
        }

        if (isHeaderEnabled()) {
            printHeader(writer);
        } else if (tableModel.getRowCount() > 0) {
            char[] line = new char[getWidth()];
            fillRowSeparator(line);
            writer.write(line);
        }

        printRows(writer);
    }


    private void printTitle(Writer writer) throws IOException {
        char[] line = new char[getWidth()];
        Arrays.fill(line, '-');
        line[0] = '+';
        line[line.length - 1] = '+';
        writer.write(line);
        newLine(writer);

        Arrays.fill(line, ' ');
        line[0] = '|';
        line[line.length - 1] = '|';
        int titleWidth = getWidth() - 4;
        String abbreviated = abbreviate(title, titleWidth);
        System.arraycopy(abbreviated.toCharArray(), 0, line, 2, abbreviated.length());
        writer.write(line);
        newLine(writer);

        fillRowSeparator(line);

        writer.write(line);
    }

    private void printHeader(Writer writer) throws IOException {
        int columnCount = tableModel.getColumnCount();
        String[] columnNames = new String[columnCount];

        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = tableModel.getColumnName(i);
        }

        if (columnNames.length > 0) {
            newLine(writer);

            char[] line = rowLine();
            fillColumns(line, columnNames);


            writer.write(line);
            newLine(writer);

            fillRowSeparator(line);
            writer.write(line);
        }
    }

    private void printRows(Writer writer) throws IOException {
        if (tableModel.getRowCount() > 0) {
            for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
                printRow(writer, rowIndex);
            }
        }
    }

    private void printRow(Writer writer, int rowIndex) throws IOException {
        newLine(writer);

        int[] columnWidths = getColumnWidthInternal();

        char[] line = rowLine();
        String[] values = new String[tableModel.getColumnCount()];
        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++) {
            String valueText = tableStringConverter.toString(tableModel, rowIndex, columnIndex);
            String abbreviated = abbreviate(valueText, columnWidths[columnIndex]);
            values[columnIndex] = abbreviated;
        }
        fillColumns(line, values);
        writer.write(line);
        newLine(writer);

        line = rowLine();
        fillRowSeparator(line);
        writer.write(line);
    }


    private char[] rowLine() {
        char[] line = new char[getWidth()];
        Arrays.fill(line, ' ');
        line[0] = '|';
        line[line.length - 1] = '|';
        fillSeparator(line, '|');
        return line;
    }

    private void fillColumns(char[] line, String[] columns) {
        int headerTextIndex = 2;
        int[] columnWidths = getColumnWidthInternal();
        for (int i = 0; i < columns.length; i++) {
            String header = columns[i];
            String abbreviated = abbreviate(header, columnWidths[i]);
            System.arraycopy(abbreviated.toCharArray(), 0, line, headerTextIndex, abbreviated.length());
            headerTextIndex += columnWidths[i] + 3;
        }
    }

    private void fillRowSeparator(char[] line) {
        Arrays.fill(line, '-');
        line[0] = '+';
        line[line.length - 1] = '+';
        fillSeparator(line, '+');
    }

    private void fillSeparator(char[] line, char separator) {
        int[] columnWidths = getColumnWidthInternal();
        if (columnWidths.length > 0) {
            int separatorIndex = 3;

            for (int columnWidth : columnWidths) {
                separatorIndex += columnWidth;
                line[separatorIndex] = separator;
                separatorIndex += 3;
            }
        }
    }

    private String abbreviate(String text, int width) {
        if (text.length() < width) {
            return text;
        }
        char[] name = text.toCharArray();
        char[] abbreviated = new char[width];

        System.arraycopy(name, 0, abbreviated, 0, abbreviated.length);
        System.arraycopy("...".toCharArray(), 0, abbreviated, abbreviated.length - 3, 3);

        return new String(abbreviated);
    }

    private void newLine(Writer writer) throws IOException {
        writer.write('\n');
    }


}
