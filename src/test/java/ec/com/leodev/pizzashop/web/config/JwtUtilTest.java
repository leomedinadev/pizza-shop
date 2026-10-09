package ec.com.leodev.pizzashop.web.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil("secreto-de-prueba");

    @Test
    void shouldCreateAValidTokenForTheUser() {
        String jwt = jwtUtil.create("leo");

        assertTrue(jwtUtil.isValid(jwt));
        assertEquals("leo", jwtUtil.getUserName(jwt));
    }

    @Test
    void shouldRejectATokenSignedWithAnotherSecret() {
        String jwt = new JwtUtil("otro-secreto").create("leo");

        assertFalse(jwtUtil.isValid(jwt));
    }

    @Test
    void shouldRejectAMalformedToken() {
        assertFalse(jwtUtil.isValid("no-es-un-jwt"));
        assertFalse(jwtUtil.isValid(""));
    }

    @Test
    void shouldNotStartWithoutASecret() {
        assertThrows(IllegalStateException.class, () -> new JwtUtil(" "));
    }
}
