package com.study.onlineshop.dao.jdbc.mapper;

import com.study.onlineshop.entity.Product;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductRowMapperTest {
    @Test
    public void testMapRow() throws Exception {
        // prepare
        LocalDateTime expectedDate = LocalDateTime.of(1990, 10, 10, 15, 25);
        ProductRowMapper productRowMapper = new ProductRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString(any())).thenReturn("Product name");
        when(resultSet.getDouble("price")).thenReturn(2000.50);
        when(resultSet.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf(expectedDate));

        // when
        Product actualProduct = productRowMapper.mapRow(resultSet);

        // then
        assertEquals(1, actualProduct.getId());
        assertEquals("Product name", actualProduct.getName());
        assertEquals(expectedDate, actualProduct.getCreationDate());
        assertEquals(2000.50, actualProduct.getPrice(), 0.0001);
    }


}