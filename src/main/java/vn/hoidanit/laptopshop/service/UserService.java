package vn.hoidanit.laptopshop.service;


import java.util.List;



import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Role;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.RoleRepository;
import vn.hoidanit.laptopshop.repository.UserRepositoty;

@Service
public class UserService {
    private final UserRepositoty userRepositoty;
    private final RoleRepository roleRepository;

    public UserService(UserRepositoty userRepositoty, RoleRepository roleRepository) {
        this.userRepositoty = userRepositoty;
        this.roleRepository = roleRepository;
        
    }

    public List<User> getAllUsers() {
        return this.userRepositoty.findAll();
    }
    public User getUserById(long id){
        return this.userRepositoty.findById(id);
    }

    public List<User> getAllUsersByEmail(String email) {
        return this.userRepositoty.findOneByEmail(email);
    }
    
    public User handleSaveUser(User user){
        User user2 =this.userRepositoty.save(user);
        System.out.println(user2);
        return user2;
    }

    public void deleteAUser(long id){
        this.userRepositoty.deleteById(id);
    }
    public Role getRoleByName(String name){
        return this.roleRepository.findByName(name);
    }

}
