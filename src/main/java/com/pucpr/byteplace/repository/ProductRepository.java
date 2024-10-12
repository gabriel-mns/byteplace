package com.pucpr.byteplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.pucpr.byteplace.model.Product;

@RepositoryRestResource(collectionResourceRel = "products", path = "products") 
public interface ProductRepository extends JpaRepository<Product, Long> {
}
