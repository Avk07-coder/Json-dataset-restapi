package com.freightfox.dataset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightfox.dataset.entity.DatasetRecord;
import com.freightfox.dataset.repository.DatasetRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DatasetService {
    private final DatasetRecordRepository repository;
    private final ObjectMapper objectMapper;

    public DatasetService(DatasetRecordRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Long insertRecord(String datasetName, JsonNode record) {
        if (record == null || !record.isObject() || record.isEmpty()) {
            throw new IllegalArgumentException("Request body must be a non-empty JSON object");
        }
        try {
            DatasetRecord saved = repository.save(new DatasetRecord(datasetName, objectMapper.writeValueAsString(record)));
            return saved.getId();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to process JSON record", e);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, List<JsonNode>> groupRecords(String datasetName, String field) {
        validateField(field);
        Map<String, List<JsonNode>> grouped = new LinkedHashMap<>();
        for (JsonNode record : loadRecords(datasetName)) {
            JsonNode value = record.get(field);
            if (value == null) {
                throw new IllegalArgumentException("Field '" + field + "' does not exist in every record");
            }
            String key = value.isValueNode() ? value.asText() : value.toString();
            grouped.computeIfAbsent(key, ignored -> new ArrayList<>()).add(record);
        }
        return grouped;
    }

    @Transactional(readOnly = true)
    public List<JsonNode> sortRecords(String datasetName, String field, String order) {
        validateField(field);
        if (!"asc".equalsIgnoreCase(order) && !"desc".equalsIgnoreCase(order)) {
            throw new IllegalArgumentException("order must be either 'asc' or 'desc'");
        }
        List<JsonNode> records = loadRecords(datasetName);
        for (JsonNode record : records) {
            if (!record.has(field)) {
                throw new IllegalArgumentException("Field '" + field + "' does not exist in every record");
            }
        }
        Comparator<JsonNode> comparator = (left, right) -> compareValues(left.get(field), right.get(field));
        if ("desc".equalsIgnoreCase(order)) comparator = comparator.reversed();
        records.sort(comparator);
        return records;
    }

    private List<JsonNode> loadRecords(String datasetName) {
        List<DatasetRecord> entities = repository.findAllByDatasetNameOrderByIdAsc(datasetName);
        if (entities.isEmpty()) throw new NoSuchElementException("Dataset '" + datasetName + "' was not found or contains no records");
        List<JsonNode> records = new ArrayList<>();
        for (DatasetRecord entity : entities) {
            try {
                records.add(objectMapper.readTree(entity.getRecordData()));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("Stored JSON record is invalid", e);
            }
        }
        return records;
    }

    private void validateField(String field) {
        if (field == null || field.isBlank()) throw new IllegalArgumentException("A non-empty JSON field name is required");
    }

    private int compareValues(JsonNode a, JsonNode b) {
        if (a == null || a.isNull()) return (b == null || b.isNull()) ? 0 : -1;
        if (b == null || b.isNull()) return 1;
        if (a.isNumber() && b.isNumber()) return new BigDecimal(a.asText()).compareTo(new BigDecimal(b.asText()));
        if (a.isBoolean() && b.isBoolean()) return Boolean.compare(a.asBoolean(), b.asBoolean());
        return a.asText().compareToIgnoreCase(b.asText());
    }
}
