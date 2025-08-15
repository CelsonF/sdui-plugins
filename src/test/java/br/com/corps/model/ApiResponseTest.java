package br.com.corps.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    void testSuccessResponse() {
        // Create test data
        List<String> testData = Arrays.asList("item1", "item2", "item3");
        String language = "en-US";
        
        // Create success response
        ApiResponse<List<String>> response = ApiResponse.success(testData, language);
        
        // Verify response
        assertEquals("success", response.getStatus());
        assertEquals("Data retrieved successfully", response.getMessage());
        assertEquals(language, response.getLanguage());
        assertEquals(testData, response.getData());
        assertEquals(3, response.getData().size());
    }
    
    @Test
    void testErrorResponse() {
        // Create error message
        String errorMessage = "Resource not found";
        
        // Create error response
        ApiResponse<List<String>> response = ApiResponse.error(errorMessage);
        
        // Verify response
        assertEquals("error", response.getStatus());
        assertEquals(errorMessage, response.getMessage());
        assertNull(response.getLanguage());
        assertNull(response.getData());
    }
    
    @Test
    void testBuilderAndGetterSetter() {
        // Create response using builder
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("custom")
                .message("Custom message")
                .language("es-ES")
                .data("Test data")
                .build();
        
        // Verify initial values
        assertEquals("custom", response.getStatus());
        assertEquals("Custom message", response.getMessage());
        assertEquals("es-ES", response.getLanguage());
        assertEquals("Test data", response.getData());
        
        // Test setters
        response.setStatus("updated");
        response.setMessage("Updated message");
        response.setLanguage("pt-BR");
        response.setData("Updated data");
        
        // Verify updated values
        assertEquals("updated", response.getStatus());
        assertEquals("Updated message", response.getMessage());
        assertEquals("pt-BR", response.getLanguage());
        assertEquals("Updated data", response.getData());
    }
    
    @Test
    void testNoArgsConstructor() {
        // Create response using no-args constructor
        ApiResponse<Object> response = new ApiResponse<>();
        
        // Verify all fields are null
        assertNull(response.getStatus());
        assertNull(response.getMessage());
        assertNull(response.getLanguage());
        assertNull(response.getData());
    }
}
