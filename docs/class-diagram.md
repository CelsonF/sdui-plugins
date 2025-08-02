# Class Diagram

## Model Classes Relationship

```mermaid
classDiagram
    class Plugin {
        -String type
        -String feature
        -Modifier modifier
        -Style style
        -List~BenefitGroup~ benefits
        -Points points
    }
    
    class BenefitGroup {
        -String profile
        -List~String~ benefits
    }
    
    class Points {
        -Integer total
        -String lastUpdated
        -String currency
    }
    
    class Modifier {
        -String padding
        -String margin
        -String alignment
    }
    
    class Style {
        -String backgroundColor
        -String borderRadius
        -String textColor
        -Boolean shadow
        -String fontFamily
        -String fontWeight
    }
    
    class PluginContainer {
        -String type
        -String feature
        -List~Plugin~ children
    }
    
    Plugin "1" *-- "0..*" BenefitGroup : contains
    Plugin "1" *-- "0..1" Points : contains
    Plugin "1" *-- "1" Modifier : contains
    Plugin "1" *-- "1" Style : contains
    PluginContainer "1" *-- "0..*" Plugin : contains
```

## System Architecture

```mermaid
classDiagram
    class SDUIController {
        -SDUIPluginService service
        +getPlugins(List~String~ feature) HttpResponse~List~Plugin~~
    }
    
    class SDUIPluginService {
        -ObjectMapper mapper
        -ResourceResolver resolver
        -Configuration config
        -List~Plugin~ plugins
        +init() void
        +getPluginsByFeature(List~String~ feature) List~Plugin~
    }
    
    class Configuration {
        -String jsonPath
    }
    
    class Application {
        +main(String[] args) void
    }
    
    SDUIController --> SDUIPluginService : uses
    SDUIPluginService --> Configuration : uses
    SDUIPluginService --> Plugin : manages
```

Note: This diagram is created using Mermaid markdown syntax. To view it properly, you'll need a Markdown viewer that supports Mermaid diagrams.
