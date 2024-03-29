package com.link_intersystems.jdbc;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ForeignKeyList extends AbstractList<ForeignKey> {

    private List<ForeignKey> foreignKeyList;

    public ForeignKeyList(List<ForeignKey> foreignKeyList) {
        this.foreignKeyList = requireNonNull(foreignKeyList);
    }

    public ForeignKey getByName(String name) {
        return stream().filter(fk -> fk.getName().equals(name)).findFirst().orElse(null);
    }

    public ForeignKeyList getByPkColumnDescription(ColumnDescription... columnDescriptions) {
        List<ForeignKey> result = new ArrayList<>();

        foreignKeys:
        for (ForeignKey foreignKey : this) {
            List<ColumnDescription> descriptions = new ArrayList<>(Arrays.asList(columnDescriptions));

            for (ForeignKeyEntry entry : foreignKey) {
                ColumnDescription entryDescription = entry.getPkColumnDescription();
                for (int i = 0; i < descriptions.size(); i++) {
                    ColumnDescription columnDescription = descriptions.get(i);
                    if (ColumnDescriptionEquality.equals(entryDescription, columnDescription)) {
                        descriptions.remove(i);
                        i--;
                    }
                }
            }

            if (descriptions.isEmpty()) {
                result.add(foreignKey);
            }
        }

        return new ForeignKeyList(result);
    }

    public ForeignKey getByFkColumnDescription(ColumnDescription... columnDescriptions) {
        return getByColumnDescription(ForeignKeyEntry::getFkColumnDescription, columnDescriptions);
    }

    private ForeignKey getByColumnDescription(Function<ForeignKeyEntry, ColumnDescription> descriptionSupplier, ColumnDescription... columnDescriptions) {
        ForeignKey result = null;

        foreignKeys:
        for (ForeignKey foreignKey : this) {
            List<ColumnDescription> descriptions = new ArrayList<>(Arrays.asList(columnDescriptions));

            for (ForeignKeyEntry entry : foreignKey) {
                ColumnDescription entryDescription = descriptionSupplier.apply(entry);
                for (int i = 0; i < descriptions.size(); i++) {
                    ColumnDescription columnDescription = descriptions.get(i);
                    if (ColumnDescriptionEquality.equals(entryDescription, columnDescription)) {
                        descriptions.remove(i);
                        i--;
                    }
                }
            }

            if (descriptions.isEmpty()) {
                result = foreignKey;
                break;
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
