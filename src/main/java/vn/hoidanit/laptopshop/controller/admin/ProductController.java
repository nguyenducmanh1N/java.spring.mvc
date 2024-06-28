package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;



@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController (ProductService productService, UploadService uploadService){
        this.productService = productService;
        this.uploadService = uploadService;
    }


    @GetMapping("/admin/product")
    public String getProduct(Model model){
        List<Product> prs = this.productService.fetchProducts();
        model.addAttribute("products", prs);
        return "admin/product/show";
    }
    
    @GetMapping("/admin/product/create") // mac dinh la get
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }
    
    
    // tao moi user
    @PostMapping(value = "/admin/product/create")
    public String handleCreateProductPage(
            @ModelAttribute("newProduct") @Valid Product pr,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {
                
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        String image = this.uploadService.handleSaveUploadFile(file, "product");
        
        pr.setImage(image);
        
        this.productService.createProduct(pr);
        return "redirect:/admin/product";
    }

    // trang update
    @RequestMapping("/admin/product/update/{id}")
    public String getUpdateProductPage(Model model, @PathVariable Long id) {
        Optional<Product> currentProduct = this.productService.fetchProductById(id);
        model.addAttribute("newProduct", currentProduct.get());
        return "admin/product/update";
    }
    
    @PostMapping("/admin/product/update") // post
    public String handleUpdateProduct( @ModelAttribute("newProduct") @Valid Product pr,
        BindingResult newProductBindingResult,
        @RequestParam("hoidanitFile") MultipartFile file) {

            // validate
        if (newProductBindingResult.hasErrors()) {
                return "admin/product/update";
        }

        Product currentProduct = this.productService.fetchProductById(pr.getId()).get() ;// lay id tu phia view
        if (currentProduct != null) {
            // update new image 
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProduct.setImage(img);
                
            }
            currentProduct.setName(pr.getName());
            currentProduct.setPrice(pr.getPrice());
            currentProduct.setDetailDesc(pr.getDetailDesc());
            currentProduct.setShortDesc(pr.getShortDesc());
            currentProduct.setQuantity(pr.getQuantity());
            currentProduct.setFactory(pr.getFactory());
            currentProduct.setTarget(pr.getTarget());
            

            this.productService.createProduct(currentProduct);
        }
        return "redirect:/admin/product";
    }
    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable Long id) {
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }
   
    @PostMapping("/admin/product/delete")
    public String postDeleteUser(Model model, @ModelAttribute("newProduct") Product pr) {
        this.productService.deleteAProduct(pr.getId());
        return "redirect:/admin/product";
    }
    // lay thong tin chi tiet nguoi dung
    @RequestMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model ,@PathVariable Long id) {
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }
}
