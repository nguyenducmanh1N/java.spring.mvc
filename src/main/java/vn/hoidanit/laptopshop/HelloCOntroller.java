package vn.hoidanit.laptopshop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloCOntroller {
    @GetMapping("/")
    public String index(){
        return "Welcome to Laptop Shop";
    }
}
