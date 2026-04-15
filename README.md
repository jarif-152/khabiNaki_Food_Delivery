# FooDDe - Food Delivery Platform

## Overview

FooDDe is a comprehensive Java-based food delivery platform built with object-oriented programming principles. It provides a full-stack solution for customers to browse restaurants, manage shopping carts, place orders, and for administrators to manage restaurants and menu items. The application also implements SOAP/WSDL web services for programmatic restaurant data access.

## Table of Contents

- [Key Features](#key-features)
- [Technical Stack](#technical-stack)
- [Architecture and Design](#architecture-and-design)
- [Project Structure](#project-structure)
- [Detailed Component Overview](#detailed-component-overview)
- [How to Run](#how-to-run)
- [API Documentation](#api-documentation)

---

## Key Features

### Customer Features

[x] **Browse Restaurants** - View all available restaurants with detailed information (name, address, hours)  
[x] **Menu Viewing** - Explore complete menus with item names, descriptions, and prices  
[x] **Shopping Cart** - Session-based cart with quantity tracking and automatic totals  
[x] **Multi-Item Orders** - Add multiple items from the same restaurant to your cart  
[x] **Smart Cart Management** - Automatically clears cart when switching restaurants  
[x] **User Checkout** - Simple form to capture delivery information (name, address, phone)  
[x] **Order Confirmation** - Receive order tracking ID after successful checkout  
[x] **Persistent Orders** - All orders saved to XML storage for record-keeping

### Admin Features

[x] **Restaurant Registration** - Add new restaurants with location and operating hours  
[x] **Menu Management** - Add items with names, prices, and descriptions  
[x] **Real-time Updates** - All changes immediately saved to XML files  
[x] **Admin Dashboard** - Unified interface for all management operations

### Technical Features

[x] **SOAP/WSDL Web Service** - Query restaurants by area name programmatically  
[x] **XML File Storage** - No database required, all data persisted as XML  
[x] **Session Management** - Stateful user experience with HTTP sessions  
[x] **Modular Architecture** - Clean separation of concerns following MVC pattern

---

## Technical Stack

| Component           | Technology      | Version  | Purpose                                     |
| ------------------- | --------------- | -------- | ------------------------------------------- |
| **Language**        | Java            | 21 (LTS) | Modern language features (Records, Streams) |
| **Web Framework**   | Javalin         | 6.1.3    | Lightweight HTTP server and routing         |
| **Template Engine** | Thymeleaf       | 3.1.2    | Server-side HTML rendering                  |
| **XML Binding**     | JAXB (Jakarta)  | 4.0.x    | Object-to-XML serialization                 |
| **Web Services**    | JAXWS (Jakarta) | 4.0.x    | SOAP/WSDL implementation                    |
| **Logging**         | SLF4J           | 2.0.12   | Application logging                         |
| **Build Tool**      | Maven           | 3.x+     | Dependency management and build             |

---

## Architecture and Design

### SOLID Principles

The project demonstrates object-oriented design best practices:

- **Single Responsibility Principle (SRP):**
  - Each class has one reason to change
  - Controllers handle HTTP requests only
  - Models represent data entities only
  - Repositories handle persistence only
  - Services contain business logic only

- **Dependency Inversion Principle (DIP):**
  - Controllers depend on `StorageRepository` interface, not concrete `XmlRepository`
  - Enables easy swapping of storage implementations (e.g., database, cloud storage)
  - Dependency injection used throughout

- **Open/Closed Principle (OCP):**
  - New features can be added without modifying existing code
  - Interface-based design allows extension
  - Repository pattern enables storage layer changes

- **Interface Segregation Principle (ISP):**
  - `StorageRepository` provides focused methods for each entity type
  - Controllers only depend on methods they use

- **Liskov Substitution Principle (LSP):**
  - Any implementation of `StorageRepository` can replace `XmlRepository`
  - Behavioral contracts maintained through interface

### MVC Pattern

```
View (Thymeleaf Templates)
    ↓
Controller (HTTP Request Handlers)
    ↓
Repository (Storage Interface)
    ↓
XmlRepository (Persistence Implementation)
```

---

## Project Structure

```
FooDDe/
├── src/main/java/com/foodde/
│   ├── Main.java                      # Application entry point
│   │
│   ├── controller/                    # HTTP request handlers
│   │   ├── RestaurantController.java  # Browse restaurants and menus
│   │   ├── CartController.java        # Shopping cart management
│   │   ├── UserController.java        # User checkout/login
│   │   └── AdminController.java       # Admin dashboard operations
│   │
│   ├── model/                         # Data entities (JAXB-annotated)
│   │   ├── Restaurant.java            # Restaurant with menu items
│   │   ├── MenuItem.java              # Individual food item
│   │   ├── User.java                  # Customer information
│   │   ├── Order.java                 # Customer order with items
│   │   └── OrderItem.java             # Order line item (quantity + item)
│   │
│   ├── repository/                    # Data persistence layer
│   │   ├── StorageRepository.java     # Storage contract interface
│   │   └── XmlRepository.java         # XML file implementation
│   │
│   └── service/                       # Business logic and APIs
│       └── RestaurantSoapService.java # SOAP/WSDL web service
│
├── src/main/resources/
│   ├── templates/                     # Thymeleaf HTML templates
│   │   ├── index.html                 # Restaurant listing page
│   │   ├── restaurant-detail.html     # Menu viewing page
│   │   ├── cart.html                  # Shopping cart page
│   │   ├── login.html                 # Checkout form
│   │   ├── order-success.html         # Order confirmation
│   │   └── admin/
│   │       └── dashboard.html         # Admin management panel
│   │
│   └── static/
│       └── style.css                  # Application styling
│
├── data/                              # XML data storage (auto-created)
│   ├── restaurant_1.xml               # Burger King
│   ├── restaurant_2.xml               # Pizza Hut
│   └── order_*.xml                    # Completed orders
│
└── pom.xml                            # Maven configuration
```

---

## Detailed Component Overview

### Controllers

#### 1. RestaurantController

**Purpose:** Handle restaurant browsing and menu viewing

**Endpoints:**

- `GET /` - List all restaurants
- `GET /restaurant/{id}` - View specific restaurant with menu

**Features:**

- Card-based restaurant display
- Full menu with prices and descriptions
- 404 error handling for invalid IDs
- Direct "Add to Cart" forms on menu items

#### 2. CartController

**Purpose:** Manage shopping cart and order checkout

**Endpoints:**

- `GET /cart` - View cart contents
- `POST /cart/add` - Add item to cart
- `POST /checkout` - Complete order

**Features:**

- Session-based cart storage (in-memory)
- Automatic restaurant switching (clears cart on change)
- Quantity increment for duplicate items
- Cart total calculation
- User authentication check before checkout
- Order persistence to XML
- Session cleanup after successful order
- Sets order status to "Preparing"

#### 3. UserController

**Purpose:** Handle user authentication and information

**Endpoints:**

- `GET /login` - Show checkout form
- `POST /login` - Process user information

**Features:**

- Captures name, address, phone
- Generates UUID for user ID
- Stores user in session
- Required for order placement

#### 4. AdminController

**Purpose:** Manage restaurants and menus

**Endpoints:**

- `GET /admin` - Admin dashboard
- `POST /admin/restaurant/add` - Add new restaurant
- `POST /admin/menu/add` - Add menu item

**Features:**

- Dual-pane interface (restaurant | menu management)
- Auto-generated restaurant IDs (UUID, truncated to 8 chars)
- Real-time menu display
- Form validation for required fields
- Immediate XML persistence

### Models

#### Restaurant

```java
class Restaurant {
    String id;
    String name;
    String address;
    String schedule;
    List<MenuItem> menu;
}
```

#### MenuItem

```java
class MenuItem {
    String id;
    String name;
    double price;
    String description;
    boolean available;
}
```

#### Order

```java
class Order {
    String id;           // UUID
    String userId;
    String restaurantId;
    List<OrderItem> items;
    double totalAmount;
    String status;       // Pending, Preparing, Delivering, Delivered
}
```

#### OrderItem

```java
class OrderItem {
    MenuItem menuItem;
    int quantity;

    double getSubtotal() {
        return menuItem.price * quantity;
    }
}
```

### Repository Layer

#### StorageRepository (Interface)

```java
interface StorageRepository {
    // Restaurant operations
    void saveRestaurant(Restaurant r);
    List<Restaurant> getAllRestaurants();
    Restaurant getRestaurantById(String id);

    // User operations
    void saveUser(User u);
    User getUserById(String id);

    // Order operations
    void saveOrder(Order o);
    List<Order> getOrdersByUserId(String userId);
}
```

#### XmlRepository (Implementation)

- **Storage Location:** `data/` directory
- **File Naming:**
  - Restaurants: `restaurant_{id}.xml`
  - Users: `user_{id}.xml`
  - Orders: `order_{id}.xml`
- **Technology:** JAXB marshalling/unmarshalling
- **Auto-Creation:** Creates data directory if missing
- **Simplicity:** No database setup required

### Service Layer

#### RestaurantSoapService

**WSDL Endpoint:** `http://localhost:8081/services/restaurant?wsdl`

**Methods:**

- `getRestaurantsByArea(String area)` - Filter restaurants by address (case-insensitive)
- `getAllRestaurants()` - Retrieve all restaurants

**Use Case:** External systems can query restaurant data programmatically

---

## How to Run

### Prerequisites

- **Java 21** or higher
- **Maven 3.x** or higher

### Build and Run

1. **Clone/Navigate to Project**

   ```bash
   cd FooDDe
   ```

2. **Compile and Run**

   ```bash
   mvn compile exec:java -Dexec.mainClass="com.foodde.Main"
   ```

3. **Alternative: Package and Run**
   ```bash
   mvn clean package
   java -jar target/FooDDe-1.0-SNAPSHOT.jar
   ```

### Access Points

| Service              | URL                                            | Description                         |
| -------------------- | ---------------------------------------------- | ----------------------------------- |
| **Main Application** | http://localhost:7070                          | Customer-facing restaurant browsing |
| **Admin Dashboard**  | http://localhost:7070/admin                    | Restaurant/menu management          |
| **SOAP WSDL**        | http://localhost:8081/services/restaurant?wsdl | Web service definition              |

### Initial Data

On first run, the application automatically creates:

- **Burger King** (123 Main St, 9AM-10PM)
  - Whopper - $5.99
  - Chicken Fries - $4.49
- **Pizza Hut** (456 Oak Ave, 10AM-11PM)
  - Pepperoni Pizza - $12.99
  - Cheesy Garlic Bread - $5.50

All data is saved to the `data/` directory as XML files.

---

## API Documentation

### SOAP/WSDL Web Service

**Endpoint:** `http://localhost:8081/services/restaurant`  
**WSDL:** `http://localhost:8081/services/restaurant?wsdl`

#### Operations

1. **getRestaurantsByArea**
   - **Input:** `area` (String) - Location name or address fragment
   - **Output:** List of Restaurant objects matching the area
   - **Example:** `getRestaurantsByArea("Main St")` → Returns Burger King

2. **getAllRestaurants**
   - **Input:** None
   - **Output:** List of all Restaurant objects
   - **Example:** Returns both Burger King and Pizza Hut

#### Sample SOAP Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:res="http://service.foodde.com/">
   <soapenv:Header/>
   <soapenv:Body>
      <res:getRestaurantsByArea>
         <area>Main St</area>
      </res:getRestaurantsByArea>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## Development Notes

### Main.java Entry Point

The `Main.java` class handles:

1. **Repository Initialization** - Creates XmlRepository instance
2. **Sample Data Population** - Seeds database with default restaurants/menus
3. **Javalin Server Setup** - Configures web server on port 7070
4. **Thymeleaf Configuration** - Registers template renderer
5. **Controller Registration** - Wires up all route handlers with dependency injection
6. **SOAP Service Publication** - Publishes web service on port 8081

### Session Management

- **Cart Storage:** In-memory session attribute `"cart"` (List<OrderItem>)
- **User Storage:** Session attribute `"user"` (User object)
- **Restaurant Context:** Session attribute `"currentRestaurantId"` for cart validation

### Error Handling

- **404 Pages:** Invalid restaurant IDs redirect to 404
- **Login Protection:** Checkout requires active user session
- **Cart Validation:** Prevents mixed-restaurant orders

### Styling

**CSS Framework:** Custom CSS (`static/style.css`)

- **Color Scheme:** Dark headers (#35424a), orange accents (#e8491d), green success (#27ae60)
- **Layout:** Flexbox and Grid for responsive design
- **Components:** Cards, tables, forms, buttons with hover effects
- **Typography:** Clean, readable fonts with proper hierarchy

---

## Future Enhancements

- Order history viewing for customers
- Order status tracking with real-time updates
- Payment gateway integration
- Restaurant search and filtering
- Rating and review system
- Delivery driver assignment
- Email notifications
- Mobile-responsive improvements
- Database migration (MySQL/PostgreSQL)
- RESTful API alongside SOAP

---

## License

This project is built for educational purposes as part of an Object-Oriented Programming course lab final project.

---

## Author

Developed with a focus on SOLID principles, clean architecture, and maintainable code structure.
