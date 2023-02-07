package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.User;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.ActivityService;
import com.example.cube.Service.CatalogueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    private CatalogueRepository catalogueRepository;
    private ActivityService activityService;
    private UserRepository userRepository;

    public CatalogueServiceImpl(CatalogueRepository catalogueRepository, ActivityService activityService, UserRepository userRepository) {
        this.catalogueRepository = catalogueRepository;
        this.activityService = activityService;
        this.userRepository = userRepository;
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

    @Override
    public String setView(String email, long id, boolean view){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        ActivityDto activityDto = new ActivityDto();
        activityDto.setCatalogue(catalogue);
        activityDto.setUser(user);
        activityDto.setView(view);
        activityService.setCatalogueActivity(activityDto);

        return "resource view : " + view;
    }
    @Override
    public String setLike(String email, long id, boolean like){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        ActivityDto activityDto = new ActivityDto();
        activityDto.setCatalogue(catalogue);
        activityDto.setUser(user);
        activityDto.setFavorite(like);
        activityService.setCatalogueActivity(activityDto);

        return "resource liked : " + like;
    }

    @Override
    public String setShare(String email, long id, boolean share){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        ActivityDto activityDto = new ActivityDto();
        activityDto.setCatalogue(catalogue);
        activityDto.setUser(user);
        activityDto.setShare(share);
        activityService.setCatalogueActivity(activityDto);

        return "resource share : " + share;
    }

    @Override
    public String setBlocked(String email, long id, boolean blocked){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        ActivityDto activityDto = new ActivityDto();
        activityDto.setCatalogue(catalogue);
        activityDto.setUser(user);
        activityDto.setBlocked(blocked);
        activityService.setCatalogueActivity(activityDto);

        return "resource blocked : " + blocked;
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
