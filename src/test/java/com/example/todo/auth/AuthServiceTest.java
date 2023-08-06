package com.example.todo.auth;

import com.example.todo.config.JwtUtils;
import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder,
                jwtUtils);
    }

    @AfterEach
    void tearDown() {
        reset(userRepository);
        reset(passwordEncoder);
        reset(jwtUtils);
    }

    @Test
    void getUserByEmail_itShouldCallTheRepositoryFindByEmailMethod() {
        // When getUserByEmail is called
        String email = "test@gmail.com";
        authService.getUserByEmail(email);
        // Then the findByEmail method from UserRepository is called
        verify(userRepository).findByEmail(email);
    }

    @Test
    void getUserByEmail_itShouldCallTheRepositoryFindByEmailMethodWithGivenEmail() {
        // When getUserByEmail is called with a String of an email
        String email = "test@gmail.com";
        authService.getUserByEmail(email);
        // Then the findByEmail method from UserRepository is called with the
        // email that was passed as an argument to getUserByEmail
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(userRepository).findByEmail(argumentCaptor.capture());
        String argument = argumentCaptor.getValue();
        assertThat(argument).isEqualTo(email);
    }

    @Test
    void getUserByEmail_itShouldReturnTheResultFromTheRepositoryFindByEmailMethod() {
        // Given UserRepository's findByEmail method returns a User
        String email = "test@gmail.com";
        User user = new User("test", email, "password");
        user.setId(1L);
        Optional<User> repositoryResult = Optional.of(user);
        given(userRepository.findByEmail(email)).willReturn(repositoryResult);
        // When getUserByEmail is called with a String of an email
        Optional<User> serviceResult = authService.getUserByEmail(email);
        // Then getUserByEmail will return the result from the findByEmail
        // method from UserRepository
        assertThat(serviceResult).isEqualTo(repositoryResult);
    }

    @Test
    void createUser_itShouldCallTheRepositorySaveMethodWithAUserObject() {
        // Given a payload of a name, email and password
        RegistrationPayload payload = new RegistrationPayload(
                "test",
                "test@gmail.com",
                "password"
        );
        // When createUser() is called with the payload
        authService.createUser(payload);
        // Then UserRepository's save() method is called with a user object
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_itShouldCreateAUserObjectWithTheSameNameFromThePayload() {
        // Given a payload of a name, email and password
        String name = "test";
        RegistrationPayload payload = new RegistrationPayload(
                name,
                "test@gmail.com",
                "password"
        );
        // When createUser() is called with the payload
        authService.createUser(payload);
        // Then the user object created has the same name from the payload
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo(name);
    }

    @Test
    void createUser_itShouldCreateAUserObjectWithTheSameEmailFromThePayload() {
        // Given a payload of a name, email and password
        String email = "test@gmail.com";
        RegistrationPayload payload = new RegistrationPayload(
                "test",
                email,
                "password"
        );
        // When createUser() is called with the payload
        authService.createUser(payload);
        // Then the user object created has the same email from the payload
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(email);
    }

    @Test
    void createUser_itShouldCreateAUserObjectWithAnEncryptedVersionOfThePasswordFromThePayload() {
        // Given a payload of a name, email and password
        String password = "password";
        RegistrationPayload payload = new RegistrationPayload(
                "test",
                "test@gmail.com",
                password
        );
        // When createUser() is called with the payload
        authService.createUser(payload);
        // Then the user object created has an encrypted version of the password
        // from the payload
        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());
        User capturedUser = argumentCaptor.getValue();
        assertThat(capturedUser.getPassword()).isNotEqualTo(password);
        assertThat(capturedUser.getPassword())
                .isEqualTo(passwordEncoder.encode(password));
    }

    @Test
    void generateUserToken_itShouldCallGenerateTokenFromJwtUtils() {
        // When generateUserToken() is called
        User user = new User("test", "test@gmail.com", "password");
        authService.generateUserToken(user);
        // Then JwtUtils' generateToken() is called
        verify(jwtUtils).generateToken(user);
    }

    @Test
    void generateUserToken_itShouldReturnTheResultFromGenerateTokenFromJwtUtils() {
        // When generateUserToken() is called
        // and JwtUtils' generateToken() returns somerandomjwttoken
        User user = new User("test", "test@gmail.com", "password");
        String token = "somerandomjwttoken";
        given(jwtUtils.generateToken(user)).willReturn(token);
        // Then generateUserToken() returns somerandomjwttoken
        String returnValue = authService.generateUserToken(user);
        assertThat(returnValue).isEqualTo(token);
    }

    @Test
    void isPasswordValid_itShouldCallMatchesFromPasswordEncoder() {
        // When isPasswordValid() is called
        String rawPassword = "password";
        String encryptedPassword = "encryptedPassword";
        authService.isPasswordValid(rawPassword, encryptedPassword);
        // Then PasswordEncoder's matches method is called
        verify(passwordEncoder).matches(rawPassword, encryptedPassword);
    }

    @Test
    void isPasswordValid_itReturnTheResultFromTheMatchesMethodInPasswordEncoder() {
        // Given PasswordEncoder's matches() method returns true
        String rawPassword = "password";
        String encryptedPassword = "encryptedPassword";
        given(passwordEncoder.matches(rawPassword, encryptedPassword))
                .willReturn(true);
        // When isPasswordValid() is called
        boolean result = authService.isPasswordValid(
                rawPassword,
                encryptedPassword
        );
        // Then PasswordEncoder's matches method is called
        assertThat(result).isEqualTo(true);
    }
}