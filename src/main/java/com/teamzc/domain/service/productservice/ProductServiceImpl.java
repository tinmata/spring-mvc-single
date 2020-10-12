package com.teamzc.domain.service.productservice;

import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.teamzc.domain.model.Product;
import com.teamzc.domain.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  @Inject private ProductRepository productRepository;

  @Override
  public List<Product> findAll() {
    logger.debug("すべての商品を取得します。");
    return productRepository.findAll();
  }

  @Override
  public Product findById(String producId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void save(Product product) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(String productId) {
    // TODO Auto-generated method stub

  }
}
