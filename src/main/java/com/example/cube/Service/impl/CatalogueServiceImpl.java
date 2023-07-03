package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Activity;
import com.example.cube.Model.Analytics;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.User;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Repository.ActivityRepository;
import com.example.cube.Repository.AnalyticsRepository;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.ActivityService;
import com.example.cube.Service.CatalogueService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    private CatalogueRepository catalogueRepository;
    private ActivityService activityService;
    private UserRepository userRepository;
    private AnalyticsRepository analyticsRepository;
    private ActivityRepository activityRepository;

    public CatalogueServiceImpl(CatalogueRepository catalogueRepository,
                                ActivityService activityService,
                                ActivityRepository activityRepository,
                                AnalyticsRepository analyticsRepository,
                                UserRepository userRepository) {
        this.catalogueRepository = catalogueRepository;
        this.activityService = activityService;
        this.activityRepository = activityRepository;
        this.analyticsRepository = analyticsRepository;
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

        Optional<Activity> optionalActivity = activityRepository.getActivityByCatalogue(catalogue);
        if (optionalActivity.isPresent()){

            optionalActivity.ifPresent(
                    activity -> activity.setView(view)
            );
            optionalActivity.ifPresent(
                    activity -> activityRepository.save(activity)
            );
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(Optional.empty());
            activityDto.setCatalogue(catalogue);
            activityDto.setUser(user);
            activityDto.setView(view);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setView(analytics.getView() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setView(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource view : " + view;
    }

    @Override
    public String setLike(String email, long id, boolean like){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByCatalogue(catalogue);
        if (optionalActivity.isPresent()){
            optionalActivity.ifPresent(activity -> activity.setFavorite(like));
            optionalActivity.ifPresent(activity -> activityRepository.save(activity));
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(Optional.empty());
            activityDto.setCatalogue(catalogue);
            activityDto.setUser(user);
            activityDto.setFavorite(like);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setFavorite(analytics.getFavorite() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setFavorite(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource liked : " + like;
    }

    @Override
    public String setShare(String email, long id, boolean share){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByCatalogue(catalogue);
        if (optionalActivity.isPresent()){

            optionalActivity.ifPresent(
                    activity -> activity.setShare(share)
            );
            optionalActivity.ifPresent(
                    activity -> activityRepository.save(activity)
            );
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(Optional.empty());
            activityDto.setCatalogue(catalogue);
            activityDto.setUser(user);
            activityDto.setShare(share);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setShare(analytics.getShare() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setShare(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

        return "resource share : " + share;
    }

    @Override
    public String setBlocked(String email, long id, boolean blocked){
        User user = userRepository.findUserByEmail(email);
        Optional<Catalogue> catalogue = Optional.ofNullable(catalogueRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Catalogue", "id", id)));

        Optional<Activity> optionalActivity = activityRepository.getActivityByCatalogue(catalogue);
        if (optionalActivity.isPresent()){

            optionalActivity.ifPresent(
                    activity -> activity.setBlocked(blocked)
            );
            optionalActivity.ifPresent(
                    activity -> activityRepository.save(activity)
            );
        } else {
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(Optional.empty());
            activityDto.setCatalogue(catalogue);
            activityDto.setUser(user);
            activityDto.setBlocked(blocked);
            activityService.setCatalogueActivity(activityDto);
        }

        Optional<Analytics> optionalAnalytics = analyticsRepository.getAnalyticsByDate(getSQLDate());
        if(optionalAnalytics.isPresent()){
            optionalAnalytics.ifPresent(analytics -> analytics.setBlocked(analytics.getBlocked() + 1));
            optionalAnalytics.ifPresent(analytics -> analyticsRepository.save(analytics));
        } else {
            Analytics analytics = new Analytics();
            analytics.setBlocked(1);
            analytics.setDate(getSQLDate());
            analyticsRepository.save(analytics);
        }

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

    private Date getSQLDate(){
        java.util.Date todayJava = new java.util.Date();
        return new Date(todayJava.getDate());
    }
}
