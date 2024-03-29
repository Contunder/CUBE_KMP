package com.example.cube.Controller;

import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.ResourceService;
import com.example.cube.Utils.AppConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class ResourceController {

    private ResourceService resourceService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public ResourceController(ResourceService resourceService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider) {
        this.resourceService = resourceService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/api/resources/add/{catalogueId}")
    public ResponseEntity<ResourceDto> createResource(HttpServletRequest request, @Valid @RequestBody ResourceDto resourceDto, @PathVariable(name = "catalogueId") long catalogueId){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(resourceService.createResource(resourceDto, catalogueId, email), HttpStatus.CREATED);
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

    @GetMapping(value = "/api/resources/user/{id}")
    public ResponseEntity<List<ResourceDto>> getResourceByRelationId(@PathVariable(name = "id") long relationId){
        return ResponseEntity.ok(resourceService.getResourceByUserId(relationId));
    }

    @GetMapping(value = "/api/resources/relation/{relation}")
    public ResponseEntity<List<ResourceDto>> getResourceByRelationId(HttpServletRequest request, @PathVariable(name = "relation") String relation){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return ResponseEntity.ok(resourceService.getResourceByRelation(email, relation));
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

    @PostMapping("/api/resources/{id}/view/{boolean}")
    public ResponseEntity<String> viewResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean view){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(resourceService.setView(email, id, view), HttpStatus.CREATED);
    }

    @PostMapping("/api/resources/{id}/like/{boolean}")
    public ResponseEntity<String> likeResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean like){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(resourceService.setLike(email, id, like), HttpStatus.CREATED);
    }

    @PostMapping("/api/resources/{id}/share/{boolean}")
    public ResponseEntity<String> shareResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean share){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(resourceService.setShare(email, id, share), HttpStatus.CREATED);
    }

    @PostMapping("/api/resources/{id}/block/{boolean}")
    public ResponseEntity<String> blockResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean blocked){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(resourceService.setBlocked(email, id, blocked), HttpStatus.CREATED);
    }
}
