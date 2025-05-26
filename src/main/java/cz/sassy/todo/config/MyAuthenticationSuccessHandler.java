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

    /**
     * This method is called when authentication is successful.
     * It checks if the authenticated user has the "ROLE_ADMIN" authority and sets the default target URL accordingly.
     *
     * @param request        the HttpServletRequest object
     * @param response       the HttpServletResponse object
     * @param authentication the Authentication object containing user details
     * @throws ServletException if an error occurs during processing
     * @throws IOException      if an I/O error occurs
     */
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
