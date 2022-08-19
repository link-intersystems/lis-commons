package com.link_intersystems.jdbc;

import java.io.*;
import java.sql.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class DefaultSqlTypeMapper implements SqlTypeMapper {

    private BiFunction<ResultSet, Integer, ZoneId> zoneIdSupplier = (rs, columnIndex) -> ZoneId.systemDefault();
    private Map<Integer, SqlTypeMapper> sqlTypeMappers;

    public DefaultSqlTypeMapper() {
        Map<Integer, SqlTypeMapper> sqlTypeMappers = new HashMap<>();
        initSqlTypeMappers(sqlTypeMappers);
        this.sqlTypeMappers = sqlTypeMappers;
    }

    public void setZoneId(ZoneId zoneId) {
        setZoneIdSupplier((rs, columnIndex) -> zoneId);
    }

    public void setZoneIdSupplier(BiFunction<ResultSet, Integer, ZoneId> zoneIdSupplier) {
        this.zoneIdSupplier = requireNonNull(zoneIdSupplier);
    }

    public Map<Integer, SqlTypeMapper> getSqlTypeMappers() {
        if (sqlTypeMappers == null) {
            Map<Integer, SqlTypeMapper> sqlTypeMappers = new HashMap<>();
            initSqlTypeMappers(sqlTypeMappers);
            this.sqlTypeMappers = sqlTypeMappers;
        }
        return sqlTypeMappers;
    }

    protected void initSqlTypeMappers(Map<Integer, SqlTypeMapper> sqlTypeMappers) {

        sqlTypeMappers.put(Types.NCHAR, ResultSet::getString);
        sqlTypeMappers.put(Types.CHAR, ResultSet::getString);
        sqlTypeMappers.put(Types.VARCHAR, ResultSet::getString);
        sqlTypeMappers.put(Types.NVARCHAR, ResultSet::getString);
        sqlTypeMappers.put(Types.LONGVARCHAR, ResultSet::getString);
        sqlTypeMappers.put(Types.LONGNVARCHAR, ResultSet::getString);
        sqlTypeMappers.put(Types.CLOB, this::clobToString);
        sqlTypeMappers.put(Types.NCLOB, this::clobToString);

        sqlTypeMappers.put(Types.TINYINT, ResultSet::getByte);
        sqlTypeMappers.put(Types.SMALLINT, ResultSet::getShort);
        sqlTypeMappers.put(Types.INTEGER, ResultSet::getInt);
        sqlTypeMappers.put(Types.BIGINT, ResultSet::getLong);

        sqlTypeMappers.put(Types.FLOAT, ResultSet::getFloat);
        sqlTypeMappers.put(Types.DOUBLE, ResultSet::getDouble);
        sqlTypeMappers.put(Types.DECIMAL, ResultSet::getBigDecimal);
        sqlTypeMappers.put(Types.NUMERIC, ResultSet::getBigDecimal);

        sqlTypeMappers.put(Types.BOOLEAN, ResultSet::getBoolean);
        sqlTypeMappers.put(Types.BIT, ResultSet::getBoolean);

        sqlTypeMappers.put(Types.TIME, this::timeToLocalTime);
        sqlTypeMappers.put(Types.DATE, this::dateToLocalDate);
        sqlTypeMappers.put(Types.TIMESTAMP, this::timestampToLocalDateTime);
        sqlTypeMappers.put(Types.TIMESTAMP_WITH_TIMEZONE, this::offsetDateTime);
        sqlTypeMappers.put(Types.TIME_WITH_TIMEZONE, this::offsetTime);

        sqlTypeMappers.put(Types.BLOB, this::blobToByteArray);
    }

    @Override
    public Object toObject(ResultSet resultSet, int columnIndex) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnType = metaData.getColumnType(columnIndex);
        SqlTypeMapper sqlTypeMapper = sqlTypeMappers.getOrDefault(columnType, ResultSet::getObject);
        return sqlTypeMapper.toObject(resultSet, columnIndex);
    }

    public Object clobToString(ResultSet resultSet, int columnIndex) throws SQLException {
        StringBuffer stringBuffer = new StringBuffer();

        Clob clob = resultSet.getClob(columnIndex);
        try (Reader reader = new BufferedReader(new InputStreamReader(clob.getAsciiStream()))) {
            char[] buff = new char[8192];

            int read;
            while ((read = reader.read(buff)) > 0) {
                stringBuffer.append(buff, 0, read);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return stringBuffer.toString();
    }

    public byte[] blobToByteArray(ResultSet resultSet, int columnIndex) throws SQLException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Blob blob = resultSet.getBlob(columnIndex);
        try (InputStream in = new BufferedInputStream(blob.getBinaryStream())) {
            byte[] buff = new byte[8192];

            int read;
            while ((read = in.read(buff)) > 0) {
                bout.write(buff, 0, read);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        return bout.toByteArray();
    }

    public Object dateToLocalDate(ResultSet resultSet, int columnIndex) throws SQLException {
        Date date = resultSet.getDate(columnIndex);
        ZoneId zoneId = zoneIdSupplier.apply(resultSet, columnIndex);
        return Instant.ofEpochMilli(date.getTime()).atZone(zoneId).toLocalDate();
    }

    public Object timestampToLocalDateTime(ResultSet resultSet, int columnIndex) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp(columnIndex);
        ZoneId zoneId = zoneIdSupplier.apply(resultSet, columnIndex);
        return Instant.ofEpochMilli(timestamp.getTime()).atZone(zoneId).toLocalDateTime();
    }

    public Object timeToLocalTime(ResultSet resultSet, int columnIndex) throws SQLException {
        Time time = resultSet.getTime(columnIndex);
        ZoneId zoneId = zoneIdSupplier.apply(resultSet, columnIndex);
        return Instant.ofEpochMilli(time.getTime()).atZone(zoneId).toLocalTime();
    }

    public Object offsetTime(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex, OffsetTime.class);
    }

    public Object offsetDateTime(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getObject(columnIndex, OffsetDateTime.class);
    }
}

