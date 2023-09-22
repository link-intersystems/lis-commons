package com.link_intersystems.util;

import java.util.Objects;

import static java.util.Objects.*;

public class CharacterSequence implements Sequence<Character> {

    private CharSequence charSequence;

    public CharacterSequence(CharSequence charSequence) {
        this.charSequence = requireNonNull(charSequence);
    }

    @Override
    public Character elementAt(int index) {
        return charSequence.charAt(index);
    }

    @Override
    public int length() {
        return charSequence.length();
    }

}
