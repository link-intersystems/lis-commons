# lis-commons-beans-records

An extension library for the lis-commons-beans lib to handle Java records like beans.

## Usage

Let's assume you have a PersonRecord like this:

    public record PersonRecord(String firstname, String lastname) {
   
       PersonRecord() {
           this("unknown", "unknown");
       }
    }

You can then get an instance of the record BeansFactory by using the name "record":
 
    BeansFactory beansFactory = BeansFactory.getInstance("record");

This factory will create BeanClass instances that can handle Java records:

    BeanClass<PersonRecord> personRecordBeanClass = beansFactory.createBeanClass(PersonRecord.class);

    PersonRecord personRecord = new PersonRecord("René", "Link");
    Bean<PersonRecord> personRecordBean = personRecordBeanClass.getBeanFromInstance(personRecord);

    PropertyList properties = personRecordBean.getProperties();

    Property firstnameProperty = properties.getByName("firstname");
    assertEquals("René", firstnameProperty.getValue());

    Property lastnameProperty = properties.getByName("lastname");
    assertEquals("Link", lastnameProperty.getValue());


    public record PersonRecord(String firstname, String lastname) {}

       
    BeansFactory beansFactory = BeansFactory.getInstance("record");
    Bean<Person> bean = beansFactory.createBean(new PersonRecord("René", "Link"));
    PropertyList properties = bean.getProperties();

    assertEquals("René", properties.getByName("firstname").getValue());
    assertEquals("Link", properties.getByName("lastname").getValue());

You can also copy a record's values to another bean.

Let's assume you hava a Java record named `PersonRecord`

    public record PersonRecord(String firstname, String lastname) {}

and a Java bean names `PersonBean`

    public class PersonBean {
    
        private String firstname;
        private String lastname;
    
        public String getFirstname() {
            return firstname;
        }
    
        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }
    
        public String getLastname() {
            return lastname;
        }
    
        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
    }

you can then copy the "properties" (record values) from the `PersonRecord` to the `PersonBean` by using different BeanFactory instances.

    BeansFactory recordBeansFactory = BeansFactory.getInstance("record");
    PersonRecord personRecord = new PersonRecord("René", "Link");
    Bean<Person> recordBean = recordBeansFactory.createBean(personRecord);

    BeansFactory javaBeansFactory = BeansFactory.getInstance("java"); // the default if no name is provided
    PersonBean personJavaBean = new PersonBean();
    Bean<PersonBean> javaBean = javaBeansFactory.createBean(personJavaBean);

    recordBean.getProperties().copy(javaBean.getProperties());

    assertEquals("René", personJavaBean.getFirstname());
    assertEquals("Link", personJavaBean.getLastname());

