# Setup and Installation Instructions

## Prerequisites

Before you begin, ensure you have the following installed:

- Java Development Kit (JDK) 11 or higher
- Maven 3.6 or higher
- Git (optional, for version control)

## Getting Started

### Clone the Repository (Optional)

If you're using Git for version control, clone the repository:

```bash
git clone https://github.com/your-username/sdui.git
cd sdui
```

### Build the Project

To build the project, run the Maven wrapper command:

```bash
# On Windows
mvnw.bat clean install

# On Unix-based systems
./mvnw clean install
```

This will:
1. Download all required dependencies
2. Compile the source code
3. Run tests
4. Package the application into a JAR file

### Run the Application

To run the application, use the Maven wrapper:

```bash
# On Windows
mvnw.bat mn:run

# On Unix-based systems
./mvnw mn:run
```

Alternatively, you can run the JAR file directly:

```bash
java -jar target/sdui-0.1.jar
```

The application will start and be available at `http://localhost:8080`.

## Configuration

### Application Properties

The application can be configured using the `application.yml` file located in the `src/main/resources` directory.

Key configuration properties:

```yaml
sdui:
  jsonPath: "sdui-plugins.json"  # Path to the JSON file containing plugin definitions
```

### Customizing Plugins

To customize the available plugins:

1. Edit the `src/main/resources/sdui-plugins.json` file
2. Follow the existing structure to add, modify, or remove plugins
3. Restart the application to apply the changes

## API Documentation

Once the application is running, you can access the Swagger UI to explore the API:

```
http://localhost:8080/swagger-ui
```

## Troubleshooting

### Common Issues

1. **Port already in use**
   - Change the server port in `application.yml`:
     ```yaml
     micronaut:
       server:
         port: 8081  # Change to an available port
     ```

2. **JSON file not found**
   - Ensure the JSON file path in `application.yml` is correct
   - Check that the file exists in the classpath

3. **Build failures**
   - Ensure you have the correct JDK version installed
   - Run `mvnw clean` to clean the project and try building again
