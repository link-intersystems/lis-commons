package com.link_intersystems.beans.java.record;

import com.link_intersystems.beans.Bean;
import com.link_intersystems.beans.BeansFactory;
import com.link_intersystems.beans.PropertyList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecordExamplesTest {

    @Test
    void accessRecordProperties() {
        BeansFactory beansFactory = BeansFactory.getInstance("record");
        Bean<PersonRecord> bean = beansFactory.createBean(new PersonRecord("René", "Link"));
        PropertyList properties = bean.getSingleProperties();

        assertEquals("René", properties.getByName("firstname").getValue());
        assertEquals("Link", properties.getByName("lastname").getValue());
    }

    @Test
    void copyRecordToJavaBean() {
        BeansFactory recordBeansFactory = BeansFactory.getInstance("record");
        PersonRecord personRecord = new PersonRecord("René", "Link");
        Bean<PersonRecord> recordBean = recordBeansFactory.createBean(personRecord);

        BeansFactory javaBeansFactory = BeansFactory.getInstance("java");
        PersonBean personJavaBean = new PersonBean();
        Bean<PersonBean> javaBean = javaBeansFactory.createBean(personJavaBean);

        recordBean.getSingleProperties().copy(javaBean.getSingleProperties());


        assertEquals("René", personJavaBean.getFirstname());
        assertEquals("Link", personJavaBean.getLastname());
    }
}
