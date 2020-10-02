package com.teamzc.domain.repository;

import java.util.List;
import com.teamzc.domain.model.Product;

public interface ProductRepository {

  List<Product> findAll();

  Product findById(String productId);
}
