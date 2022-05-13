package com.link_intersystems.jdbc;

import java.util.AbstractList;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ForeignKeyList extends AbstractList<ForeignKey> {

    private List<ForeignKey> foreignKeyList;

    public ForeignKeyList(List<ForeignKey> foreignKeyList) {
        this.foreignKeyList = foreignKeyList;
    }

    public ForeignKey getByPkColumnDescription(ColumnDescription columnDescription) {
        ForeignKey result = null;

        ColumnDescriptionEquality equality = new ColumnDescriptionEquality(columnDescription);

        foreignKeys:
        for (ForeignKey foreignKey : this) {
            for (ForeignKeyEntry entry : foreignKey) {
                ColumnDescription pkColumnDescription = entry.getPkColumnDescription();
                if (equality.equalsDescription(pkColumnDescription)) {
                    result = foreignKey;
                    break foreignKeys;
                }
            }
        }

        return result;
    }

    public ForeignKey getByFkColumnDescription(ColumnDescription columnDescription) {
        ForeignKey result = null;

        ColumnDescriptionEquality equality = new ColumnDescriptionEquality(columnDescription);

        foreignKeys:
        for (ForeignKey foreignKey : this) {
            for (ForeignKeyEntry entry : foreignKey) {
                ColumnDescription pkColumnDescription = entry.getFkColumnDescription();
                if (equality.equalsDescription(pkColumnDescription)) {
                    result = foreignKey;
                    break foreignKeys;
                }
            }
        }

        return result;
    }

    @Override
    public ForeignKey get(int index) {
        return foreignKeyList.get(index);
    }

    @Override
    public int size() {
        return foreignKeyList.size();
    }
}
