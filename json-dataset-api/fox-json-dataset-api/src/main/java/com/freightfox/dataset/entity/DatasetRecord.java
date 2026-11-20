package com.freightfox.dataset.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dataset_records", indexes = @Index(name = "idx_dataset_name", columnList = "dataset_name"))
public class DatasetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_name", nullable = false, length = 150)
    private String datasetName;

    @Column(name = "record_data", nullable = false, columnDefinition = "json")
    private String recordData;

    protected DatasetRecord() { }

    public DatasetRecord(String datasetName, String recordData) {
        this.datasetName = datasetName;
        this.recordData = recordData;
    }

    public Long getId() { return id; }
    public String getDatasetName() { return datasetName; }
    public String getRecordData() { return recordData; }
}
