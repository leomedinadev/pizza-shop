package ec.com.leodev.pizzashop.web;

import ec.com.leodev.pizzashop.persistence.entity.UserEntity;
import ec.com.leodev.pizzashop.persistence.entity.UserRoleEntity;
import ec.com.leodev.pizzashop.web.config.JwtUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SecurityIntegrationTest {

    private static final String PASSWORD = "clave1234";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        saveUser("admin", "ADMIN", false);
        saveUser("cliente", "CUSTOMER", false);
        saveUser("bloqueado", "ADMIN", true);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldRejectRequestsWithoutToken() throws Exception {
        mockMvc.perform(get("/api/pizzas/findAll"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectInvalidOrIncompleteTokens() throws Exception {
        for (String authorization : new String[]{"Bearer", "Bearer ", "Bearer no-es-un-jwt", "Basic abc"}) {
            mockMvc.perform(get("/api/pizzas/findAll").header(HttpHeaders.AUTHORIZATION, authorization))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    void shouldAllowAuthenticatedUsersToReadPizzas() throws Exception {
        mockMvc.perform(get("/api/pizzas/findAll").header(HttpHeaders.AUTHORIZATION, bearer("cliente")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldOnlyLetAdminsDeletePizzas() throws Exception {
        mockMvc.perform(delete("/api/pizzas/999").header(HttpHeaders.AUTHORIZATION, bearer("cliente")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldOnlyLetAdminsReadOrders() throws Exception {
        mockMvc.perform(get("/api/orders").header(HttpHeaders.AUTHORIZATION, bearer("cliente")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/orders").header(HttpHeaders.AUTHORIZATION, bearer("admin")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectTokensOfLockedOrDeletedUsers() throws Exception {
        mockMvc.perform(get("/api/pizzas/findAll").header(HttpHeaders.AUTHORIZATION, bearer("bloqueado")))
                .andExpect(status().isForbidden());
        mockMvc.perform(get("/api/pizzas/findAll").header(HttpHeaders.AUTHORIZATION, bearer("no-existe")))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnATokenOnLoginWithValidCredentials() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"admin\", \"password\": \"" + PASSWORD + "\"}"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void shouldRejectLoginWithWrongPassword() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"admin\", \"password\": \"otra-clave\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldApplyTheCorsConfiguration() throws Exception {
        mockMvc.perform(options("/api/pizzas/findAll")
                        .header(HttpHeaders.ORIGIN, "http://localhost:4200")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:4200"));

        mockMvc.perform(options("/api/pizzas/findAll")
                        .header(HttpHeaders.ORIGIN, "http://otro-sitio.test")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isForbidden());
    }

    private String bearer(String username) {
        return "Bearer " + jwtUtil.create(username);
    }

    private void saveUser(String username, String role, boolean locked) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setEmail(username + "@pizza.test");
        user.setLocked(locked);
        user.setDisabled(false);
        entityManager.persist(user);

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUsername(username);
        userRole.setRole(role);
        userRole.setGrantedDate(LocalDateTime.now());
        entityManager.persist(userRole);
    }
}
