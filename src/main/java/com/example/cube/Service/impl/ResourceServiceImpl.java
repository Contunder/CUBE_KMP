package com.example.cube.Service.impl;

import com.example.cube.Exception.ResourceNotFoundException;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.ResourceRepository;
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

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               CatalogueRepository catalogueRepository) {
          this.resourceRepository = resourceRepository;
          this.catalogueRepository = catalogueRepository;
    }

    @Override
    public ResourceDto createResource(ResourceDto resourceDto, long catalogueId) {

        Resource resource = mapToEntity(resourceDto);

        Set<Catalogue> catalogue = new HashSet<>();
        Catalogue resourceCatalogue = catalogueRepository.findById(catalogueId).get();
        catalogue.add(resourceCatalogue);
        resource.setCatalogue(catalogue);

        Resource newPost = resourceRepository.save(resource);

        ResourceDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public ResourceResponse getAllResources(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Resource> posts = resourceRepository.findAll(pageable);

        List<Resource> listOfPosts = posts.getContent();

        List<ResourceDto> content= listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setContent(content);
        resourceResponse.setPageNo(posts.getNumber());
        resourceResponse.setPageSize(posts.getSize());
        resourceResponse.setTotalElements(posts.getTotalElements());
        resourceResponse.setTotalPages(posts.getTotalPages());
        resourceResponse.setLast(posts.isLast());

        return resourceResponse;
    }

    @Override
    public ResourceDto getResourceById(long id) {
        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(resource);
    }

    @Override
    public ResourceDto updateResource(ResourceDto resourceDto, long id, long catalogueId) {

        Resource resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

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
        Resource post = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        resourceRepository.delete(post);
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
