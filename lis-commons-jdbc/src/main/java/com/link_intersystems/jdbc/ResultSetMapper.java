package com.link_intersystems.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class ResultSetMapper<T> {

    private RowMapper<T> rowMapper;
    private Supplier<List<T>> listSupplier;

    public ResultSetMapper(RowMapper<T> rowMapper) {
        this(rowMapper, ArrayList::new);
    }

    public ResultSetMapper(RowMapper<T> rowMapper, Supplier<List<T>> listSupplier) {
        this.rowMapper = Objects.requireNonNull(rowMapper);
        this.listSupplier = Objects.requireNonNull(listSupplier);
    }

    public List<T> map(ResultSet resultSet) throws SQLException {
        List<T> resultList = listSupplier.get();

        while (resultSet.next()) {
            T element = rowMapper.map(resultSet);
            resultList.add(element);
        }

        return resultList;
    }
}
