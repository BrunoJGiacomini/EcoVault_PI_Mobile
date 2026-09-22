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
    private double latitude;
    private double longitude;

    private String distanciaExibida;
    private float distanciaEmMetros = Float.MAX_VALUE;

    public CollectionPoint(String id, String name, String address, String distance,
                           String openStatus, boolean isOpen, List<AcceptedItem> acceptedItems,
                           double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.distance = distance;
        this.openStatus = openStatus;
        this.isOpen = isOpen;
        this.acceptedItems = acceptedItems;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getDistance() { return distance; }
    public String getOpenStatus() { return openStatus; }
    public boolean isOpen() { return isOpen; }
    public List<AcceptedItem> getAcceptedItems() { return acceptedItems; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public String getDistanciaExibida() { return distanciaExibida; }
    public void setDistanciaExibida(String distanciaExibida) { this.distanciaExibida = distanciaExibida; }

    public float getDistanciaEmMetros() { return distanciaEmMetros; }
    public void setDistanciaEmMetros(float distanciaEmMetros) { this.distanciaEmMetros = distanciaEmMetros; }
}
