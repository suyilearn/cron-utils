package com.cronutils.model.field.expression;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cronutils.model.field.value.IntegerFieldValue;
import com.cronutils.model.field.value.SpecialChar;
import com.cronutils.model.field.value.SpecialCharFieldValue;

import static org.junit.Assert.assertEquals;

/*
 * Copyright 2015 jmrozanec
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(AndroidJUnit4.class)
public class BetweenTest {
    private int from;
    private int to;

    @Before
    public void setUp() {
        from = 1;
        to = 5;
    }

    @Test
    public void testGetFrom() throws Exception {
        assertEquals(from, new Between(new IntegerFieldValue(from), new IntegerFieldValue(to)).getFrom().getValue());
    }

    @Test
    public void testGetTo() throws Exception {
        assertEquals(to, new Between(new IntegerFieldValue(from), new IntegerFieldValue(to)).getTo().getValue());
    }

    @Test
    public void testNonNumericRangeSupported() throws Exception {
        SpecialChar specialChar = SpecialChar.L;
        Between between = new Between(new SpecialCharFieldValue(specialChar), new IntegerFieldValue(to));
        assertEquals(specialChar, between.getFrom().getValue());
        assertEquals(to, between.getTo().getValue());
        assertEquals(String.format("%s-%s", specialChar, to), between.asString());
    }
}