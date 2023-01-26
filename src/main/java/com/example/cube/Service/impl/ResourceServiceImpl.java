package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import com.example.cube.Payload.ActivityDto;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.ResourceRepository;
import com.example.cube.Repository.UserRepository;
import com.example.cube.Service.ActivityService;
import com.example.cube.Service.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private ResourceRepository resourceRepository;

    private CatalogueRepository catalogueRepository;
    private ActivityService activityService;
    private UserRepository userRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               CatalogueRepository catalogueRepository,
                               ActivityService activityService,
                               UserRepository userRepository) {
          this.resourceRepository = resourceRepository;
          this.catalogueRepository = catalogueRepository;
          this.activityService = activityService;
          this.userRepository = userRepository;
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
        activityDto.setResource(newResource);
        activityDto.setUser(user);
        activityDto.setCreated(true);
        activityService.setActivity(activityDto);

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

        Optional<Resource> resources = resourceRepository.getResourceById(catalogueId);

        return resources.stream().map((resource) -> mapToDTO(resource))
                .collect(Collectors.toList());
    }

    private ResourceDto mapToDTO(Resource resource){
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId(resource.getId());
        resourceDto.setAccess(resource.getAccess());
        resourceDto.setValue(resource.getValue());
        return resourceDto;
    }

    private Resource mapToEntity(ResourceDto resourceDto){
        Resource resource = new Resource();
        resource.setId(resourceDto.getId());
        resource.setAccess(resourceDto.getAccess());
        resource.setValue(resourceDto.getValue());
        return resource;
    }
}
