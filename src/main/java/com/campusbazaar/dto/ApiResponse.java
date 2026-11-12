package com.campusbazaar.dto;

import com.campusbazaar.util.JsonUtil;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Standardized Unified API Response Envelope for all REST controllers.
 */
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T data;
    private final String timestamp;

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now().toString();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Operation successful", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public String getTimestamp() { return timestamp; }

    public String toJson() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", success);
        map.put("message", message);
        map.put("data", data);
        map.put("timestamp", timestamp);
        return JsonUtil.toJson(map);
    }
}
