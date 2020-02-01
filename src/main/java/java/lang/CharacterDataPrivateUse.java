/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.lang;

/** The CharacterData class encapsulates the large tables found in
    Java.lang.Character. */

class CharacterDataPrivateUse extends CharacterData {

    @Override
    int getProperties(int ch) {
        return 0;
    }

    @Override
    int getType(int ch) {
	return (ch & 0xFFFE) == 0xFFFE
	    ? Character.UNASSIGNED
	    : Character.PRIVATE_USE;
    }

    @Override
    boolean isJavaIdentifierStart(int ch) {
		return false;
    }

    @Override
    boolean isJavaIdentifierPart(int ch) {
		return false;
    }

    @Override
    boolean isUnicodeIdentifierStart(int ch) {
		return false;
    }

    @Override
    boolean isUnicodeIdentifierPart(int ch) {
		return false;
    }

    @Override
    boolean isIdentifierIgnorable(int ch) {
		return false;
    }

    @Override
    int toLowerCase(int ch) {
		return ch;
    }

    @Override
    int toUpperCase(int ch) {
		return ch;
    }

    @Override
    int toTitleCase(int ch) {
		return ch;
    }

    @Override
    int digit(int ch, int radix) {
		return -1;
    }

    @Override
    int getNumericValue(int ch) {
		return -1;
    }

    @Override
    boolean isWhitespace(int ch) {
		return false;
    }

    @Override
    byte getDirectionality(int ch) {
	return (ch & 0xFFFE) == 0xFFFE
	    ? Character.DIRECTIONALITY_UNDEFINED
	    : Character.DIRECTIONALITY_LEFT_TO_RIGHT;
    }

    @Override
    boolean isMirrored(int ch) {
		return false;
    }

    static final CharacterData instance = new CharacterDataPrivateUse();
    private CharacterDataPrivateUse() {};
}

	
