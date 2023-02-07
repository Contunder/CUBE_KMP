package com.example.cube.Service;

import com.example.cube.Payload.CatalogueDto;

import java.util.List;

public interface CatalogueService {
    CatalogueDto addCatalogue(CatalogueDto catalogueDto);

    CatalogueDto getCatalogue(Long catalogueId);

    List<CatalogueDto> getAllCatalogues();

    CatalogueDto updateCatalogue(CatalogueDto catalogueDto, Long catalogueId);

    void deleteCatalogue(Long catalogueId);

    String setView(String email, long id, boolean view);

    String setLike(String email, long id, boolean like);

    String setShare(String email, long id, boolean share);

    String setBlocked(String email, long id, boolean blocked);
}
