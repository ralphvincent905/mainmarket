# MainMarket Security & User Management Implementation

## Overview
This document describes the security and user management implementation for the MainMarket e-commerce application (Deliverable 2).

## Key Components Implemented

### 1. User Entity with Spring Security Integration
**File:** `src/main/java/ca/humber/cpan228/mainmarket/entity/User.java`

- Implements Spring Security's `UserDetails` interface
- Stores user authentication and authorization information
- Uses Bcrypt-encoded passwords (never stored in plain text)
- Includes account status flags (enabled, locked, expired, etc.)
- Mapped to the database via JPA

**Key Fields:**
- `userId`: Unique identifier
- `username`: Unique username for authentication
- `email`: Unique email address
- `password`: Bcrypt-encoded password
- `role`: Role enum (ADMIN, STAFF, or CUSTOMER)
- Account status flags for security

### 2. Role-Based Access Control (RBAC)
**File:** `src/main/java/ca/humber/cpan228/mainmarket/entity/Role.java`

Three meaningful roles defined for the e-commerce domain:

#### **ADMIN Role**
- **Description:** Administrator with full system access
- **Capabilities:**
  - View and manage all users
  - Create, edit, and delete products
  - Access admin dashboard
  - View system statistics
  - Manage user roles
- **Restrictions:** None
- **Access Points:** `/admin/**`

#### **STAFF Role**
- **Description:** Staff member for product and order management
- **Capabilities:**
  - Create, edit, and delete products
  - View customer orders
  - View product inventory
- **Restrictions:** 
  - Cannot access admin panel
  - Cannot manage user accounts
  - Cannot view system settings
- **Access Points:** `/products/new`, `/products` (POST)

#### **CUSTOMER Role**
- **Description:** Customer for browsing and purchasing
- **Capabilities:**
  - Browse all products
  - View product details
  - Create user account (new registration)
  - Make purchases
- **Restrictions:**
  - Cannot add/edit/delete products
  - Cannot access admin features
  - Cannot manage other user accounts
- **Access Points:** `/products` (GET), `/` (home)

### 3. User Repository
**File:** `src/main/java/ca/humber/cpan228/mainmarket/repository/UserRepository.java`

- Extends `JpaRepository` for CRUD operations
- Custom query methods:
  - `findByUsername(String)`: Lookup user by username
  - `findByEmail(String)`: Lookup user by email
  - `existsByUsername(String)`: Check if username exists
  - `existsByEmail(String)`: Check if email exists

### 4. Custom UserDetailsService
**File:** `src/main/java/ca/humber/cpan228/mainmarket/service/CustomUserDetailsService.java`

- Implements Spring Security's `UserDetailsService`
- Loads user credentials from database during authentication
- Throws `UsernameNotFoundException` if user doesn't exist
- Integrates with DaoAuthenticationProvider

### 5. User Service Layer
**File:** `src/main/java/ca/humber/cpan228/mainmarket/service/UserService.java`

- Handles user registration business logic
- Validates username and email uniqueness
- Encodes passwords using Bcrypt before storage
- Assigns CUSTOMER role to new registrations
- Provides user lookup functionality

### 6. Spring Security Configuration
**File:** `src/main/java/ca/humber/cpan228/mainmarket/config/SecurityConfig.java`

**Key Features:**
- **Password Encoding:** BCryptPasswordEncoder with strength 10
- **Authentication Provider:** DaoAuthenticationProvider configured with UserDetailsService
- **HTTP Security Rules:**
  - Public access: `/`, `/login`, `/register`, `/products`, `/about`, `/css/**`, `/js/**`
  - Admin only: `/admin/**`
  - Staff + Admin: `/staff/**` (future use)
  - Authenticated required: All other endpoints
- **Form Login:** Configured with custom login page and redirect logic
- **Logout:** Configured with proper session cleanup
- **CSRF Protection:** Disabled for H2 console access
- **Method-Level Security:** `@EnableMethodSecurity` for `@PreAuthorize` annotations
- **H2 Console:** Enabled for development database access

### 7. Authentication Controller
**File:** `src/main/java/ca/humber/cpan228/mainmarket/controller/AuthController.java`

**Endpoints:**
- `GET /login`: Display login form (redirects if already authenticated)
- `GET /register`: Display registration form
- `POST /register`: Process user registration with validation
  - Validates password confirmation
  - Enforces minimum 6-character password
  - Returns to registration page on error
  - Redirects to login on success

### 8. Admin Controller
**File:** `src/main/java/ca/humber/cpan228/mainmarket/controller/AdminController.java`

