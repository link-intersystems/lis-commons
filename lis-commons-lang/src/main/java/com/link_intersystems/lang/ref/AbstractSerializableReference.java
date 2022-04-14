/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.lang.ref;

import com.link_intersystems.util.SerializationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A reference to an object that is not serializable but can be restored out of
 * other information (e.g. Class object can be restored from a full qualified
 * class name that is a string).
 *
 * @author René Link <a
 *         href="mailto:rene.link@link-intersystems.com">[rene.link@link-
 *         intersystems.com]</a>
 *
 * @param <T>
 *            of the wrapped non serializable object.
 * @since 1.0.0.0
 * @version 1.2.0.0
 */
public abstract class AbstractSerializableReference<T> implements
		SerializableReference<T> {

	/**
	 * Member field that holds a reference to the wrapped non-serializable
	 * object.
	 *
	 * @since 1.2.0.0
	 */
	private transient T transientReferent;

	/**
	 *
	 */
	private static final long serialVersionUID = -7758428790458970417L;

	private Serializable restoreInfo;

	/**
	 * Constructor that does not initialize the referent.
	 *
	 * @since 1.2.0.0
	 */
	protected AbstractSerializableReference() {
		this(null);
	}

	/**
	 * Constructor that initializes the referent.
	 *
	 * @param transientReferent
	 *            the referent that this {@link Reference} refers to.
	 * @since 1.2.0.0
	 */
	protected AbstractSerializableReference(T transientReferent) {
		this.transientReferent = transientReferent;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return the wrapped object that is not serializable by either returning
	 *         the object that this {@link SerializableReference} was construted
	 *         with or in case of deserialization delegates to the restore
	 *         routine to create one.
	 * @since 1.0.0.0
	 */
	public T get() {
		return transientReferent;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		if (transientReferent != null) {
			try {
				restoreInfo = serialize(transientReferent);
			} catch (SerializationException e) {
				throw e;
			} catch (Exception e) {
				Class<?> transientReferentClass = transientReferent.getClass();
				throw new SerializationException(
						"Unable to serialize object of "
								+ transientReferentClass, e);
			}
		}
		out.defaultWriteObject();
	}

	/**
	 * Convert the non-serializable referent to a serializable form. Called when
	 * the serialization is in progress.
	 *
	 * @param nonSerializableObject
	 *            the referent that must be converted to a serializable form.
	 * @return the serializable form that contains all information needed to
	 *         restore the referent on deserialization.
	 * @throws Exception
	 *             if any exception occurs.
	 * @since 1.2.0.0
	 */
	protected abstract Serializable serialize(T nonSerializableObject)
			throws Exception;

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		if (restoreInfo != null) {
			try {
				transientReferent = deserialize(restoreInfo);
			} catch (SerializationException e) {
				throw e;
			} catch (Exception e) {
				throw new SerializationException(
						"Unable to deserialize object reference", e);
			}
		}
	}

	/**
	 * Restore the referent from it's serializable form that was created by
	 * {@link #serialize(Object)}. Called when the de-serialization is in
	 * progress.
	 *
	 * @param restoreInfo
	 *            the serialized form that was created by
	 *            {@link #serialize(Object)}.
	 * @return the restored non-serializable referent.
	 * @throws Exception
	 *             if any exception occurs.
	 * @since 1.2.0.0
	 */
	protected abstract T deserialize(Serializable restoreInfo) throws Exception;

	/**
	 * {@inheritDoc}.
	 *
	 * @since 1.2.0.0
	 */
	@Override
	public String toString() {
		T referent = get();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Reference[");
		if (referent != null) {
			stringBuilder.append(referent.toString());
		} else {
			stringBuilder.append("null");
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	/**
	 * {@inheritDoc}. The hash code is based on the referent's hash code.
	 *
	 * @since 1.2.0.0
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((transientReferent == null) ? 0 : transientReferent
						.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}. Delegates to the referent's equals method. So a
	 * {@link AbstractSerializableReference} is considert equal to another
	 * {@link AbstractSerializableReference} if their referents are equal
	 * according to their equal method's implementation.
	 *
	 * @since 1.2.0.0
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		AbstractSerializableReference other = AbstractSerializableReference.class
				.cast(obj);

		if (transientReferent == null && other.transientReferent != null) {
			return false;
		}

		boolean referentsAreEqual = transientReferent
				.equals(other.transientReferent);
		return referentsAreEqual;
	}

}
