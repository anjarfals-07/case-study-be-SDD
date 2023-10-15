    package com.api.fitnescenter.controller;

    import com.api.fitnescenter.dto.ListOfServicesRequest;
    import com.api.fitnescenter.entity.ListOfServices;
    import com.api.fitnescenter.service.ListFitnessService;
    import io.swagger.v3.oas.annotations.Operation;
    import io.swagger.v3.oas.annotations.Parameter;
    import io.swagger.v3.oas.annotations.enums.ParameterIn;
    import io.swagger.v3.oas.annotations.media.Content;
    import io.swagger.v3.oas.annotations.media.Schema;
    import io.swagger.v3.oas.annotations.responses.ApiResponse;
    import io.swagger.v3.oas.annotations.responses.ApiResponses;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/fitness-services")
    public class FitnessServiceController {

        private final ListFitnessService fitnessService;

        @Autowired
        public FitnessServiceController(ListFitnessService fitnessService) {
            this.fitnessService = fitnessService;
        }

        @GetMapping("/list")
        public ResponseEntity<List<ListOfServices>> getAllServices() {
            List<ListOfServices> services = fitnessService.getAllServices();
            return ResponseEntity.ok(services);
        }

        @PreAuthorize("hasRole('ADMIN')")
        @PostMapping("/create")
        @Operation(summary = "Create a fitness service", description = "Creates a fitness service with the specified details.")
        public ResponseEntity<ListOfServices> createFitnessService(
                @RequestBody ListOfServicesRequest fitnessServiceRequest) {
            // Proses pembuatan fitness service di sini
            ListOfServices createdFitnessService = fitnessService.createFitnessService(fitnessServiceRequest);
            return ResponseEntity.ok(createdFitnessService);
        }

        @PutMapping("/update/{id}")
        public ResponseEntity<ListOfServices> updateFitnessService(@PathVariable Long id, @RequestBody ListOfServicesRequest fitnessServiceRequest) {
            ListOfServices updatedFitnessService = fitnessService.updateFitnessService(id, fitnessServiceRequest);
            return ResponseEntity.ok(updatedFitnessService);
        }

        @GetMapping("/get/{id}")
        public ResponseEntity<ListOfServices> getFitnessServiceById(@PathVariable Long id) {
            ListOfServices fitnessServiceEntity = fitnessService.getFitnessServiceById(id);
            return ResponseEntity.ok(fitnessServiceEntity);
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteFitnessService(@PathVariable Long id) {
            fitnessService.deleteFitnessService(id);
            return ResponseEntity.ok("Fitness Service deleted successfully");
        }
    }
