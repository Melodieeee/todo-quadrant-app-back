package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dao.UserDao;
import com.melody.todoquadrantappback.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void getUserById_shouldReturnUser() {
        String userId = "User123";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("Test User");
        when(userDao.findById(userId)).thenReturn(Optional.of(mockUser));
        User result = userService.getUserById(userId);
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("Test User", result.getName());
        verify(userDao).findById(userId);
    }

    @Test
    void getUserById_shouldReturnNullIfNotFound() {
        String userId = "User123";
        when(userDao.findById(userId)).thenReturn(Optional.empty());
        User result = userService.getUserById(userId);
        assertNull(result);
        verify(userDao).findById(userId);
    }

    @Test
    void getUserByEmail_shouldReturnUser() {
        String userEmail = "user@example.com";
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        mockUser.setName("Test User");
        when(userDao.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        User result = userService.getUserByEmail(userEmail);
        assertNotNull(result);
        assertEquals(userEmail, result.getEmail());
        assertEquals("Test User", result.getName());
        verify(userDao).findByEmail(userEmail);
    }

    @Test
    void getUserByEmail_shouldReturnNullIfNotFound() {
        String userEmail = "user@example.com";
        when(userDao.findByEmail(userEmail)).thenReturn(Optional.empty());
        User result = userService.getUserByEmail(userEmail);
        assertNull(result);
        verify(userDao).findByEmail(userEmail);
    }

    @Test
    void createUser_shouldSaveAndReturnUser() {
        User newUser = new User();
        newUser.setName("New User");
        when(userDao.save(newUser)).thenReturn(newUser);
        User result = userService.createUser(newUser);
        assertNotNull(result);
        assertEquals("New User", result.getName());
        verify(userDao).save(newUser);
    }
}