package com.example.post.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dashboard_stats")
public class DashboardStats {

    @Id
    private Long id;

    private long resolvedPosts;

    @ElementCollection
    private Set<String> cities = new HashSet<>();

}