package com.example.ecovault_pi_mobile.model;

import java.util.List;

public class CollectionPoint {
    private String id;
    private String name;
    private String address;
    private String distance;
    private String openStatus;
    private boolean isOpen;
    private List<AcceptedItem> acceptedItems;

    public CollectionPoint(String id, String name, String address, String distance,
                           String openStatus, boolean isOpen, List<AcceptedItem> acceptedItems) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.openStatus = openStatus;
        this.isOpen = isOpen;
        this.acceptedItems = acceptedItems;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDistance() { return distance; }
    public String getOpenStatus() { return openStatus; }
    public boolean isOpen() { return isOpen; }
    public List<AcceptedItem> getAcceptedItems() { return acceptedItems; }
}