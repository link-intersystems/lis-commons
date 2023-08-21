package com.link_intersystems.beans;

public class PropertySelectors {
    public static final PropertySelector BY_NAME = (propertyList, propertyTemplate) -> {
        PropertyDesc templateDesc = propertyTemplate.getPropertyDesc();
        String propertyName = templateDesc.getName();
        Property byName = propertyList.getByName(propertyName);
        if(byName == null){
            return null;
        }

        if (byName.getPropertyDesc().getType().isAssignableFrom(templateDesc.getType())) {
            return byName;
        }

        return null;
    };
}
