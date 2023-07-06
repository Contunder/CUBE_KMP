package com.example.cube.Service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.cube.Model.Activity;
import com.example.cube.Model.Analytics;
import com.example.cube.Model.Catalogue;
import com.example.cube.Model.User;
import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Repository.ActivityRepository;
import com.example.cube.Repository.AnalyticsRepository;
import com.example.cube.Repository.CatalogueRepository;
import com.example.cube.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CatalogueServiceImplTest {

    @Mock
    private CatalogueRepository catalogueRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private CatalogueServiceImpl catalogueService;

    private CatalogueDto catalogueDto;
    private Catalogue catalogue;
    private Analytics analytics;

    @BeforeEach
    public void setUp() {
        catalogueDto = new CatalogueDto();
        catalogueDto.setId(1L);
        catalogueDto.setCategory("Electronics");

        catalogue = new Catalogue();
        catalogue.setId(catalogueDto.getId());
        catalogue.setCategory(catalogueDto.getCategory());

        analytics = new Analytics();
        analytics.setDate(getSQLDate());
    }

    @Test
    public void addCatalogue() {
        // given
        when(catalogueRepository.save(any(Catalogue.class))).thenReturn(catalogue);

        // when
        CatalogueDto result = catalogueService.addCatalogue(catalogueDto);

        // then
        Long catalogueId = 1L;
        String catalogueCategory = "Electronics";
        assertEquals(catalogueId, result.getId());
        assertEquals(catalogueCategory, result.getCategory());
    }

    @Test
    public void GetCatalogue() {
        // given
        Long catalogueId = 1L;
        when(catalogueRepository.findById(catalogueId)).thenReturn(Optional.of(catalogue));

        // when
        CatalogueDto result = catalogueService.getCatalogue(catalogueId);

        // then
        String catalogueCategory = "Electronics";
        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(catalogueCategory);
    }

    @Test
    public void GetAllCatalogues() {
        // given
        when(catalogueRepository.findAll()).thenReturn(Arrays.asList(catalogue));

        // when
        List<CatalogueDto> result = catalogueService.getAllCatalogues();

        // then
        String catalogueCategory = "Electronics";
        assertThat(result).isNotNull();
        assertThat(result.get(0).getCategory()).isEqualTo(catalogueCategory);
    }

    @Test
    public void UpdateCatalogue() {
        // given
        Long catalogueId = 1L;

        when(catalogueRepository.findById(catalogueId)).thenReturn(Optional.of(catalogue));
        when(catalogueRepository.save(catalogue)).thenReturn(catalogue);

        // when
        CatalogueDto result = catalogueService.updateCatalogue(catalogueDto, catalogueId);

        // then
        String catalogueCategory = "Electronics";
        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(catalogueCategory);
    }

    @Test
    public void DeleteCatalogue() {
        // given

        when(catalogueRepository.findById(catalogue.getId()))
                .thenReturn(Optional.of(catalogue));

        // when
        catalogueService.deleteCatalogue(catalogue.getId());

        // then
        verify(catalogueRepository).delete(catalogue);
    }

    @Test
    public void SetView() {
        // given
        Long catalogueId = 1L;
        String email = "test@test.fr";

        when(catalogueRepository.findById(catalogueId))
                .thenReturn(Optional.ofNullable(catalogue));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByCatalogue(Optional.ofNullable(catalogue)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate()))
                .thenReturn(Optional.of(new Analytics()));

        // when
        String view = catalogueService.setView(email, 1, true);

        // then
        assertThat(view).isEqualTo("resource view : true");
    }

    @Test
    public void SetLike() {
        // given
        Long catalogueId = 1L;
        String email = "test@test.fr";

        when(catalogueRepository.findById(catalogueId))
                .thenReturn(Optional.ofNullable(catalogue));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByCatalogue(Optional.ofNullable(catalogue)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate())).thenReturn(Optional.of(analytics));

        // when
        String view = catalogueService.setLike(email, 1, true);

        // then
        assertThat(view).isEqualTo("resource liked : true");
    }

    @Test
    public void SetShare() {
        // given
        Long catalogueId = 1L;
        String email = "test@test.fr";

        when(catalogueRepository.findById(catalogueId))
                .thenReturn(Optional.ofNullable(catalogue));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByCatalogue(Optional.ofNullable(catalogue)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate())).thenReturn(Optional.of(analytics));

        // when
        String view = catalogueService.setShare(email, 1, true);

        // then
        assertThat(view).isEqualTo("resource share : true");
    }

    @Test
    public void SetBlocked() {
        // given
        Long catalogueId = 1L;
        String email = "test@test.fr";

        when(catalogueRepository.findById(catalogueId))
                .thenReturn(Optional.ofNullable(catalogue));
        when(userRepository.findUserByEmail(email))
                .thenReturn(new User());
        when(activityRepository.getActivityByCatalogue(Optional.ofNullable(catalogue)))
                .thenReturn(Optional.of(new Activity()));
        when(analyticsRepository.getAnalyticsByDate(getSQLDate())).thenReturn(Optional.of(analytics));

        // when
        String view = catalogueService.setBlocked(email, 1, true);

        // then
        assertThat(view).isEqualTo("resource blocked : true");
    }

    private static Date getSQLDate(){
        java.util.Date todayJava = new java.util.Date();
        return new Date(todayJava.getDate());
    }


}
