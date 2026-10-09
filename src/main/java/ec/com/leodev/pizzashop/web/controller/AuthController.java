package ec.com.leodev.pizzashop.web.controller;

import ec.com.leodev.pizzashop.service.dto.LoginDto;
import ec.com.leodev.pizzashop.web.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        String jwt = this.jwtUtil.create(authentication.getName());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }
}
