package com.campusretail.productcatalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campusretail.productcatalogservice.entity.Product;

import java.util.List;

/**
 * Repository interface for the Product entity
 * extending from JPARepository to have all the
 * default methods and creating a new method to
 * search by name
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductName(String name);
}
