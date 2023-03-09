package com.example.cube.Repository;

import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Optional<Resource> getResourceById(Long id);
    Resource getResourceByIdOrderById(Long id);
    Set<Resource> findAllByCatalogueIdIn(Collection<Long> catalogue_id);

}
