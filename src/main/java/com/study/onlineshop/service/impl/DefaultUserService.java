package com.study.onlineshop.service.impl;

import com.study.onlineshop.dao.UserDao;
import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;
import com.study.onlineshop.exception.UserExistsException;
import com.study.onlineshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class DefaultUserService implements UserService {

    @Autowired
    private UserDao userDao;
    private static final String HASH_ALGORITM = "SHA-256";
    private static final UserRole DEFAULT_USER_ROLE = UserRole.USER;

    @Override
    public User getUser(String login, String password) {
        User user = userDao.getUser(login);
        if (user == null) {
            return null;
        }
        String encryptedPassword = getEncryptedPassword(password, user.getSole());
        if (encryptedPassword.equals(user.getEncryptedPassword())) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void add(String login, String password) {
        add(login, password, DEFAULT_USER_ROLE);
    }

    @Override
    public void add(String login, String password, UserRole userRole) {
        if (userDao.getUser(login) != null) {
            throw new UserExistsException("User " + login + " already exists");
        }
        User user = new User();
        user.setLogin(login);
        String sole = nextSole();
        String encryptedPassword = getEncryptedPassword(password, sole);
        user.setEncryptedPassword(encryptedPassword);
        user.setSole(sole);
        user.setUserRole(userRole);
        int id = userDao.add(user);
        user.setId(id);
    }

    private String nextSole() {
        UUID sole = UUID.randomUUID();
        return sole.toString();
    }

    private String getEncryptedPassword(String password, String salt) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASH_ALGORITM);
            messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't encrypt password", e);
        }
    }
}