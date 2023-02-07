package com.example.cube.Controller;

import com.example.cube.Payload.CatalogueDto;
import com.example.cube.Security.JwtAuthenticationFilter;
import com.example.cube.Security.JwtTokenProvider;
import com.example.cube.Service.CatalogueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogue")
public class CatalogueController {

    private CatalogueService catalogueService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public CatalogueController(CatalogueService catalogueService, JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider) {
        this.catalogueService = catalogueService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
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

    @PostMapping("/{id}/view/{boolean}")
    public ResponseEntity<String> viewResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean view){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(catalogueService.setView(email, id, view), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/like/{boolean}")
    public ResponseEntity<String> likeResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean like){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(catalogueService.setLike(email, id, like), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/share/{boolean}")
    public ResponseEntity<String> shareResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean share){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(catalogueService.setShare(email, id, share), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/block/{boolean}")
    public ResponseEntity<String> blockResource(HttpServletRequest request, @PathVariable(name = "id") long id, @PathVariable("boolean") boolean blocked){
        String token = jwtAuthenticationFilter.getTokenFromRequest(request);
        String email = jwtTokenProvider.getUsername(token);

        return new ResponseEntity<>(catalogueService.setBlocked(email, id, blocked), HttpStatus.CREATED);
    }
}
