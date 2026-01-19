package com.codingshuttle.project.airBnb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "hotel")
// This tells Jackson to ignore the internal Hibernate fields
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "TEXT[]")
    private String[] photos;

    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //@Column(nullable = false)
    private Boolean active=false;

    @OneToMany(mappedBy = "hotel")
    @JsonIgnore
    @ToString.Exclude
    private List<Room> rooms;

    @Embedded
    private HotelContactInfo contactInfo;
    //contact_info_address
    //contact_info_phone_number

    @ManyToOne(optional = false)
    private User owner;


}
