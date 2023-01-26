package com.example.cube.Controller;

import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Service.CatalogueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

    private CatalogueService catalogueService;

    public CatalogueController(CatalogueService catalogueService) {
        this.catalogueService = catalogueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CatalogueDto> addCatalogue(@RequestBody CatalogueDto catalogueDto){
        CatalogueDto savedCatalogue = catalogueService.addCatalogue(catalogueDto);
        return new ResponseEntity<>(savedCatalogue, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<CatalogueDto> getCatalogue(@PathVariable("id") Long CatalogueId){
         CatalogueDto CatalogueDto = catalogueService.getCatalogue(CatalogueId);
         return ResponseEntity.ok(CatalogueDto);
    }

    @GetMapping
    public ResponseEntity<List<CatalogueDto>> getCategories(){
        return ResponseEntity.ok(catalogueService.getAllCatalogues());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CatalogueDto> updateCatalogue(@RequestBody CatalogueDto catalogueDto,
                                                      @PathVariable("id") Long catalogueId){
        return ResponseEntity.ok(catalogueService.updateCatalogue(catalogueDto, catalogueId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCatalogue(@PathVariable("id") Long CatalogueId){
        catalogueService.deleteCatalogue(CatalogueId);
        return ResponseEntity.ok("Catalogue deleted successfully!.");
    }
}
