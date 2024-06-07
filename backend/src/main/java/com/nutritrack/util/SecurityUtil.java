package com.nutritrack.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class SecurityUtil {

    public static Long getUserIdFromToken() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        String userIdString = (String) authentication.getToken().getClaims().get("sub");
        return Long.valueOf(userIdString);
    }
}
