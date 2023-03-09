package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.*;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Repository.*;
import com.example.cube.Service.ActivityService;
import com.example.cube.Service.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private ResourceRepository resourceRepository;

    private CatalogueRepository catalogueRepository;
    private ActivityService activityService;
    private UserRepository userRepository;
    private FriendRepository friendRepository;
    private ActivityRepository activityRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               CatalogueRepository catalogueRepository,
                               ActivityService activityService,
                               UserRepository userRepository,
                               FriendRepository friendRepository,
                               ActivityRepository activityRepository) {
          this.resourceRepository = resourceRepository;
          this.catalogueRepository = catalogueRepository;
          this.activityService = activityService;
          this.userRepository = userRepository;
          this.friendRepository = friendRepository;
          this.activityRepository = activityRepository;
    }

    @Override
    public ResourceDto createResource(ResourceDto resourceDto, long catalogueId, String email) {

        Resource resource = mapToEntity(resourceDto);

        Set<Catalogue> catalogue = new HashSet<>();
        Catalogue resourceCatalogue = catalogueRepository.findById(catalogueId).get();
        catalogue.add(resourceCatalogue);
        resource.setCatalogue(catalogue);
        Resource newResource = resourceRepository.save(resource);

        User user = userRepository.findUserByEmail(email);

        ActivityDto activityDto = new ActivityDto();
        activityDto.setResource(Optional.of(newResource));
        activityDto.setCatalogue(Optional.empty());
        activityDto.setUser(user);
        activityDto.setCreated(true);
        activityService.setResourceActivity(activityDto);

        return mapToDTO(newResource);
    }

    @Override
    public ResourceResponse getAllResources(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Resource> resource = resourceRepository.findAll(pageable);

        List<Resource> listOfResources = resource.getContent();

        List<ResourceDto> content= listOfResources.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setContent(content);
        resourceResponse.setPageNo(resource.getNumber());
        resourceResponse.setPageSize(resource.getSize());
        resourceResponse.setTotalElements(resource.getTotalElements());
        resourceResponse.setTotalPages(resource.getTotalPages());
        resourceResponse.setLast(resource.isLast());

        return resourceResponse;
    }

    @Override
    public ResourceDto getResourceById(long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id));
        return mapToDTO(resource);
    }

    @Override
    public List<ResourceDto> getResourceByUserId(long id) {
        User user = userRepository.findUserById(id);
        List<Resource> resources = resourceRepository.findAll();
        List<Activity> activitys = activityRepository.getActivitiesByUser(user);

        activitys.stream()
                .filter(Activity::isCreated)
                .toList();

        resources.stream()
                .filter(resource -> activitys.stream()
                        .filter(activity -> resource.getId() == activity.getResource().getId())
                        .isParallel()
                )
                .toList();

        return resources.stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<ResourceDto> getResourceByRelation(String email, String relation) {
        User user = userRepository.findUserByEmail(email);
        List<Friend> friends = friendRepository.getFriendsByUser(user);
        List<Resource> resources = resourceRepository.findAll();
        List<Activity> activitys = activityRepository.findAll();

        friends.stream()
                .filter(friend -> friend.getRelation().equals(relation))
                .filter(friend -> friend.getUser().equals(user))
                .toList();

        activitys.stream()
                .filter(Activity::isCreated)
                .filter(activity -> friends.stream()
                        .filter(friend -> activity.getUser().getId() == friend.getFriend().getId())
                        .isParallel()
                )
                .toList();

        resources.stream()
                .filter(resource -> activitys.stream()
                        .filter(activity -> resource.getId() == activity.getResource().getId())
                        .isParallel())
                .toList();

        return resources.stream().map(this::mapToDTO).toList();
    }

    @Override
    public ResourceDto updateResource(ResourceDto resourceDto, long id, long catalogueId) {

        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id));

        resource.setAccess(resourceDto.getAccess());
        resource.setValue(resourceDto.getValue());

        Set<Catalogue> catalogue = new HashSet<>();
        Catalogue resourceCatalogue = catalogueRepository.findById(catalogueId).get();
        catalogue.add(resourceCatalogue);
        resource.setCatalogue(catalogue);

        Resource updatedResource = resourceRepository.save(resource);
        return mapToDTO(updatedResource);
    }

    @Override
    public void deleteResourceById(long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id));
        resourceRepository.delete(resource);
    }

    @Override
    public List<ResourceDto> getResourcesByCategory(Long catalogueId) {

        Set<Resource> resources = resourceRepository.findAllByCatalogueIdIn(Collections.singleton(catalogueId));

        return resources.stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String setView(String email, long id, boolean view){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Activity activity = activityRepository.getActivityByResource(resource);
        if (Objects.isNull(activity)){
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setView(view);
            activityService.setResourceActivity(activityDto);
        } else {
            activity.setView(view);
            activityRepository.save(activity);
        }

        return "resource view : " + view;
    }
    @Override
    public String setLike(String email, long id, boolean like){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Activity activity = activityRepository.getActivityByResource(resource);
        if (Objects.isNull(activity)){
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setFavorite(like);
            activityService.setResourceActivity(activityDto);
        } else {
            activity.setFavorite(like);
            activityRepository.save(activity);
        }

        return "resource liked : " + like;
    }

    @Override
    public String setShare(String email, long id, boolean share){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Activity activity = activityRepository.getActivityByResource(resource);
        if (Objects.isNull(activity)){
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setShare(share);
            activityService.setResourceActivity(activityDto);
        } else {
            activity.setShare(share);
            activityRepository.save(activity);
        }

        return "resource share : " + share;
    }

    @Override
    public String setBlocked(String email, long id, boolean blocked){
        User user = userRepository.findUserByEmail(email);
        Optional<Resource> resource = Optional.ofNullable(resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource", "id", id)));

        Activity activity = activityRepository.getActivityByResource(resource);
        if (Objects.isNull(activity)){
            ActivityDto activityDto = new ActivityDto();
            activityDto.setResource(resource);
            activityDto.setCatalogue(Optional.empty());
            activityDto.setUser(user);
            activityDto.setBlocked(blocked);
            activityService.setResourceActivity(activityDto);
        } else {
            activity.setBlocked(blocked);
            activityRepository.save(activity);
        }

        return "resource blocked : " + blocked;
    }

    private ResourceDto mapToDTO(Resource resource){
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId());
        resourceDto.setAccess(resource.getAccess());
        resourceDto.setValue(resource.getValue());
        resourceDto.setCatalogue(resource.getCatalogue());
        return resourceDto;
    }

    private Resource mapToEntity(ResourceDto resourceDto){
        Resource resource = new Resource();
        resource.setId(resourceDto.getId());
        resource.setAccess(resourceDto.getAccess());
        resource.setValue(resourceDto.getValue());
        resource.setCatalogue(resourceDto.getCatalogue());
        return resource;
    }
}