**Endpoints:**
- `GET /admin/dashboard`: Admin dashboard with statistics and user list
- Access restricted to ADMIN role via `@PreAuthorize`

**Features:**
- Displays total user count
- Shows count by role (Admin, Staff, Customer)
- Lists all users with their details
- Shows role descriptions and access levels

### 9. Product Controller with Role-Based Restrictions
**File:** `src/main/java/ca/humber/cpan228/mainmarket/controller/ProductController.java`

**Role-Based Access:**
- `GET /products`: Open to all (public)
- `GET /products/new`: Restricted to ADMIN and STAFF roles
- `POST /products`: Restricted to ADMIN and STAFF roles
- Uses `@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")` annotations

### 10. Data Initialization
**File:** `src/main/java/ca/humber/cpan228/mainmarket/config/DataLoader.java`

Automatically creates demo users on application startup:
- **Admin User:** username: `admin`, password: `admin123`
- **Staff User:** username: `staff`, password: `staff123`
- **Customer User:** username: `customer`, password: `customer123`

All passwords are automatically encoded with Bcrypt.

### 11. Template Updates

#### Login Form
**File:** `src/main/resources/templates/login.html`
- Username and password input fields
- Error message display for failed authentication
- Link to registration for new users
- Professional styling with Bootstrap integration

#### Registration Form
**File:** `src/main/resources/templates/register.html`
- First name and last name input
- Username and email input
- Password confirmation validation
- Role information display (new accounts = CUSTOMER)
- Client-side and server-side validation
- Error message display

#### Updated Navigation Bar
**File:** `src/main/resources/templates/fragments/navbar.html`
- Conditional "Add Product" link (ADMIN and STAFF only)
- Conditional login/logout links based on authentication
- Display current username when logged in
- Display user role when logged in
- Secure logout with POST form

#### Enhanced Home Page
**File:** `src/main/resources/templates/index.html`
- Security information section
- Role descriptions with capabilities
- Demo credentials for testing
- Link to admin dashboard for admins
- Security implementation highlights

#### Admin Dashboard
**File:** `src/main/resources/templates/admin-dashboard.html`
- User statistics (total, by role)
- User list with details
- Role descriptions
- Access control information

## Security Features

### Password Security
- ✅ **Bcrypt Hashing:** All passwords are hashed with Bcrypt (strength 10)
- ✅ **Never Plain Text:** Passwords are never stored in plain text
- ✅ **Pre-storage Encoding:** Passwords encoded before database storage
- ✅ **Secure Comparison:** Spring Security compares hashes, not plain text

### Authentication
- ✅ **Database-Backed:** User credentials stored and validated from database
- ✅ **Session Management:** Spring Security session handling
- ✅ **Login Page:** Custom login form with CSRF protection
- ✅ **Failed Login Handling:** Error messages for invalid credentials

### Authorization
- ✅ **Role-Based Access Control:** Three distinct roles with different permissions
- ✅ **Method-Level Security:** `@PreAuthorize` annotations on sensitive endpoints
- ✅ **URL-Pattern Protection:** Security rules configured in SecurityFilterChain
- ✅ **Thymeleaf Integration:** `sec:authorize` for conditional UI rendering

### Data Validation
- ✅ **Registration Validation:** Username, email, and password validation
- ✅ **Uniqueness Checks:** Prevents duplicate usernames and emails
- ✅ **Password Confirmation:** Client and server-side verification
- ✅ **Password Requirements:** Minimum 6 characters enforced

## Testing the Implementation

### Step 1: Start the Application
```bash
cd c:\Users\ralph\mainmarket
mvn spring-boot:run
```

### Step 2: Access the Application
Open browser to `http://localhost:8080`

### Step 3: Test Public Access
- Can access `/` (home) without authentication
- Can access `/products` (product list) without authentication
- Can access `/register` (registration form) without authentication
- Can access `/login` (login form) without authentication

### Step 4: Test Registration
1. Click "Register" link
2. Fill in user details:
   - First Name: John
   - Last Name: Doe
   - Username: johndoe
   - Email: john@example.com
   - Password: password123 (min 6 chars)
   - Confirm Password: password123
3. Click "Create Account"
4. Should redirect to login page with success message

### Step 5: Test Login with Different Roles

#### Test as ADMIN
1. Click "Login"
2. Username: `admin`
3. Password: `admin123`
4. Should see "Admin Dashboard" link in navbar
5. Can access `/admin/dashboard`
6. Can add products via "Add Product" link
7. Cannot see non-admin links

#### Test as STAFF
1. Login with Username: `staff`
2. Password: `staff123`
3. Can see "Add Product" link
4. Cannot access `/admin/dashboard` (403 Forbidden)
5. Can add/edit products

