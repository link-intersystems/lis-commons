/**
 * Copyright 2011 Link Intersystems GmbH <rene.link@link-intersystems.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.link_intersystems.math;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntModeTest {

    @Test
    public void modeNoValues() {
        Mode<Integer> mode = new IntMode();
        Integer value = mode.getValue();
        assertNull(value);
    }

    @Test
    public void mode() {
        Mode<Integer> mode = new IntMode();
        mode.addValue(1);
        mode.addValue(2);
        mode.addValue(3);
        mode.addValue(4);
        mode.addValue(2);
        mode.addValue(2.2);
        mode.addValue(2);
        mode.addValue(1);
        mode.addValue(1);
        Integer value = mode.getValue();
        assertEquals(2, value.intValue());
    }

    @Test
    public void modes() {
        Mode<Integer> mode = new IntMode();
        mode.addValue(1);
        mode.addValue(1);
        mode.addValue(1);
        mode.addValue(2);
        mode.addValue(2);
        mode.addValue(2);
        mode.addValue(3);
        mode.addValue(3);
        mode.addValue(3);
        mode.addValue(4);
        mode.addValue(4);
        mode.addValue(5);
        List<Integer> modes = mode.getValues();
        assertTrue(modes.contains(1));
        assertTrue(modes.contains(2));
        assertTrue(modes.contains(3));
    }

    @Test
    public void modeChanged() {
        Mode<Integer> mode = new IntMode();
        boolean changed = mode.addValue(1);
        assertTrue(changed);
        changed = mode.addValue(1);
        assertFalse(changed);
        changed = mode.addValue(2);
        assertFalse(changed);
        changed = mode.addValue(2);
        assertTrue(changed);
        changed = mode.addValue(1);
        assertTrue(changed);
        changed = mode.addValue(3);
        assertFalse(changed);
        changed = mode.addValue(3);
        assertFalse(changed);
        changed = mode.addValue(3);
        assertTrue(changed);
    }
}
