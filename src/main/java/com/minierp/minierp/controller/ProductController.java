package com.minierp.minierp.controller;

import com.minierp.minierp.entity.Product;
import com.minierp.minierp.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    private static final String UPLOAD_DIR = "uploads/products";

    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        product.setProductNo(id);
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productRepository.deleteById(id);
    }

    // ── 이미지 업로드 ──
    @PostMapping("/{id}/image")
    public Map<String, String> uploadImage(@PathVariable String id,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String ext = "";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String filename = id + "_" + UUID.randomUUID() + ext;
        Path target = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String imageUrl = "/uploads/products/" + filename;

        productRepository.findById(id).ifPresent(p -> {
            p.setImageUrl(imageUrl);
            productRepository.save(p);
        });

        return Map.of("imageUrl", imageUrl);
    }
}