# MainMarket

A Spring Boot-based online marketplace application built with Java, featuring user authentication, product management, and an admin dashboard.

## Features

- User registration and login
- Product catalog with categories and brands
- Admin dashboard for managing products and users
- Secure authentication with Spring Security
- MySQL database integration

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL database (for production)

## Running the Application

### Using Maven

1. Clone the repository
2. Navigate to the project directory
3. Run the following command:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Using Docker

Ensure Docker and Docker Compose are installed, then run:

```bash
docker-compose up
```

## Configuration

The application uses different profiles:

- `default`: Uses H2 in-memory database
- `qa`: Uses MySQL with QA configuration
- `prod`: Uses MySQL with production configuration

## Database Setup

For MySQL profiles, ensure your database is set up with the schema and data from `src/main/resources/data-mysql.sql`.

## Testing

Run tests with:

```bash
mvn test
```

See [TESTING_GUIDE.md](TESTING_GUIDE.md) for detailed testing instructions.

## Security

Refer to [SECURITY_IMPLEMENTATION.md](SECURITY_IMPLEMENTATION.md) for security details and implementation.

## Project Structure

- `src/main/java/ca/humber/cpan228/mainmarket/`: Main application code
  - `controller/`: REST controllers
  - `entity/`: JPA entities
  - `repository/`: Data repositories
  - `service/`: Business logic services
  - `config/`: Configuration classes
- `src/main/resources/`: Application properties and static resources
- `src/test/`: Unit and integration tests

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests
5. Submit a pull request

