package com.example.cube.Model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friends")
public class Friend {

    @EmbeddedId
    private Key key = new Key();

    @ManyToOne
    @MapsId("user")
    private User user;

    @ManyToOne
    @MapsId("friend")
    private User friend;

    private String relation;
    private boolean isActive;


    @Embeddable
    public static class Key implements Serializable {
        private Long user;
        private Long friend;
    }
}
