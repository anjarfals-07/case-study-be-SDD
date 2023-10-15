package com.api.fitnescenter.service;

import com.api.fitnescenter.dto.ListOfServicesRequest;
import com.api.fitnescenter.entity.ListOfServices;
import com.api.fitnescenter.repository.FitnessServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListFitnessService {

    @Autowired
    private FitnessServicesRepository fitnessServicesRepository;

    public ListFitnessService(FitnessServicesRepository fitnessServicesRepository) {
        this.fitnessServicesRepository = fitnessServicesRepository;
    }

    public List<ListOfServices> getAllServices() {
        // Mengambil semua layanan kebugaran dari repository
        return fitnessServicesRepository.findAll();
    }

    public ListOfServices createFitnessService(ListOfServicesRequest fitnessServiceRequest) {
        ListOfServices fitnessServiceEntity = new ListOfServices();
        fitnessServiceEntity.setServiceName(fitnessServiceRequest.getServiceName());
        fitnessServiceEntity.setPricePerSession(fitnessServiceRequest.getPricePerSession());
        fitnessServiceEntity.setDuration(fitnessServiceRequest.getDuration());
        fitnessServiceEntity.setDescription(fitnessServiceRequest.getDescription());
        return fitnessServicesRepository.save(fitnessServiceEntity);
    }

    public ListOfServices updateFitnessService(Long id, ListOfServicesRequest fitnessServiceRequest) {
        Optional<ListOfServices> optionalFitnessService = fitnessServicesRepository.findById(id);
        if (optionalFitnessService.isPresent()) {
            ListOfServices fitnessServiceEntity = optionalFitnessService.get();
            fitnessServiceEntity.setServiceName(fitnessServiceRequest.getServiceName());
            fitnessServiceEntity.setPricePerSession(fitnessServiceRequest.getPricePerSession());
            fitnessServiceEntity.setDuration(fitnessServiceRequest.getDuration());
            fitnessServiceEntity.setDescription(fitnessServiceRequest.getDescription());
            return fitnessServicesRepository.save(fitnessServiceEntity);
        } else {
            throw new RuntimeException("Fitness Service not found");
        }
    }
    public void deleteFitnessService(Long id) {
        fitnessServicesRepository.deleteById(id);
    }

    public ListOfServices getFitnessServiceById(Long id) {
        return fitnessServicesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fitness Service not found"));
    }

    // Update


}
