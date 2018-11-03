package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.Product;
import com.study.onlineshop.entity.User;
import com.study.onlineshop.entity.UserRole;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRowMapperTest {
    @Test
    public void testMapRow() throws Exception {
        // prepare
        UserRowMapper userRowMapper = new UserRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("login")).thenReturn("User name");
        when(resultSet.getString("encrypted_password")).thenReturn("encrypted_password_value");
        when(resultSet.getString("sole")).thenReturn("sole_value");
        when(resultSet.getString("user_role")).thenReturn(UserRole.USER.toString());

        // when
        User actualUser = userRowMapper.mapRow(resultSet);

        // then
        assertEquals(1, actualUser.getId());
        assertEquals("User name", actualUser.getLogin());
        assertEquals("encrypted_password_value", actualUser.getEncryptedPassword());
        assertEquals("sole_value", actualUser.getSole());
        assertEquals("USER", actualUser.getUserRole().toString());
    }


}