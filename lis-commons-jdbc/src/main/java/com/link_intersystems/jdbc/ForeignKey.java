package com.link_intersystems.jdbc;

import java.text.MessageFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ForeignKey extends AbstractList<ForeignKeyEntry> {

    private List<ForeignKeyEntry> foreignKeyEntryList = new ArrayList<>();

    public ForeignKey(List<ForeignKeyEntry> foreignKeyEntryList) {
        if (foreignKeyEntryList.isEmpty()) {
            throw new IllegalArgumentException("foreignKeyEntryList must not be empty");
        }

        ForeignKeyEntry mainFkEntry = null;
        for (ForeignKeyEntry jdbcForeignKeyEntry : foreignKeyEntryList) {
            if (mainFkEntry == null) {
                mainFkEntry = jdbcForeignKeyEntry;
            } else if (!mainFkEntry.isSameForeignKey(jdbcForeignKeyEntry)) {
                String msg = MessageFormat.format("foreignKeyEntryList contains different foreign keys: {0} != {1}", mainFkEntry, jdbcForeignKeyEntry);
                throw new IllegalArgumentException(msg);
            }


        }
        this.foreignKeyEntryList.addAll(foreignKeyEntryList);
    }

    @Override
    public ForeignKeyEntry get(int index) {
        return foreignKeyEntryList.get(index);
    }

    public List<ColumnDescription> getPKColumnDescriptions() {
        return stream().map(ForeignKeyEntry::getPkColumnDescription).collect(Collectors.toList());
    }

    public List<ColumnDescription> getFKColumnDescriptions() {
        return stream().map(ForeignKeyEntry::getFkColumnDescription).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return foreignKeyEntryList.size();
    }

    public String getName() {
        return get(0).getFkName();
    }
}
