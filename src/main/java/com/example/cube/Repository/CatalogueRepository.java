package com.example.cube.Repository;

import com.example.cube.Model.Catalogue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {

    Catalogue getCatalogueById(long id);
    Set<Catalogue> getCatalogueByIdIs(long id);
}
