package com.franchise.api.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sucursal")
public class Branch {
    @Id
    private Long id;
    private String name;
    private Long franchiseId;
}
