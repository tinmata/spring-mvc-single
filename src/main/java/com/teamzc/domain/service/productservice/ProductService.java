package com.teamzc.domain.service.productservice;

import java.util.List;
import com.teamzc.domain.model.Product;

public interface ProductService {

  List<Product> findAll();

  Product findById(String producId);

  void save(Product product);

  void deleteById(String productId);
}
