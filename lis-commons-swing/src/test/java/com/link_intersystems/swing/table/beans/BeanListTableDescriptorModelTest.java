package com.link_intersystems.swing.table.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyDescriptor;
import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class BeanListTableDescriptorModelTest {

    private BeanListTableDescriptorModel<PersonBean> listTableCellSupport;
    private PersonBean personBean;

    @BeforeEach
    void setUp() {
        listTableCellSupport = BeanListTableDescriptorModel.of(PersonBean.class);
        personBean = new PersonBean();
        personBean.setFirstname("Nick");
        personBean.setLastname("Wahlberg");
        personBean.setBirthday(LocalDate.parse("1992-02-15"));
        personBean.setWriteOnlyProperty("someValue");

    }

    @Test
    void getColumnCount() {
        assertEquals(3, listTableCellSupport.getColumnCount());
    }

    @Test
    void getColumnCountFiltered() {
        listTableCellSupport.setPropertyFilter(p -> p.getName().endsWith("name"));

        assertEquals(2, listTableCellSupport.getColumnCount());
    }

    @Test
    void getColumnName() {
        assertEquals("birthday", getColumnName(0));
        assertEquals("firstname", getColumnName(1));
        assertEquals("lastname", getColumnName(2));
    }

    private String getColumnName(int column) {
        return listTableCellSupport.getColumnDescriptor(column).getName();
    }

    @Test
    void getColumnNameReverseOder() {
        Comparator<PropertyDescriptor> reverseNameOder = (pd1, pd2) -> pd2.getName().compareTo(pd1.getName());
        listTableCellSupport.setPropertyOrder(reverseNameOder);

        assertEquals("lastname", getColumnName(0));
        assertEquals("firstname", getColumnName(1));
        assertEquals("birthday", getColumnName(2));
    }

    @Test
    void reorderAfterPropertyOrderSet() {
        listTableCellSupport.getColumnCount();

        getColumnNameReverseOder();
    }

    @Test
    void getColumnNameFiltered() {
        listTableCellSupport.setPropertyFilter(p -> p.getName().endsWith("name"));

        assertEquals("firstname", getColumnName(0));
        assertEquals("lastname", getColumnName(1));
    }

    @Test
    void applyFilterAfterSet() {
        listTableCellSupport.getColumnCount();
        getColumnNameFiltered();
    }

    @Test
    void getValue() {
        Object firstColumnValue = getValue(personBean, 1);

        assertEquals("Nick", firstColumnValue);
    }

    private Object getValue(PersonBean bean, int column) {
        return listTableCellSupport.getColumnDescriptor(column).getValueGetter().apply(bean);
    }

    @Test
    void getValueOfWriteOnlyProperty() {
        listTableCellSupport.setPropertyFilter(p -> p.getName().equals("writeOnlyProperty"));

        Object value = getValue(personBean, 0);

        assertNull(value);
    }

}
