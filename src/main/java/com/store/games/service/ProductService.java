package com.store.games.service;

import com.store.games.model.Product;
import com.store.games.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product liker(Long id) {
        Product product = this.findProductById(id);
        product.setLikes(product.getLikes() + 1);
        return productRepository.save(product);
    }

    public Product disliker (Long id){
        Product product = this.findProductById(id);
        if (product.getLikes()>0){
            product.setLikes(product.getLikes() - 1);
        } else{
            product.setLikes(0);
        }
        return productRepository.save(product);
    }

    private Product findProductById(Long id){
        Product product = productRepository.findById(id).orElse(null);
        if (product==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrada!", null);
        }
        return product;
    }

}
