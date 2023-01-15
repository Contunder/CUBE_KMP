package com.example.cube.Repository;

import com.example.cube.Model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> getResourceById(Long id);

}
