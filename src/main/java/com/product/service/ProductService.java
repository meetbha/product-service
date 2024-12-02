package com.product.service;

import com.product.dto.ProductDto;
import com.product.exception.DuplicateDataFoundException;
import com.product.exception.ProductNotFoundException;
import com.product.model.Product;
import com.product.util.Constants;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.UUID;

import static com.product.util.Constants.*;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.jboss.resteasy.reactive.RestResponse.Status.NOT_FOUND;
import static org.jboss.resteasy.reactive.RestResponse.Status.NO_CONTENT;


@Slf4j
@ApplicationScoped
public class ProductService {

    public Uni<Response> getAllProducts() {
        return Product.listAll().invoke(products -> log.info("Fetched {} products", products.size()))
                .onItem().transform(value -> Response.status(OK).entity(value).build());
    }

    public Uni<Response> createProduct(Product product) {
        return Product.<Product>find(Constants.NAME, product.getName()).firstResult()
                .onItem().ifNotNull().failWith(() -> new DuplicateDataFoundException(PRODUCT_EXISTS + product.getName()))
                .onItem().transformToUni(prod -> {
                    if (prod == null) {
                        return product.persistAndFlush().invoke(newProd -> log.info("product added {}", newProd)).onItem().transform(item -> Response.status(CREATED).entity(item).build());
                    }
                    return Uni.createFrom().item(Response.status(Response.Status.CONFLICT).build());
                });
    }

    public Uni<Product> getProductById(UUID id) {
        return Product.<Product>findById(id)
                .onItem().ifNull().failWith(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND + id)).invoke(product -> log.info("Product fetched with id {}", id));
    }

    public Uni<Response> deleteProductById(UUID id) {
        return Panache.withTransaction(() -> Product.deleteById(id)).invoke(value -> log.info("product id deleted {}",value))
                .onItem().transform(deleted -> deleted ? Response.ok().status(NO_CONTENT).build() : Response.ok().status(NOT_FOUND).build());
    }

    public Uni<Response> updateProduct(UUID id, String description) {
        return getProductById(id)
                .onItem().ifNotNull().transformToUni(product -> {
                    product.setDescription(description);
                    product.setCreatedAt(new Date());
                    return product.persistAndFlush().onItem().transform(item -> Response.status(OK).entity(product).build());
                }).invoke(() -> log.info("Product description updated {}", description));
    }

}
