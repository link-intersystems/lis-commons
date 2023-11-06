# lis-commons-beans-mockito

A mockito extension for bean related stuff.

## BeanMatchers

The BeanMatchers class provides support for java bean related equality matching.

> verify(personSetter, times(1)).setPerson(BeanMatchers.propertiesEqual(expectedPerson));
>
> // or exclude properties
>
> verify(personSetter, times(1)).setPerson(BeanMatchers.propertiesEqual(expectedPerson,"firstname"));

### Custom BeanMatcher

You can also create a custom BeanMatcher with another BeansFactory. E.g. one can create BeanMatcher that supports
Java records.

> BeansFactory recordBeansFactory = BeansFactory.getInstance("record");
> BeanMatcher recordBeanMatcher = new BeanMatcher(recordBeansFactory);
>
> verify(personSetter, times(1)).setPerson(recordBeanMatcher.propertiesEqual(expectedPersonRecord));