# CIPS - Cloud Infrastructure Provisioning System

This is a demo project created solely for demonstrating the application of the **Abstract Factory design pattern**.

## Design Pattern: Abstract Factory

The core of this project is the `CloudInfrastructureFactory` interface, which defines a contract for creating families of related cloud resources (Compute Instances and Cloud Storage) without specifying their concrete classes.

- **Abstract Factory**: `CloudInfrastructureFactory`
- **Concrete Factories**: `AWSFactory`, `GCPFactory`
- **Abstract Products**: `ComputeInstance`, `CloudStorage`
- **Concrete Products**: 
    - AWS: `EC2Instance`, `S3Bucket`
    - GCP: `GCEInstance`, `GCSBucket`

## Tech Stack

### Backend
- **Framework**: Spring Boot
- **Language**: Java 25
- **Database**: SQLite (via JDBC)
- **Persistence**: Spring Data JPA / Hibernate

### Frontend
- **Framework**: Vue 3 (Vite)
- **Styling**: Tailwind CSS & PrimeVue
- **State Management**: Pinia
- **API Client**: Axios

## Disclaimer

This project is a demonstration of architectural patterns and is not intended for production use.

