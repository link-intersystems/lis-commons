package com.link_intersystems.swing.action;

import javax.swing.*;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Stream;

public class ActionUtil {

    /**
     * Streams the generic {@link Action} property keys, like {@link Action#NAME}.
     *
     * @return
     */
    public static Stream<String> actionKeys() {
        Stream<String> keys = Arrays.stream(Action.class.getDeclaredFields()).filter(f -> {
            int m = f.getModifiers();
            return Modifier.isPublic(m) && Modifier.isStatic(m) && Modifier.isFinal(m);
        }).filter(f -> String.class.equals(f.getType())).map(f -> {
            try {
                return (String) f.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return keys;
    }

}
