package cz.sassy.EshopB2B.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ContentController is a Spring MVC controller that handles requests for various pages in the application.
 * It provides methods to handle requests for the root, home, admin, user, and login pages.
 */
@SuppressWarnings("unused")
@Controller
public class ContentController {

    @GetMapping("/")
    public String rootHandler() {
        return "redirect:/home";
    }


    @GetMapping("/home")
    public String homeHandler() {
        return "home";
    }

    @GetMapping("/admin/home")
    public String adminHandler() {
        return "home_admin";
    }

    @GetMapping("/user/home")
    public String userHandler() {
        return "home_user";
    }

    @GetMapping("/login")
    public String loginHandler() {
        return "custom_login";
    }
}
