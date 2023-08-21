package com.link_intersystems.beans;

public interface PropertySelector {
    /**
     * Selects a {@link Property} from the given {@link PropertyList} that matches the given property template or
     * null if the property could not be found in the property list.
     *
     * @param propertyList
     * @param propertyTemplate
     */
    Property select(PropertyList propertyList, Property propertyTemplate);
}
