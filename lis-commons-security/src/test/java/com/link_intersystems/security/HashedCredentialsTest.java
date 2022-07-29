package com.link_intersystems.security;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import static com.link_intersystems.security.HashedCredentials.SYSTEM_PROPERTY_DEFAULT_MESSAGE_DIGEST_NAME;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class HashedCredentialsTest {

    @Test
    void hashUsingSystemPropertyMessageDigestName() throws NoSuchAlgorithmException {
        System.setProperty(SYSTEM_PROPERTY_DEFAULT_MESSAGE_DIGEST_NAME, "MD5");
        try {
            HashedCredentials hashedCredentials = new HashedCredentials("user1".toCharArray(), "pass1".toCharArray());
            assertEquals("ca34ff7d9e295155b189c3591f8ca195", hashedCredentials.toString());
        } finally {
            Properties properties = System.getProperties();
            properties.remove(SYSTEM_PROPERTY_DEFAULT_MESSAGE_DIGEST_NAME);
            assertNull(System.getProperty(SYSTEM_PROPERTY_DEFAULT_MESSAGE_DIGEST_NAME));
        }
    }

    @Test
    void toStringTest() throws NoSuchAlgorithmException {
        HashedCredentials hashedCredentials = new HashedCredentials("user1".toCharArray(), "pass1".toCharArray());
        assertEquals("7063cdd951fa1311939d12698440ee25816e1264e107e2e865200e1f28170d33", hashedCredentials.toString());
    }

    @Test
    void equals() throws NoSuchAlgorithmException {
        HashedCredentials hashedCredentials1 = new HashedCredentials("user1".toCharArray(), "password1".toCharArray());
        HashedCredentials hashedCredentials2 = new HashedCredentials("user1".toCharArray(), "password1".toCharArray());
        HashedCredentials hashedCredentials3 = new HashedCredentials("user2".toCharArray(), "password2".toCharArray());

        assertEquals(hashedCredentials1, hashedCredentials2);
        assertNotEquals(hashedCredentials1, hashedCredentials3);
    }

    @Test
    void nullUserName() throws NoSuchAlgorithmException {
        new HashedCredentials(null, "pass1".toCharArray());
    }

    @Test
    void nullPassword() throws NoSuchAlgorithmException {
        new HashedCredentials("user1".toCharArray(), null);
    }
}