#### Test as CUSTOMER
1. Login with Username: `customer`
2. Password: `customer123`
3. Cannot see "Add Product" link
4. Cannot access `/admin/dashboard`
5. Can browse products and view home page

### Step 6: Test Access Restrictions
1. Log out
2. Try to access `/admin/dashboard` → Redirects to login
3. Login as CUSTOMER
4. Try to access `/admin/dashboard` → 403 Forbidden
5. Try to access `/products/new` → 403 Forbidden

## Dependencies Added

To accomplish this security implementation, the following dependencies were added to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>
```

## Database Schema

The application uses H2 in-memory database with the following `users` table:

```sql
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    is_enabled BOOLEAN DEFAULT true,
    is_account_non_locked BOOLEAN DEFAULT true,
    is_account_non_expired BOOLEAN DEFAULT true,
    is_credentials_non_expired BOOLEAN DEFAULT true
);
```

## File Structure

```
src/
├── main/
│   ├── java/ca/humber/cpan228/mainmarket/
│   │   ├── config/
│   │   │   ├── SecurityConfig.java (Spring Security configuration)
│   │   │   └── DataLoader.java (Demo data initialization)
│   │   ├── controller/
│   │   │   ├── AuthController.java (Login/Registration endpoints)
│   │   │   ├── AdminController.java (Admin dashboard)
│   │   │   ├── ProductController.java (Updated with role restrictions)
│   │   │   └── HomeController.java
│   │   ├── entity/
│   │   │   ├── User.java (Implements UserDetails)
│   │   │   ├── Role.java (Enum with three roles)
│   │   │   ├── Product.java
│   │   │   ├── Brand.java
│   │   │   └── Category.java
│   │   ├── repository/
│   │   │   ├── UserRepository.java (Custom queries)
│   │   │   ├── ProductRepository.java
│   │   │   ├── BrandRepository.java
│   │   │   └── CategoryRepository.java
│   │   └── service/
│   │       ├── CustomUserDetailsService.java
│   │       └── UserService.java
│   └── resources/
│       ├── templates/
│       │   ├── login.html (Login form)
│       │   ├── register.html (Registration form)
│       │   ├── admin-dashboard.html (Admin interface)
│       │   ├── index.html (Updated home with security info)
│       │   ├── fragments/
│       │   │   └── navbar.html (Updated with auth links)
│       │   └── ... (other templates)
│       └── application.properties
└── test/
    └── ...
```

## Deliverable Requirements Met

✅ **Users and Registration**
- Registration flow implemented with form validation
- Passwords encoded with Bcrypt
- User entity implements UserDetails interface
- Users stored in database through UserRepository

✅ **Three Meaningful Roles**
- ADMIN: Full system management access
- STAFF: Product and order management access
- CUSTOMER: Browse and purchase access

✅ **Role-Based Restrictions**
- Navbar shows/hides "Add Product" based on role
- Product creation restricted to ADMIN and STAFF
- Admin dashboard only accessible to ADMIN
- Method-level security with @PreAuthorize
- URL-pattern security in SecurityFilterChain

## Security Best Practices Implemented

1. **Password Security:** Bcrypt hashing prevents rainbow table attacks
2. **SQL Injection Prevention:** JPA parameterized queries
3. **CSRF Protection:** Spring Security CSRF tokens (enabled by default)
4. **Authentication:** Database-backed user authentication
5. **Authorization:** Multi-layered role-based access control
6. **Session Management:** Spring Security session handling
7. **Secure Logout:** POST-based logout form
8. **Input Validation:** Server-side validation on registration
9. **Error Handling:** Generic error messages (no user enumeration)
10. **Development Mode:** H2 console access in dev environment only

## Future Enhancements

1. **Email Verification:** Verify email address during registration
2. **Password Reset:** Implement forgotten password flow
3. **Two-Factor Authentication:** Add 2FA for enhanced security
4. **Audit Logging:** Log all user actions for compliance
5. **Password Policy:** Enforce stronger password requirements
6. **Account Lockout:** Lock account after failed login attempts
7. **Role Management UI:** Admin interface to assign/change roles
8. **OAuth/SSO:** Integrate with third-party authentication providers
9. **API Security:** Add JWT authentication for REST APIs
10. **Password Expiration:** Force password changes periodically

## Conclusion

The MainMarket application now has a robust security and user management system with role-based access control, Bcrypt password encoding, and Spring Security integration. The three roles (ADMIN, STAFF, CUSTOMER) provide meaningful restrictions based on the e-commerce domain, ensuring that users can only access features appropriate to their role.
