package com.example.cube.Exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionWithLongValue() {
        // Arrange
        String resourceName = "Post";
        String fieldName = "id";
        long fieldValue = 1L;
        String expectedMessage = "Post not found with id : '1'";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Assert
        Assertions.assertEquals(resourceName, exception.getResourceName());
        Assertions.assertEquals(fieldName, exception.getFieldName());
        Assertions.assertEquals(fieldValue, exception.getFieldLongValue());
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionWithStringValue() {
        // Arrange
        String resourceName = "Post";
        String fieldName = "title";
        String fieldValue = "Test";
        String expectedMessage = "Post not found with title : 'Test'";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        // Assert
        Assertions.assertEquals(resourceName, exception.getResourceName());
        Assertions.assertEquals(fieldName, exception.getFieldName());
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

}