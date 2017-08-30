/**
 * Copyright 2016-2017 The Reaktivity Project
 *
 * The Reaktivity Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.reaktivity.nukleus.maven.plugin.internal.generated;

import static java.nio.ByteBuffer.allocateDirect;
import static org.junit.Assert.assertEquals;

import org.agrona.MutableDirectBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.junit.Test;
import org.reaktivity.reaktor.internal.test.types.FlatFW;

public class FlatFWIT
{
    FlatFW.Builder flatRW = new FlatFW.Builder();
    FlatFW flatRO = new FlatFW();
    MutableDirectBuffer buffer = new UnsafeBuffer(allocateDirect(100));

    @Test
    public void shouldDefaultValues() throws Exception
    {
        // Set an explicit value first in the same memory to make sure it really
        // gets set to the default value next time round
        flatRW.wrap(buffer, 0, buffer.capacity())
                .fixed1(10)
                .fixed2(20)
                .string1("value1")
                .fixed3(30)
                .string2("value2")
                .build();
        flatRO.wrap(buffer,  0,  100);
        assertEquals(20, flatRO.fixed2());

        flatRW.wrap(buffer, 0, 100)
                .fixed1(10)
                .string1("value1")
                .string2("value2")
                .build();
        flatRO.wrap(buffer,  0,  100);
        assertEquals(222, flatRO.fixed2());
        assertEquals(333, flatRO.fixed3());
    }

    @Test // TODO (expected = IllegalStateException.class)
    public void shouldFailToSetMemberFollowingUnsetRequiredMemberfixed1fixed2() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
                .fixed2(10);
    }

    @Test // TODO (expected = ...
    public void shouldFailToSetMemberFollowingUnsetRequiredMemberfixed1string1() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
                .string1("value1");
    }


    @Test //TODO: expected...
    public void shouldFailToSetMemberFollowingUnsetRequiredMemberfixed3() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .fixed1(10)
            .fixed2(111)
            .string1("value1")
            .string2("value2");
    }

    @Test //TODO: expected...
    public void shouldFailToSetMemberFollowingUnsetRequiredMemberstring2() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .fixed1(10)
            .fixed2(111)
            .string1("value1")
            .fixed3(33)
            .build();
    }

    @Test //TODO: expected...
    public void shouldFailToBuildIfRequiredMemberNotSetfixed1() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .build();
    }

    @Test //TODO: expected...
    public void shouldFailToBuildIfRequiredMemberNotSetstring1() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .fixed1(10)
            .build();
    }

    @Test //TODO: expected...
    public void shouldFailToBuildIfRequiredMemberNotSetfixed3() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .fixed1(10)
            .fixed2(111)
            .string1("value1")
            .build();
    }

    @Test //TODO: expected...
    public void shouldFailToBuildIfRequiredMemberNotSetstring2() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .fixed1(10)
            .fixed2(111)
            .string1("value1")
            .fixed3(33)
            .build();
    }

    @Test //TODO: expected...
    public void shouldFailToBuildIfRequiredMemberNotSetextension() throws Exception
    {
        flatRW.wrap(buffer, 0, 100)
            .fixed1(10)
            .fixed2(111)
            .string1("value1")
            .fixed3(33)
            .string2("value2")
            .build();
    }

    @Test
    public void shouldSetAllValues() throws Exception
    {
        flatRW.wrap(buffer, 0, buffer.capacity())
                .fixed1(10)
                .fixed2(111)
                .string1("value1")
                .fixed3(33)
                .string2("value2")
                .build();
        flatRO.wrap(buffer,  0,  100);
        assertEquals(10, flatRO.fixed1());
        assertEquals(111, flatRO.fixed2());
        assertEquals("value1", flatRO.string1().asString());
        assertEquals(33, flatRO.fixed3());
        assertEquals("value2", flatRO.string2().asString());
    }

}
