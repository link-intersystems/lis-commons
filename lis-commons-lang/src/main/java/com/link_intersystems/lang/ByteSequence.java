package com.link_intersystems.lang;

public interface ByteSequence {

	int length();

	byte byteAt(int index);

	ByteSequence subSequence(int start, int end);
}
