package com.link_intersystems.lang.ref;

import org.junit.Assert;
import org.junit.Test;

public class DefaultMutableReferenceTest {

	@Test
	public void setAndGet() {
		DefaultMutableReference<String> defaultMutableReference = new DefaultMutableReference<String>();

		Assert.assertNull(defaultMutableReference.get());

		String newReferent = new String("someString");

		defaultMutableReference.set(newReferent);
		Assert.assertEquals(newReferent, defaultMutableReference.get());

		Assert.assertSame(newReferent, defaultMutableReference.get());
	}

	@Test
	public void setViaConstructor() {
		String newReferent = new String("someString");
		
		DefaultMutableReference<String> defaultMutableReference = new DefaultMutableReference<String>(newReferent);

		Assert.assertEquals(newReferent, defaultMutableReference.get());
		Assert.assertSame(newReferent, defaultMutableReference.get());
	}
}
