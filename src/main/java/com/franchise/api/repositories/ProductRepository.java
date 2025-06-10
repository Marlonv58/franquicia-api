package com.franchise.api.repositories;

import com.franchise.api.entities.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Flux<Product> findByBranchId(Long branchId);

    @Query("SELECT * FROM producto WHERE branch_id = :branchId ORDER BY stock DESC LIMIT 1")
    Mono<Product> findTopByBranchIdOrderByStockDesc(Long branchId);
}
