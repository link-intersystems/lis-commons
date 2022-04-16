package com.link_intersystems.lang;

/**
 * A ByteSequence is a readable sequence of byte values.
 * This interface provides uniform, read-only access to many different kinds of byte sequences.
 *
 * This interface does not refine the general contracts of the equals and hashCode methods.
 * The result of comparing two objects that implement {@link ByteSequence} is therefore, in general, undefined.
 * Each object may be implemented by a different class, and there is no guarantee that each class will be capable
 * of testing its instances for equality with those of the other.
 * It is therefore inappropriate to use arbitrary {@link ByteSequence} instances as elements in a set or as keys in a map.
 */
public interface ByteSequence {

    int length();

    byte byteAt(int index);

    ByteSequence subSequence(int start, int end);
}
