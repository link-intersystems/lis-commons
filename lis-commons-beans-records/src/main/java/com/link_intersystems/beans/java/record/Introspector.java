package com.link_intersystems.beans.java.record;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

/**
 * An {@link java.beans.Introspector} like implementation that provides {@link BeanInfo} on Java record types.
 * <p>
 * Each record field will be recognized as a bean property. E.g. For a <code>PersonRecord</code>
 *
 * <pre>
 *     public record PersonRecord(String firstname, String lastname) {}
 * </pre>
 * <p>
 * you can obtain a {@link BeanInfo} by calling
 *
 * <pre>
 *     BeanInfo personRecordBeanInfo = Introspector.getBeanInfo(PersonRecord.class);
 * </pre>
 * <p>
 * whose {@link java.beans.PropertyDescriptor}s refer to the record fields.
 *
 * <pre>
 *     PropertyDescriptor[] propertyDescriptors = personRecordBeanInfo.getPropertyDescriptors();
 *
 *     assertEquals(2, propertyDescriptors.length()); // the firstname and lastname field of the PersonRecord
 * </pre>
 * <p>
 * Since a Java record is a value object and therefore all fields are immutable, all
 * {@link PropertyDescriptor#getWriteMethod()} calls will return <code>null</code>.
 */
public class Introspector {

    public static BeanInfo getBeanInfo(Class<?> recordType) throws IntrospectionException {
        return new RecordBeanInfo(recordType);
    }
}
