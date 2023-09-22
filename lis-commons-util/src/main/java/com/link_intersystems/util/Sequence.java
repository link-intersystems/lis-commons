package com.link_intersystems.util;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.*;

public interface Sequence<E> extends Iterable<E> {

    public E elementAt(int index);

    public int length();

    default public Stream<E> stream() {
        if (length() == 0) {
            return Stream.empty();
        }


        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
    }

    @Override
    default public Iterator<E> iterator() {
        return listIterator();
    }

    default public ListIterator<E> listIterator() {
        class SequenceListIterator<E> implements ListIterator<E> {

            private int nextIndex = 0;
            private Sequence<E> sequence;

            public SequenceListIterator(Sequence<E> sequence) {
                this.sequence = requireNonNull(sequence);
            }

            @Override
            public boolean hasNext() {
                return nextIndex < sequence.length();
            }

            @Override
            public E next() {
                if (hasNext()) {
                    return sequence.elementAt(nextIndex++);
                }

                throw new NoSuchElementException("No element available at index " + nextIndex);
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            @Override
            public E previous() {
                if (hasPrevious()) {
                    return sequence.elementAt(--nextIndex);
                }

                throw new NoSuchElementException("No element available before index " + nextIndex);
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Elements can not be removed from a sequence");
            }

            @Override
            public void set(E element) {
                throw new UnsupportedOperationException("Elements can not be set on a sequence");
            }

            @Override
            public void add(E element) {
                throw new UnsupportedOperationException("Elements can not be added to a sequence");
            }
        }

        return new SequenceListIterator(this);
    }

    /**
     * Returns a {@code Sequence} that is a subsequence of this sequence.
     * The subsequence starts with the element at the specified index and
     * ends with the element at index {@code end - 1}.  The length
     * (elements) of the
     * returned sequence is {@code end - start}, so if {@code start == end}
     * then an empty sequence is returned.
     *
     * @param start the start index, inclusive
     * @param end   the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code start} or {@code end} are negative,
     *                                   if {@code end} is greater than {@code length()},
     *                                   or if {@code start} is greater than {@code end}
     */
    default Sequence<E> subSequence(int start, int end) {
        class SubSequence<E> implements Sequence<E> {

            private Sequence<E> origSequence;
            private int start;
            private int end;

            public SubSequence(Sequence<E> origSequence, int start, int end) {
                if (start < 0 || start > end || end > origSequence.length()) {
                    throw new IllegalArgumentException("start and end must be within original sequence bounds: 0 - " + origSequence.length());
                }
                this.origSequence = requireNonNull(origSequence);
                this.start = start;
                this.end = end;
            }

            @Override
            public E elementAt(int index) {
                if (index < length()) {
                    return origSequence.elementAt(start + index);
                }

                throw new IndexOutOfBoundsException(index);
            }

            @Override
            public int length() {
                return end - start;
            }
        }

        return new SubSequence<>(this, start, end);
    }
}
