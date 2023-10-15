package com.api.fitnescenter.repository;

import com.api.fitnescenter.entity.ListOfServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessServicesRepository extends JpaRepository<ListOfServices, Long> {
}
