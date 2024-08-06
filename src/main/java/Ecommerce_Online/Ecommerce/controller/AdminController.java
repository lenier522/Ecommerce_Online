package Ecommerce_Online.Ecommerce.controller;

import Ecommerce_Online.Ecommerce.model.Category;
import Ecommerce_Online.Ecommerce.model.Product;
import Ecommerce_Online.Ecommerce.model.ProductOrder;
import Ecommerce_Online.Ecommerce.model.UserDtls;
import Ecommerce_Online.Ecommerce.service.CartService;
import Ecommerce_Online.Ecommerce.service.CategoryService;
import Ecommerce_Online.Ecommerce.service.OrderService;
import Ecommerce_Online.Ecommerce.service.ProductService;
import Ecommerce_Online.Ecommerce.service.UserService;
import Ecommerce_Online.Ecommerce.util.CommonUtil;
import Ecommerce_Online.Ecommerce.util.OrderStatus;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommonUtil commonUtil;

    @ModelAttribute
    public void getUserDetails(Principal p, Model m) {

        if (p != null) {
            String email = p.getName();
            UserDtls userDtls = userService.getUserByEmail(email);
            m.addAttribute("user", userDtls);
            Integer countCart = cartService.getCountCart(userDtls.getId());
            m.addAttribute("countCart", countCart);
        }
        List<Category> allActiveCategory = categoryService.getAllActiveCategory();
        m.addAttribute("categorys", allActiveCategory);
    }

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/loadAddProduct")
    public String loadAddProduct(Model m) {
        List<Category> categories = categoryService.getAllCategory();
        m.addAttribute("categories", categories);
        return "admin/add_product";
    }

    @GetMapping("/categoria")
    public String categoria(Model m, @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
        // m.addAttribute("categorys", categoryService.getAllCategory());
        Page<Category> page = categoryService.getAllCategorPagination(pageNo, pageSize);
        List<Category> categorys = page.getContent();
        m.addAttribute("categorys", categorys);

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "admin/category";
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
        category.setImgName(imageName);

        Boolean existCategory = categoryService.existCategory(category.getName());
        if (existCategory) {
            session.setAttribute("errorMsg", "Categoria ya existe");
        } else {
            Category saveCategory = categoryService.saveCategory(category);
            if (ObjectUtils.isEmpty(saveCategory)) {
                session.setAttribute("errorMsg", "No guardado Error Interno del Servidor");
            } else {

                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                session.setAttribute("succMsg", "Guardado Correctamente");
            }
        }
        return "redirect:admin/categoria";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable int id, HttpSession session) {

        Boolean deleteCategory = categoryService.deleteCategory(id);
        if (deleteCategory) {
            session.setAttribute("succMsg", "Categoria Eliminada Correctamente");
        } else {
            session.setAttribute("errorMsg", "Problema con el Servidor");
        }

        return "redirect:/admin/categoria";
    }

    @GetMapping("/loadEditCategory/{id}")
    public String loadEditCategory(@PathVariable int id, Model m) {
        m.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/edit_category";
    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
            HttpSession session) throws IOException {

        Category oldcategory = categoryService.getCategoryById(category.getId());
        String imgName = file.isEmpty() ? oldcategory.getImgName() : file.getOriginalFilename();

        if (!ObjectUtils.isEmpty(category)) {

            oldcategory.setName(category.getName());
            oldcategory.setIsActive(category.getIsActive());
            oldcategory.setImgName(imgName);
        }
        Category updatecategory = categoryService.saveCategory(oldcategory);

        if (!ObjectUtils.isEmpty(updatecategory)) {

            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("succMsg", "Categoria Actualizada");
        } else {
            session.setAttribute("errorMsg", "Problema con el Servidor");
        }
        return "redirect:admin/loadEditCategory/" + category.getId();
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
            HttpSession session) throws IOException {

        String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();
        product.setImage(imageName);
        product.setDiscount(0);
        product.setDiscountPrice(product.getPrice());

        Product saveProduct = productService.saveProduct(product);

        if (!ObjectUtils.isEmpty(saveProduct)) {
            // Ruta externa donde se guardarán las imágenes
            String uploadDir = "uploads/img/product";
            File uploadDirFile = new File(uploadDir);

            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs(); // Crear directorio si no existe
            }

            // Guardar la imagen en la ruta especificada
            Path path = Paths.get(uploadDir + File.separator + image.getOriginalFilename());

            System.out.println(path);

            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            session.setAttribute("succMsg", "Producto Guardado Correctamente");
        } else {
            session.setAttribute("errorMsg", "Problema con el Servidor");
        }

        return "redirect:/admin/loadAddProduct";
    }

    @GetMapping("/products")
    public String loadViewProduct(Model m, @RequestParam(defaultValue = "") String ch,
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {

//        List<Product> products = null;
//        if (ch != null && ch.length() > 0) {
//            products = productService.searchProduct(ch);
//        } else {
//            products = productService.getAllProducts();
//        }
//        m.addAttribute("products", products);
        Page<Product> page = null;
        if (ch != null && ch.length() > 0) {
            page = productService.searchProductPagination(pageNo, pageSize, ch);
        } else {
            page = productService.getAllProductsPagination(pageNo, pageSize);
        }
        m.addAttribute("products", page.getContent());

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());
        return "admin/products";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        Boolean deleteProduct = productService.deleteProduct(id);
        if (deleteProduct) {
            session.setAttribute("succMsg", "Producto Eliminado Correctamente");
        } else {
            session.setAttribute("errorMsg", "Problema con el Servidor");
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(@PathVariable int id, Model m) {
        m.addAttribute("product", productService.getProductById(id));
        m.addAttribute("categories", categoryService.getAllCategory());
        return "admin/edit_product";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
            HttpSession session, Model m) {

        if (product.getDiscount() < 0 || product.getDiscount() > 100) {
            session.setAttribute("errorMsg", "Descuento Invalido");
        } else {
            Product updateProduct = productService.updateProduct(product, image);

            if (!ObjectUtils.isEmpty(updateProduct)) {
                session.setAttribute("succMsg", "Producto Actualizado Correctamente");
            } else {
                session.setAttribute("errorMsg", "Problema con el Servidor");
            }
        }

        return "redirect:admin/editProduct/" + product.getId();
    }

    @GetMapping("/users")
    public String getAllUsers(Model m, @RequestParam Integer type) {

        List<UserDtls> users = null;

        if (type == 1) {
            users = userService.getUsers("ROLE_USER");
        } else {
            users = userService.getUsers("ROLE_ADMIN");
        }

        m.addAttribute("users", users);
        m.addAttribute("userType", type);
        return "admin/users";
    }

    @GetMapping("/updateSts")
    public String updateUserAccountStatus(@RequestParam Boolean status, @RequestParam Integer type,
            @RequestParam Integer id,
            HttpSession session) {

        Boolean f = userService.updateAccountStatus(id, status);
        if (f) {
            session.setAttribute("succMsg", "Estado actualizado");
        } else {
            session.setAttribute("errorMsg", "Error interno del Servidor");
        }

        return "redirect:admin/users?type=" + type;
    }

    @GetMapping("/orders")
    public String getAllOrders(Model m, @RequestParam(name = "pageNo",
            defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
//        List<ProductOrder> allOrders = orderService.getAllOrders();
//        m.addAttribute("orders", allOrders);
//        m.addAttribute("srch",false);

        if (pageNo < 0) {
            pageNo = 0;
        }

        Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
        m.addAttribute("orders", page.getContent());
        m.addAttribute("srch", false);

        m.addAttribute("pageNo", page.getNumber());
        m.addAttribute("pageSize", pageSize);
        m.addAttribute("totalElements", page.getTotalElements());
        m.addAttribute("totalPages", page.getTotalPages());
        m.addAttribute("isFirst", page.isFirst());
        m.addAttribute("isLast", page.isLast());

        return "admin/orders";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st, HttpSession session) {

        OrderStatus[] values = OrderStatus.values();
        String status = null;

        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                status = orderSt.getName();
            }
        }

        ProductOrder updateOrder = orderService.updateOrderStatus(id, status);
        try {
            commonUtil.sendEmailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!ObjectUtils.isEmpty(updateOrder)) {
            session.setAttribute("succMsg", "Estado Actualizado");
        } else {
            session.setAttribute("errorMsg", "No se pudo Actualizar");
        }
        return "redirect:admin/orders";
    }

    @GetMapping("/search-order")
    public String searchProduct(@RequestParam String orderId, Model m, HttpSession session,
            @RequestParam(name = "pageNo",
                    defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {

        if (orderId != null && orderId.length() > 0) {

            ProductOrder order = orderService.getOrdersByOrderId(orderId.trim());

            if (ObjectUtils.isEmpty(order)) {
                session.setAttribute("errorMsg", "Orden Incorrecta");
                m.addAttribute("orderDtls", null);
            } else {
                m.addAttribute("orderDtls", order);
            }

            m.addAttribute("srch", true);
        } else {
//            List<ProductOrder> allOrders = orderService.getAllOrders();
//            m.addAttribute("orders", allOrders);
//            m.addAttribute("srch", false);

            Page<ProductOrder> page = orderService.getAllOrdersPagination(pageNo, pageSize);
            m.addAttribute("orders", page);
            m.addAttribute("srch", false);

            m.addAttribute("pageNo", page.getNumber());
            m.addAttribute("pageSize", pageSize);
            m.addAttribute("totalElements", page.getTotalElements());
            m.addAttribute("totalPages", page.getTotalPages());
            m.addAttribute("isFirst", page.isFirst());
            m.addAttribute("isLast", page.isLast());
        }
        return "admin/orders";

    }

    @GetMapping("/add-admin")
    public String loadAdminAdd() {

        return "admin/add_admin";
    }

    @PostMapping("save-admin")
    public String saveAdmin(@ModelAttribute UserDtls user, @RequestParam("img") MultipartFile file, HttpSession session) throws IOException {

        String imageName = file.isEmpty() ? "default.jpg" : file.getOriginalFilename();
        user.setProfileImage(imageName);
        UserDtls saveAdmin = userService.saveAdmin(user);

        if (!ObjectUtils.isEmpty(saveAdmin)) {
            if (!file.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "profile" + File.separator
                        + file.getOriginalFilename());

                System.out.println(path);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                session.setAttribute("succMsg", "Guardado Correctamente");
            }
        } else {
            session.setAttribute("errorMsg", "No guardado Error Interno del Servidor");
        }
        return "redirect:admin/add-admin";
    }

}
