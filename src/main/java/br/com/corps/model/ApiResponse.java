package br.com.corps.model;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic API response wrapper
 * 
 * @param <T> Type of data contained in the response
 */
@Introspected
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * Response status (success, error)
     */
    private String status;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Language used in the response
     */
    private String language;
    
    /**
     * Response data
     */
    private T data;
    
    /**
     * Create a success response
     * 
     * @param data the response data
     * @param language the language used
     * @param <T> the type of data
     * @return a success response
     */
    public static <T> ApiResponse<T> success(T data, String language) {
        return ApiResponse.<T>builder()
                .status("success")
                .message("Data retrieved successfully")
                .language(language)
                .data(data)
                .build();
    }
    
    /**
     * Create an error response
     * 
     * @param message the error message
     * @param <T> the type of data
     * @return an error response
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .build();
    }
}
