package com.link_intersystems.lang.reflect;

import java.io.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Serialization {
    public static <T extends Serializable> T clone(T object) {
        if (object == null) {
            return null;
        }

        return deserialize(serialize(object));
    }

    public static <T extends Serializable> byte[] serialize(T object) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(bout)) {
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to clone object.", e.getCause());
        }

        return bout.toByteArray();
    }

    public static <T> T deserialize(byte[] serializedObject) {
        return deserialize(serializedObject, ClassLoader.getPlatformClassLoader());
    }

    public static <T> T deserialize(byte[] serializedObject, ClassLoader classLoader) {
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serializedObject))) {
            return (T) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Unable to clone object.", e.getCause());
        }
    }
}
