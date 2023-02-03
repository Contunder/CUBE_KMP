package com.example.cube.Service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.cube.Model.Catalogue;
import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Repository.CatalogueRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogueServiceImplTest {

    @Mock
    private CatalogueRepository catalogueRepository;

    @InjectMocks
    private CatalogueServiceImpl catalogueService;

    private CatalogueDto catalogueDto;
    private Catalogue catalogue;

    @Before
    public void setUp() {
        catalogueDto = new CatalogueDto();
        catalogueDto.setId(1L);
        catalogueDto.setCategory("Electronics");

        catalogue = new Catalogue();
        catalogue.setId(catalogueDto.getId());
        catalogue.setCategory(catalogueDto.getCategory());
    }

    @Test
    public void addCatalogue_ValidDto_ShouldAddCatalogue() {
        // given
        Long catalogueId = 1L;
        String category = "test category";
        CatalogueDto catalogueDto = new CatalogueDto();
        catalogueDto.setId(catalogueId);
        catalogueDto.setCategory(category);
        Catalogue catalogue = new Catalogue();
        catalogue.setId(catalogueId);
        catalogue.setCategory(category);

        when(catalogueRepository.save(any(Catalogue.class))).thenReturn(catalogue);

        // when
        CatalogueDto result = catalogueService.addCatalogue(catalogueDto);

        // then
        assertEquals(catalogueId, result.getId());
        assertEquals(category, result.getCategory());
    }

    @Test
    public void testGetCatalogue() {
        // given
        Long catalogueId = 1L;
        when(catalogueRepository.findById(catalogueId)).thenReturn(Optional.of(catalogue));

        // when
        CatalogueDto result = catalogueService.getCatalogue(catalogueId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(catalogue.getCategory());
    }

    @Test
    public void testGetAllCatalogues() {
        // given
        when(catalogueRepository.findAll()).thenReturn(Arrays.asList(catalogue));

        // when
        List<CatalogueDto> result = catalogueService.getAllCatalogues();

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCategory()).isEqualTo(catalogue.getCategory());
    }

    @Test
    public void testUpdateCatalogue() {
        // given
        Long catalogueId = 1L;

        when(catalogueRepository.findById(catalogueId)).thenReturn(Optional.of(catalogue));
        when(catalogueRepository.save(catalogue)).thenReturn(catalogue);

        // when
        CatalogueDto result = catalogueService.updateCatalogue(catalogueDto, catalogueId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(catalogue.getCategory());
    }

    @Test
    public void deleteCatalogue_ValidId_ShouldDeleteCatalogue() {
        // given
        Long catalogueId = 1L;
        Catalogue catalogue = new Catalogue();
        catalogue.setId(catalogueId);

        when(catalogueRepository.findById(catalogueId))
                .thenReturn(Optional.of(catalogue));

        // when
        catalogueService.deleteCatalogue(catalogueId);

        // then
        verify(catalogueRepository).delete(catalogue);
    }

}
