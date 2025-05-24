package cz.sassy.todo.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * MyAuthenticationSuccessHandler is a custom authentication success handler that extends
 * SavedRequestAwareAuthenticationSuccessHandler. It determines the target URL after successful authentication
 * based on the user's roles.
 */

public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            setDefaultTargetUrl("/private?success");
        } else {
            setDefaultTargetUrl("/private?success");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
