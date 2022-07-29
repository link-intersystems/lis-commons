package com.link_intersystems.security;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static java.lang.Integer.toHexString;
import static java.lang.System.arraycopy;
import static java.util.Arrays.fill;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public final class HashedCredentials {

    private static final String FALLBACK_MESSAGE_DIGEST = "SHA-256";
    public static final char[] EMPTY_CHARS = new char[0];

    public static final String SYSTEM_PROPERTY_DEFAULT_MESSAGE_DIGEST_NAME = HashedCredentials.class.getName() + ".defaultMessageDigest";

    private static String getDefaultMessageDigestName() {
        return System.getProperty(SYSTEM_PROPERTY_DEFAULT_MESSAGE_DIGEST_NAME, FALLBACK_MESSAGE_DIGEST);
    }

    private final byte[] hashedCredentials;

    /**
     *
     * @param username the username to hash. Array will be cleared after use so that it does not remain in memory.
     * @param password the password to hash. Array will be cleared after use so that it does not remain in memory.
     * @throws NoSuchAlgorithmException
     */
    public HashedCredentials(char[] username, char[] password) throws NoSuchAlgorithmException {
        this(getDefaultMessageDigestName(), username, password);
    }

    /**
     *
     * @param messageDigestName the name of the {@link MessageDigest#getInstance(String)} to use.
     * @param username the username to hash. Array will be cleared after use so that it does not remain in memory.
     * @param password the password to hash. Array will be cleared after use so that it does not remain in memory.
     * @throws NoSuchAlgorithmException
     */
    public HashedCredentials(String messageDigestName, char[] username, char[] password) throws NoSuchAlgorithmException {
        this(MessageDigest.getInstance(messageDigestName), username, password);
    }

    /**
     *
     * @param md the {@link MessageDigest} to use.
     * @param username the username to hash. Array will be cleared after use so that it does not remain in memory.
     * @param password the password to hash. Array will be cleared after use so that it does not remain in memory.
     */
    public HashedCredentials(MessageDigest md, char[] username, char[] password) {
        hashedCredentials = hash(md, username, password);
    }

    private static byte[] hash(MessageDigest messageDigest, char[] username, char[] password) {
        username = username == null ? EMPTY_CHARS : username;
        password = password == null ? EMPTY_CHARS : password;

        char[] credentials = new char[username.length + password.length];

        arraycopy(username, 0, credentials, 0, username.length);
        fill(username, ' ');

        arraycopy(password, 0, credentials, username.length, password.length);
        fill(password, ' ');

        CharBuffer charBuffer = CharBuffer.wrap(credentials);
        ByteBuffer charsetEncodedCredentials = StandardCharsets.UTF_8.encode(charBuffer);
        fill(credentials, ' ');

        byte[] charsetEncodedCredentialsArray = charsetEncodedCredentials.array();
        int remaining = charsetEncodedCredentials.remaining();

        messageDigest.update(charsetEncodedCredentialsArray, 0, remaining);
        byte[] hashedCredentials = messageDigest.digest();

        fill(charsetEncodedCredentialsArray, (byte) 0);

        return hashedCredentials;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashedCredentials that = (HashedCredentials) o;
        return Arrays.equals(hashedCredentials, that.hashedCredentials);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hashedCredentials);
    }


    @Override
    public String toString() {
        StringBuilder hexString = new StringBuilder(2 * hashedCredentials.length);
        for (int i = 0; i < hashedCredentials.length; i++) {
            String hex = toHexString(0xff & hashedCredentials[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
