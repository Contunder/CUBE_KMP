package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Catalogue;
import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Service.CatalogueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    private CatalogueRepository catalogueRepository;

    public CatalogueServiceImpl(CatalogueRepository catalogueRepository) {
        this.catalogueRepository = catalogueRepository;
    }

    @Override
    public CatalogueDto addCatalogue(CatalogueDto categoryDto) {
        Catalogue catalogue = mapToEntity(categoryDto);
        Catalogue savedCatalogue = catalogueRepository.save(catalogue);
        return mapToDTO(savedCatalogue);
    }

    @Override
    public CatalogueDto getCatalogue(Long catalogueId) {

        Catalogue catalogue = catalogueRepository.findById(catalogueId)
                .orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", catalogueId));

        return mapToDTO(catalogue);
    }

    @Override
    public List<CatalogueDto> getAllCatalogues() {

        List<Catalogue> catalogues = catalogueRepository.findAll();

        return catalogues.stream().map((category) -> mapToDTO(category))
                .collect(Collectors.toList());
    }

    @Override
    public CatalogueDto updateCatalogue(CatalogueDto catalogueDto, Long catalogueId) {

        Catalogue catalogue = catalogueRepository.findById(catalogueId)
                .orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", catalogueId));

        catalogue.setCategory(catalogueDto.getCategory());
        catalogue.setId(catalogueId);

        Catalogue updatedCatalogue = catalogueRepository.save(catalogue);

        return mapToDTO(updatedCatalogue);
    }

    @Override
    public void deleteCatalogue(Long catalogueId) {

        Catalogue category = catalogueRepository.findById(catalogueId)
                .orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", catalogueId));

        catalogueRepository.delete(category);
    }

    private CatalogueDto mapToDTO(Catalogue catalogue){
        CatalogueDto catalogueDto = new CatalogueDto();
        catalogueDto.setId(catalogue.getId());
        catalogueDto.setCategory(catalogue.getCategory());
        return catalogueDto;
    }

    private Catalogue mapToEntity(CatalogueDto catalogueDto){
        Catalogue catalogue = new Catalogue();
        catalogue.setCategory(catalogueDto.getCategory());
        return catalogue;
    }
}
