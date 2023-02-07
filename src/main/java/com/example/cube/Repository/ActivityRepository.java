package com.example.cube.Repository;

import com.example.cube.Model.Activity;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.Resource;
import com.example.cube.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> getActivitiesByUser(User user);
    List<Activity> getActivitiesByResource(Optional<Resource> resource);
    List<Activity> getActivitiesByCatalogue(Optional<Catalogue> catalogue);
    Activity getActivityByResource(Optional<Resource> resource);
    Activity getActivityByCatalogue(Optional<Catalogue> catalogue);

}
