package com.freightfox.dataset.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.freightfox.dataset.service.DatasetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dataset")
public class DatasetController {
    private final DatasetService service;
    public DatasetController(DatasetService service) { this.service = service; }

    @PostMapping("/{datasetName}/record")
    public ResponseEntity<Map<String, Object>> insertRecord(@PathVariable String datasetName, @RequestBody JsonNode record) {
        validateDatasetName(datasetName);
        Long recordId = service.insertRecord(datasetName, record);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Record added successfully");
        response.put("dataset", datasetName);
        response.put("recordId", recordId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{datasetName}/query")
    public ResponseEntity<Map<String, Object>> queryRecords(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String order) {
        validateDatasetName(datasetName);
        if ((groupBy == null && sortBy == null) || (groupBy != null && sortBy != null)) {
            throw new IllegalArgumentException("Provide exactly one query parameter: groupBy or sortBy");
        }
        Map<String, Object> response = new LinkedHashMap<>();
        if (groupBy != null) response.put("groupedRecords", service.groupRecords(datasetName, groupBy));
        else response.put("sortedRecords", service.sortRecords(datasetName, sortBy, order));
        return ResponseEntity.ok(response);
    }

    private void validateDatasetName(String name) {
        if (name == null || !name.matches("[A-Za-z0-9_-]{1,150}")) {
            throw new IllegalArgumentException("datasetName may contain only letters, numbers, underscore and hyphen (max 150 characters)");
        }
    }
}
