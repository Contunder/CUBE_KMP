package com.example.cube.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "access", nullable = false)
    private String access;
    @Column(name = "value", nullable = false)
    private String value;
    private String joint;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "resource_catalogue",
            joinColumns = @JoinColumn(name = "resource_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "catalogue_id", referencedColumnName = "id")
    )
    private Set<Catalogue> Catalogue;

}
