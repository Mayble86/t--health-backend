package com.olegf.spingapp.thealthbackend.domain.repository;

import com.olegf.spingapp.thealthbackend.domain.entity.Dashboard;
import org.springframework.data.repository.CrudRepository;

public interface DashboardRepository extends CrudRepository<Dashboard, Long> {
    Dashboard getByAction(String name);
}
