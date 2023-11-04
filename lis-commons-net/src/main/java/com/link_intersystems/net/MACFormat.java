package com.link_intersystems.net;

import java.nio.ByteBuffer;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class MACFormat extends Format {
    public static final char IEEE_802_OCTET_DELIM = '-';

    private char octetDelim;

    public MACFormat() {
        this(IEEE_802_OCTET_DELIM);
    }

    public MACFormat(char octetDelim) {
        this.octetDelim = octetDelim;
    }

    public void setOctetDelim(char octetDelim) {
        this.octetDelim = octetDelim;
    }

    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        if (!(obj instanceof MAC)) {
            throw new IllegalArgumentException("obj is not a " + MAC.class.getName());
        }

        MAC mac = (MAC) obj;
        byte[] byteArray = mac.toByteArray();

        for (int i = 0; i < byteArray.length; i++) {
            pos.setBeginIndex(i);
            pos.setEndIndex(i + 3);
            byte octet = byteArray[i];
            String formattedOctet = String.format("%02X", octet);
            toAppendTo.append(formattedOctet);
            if (i + 1 < byteArray.length) {
                toAppendTo.append(octetDelim);
            }
        }

        return toAppendTo;
    }

    public MAC parse(String source) throws ParseException {
        ParsePosition pos = new ParsePosition(0);
        MAC result = parseObject(source, pos);
        if (pos.getIndex() == 0) {
            StringBuilder msg = new StringBuilder();
            msg.append("Unable to parse MAC with octet delimiter ");
            msg.append("[");
            msg.append(octetDelim);
            msg.append("]");
            msg.append(" : \"");
            msg.append(source);
            msg.append("\" ");
            msg.append("error at index ");
            msg.append(pos.getErrorIndex());
            msg.append(" [");
            msg.append(source.charAt(pos.getErrorIndex()));
            msg.append("]");

            throw new ParseException(msg.toString(), pos.getErrorIndex());
        }
        return result;
    }

    @Override
    public MAC parseObject(String source, ParsePosition pos) {
        ByteBuffer macBuff = ByteBuffer.allocate(6);

        int startPos = pos.getIndex();

        for (int i = pos.getIndex(); i < startPos + 17; i += 3) {
            String octet = source.substring(i, i + 2);
            try {
                int parsedOctet = Integer.parseInt(octet, 16);
                macBuff.put((byte) parsedOctet);
            } catch (NumberFormatException e) {
                pos.setErrorIndex(i);
                return null;
            }

            if (i + 2 < startPos + 17 && source.charAt(i + 2) != octetDelim) {
                pos.setErrorIndex(i + 2);
                return null;
            }


        }
        pos.setIndex(startPos + 17);

        return new MAC(macBuff.array());
    }

}

