package com.example.cube.Controller;

import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Service.ResourceService;
import com.example.cube.Utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@Configuration
@EnableWebMvc
@RestController
@RequestMapping()
public class ResourceController {

    private ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping("/api/resources/add/{catalogueId}")
    public ResponseEntity<ResourceDto> createResource(@Valid @RequestBody ResourceDto resourceDto, @PathVariable(name = "catalogueId") long catalogueId){
        return new ResponseEntity<>(resourceService.createResource(resourceDto, catalogueId), HttpStatus.CREATED);
    }

    @GetMapping("/api/resources")
    public ResourceResponse getAllResources(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return resourceService.getAllResources(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping(value = "/api/resources/{id}")
    public ResponseEntity<ResourceDto> getResourceById(@PathVariable(name = "id") long id){
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/resources/{id}/{catalogueId}")
    public ResponseEntity<ResourceDto> updateResource(@Valid @RequestBody ResourceDto resourceDto, @PathVariable(name = "id") long id, @PathVariable(name = "catalogueId") long catalogueId){

       ResourceDto ResourceResponse = resourceService.updateResource(resourceDto, id, catalogueId);

       return new ResponseEntity<>(ResourceResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/resources/{id}")
    public ResponseEntity<String> deleteResource(@PathVariable(name = "id") long id){

        resourceService.deleteResourceById(id);

        return new ResponseEntity<>("Resource entity deleted successfully.", HttpStatus.OK);
    }

    @GetMapping("/api/resources/category/{id}")
    public ResponseEntity<List<ResourceDto>> getResourcesByCategory(@PathVariable("id") Long categoryId){
        List<ResourceDto> ResourceDtos = resourceService.getResourcesByCategory(categoryId);
        return ResponseEntity.ok(ResourceDtos);
    }
}
