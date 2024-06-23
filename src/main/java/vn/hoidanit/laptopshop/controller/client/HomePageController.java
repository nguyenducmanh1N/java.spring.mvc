package vn.hoidanit.laptopshop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomePageController {
    
    @GetMapping("/")
    public String getHomepage() {
        return "client/homepage/show";
    }
    
}
