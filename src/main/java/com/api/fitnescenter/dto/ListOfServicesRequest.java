package com.api.fitnescenter.dto;

import lombok.Data;

@Data
public class ListOfServicesRequest {
    private String serviceName;
    private String description;
    private Double pricePerSession; // Harga per pertemuan
    private Integer duration;
}
