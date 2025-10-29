package com.olegf.spingapp.thealthbackend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {
    @Id
    private Long id;
    private int steps;
    private float activity;
    private int calories;
    private String goals;
}
