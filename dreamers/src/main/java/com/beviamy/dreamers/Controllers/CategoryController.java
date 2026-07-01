    package com.beviamy.dreamers.Controllers;

    import com.beviamy.dreamers.APIResonse;
    import com.beviamy.dreamers.Repository.CategoryRepository;
    import com.beviamy.dreamers.exeption.AlreadyexistsExeptiom;
    import com.beviamy.dreamers.exeption.CategoryNotFoundExeption;
    import com.beviamy.dreamers.models.Category;
    import com.beviamy.dreamers.service.Category.ICategoryService;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    import static org.springframework.http.HttpStatus.*;

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("/api/v1/category")
    public class CategoryController {
        private final ICategoryService categoryService;

        @GetMapping("/all")
        public ResponseEntity<APIResonse> getAllCategories(){
            try {
                List<Category> categories = categoryService.findAll();
                return ResponseEntity.ok(new APIResonse("found",categories));

            }
            catch (Exception e){
                return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                        .body(new APIResonse("Error",INTERNAL_SERVER_ERROR));
            }
        }

        @PostMapping("/add")
        public ResponseEntity<APIResonse> addCategory(@RequestBody Category name){
            try {
                Category category = categoryService.addCategory(name);
                return ResponseEntity.ok(new APIResonse("added",category));
            }catch (AlreadyexistsExeptiom e){
                return ResponseEntity.status(CONFLICT)
                        .body(new APIResonse(e.getMessage(),CONFLICT));
            }
        }

        @GetMapping("/category/{id}")
        public ResponseEntity<APIResonse> getCategoryById(@PathVariable long id){
            try {
                Category category = categoryService.findById(id);
                return ResponseEntity.ok(new APIResonse("found",category));
            }
            catch (CategoryNotFoundExeption e){
                return ResponseEntity.status(NOT_FOUND).body(new APIResonse(e.getMessage(),NOT_FOUND));
            }
        }
        @GetMapping("/{name}")
        public ResponseEntity<APIResonse> getCategoryByName(@PathVariable("name") String name) {
            try {
                Category category = categoryService.findByName(name);
                if (category == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new APIResonse("Category not found: " + name, HttpStatus.NOT_FOUND.value()));
                }
                return ResponseEntity.ok(new APIResonse("found", category));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new APIResonse("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }
        }
        @DeleteMapping("/{id}")
        public ResponseEntity<APIResonse> deleteCategoryById(@PathVariable long id){
            try {
                categoryService.deleteCategory(id);
                return ResponseEntity.ok(new APIResonse("deleted",null));
            }
            catch (CategoryNotFoundExeption e){
                return ResponseEntity.status(NOT_FOUND).body(new APIResonse(e.getMessage(),NOT_FOUND));
            }
        }
        @PutMapping("/category/{id}/update")
        public ResponseEntity<APIResonse> updateCategory(@RequestParam long id, @RequestBody Category name){
            try {
                Category category = categoryService.updateCategory(name, id);
                return ResponseEntity.ok(new APIResonse("updated",category));
            }
            catch (CategoryNotFoundExeption e){
                return ResponseEntity.status(NOT_FOUND).body(new APIResonse(e.getMessage(),NOT_FOUND));
            }
        }

    }
