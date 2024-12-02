package com.product;

import com.product.dto.ProductDto;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import com.product.service.ProductService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Path("/api/products")
@ApplicationScoped
public class ProductResource {

    private final ProductService productService;

    private final ProductMapper productMapper;

    public ProductResource(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * getProducts - Fetch list of products
     * @return all the list of products
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProducts() {
        return productService.getAllProducts();
    }

    /**
     * addProduct - Add new product
     * @param productRequest new product to be created
     * @return newly created product
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addProduct(@Valid ProductDto productRequest) {
        Product product = productMapper.productDtoToEntity(productRequest);
        return productService.createProduct(product);
    }

    /**
     * getProductById - Fetch product based on the provided id
     * @param id uuid
     * @return product data with given id
     */
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProductById(UUID id) {
        return productService.getProductById(id)
                .onItem().transform(product -> Response.ok(product).build());
    }

    /**
     * deleteProduct - Delete the product based on the given id
     * @param id uuid
     * @return status
     */
    @Path("/{id}")
    @DELETE
    public Uni<Response> deleteProduct(UUID id) {
        return productService.deleteProductById(id);
    }

    /**
     * updateProduct - Update product description based on the given id
     * @param id uuid
     * @param description data to be updated
     * @return updated product
     */
    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> updateProduct(UUID id, @QueryParam("description") String description) {
        return productService.updateProduct(id, description);
    }
}
