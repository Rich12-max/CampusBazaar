package com.campusbazaar.util;

import java.util.*;

/**
 * Lightweight Zero-Dependency JSON Serializer and Parser for Core Java.
 * Converts Java objects, maps, and collections into valid JSON strings
 * and extracts simple string and numeric keys from JSON payloads.
 */
public class JsonUtil {

    public static String escape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        String t = "000" + Integer.toHexString(c);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static String toJson(Map<String, Object> map) {
        if (map == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first) sb.append(",");
            sb.append("\"").append(escape(entry.getKey())).append("\":");
            sb.append(valueToJson(entry.getValue()));
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    public static String listToJson(List<?> list) {
        if (list == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : list) {
            if (!first) sb.append(",");
            sb.append(valueToJson(item));
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

    public static String valueToJson(Object val) {
        if (val == null) return "null";
        if (val instanceof String) return "\"" + escape((String) val) + "\"";
        if (val instanceof Number || val instanceof Boolean) return val.toString();
        if (val instanceof Map) return toJson((Map<String, Object>) val);
        if (val instanceof List) return listToJson((List<?>) val);
        return "\"" + escape(val.toString()) + "\"";
    }

    public static String getString(String json, String key, String defaultVal) {
        if (json == null) return defaultVal;
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return defaultVal;
        int colon = json.indexOf(":", idx + pattern.length());
        if (colon == -1) return defaultVal;
        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;
        if (start < json.length() && json.charAt(start) == '"') {
            int end = json.indexOf("\"", start + 1);
            if (end != -1) {
                return json.substring(start + 1, end);
            }
        }
        return defaultVal;
    }

    public static double getDouble(String json, String key, double defaultVal) {
        if (json == null) return defaultVal;
        String pattern = "\"" + key + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return defaultVal;
        int colon = json.indexOf(":", idx + pattern.length());
        if (colon == -1) return defaultVal;
        int start = colon + 1;
        while (start < json.length() && (Character.isWhitespace(json.charAt(start)) || json.charAt(start) == '"')) start++;
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) end++;
        if (start < end) {
            try {
                return Double.parseDouble(json.substring(start, end));
            } catch (NumberFormatException ignored) {}
        }
        return defaultVal;
    }

    public static int getInt(String json, String key, int defaultVal) {
        return (int) getDouble(json, key, defaultVal);
    }
}
