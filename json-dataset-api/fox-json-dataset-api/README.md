# FreightFox Backend Assignment – JSON Dataset API

A Java 21 / Spring Boot REST API that stores JSON records in MySQL and supports dynamic group-by and sort-by operations.

## Requirements
- JDK 21
- Maven 3.9+
- MySQL 8+
- Postman (for manual API testing)

## Database setup
Create a MySQL user/database or use a local root account. The default JDBC URL creates `freightfox_db` if it does not exist. Update `src/main/resources/application.properties` or set environment variables:

- `DB_URL` (default: `jdbc:mysql://localhost:3306/freightfox_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `DB_USERNAME` (default: `root`)
- `DB_PASSWORD` (default: `root`)

Change the default password to your own local MySQL password. Hibernate creates/updates the `dataset_records` table automatically.

## Run
```bash
mvn clean test
mvn spring-boot:run
```
Server: `http://localhost:8080`

## API documentation
### 1. Insert a JSON record
`POST /api/dataset/{datasetName}/record`

Example: `POST http://localhost:8080/api/dataset/employee_dataset/record`

Body (`Content-Type: application/json`):
```json
{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "department": "Engineering"
}
```
Response (201 Created):
```json
{
  "message": "Record added successfully",
  "dataset": "employee_dataset",
  "recordId": 1
}
```
`recordId` is the generated database row ID; the `id` inside the JSON remains part of the original record.

### 2. Group records by a JSON field
`GET /api/dataset/{datasetName}/query?groupBy={field}`

Example: `GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department`

Response:
```json
{
  "groupedRecords": {
    "Engineering": [
      { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
    ]
  }
}
```

### 3. Sort records by a JSON field
`GET /api/dataset/{datasetName}/query?sortBy={field}&order={asc|desc}`

Example: `GET http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=asc`

Response:
```json
{
  "sortedRecords": [
    { "id": 1, "name": "John Doe", "age": 30, "department": "Engineering" }
  ]
}
```
`order` defaults to `asc`. Only one of `groupBy` or `sortBy` may be supplied per request.

## Validation and errors
- Dataset names allow letters, numbers, `_` and `-`, up to 150 characters.
- Insert body must be a non-empty JSON object.
- Group/sort field must exist in every record in the selected dataset.
- Sort order must be `asc` or `desc`.
- Invalid requests return HTTP 400; a missing/empty dataset returns HTTP 404.
- Error response includes `timestamp`, `status`, `error`, and `message`.

## Notes
- Each dataset is identified by its name; multiple datasets share the same relational table.
- JSON is stored as text in MySQL native `JSON` column, keeping the schema flexible across records with different fields.
- Grouping preserves record insertion order within each group. Numeric JSON values are compared numerically when sorting; booleans are compared as booleans; other values are compared case-insensitively as text.
