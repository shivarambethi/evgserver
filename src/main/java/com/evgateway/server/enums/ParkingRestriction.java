package com.evgateway.server.enums;

public enum ParkingRestriction {
	
	EV_ONLY, // Reserved parking spot for electric vehicles.
    PLUGGED, // Parking is only allowed while plugged in (charging).
    DISABLED, // Reserved parking spot for disabled people with valid ID.
    CUSTOMERS, // Parking spot for customers/guests only, for example in case of a hotel or shop.
    MOTORCYCLES // Parking spot only suitable for (electric) motorcycles or scooters.

}
