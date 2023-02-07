package com.example.cube.Service;

import com.example.cube.Payload.ResourceDto;
import com.example.cube.Payload.ResourceResponse;

import java.util.List;

public interface ResourceService {
    ResourceDto createResource(ResourceDto resourceDto, long catalogueId, String email);

    ResourceResponse getAllResources(int pageNo, int pageSize, String sortBy, String sortDir);

    ResourceDto getResourceById(long id);

    ResourceDto updateResource(ResourceDto resourceDto, long id, long catalogueId);

    void deleteResourceById(long id);

    List<ResourceDto> getResourcesByCategory(Long categoryId);

    String setView(String email, long id, boolean view);

    String setLike(String email, long id, boolean like);

    String setShare(String email, long id, boolean share);

    String setBlocked(String email, long id, boolean blocked);
}
