/**
 * The bean api is an abstraction of the Java Bean Specification and can be applied to different styles
 * of beans.
 * <p>
 * The term bean is highly overloaded nowadays. From the perspective of the Java Bean Specification a
 * bean is an object that exposes properties via getter and setter methods, has a default constructor and
 * events that listeners can listen to.
 * <p>
 * But if you think about other beans, like spring beans or hibernate beans, you will recognize that these beans do
 * not share all characteristics of a Java Bean Specification bean. e.g. they often don't need to have a default constructor,
 * or their property accessors (getter or setter) must not be public.
 * <p>
 * This api abstract from these implementation details and gives a bean api that can be adapted to any cases. This
 * gives one the opportunity to handle beans of different characteristics in the same way, which can be useful for
 * library and framework developers.
 * <p>
 * Furthermore, this api provides convenient access to beans, bean metadata and their properties.
 * <pre>
 * BeansFactory factory = BeansFactory.getDefault();
 * Bean<DefaultButtonModel> buttonModelBean = factory.createBean(new DefaultButtonModel());
 *
 * ChangeListener changeListener = ce -> System.out.println("button model changed.");
 * ActionListener actionListener = ae -> System.out.println("action performed.");
 *
 * buttonModelBean.addListener(changeListener);
 * buttonModelBean.addListener(actionListener);
 *
 * DefaultButtonModel buttonModel = buttonModelBean.getBeanObject();
 *
 * buttonModel.setPressed(true);
 * buttonModel.setArmed(true);
 * buttonModel.setPressed(false);
 *
 * PropertyList properties = buttonModelBean.getProperties();
 * Property armedProperty = properties.getByName("armed");
 * System.out.println("button model armed: " + armedProperty.getValue());
 * </pre>
 * will output
 * <pre>
 * button model changed.
 * button model changed.
 * action performed.
 * button model changed.
 * button model armed: true
 * </pre>
 */
package com.link_intersystems.beans;