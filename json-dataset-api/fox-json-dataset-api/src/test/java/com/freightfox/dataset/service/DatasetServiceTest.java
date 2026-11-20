package com.freightfox.dataset.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freightfox.dataset.entity.DatasetRecord;
import com.freightfox.dataset.repository.DatasetRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetServiceTest {
    @Mock DatasetRecordRepository repository;
    DatasetService service;
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach void setUp() { service = new DatasetService(repository, mapper); }

    @Test void groupsRecordsByDepartment() throws Exception {
        when(repository.findAllByDatasetNameOrderByIdAsc("employees")).thenReturn(List.of(
            new DatasetRecord("employees", "{\"name\":\"John\",\"department\":\"Engineering\"}"),
            new DatasetRecord("employees", "{\"name\":\"Jane\",\"department\":\"Engineering\"}"),
            new DatasetRecord("employees", "{\"name\":\"Alice\",\"department\":\"Marketing\"}")));
        var result = service.groupRecords("employees", "department");
        assertEquals(2, result.size());
        assertEquals(2, result.get("Engineering").size());
        assertEquals(1, result.get("Marketing").size());
    }

    @Test void sortsNumericFieldsAscending() throws Exception {
        when(repository.findAllByDatasetNameOrderByIdAsc("employees")).thenReturn(List.of(
            new DatasetRecord("employees", "{\"name\":\"John\",\"age\":30}"),
            new DatasetRecord("employees", "{\"name\":\"Jane\",\"age\":25}")));
        var result = service.sortRecords("employees", "age", "asc");
        assertEquals(25, result.get(0).get("age").asInt());
        assertEquals(30, result.get(1).get("age").asInt());
    }

    @Test void rejectsUnsupportedSortOrder() {
        assertThrows(IllegalArgumentException.class, () -> service.sortRecords("employees", "age", "sideways"));
    }
}
