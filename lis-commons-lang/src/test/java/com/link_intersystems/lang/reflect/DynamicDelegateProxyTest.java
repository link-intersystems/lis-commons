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
package com.link_intersystems.lang.reflect;

import java.util.List;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.link_intersystems.lang.ref.Reference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicDelegateProxyTest {

	private IMocksControl strictControl;
	private IMocksControl niceControl;
	private List<String> listMock;
	private List<String> anotherListMock;
	private Reference<List<String>> listRefMock;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setupMocks() {
		strictControl = EasyMock.createStrictControl();
		niceControl = EasyMock.createNiceControl();
		listMock = strictControl.createMock(List.class);
		anotherListMock = strictControl.createMock(List.class);
		listRefMock = niceControl.createMock(Reference.class);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void passthroughDelegate() {
		EasyMock.expect(listRefMock.get()).andReturn(listMock).anyTimes();
		EasyMock.expect(listMock.size()).andReturn(5);
		replayMocks();
		List<String> stringListDelegate = DynamicDelegateProxy.create(
				List.class, listRefMock);
		int size = stringListDelegate.size();
		assertEquals(5, size);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void changingTarget() {
		EasyMock.expect(listRefMock.get()).andReturn(listMock);
		EasyMock.expect(listRefMock.get()).andReturn(anotherListMock);
		EasyMock.expect(listMock.size()).andReturn(5);
		EasyMock.expect(anotherListMock.size()).andReturn(2);
		replayMocks();
		List<String> stringListDelegate = DynamicDelegateProxy.create(
				List.class, listRefMock);
		int size = stringListDelegate.size();
		assertEquals(5, size);
		size = stringListDelegate.size();
		assertEquals(2, size);
	}

	@After
	public void verifyMocks() {
		strictControl.verify();
		niceControl.verify();
	}

	private void replayMocks() {
		strictControl.replay();
		niceControl.replay();
	}
}
