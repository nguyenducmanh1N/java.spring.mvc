package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import vn.hoidanit.laptopshop.domain.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.ServletContext;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class UserController {
    
    private final UserService userService;
    
    private final UploadService uploadService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, 
            UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String getHomePage(Model model){   
        List<User> arrUsers = this.userService.getAllUsersByEmail("@");
        System.out.println(arrUsers);  
        model.addAttribute("home", "test");
        model.addAttribute("hoidanit", "from controller with model");
        return "hello";
    
    }
// danh sach user
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "admin/user/show";
    }
    
    // lay thong tin chi tiet nguoi dung
    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model ,@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        return "admin/user/detail";
    }

    @GetMapping("/admin/user/create")// mac dinh la get
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";
    }
// tao moi user
    @PostMapping(value = "/admin/user/create")
    public String createUserPage(Model model, 
            @ModelAttribute("newUser") User user1,
            @RequestParam("hoidanitFile") MultipartFile file) {
        
        String avatar = this.uploadService.handleSavaeUploadFile(file,"avatar");
        String hashPassword = this.passwordEncoder.encode(user1.getPassword());

        user1.setAvatar(avatar);
        user1.setPassword(hashPassword);
        // save 
        user1.setRole(this.userService.getRoleByName(user1.getRole().getName()));
        this.userService.handleSaveUser(user1);
        return "redirect:/admin/user";
    }
    // trang update
    @RequestMapping("/admin/user/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser",currentUser);
        return "admin/user/update";
    }
    

    @PostMapping("/admin/user/update")//post
    public String postUpdateUser(Model model, @ModelAttribute("newUser") User user1) {
        User currentUser = this.userService.getUserById(user1.getId());// lay id tu phia view
        if(currentUser != null){
            currentUser.setAddress(user1.getAddress());
            currentUser.setFullName(user1.getFullName());
            currentUser.setPhone(user1.getPhone());

            this.userService.handleSaveUser(currentUser);
        }
        return "redirect:/admin/user";
    }
    // tai trang delete
    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        // User user = new User();
        // user.setId(id);
        // model.addAttribute("newUser", user);
        model.addAttribute("newUser", new User());
        return "admin/user/delete";
    }
    
    // tai trang delete
    @PostMapping("/admin/user/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newUser") User user1) {
        this.userService.deleteAUser(user1.getId());
        return "redirect:/admin/user";
    }


}

// @RestController
// public class UserController {

//     private UserService userService;

//     public UserController(UserService userService) {
//         this.userService = userService;
//     }

//     @GetMapping("/")
//     public String getHomePage() {
//         return this.userService.handleHello();
//     }
// }