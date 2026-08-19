package com.example.post.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class GeoLocationService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public double calculateDistance(double lat1,double lon1,double lat2,double lon2) {

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double lonDifferenceRad = Math.toRadians(lon2 - lon1);

        double angle = Math.acos(
                Math.sin(lat1Rad) * Math.sin(lat2Rad)
                        + Math.cos(lat1Rad)
                        * Math.cos(lat2Rad)
                        * Math.cos(lonDifferenceRad)
        );

        return EARTH_RADIUS_KM * angle;
    }

    public double calculateDistanceWeight(double distanceKm) {
        return 1.0 / (1.0 + distanceKm);
    }

    public double calculateRecencyWeight(LocalDateTime createdAt) {

        long ageMinutes = Duration.between(createdAt, LocalDateTime.now()).toMinutes();
        double ageHours = ageMinutes / 60.0;
        return Math.exp(-ageHours / 3.0);
    }
    
}