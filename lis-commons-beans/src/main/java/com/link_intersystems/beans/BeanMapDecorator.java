package com.link_intersystems.beans;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BeanMapDecorator extends HashMap<String, Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = -6218854323681382593L;

	public static final class IndexedValue {

		private final IndexedProperty<Object> indexedProperty;

		private IndexedValue(IndexedProperty<Object> indexedProperty) {
			this.indexedProperty = indexedProperty;
		}

		public Object getElement(int index) {
			return indexedProperty.getValue(index);
		}

	}

	public static final class IndexedElementSetter {

		private final int index;
		private final Object element;

		private IndexedElementSetter(int index, Object element) {
			this.index = index;
			this.element = element;
		}

		public int getIndex() {
			return index;
		}

		public Object getElement() {
			return element;
		}

	}

	public static Object indexedValueSetter(int index, Object value) {
		return new IndexedElementSetter(index, value);
	}

	private Bean<?> bean;

	public BeanMapDecorator(Object bean) {
		this.bean = new Bean<Object>(bean);
	}

	public int size() {
		BeanClass<?> beanClass = bean.getBeanClass();
		return beanClass.getPropertyDescriptors().size();
	}

	public boolean isEmpty() {
		/*
		 * Since all objects extend Object they have at least one property
		 * 'class'. Thus a bean map decorator can never be empty.
		 */
		return false;
	}

	public boolean containsKey(Object key) {
		if (key == null) {
			return false;
		}
		String propertyName = key.toString();
		return bean.hasAnyProperty(propertyName);
	}

	public Object get(Object key) {
		if (key == null) {
			return null;
		}
		String propertyName = key.toString();
		boolean hasAnyProperty = bean.hasAnyProperty(propertyName);
		if (!hasAnyProperty) {
			return null;
		}

		boolean isIndexedProperty = bean.hasIndexedProperty(propertyName);

		if (isIndexedProperty) {
			IndexedProperty<Object> indexedProperty = bean
					.getIndexedProperty(propertyName);
			checkReadAccess(indexedProperty);
			return new IndexedValue(indexedProperty);
		} else {
			Property<Object> property = bean.getProperty(propertyName);
			checkReadAccess(property);
			return property.getValue();
		}
	}

	public Object put(String key, Object value) {
		if (key == null) {
			throw new IllegalArgumentException(
					"BeanMapDecorator does not support putting 'null' keys, because a bean can never have a 'null' property.");
		}
		boolean hasAnyProperty = bean.hasAnyProperty(key);
		if (!hasAnyProperty) {
			throw new NoSuchPropertyException(bean.getBeanClass().getType(),
					key);
		}
		Object previousValue = null;

		boolean isIndexedProperty = bean.hasIndexedProperty(key);
		if (isIndexedProperty) {
			if (!(value instanceof IndexedElementSetter)) {
				throw new IllegalArgumentException(
						"Property named "
								+ key
								+ " is an indexed property. To set an indexed property's value you must use "
								+ IndexedElementSetter.class.getSimpleName());
			}
			IndexedElementSetter indexedValueSet = IndexedElementSetter.class
					.cast(value);
			IndexedProperty<Object> indexedProperty = bean
					.getIndexedProperty(key);
			checkWriteAccess(indexedProperty);
			indexedProperty.setValue(indexedValueSet.getIndex(),
					indexedValueSet.getElement());
		} else {
			Property<Object> property = bean.getProperty(key);
			checkWriteAccess(property);
			previousValue = getValueIfReadable(key);
			property.setValue(value);

		}

		return previousValue;
	}

	private Object getValueIfReadable(String propertyName) {
		Property<Object> property = bean.getProperty(propertyName);
		if (property.isReadable()) {
			return property.getValue();
		}
		return null;
	}

	private void checkWriteAccess(IndexedProperty<?> property) {
		if (!property.isIndexedWritable()) {
			throw new UnsupportedOperationException(
					"BeanMapDecorator can not put property "
							+ property
							+ ", because the indexed property has no indexed "
							+ "setter method, e.g. void setter(int index, PropertyType value); ");
		}
	}

	private void checkWriteAccess(Property<?> property) {
		if (!property.isWritable()) {
			throw new UnsupportedOperationException(
					"BeanMapDecorator can not put property " + property
							+ ", because the property is not writable");
		}
	}

	private void checkReadAccess(IndexedProperty<?> property) {
		if (!property.isIndexedReadable()) {
			throw new UnsupportedOperationException(
					"BeanMapDecorator can not get property "
							+ property
							+ ", because the indexed property has no indexed "
							+ "getter method, e.g. PropertyType getter(int index);");
		}
	}

	private void checkReadAccess(Property<?> property) {
		if (!property.isReadable()) {
			throw new UnsupportedOperationException(
					"BeanMapDecorator can not get property " + property
							+ ", because the property is not readable");
		}
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException(
				"BeanMapDecorator does not support remove");
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>
	 * This implementation iterates over the specified map's <tt>entrySet()</tt>
	 * collection, and calls this map's <tt>put</tt> operation once for each
	 * entry returned by the iteration.
	 *
	 * <p>
	 * Note that this implementation throws an
	 * <tt>UnsupportedOperationException</tt> if this map does not support the
	 * <tt>put</tt> operation and the specified map is nonempty.
	 *
	 * @throws UnsupportedOperationException
	 *             {@inheritDoc}
	 * @throws ClassCastException
	 *             {@inheritDoc}
	 * @throws NullPointerException
	 *             {@inheritDoc}
	 * @throws IllegalArgumentException
	 *             {@inheritDoc}
	 */
	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Map.Entry<? extends String, ? extends Object> e : m.entrySet())
			put(e.getKey(), e.getValue());
	}

	public void clear() {
		throw new UnsupportedOperationException(
				"A BeanMapDecorator can not be cleared, because"
						+ " properties of a bean belong to the bean's"
						+ " class and therefore can not be removed at runtime.");
	}

	public Set<String> keySet() {
		Set<String> keySet = new AbstractSet<String>() {

			@Override
			public Iterator<String> iterator() {
				return bean.getBeanClass().getPropertyNames().iterator();
			}

			@Override
			public int size() {
				return bean.getBeanClass().getPropertyNames().size();
			}
		};
		return keySet;
	}

	public Collection<Object> values() {
		Collection<Object> values = new AbstractCollection<Object>() {

			@Override
			public Iterator<Object> iterator() {
				return new Iterator<Object>() {
					private Iterator<String> propertsNameIterator = bean
							.getBeanClass().getPropertyNames().iterator();

					public boolean hasNext() {
						return propertsNameIterator.hasNext();
					}

					public Object next() {
						String propertyName = propertsNameIterator.next();
						PropertyDescriptor propertyDescriptor = bean
								.getBeanClass().getPropertyDescriptors()
								.get(propertyName);
						if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
							IndexedProperty<Object> indexedProperty = bean
									.getIndexedProperty(propertyName);
							return new IndexedValue(indexedProperty);
						} else {
							return bean.getProperty(propertyName).getValue();
						}
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				return bean.getBeanClass().getPropertyNames().size();
			}
		};
		return values;
	}

	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		Set<java.util.Map.Entry<String, Object>> entries = new AbstractSet<Map.Entry<String, Object>>() {

			@Override
			public Iterator<java.util.Map.Entry<String, Object>> iterator() {

				return new Iterator<Map.Entry<String, Object>>() {
					private Iterator<String> propertyNamesIterator = bean
							.getBeanClass().getPropertyNames().iterator();

					public boolean hasNext() {
						return propertyNamesIterator.hasNext();
					}

					public java.util.Map.Entry<String, Object> next() {
						final String propertyName = propertyNamesIterator
								.next();

						return new Map.Entry<String, Object>() {

							public Object setValue(Object value) {
								return put(propertyName, value);
							}

							public Object getValue() {
								return get(propertyName);
							}

							public String getKey() {
								return propertyName;
							}
						};
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}

			@Override
			public int size() {
				return BeanMapDecorator.this.size();
			}
		};
		return entries;
	}

	public boolean containsValue(Object value) {
		return values().contains(value);
	}

}
