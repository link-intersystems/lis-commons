package com.link_intersystems.beans.java;

import com.link_intersystems.beans.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class JavaBeansUsageExample {

    public static void main(String[] args) throws BeanClassException {
        BeansFactory factory = BeansFactory.getDefault();
        Bean<DefaultButtonModel> buttonModelBean = factory.createBean(new DefaultButtonModel());

        ChangeListener changeListener = ce -> System.out.println("button model changed.");
        ActionListener actionListener = ae -> System.out.println("action performed.");

        buttonModelBean.addListener(changeListener);
        buttonModelBean.addListener(actionListener);

        DefaultButtonModel buttonModel = buttonModelBean.getBeanObject();

        buttonModel.setPressed(true);
        buttonModel.setArmed(true);
        buttonModel.setPressed(false);

        PropertyList properties = buttonModelBean.getProperties().filter(Property.NONE_INDEXED);
        Property armedProperty = properties.getByName("armed");
        System.out.println("button model armed: " + armedProperty.getValue());
    }


}
