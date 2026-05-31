# Audi Dealership Management System

Proiect PAO Java pentru tema Audi Dealership.

Implementarea principala este in:

`src/com/pao/proiect/audidealership`

Etapa II adauga:

- `schema.sql`
- `resources/db.properties`
- `DatabaseConnection`
- `Repository<T, ID>`
- repository-uri JDBC pentru `Customer`, `AudiCar`, `SalesAgent`, `Order`
- tranzactie JDBC pentru salvarea unei comenzi si marcarea masinii ca vanduta
- interogari SQL cu `JOIN`
- `AuditService` thread-safe care scrie in `audit.csv`

README-ul detaliat al proiectului este in `src/com/pao/project/README.md`.
