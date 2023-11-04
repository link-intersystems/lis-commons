package com.link_intersystems.net;

import com.link_intersystems.net.wol.ByteArrayAssertion;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.jupiter.api.Assertions.*;

class MACFormatTest {

    @Test
    void parseWithinString() {
        MACFormat macFormat = new MACFormat();

        ParsePosition pos = new ParsePosition("Hello ".length());
        String str = "Hello 00-B0-D0-63-C2-26 World";
        MAC mac = macFormat.parseObject(str, pos);
        assertNotNull(mac);

        assertEquals(23, pos.getIndex());
        assertEquals(" World", str.substring(pos.getIndex()));
    }

    @Test
    void parse() throws ParseException {
        MACFormat macFormat = new MACFormat();

        MAC mac = macFormat.parse("00-B0-D0-63-C2-26");

        assertNotNull(mac);

        ByteArrayAssertion macAssertion = new ByteArrayAssertion(mac.toByteArray());

        macAssertion.assertEquals(0, 0x00, 0xB0, 0xD0, 0x63, 0xC2, 0x26);

    }

    @Test
    void wrongOctetDelim() {
        MACFormat macFormat = new MACFormat();
        macFormat.setOctetDelim(':');

        ParseException parseException = assertThrows(ParseException.class, () -> macFormat.parse("00-B0-D0-63-C2-26"));

        assertEquals(2, parseException.getErrorOffset());
    }

    @Test
    void wrongHexOctet() {
        MACFormat macFormat = new MACFormat();

        ParseException parseException = assertThrows(ParseException.class, () -> macFormat.parse("A3-B0-G0-63-C2-26"));

        assertEquals(6, parseException.getErrorOffset());
    }

    @Test
    void format() {
        MACFormat macFormat = new MACFormat();
        MAC mac = new MAC(new byte[]{0x00, (byte) 0xB0, (byte) 0xD0, 0x63, (byte) 0xC2, 0x26});

        String formatted = macFormat.format(mac);

        assertEquals("00-B0-D0-63-C2-26", formatted);
    }

    @Test
    void formatWrongObj() {
        MACFormat macFormat = new MACFormat();

        assertThrows(IllegalArgumentException.class, () -> macFormat.format("00-B0-D0-63-C2-26"));
    }
}