package com.link_intersystems.util;

import java.io.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class Serialization {
    public static <T extends Serializable> byte[] serialize(T object) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(bout)) {
            objectOutputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bout.toByteArray();
    }

    public static <T> T deserialize(byte[] serializedObject) {
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(serializedObject))) {
            return (T) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException(e);
        }
    }

    public static <T extends Serializable> T clone(T serializable) {
        return deserialize(serialize(serializable));
    }
}
