package com.product.model;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "Products")
@Table(name = "Products")
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

}
