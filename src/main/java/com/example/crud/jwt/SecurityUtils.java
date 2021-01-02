package com.example.crud.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    created by HuyenNgTn on 09/12/2020
*/
public final class SecurityUtils {
    public static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        return springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        return (String) authentication.getPrincipal();
                    }
                    return null;
                });
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.addAll(authentication.getAuthorities());
                    return authorities.stream()
                            .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(null));
                })
                .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .map(authentication -> {
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.addAll(authentication.getAuthorities());
                    return authorities.stream()
                            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
                })
                .orElse(false);
    }

    public static List<String> getCurrentRoles() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.addAll(securityContext.getAuthentication().getAuthorities());
        return authorities.stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
    }
}
