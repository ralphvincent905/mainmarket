# Quick Start Guide - Testing Security Implementation

## Starting the Application

```bash
cd c:\Users\ralph\mainmarket
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

On startup, you'll see:
```
✓ Admin user created - Username: admin, Password: admin123
✓ Staff user created - Username: staff, Password: staff123
✓ Customer user created - Username: customer, Password: customer123
✓ User initialization complete
```

## Test Scenarios

### 1. Public Access (No Login Required)
- Visit `http://localhost:8080` → Home page with security info ✓
- Visit `http://localhost:8080/products` → Browse products ✓
- Visit `http://localhost:8080/about` → About page ✓
- Try to access `http://localhost:8080/admin/dashboard` → Redirects to login ✓

### 2. User Registration (New Account)
1. Click "Register" link in navbar
2. Fill in registration form:
   ```
   First Name: John
   Last Name: Doe
   Username: johndoe
   Email: john.doe@example.com
   Password: password123
   Confirm Password: password123
   ```
3. Click "Create Account"
4. Should redirect to login with success message
5. Login with new credentials → New account has CUSTOMER role

### 3. Test as ADMIN
1. Go to `http://localhost:8080/login`
2. Enter credentials:
   ```
   Username: admin
   Password: admin123
   ```
3. Click "Sign In"
4. You should see:
   - Username "admin" displayed in navbar with (ADMIN) role
   - "Add Product" link in navbar (enabled)
   - "Logout" link instead of Login
5. Click "Add Product" → Form accessible (200 OK)
6. Click admin dropdown or visit `/admin/dashboard` → Admin dashboard visible
   - Shows user statistics
   - Lists all users with roles
   - Shows role descriptions

### 4. Test as STAFF
1. Logout by clicking logout link
2. Go to `http://localhost:8080/login`
3. Enter credentials:
   ```
   Username: staff
   Password: staff123
   ```
4. Click "Sign In"
5. You should see:
   - Username "staff" displayed in navbar with (STAFF) role
   - "Add Product" link in navbar (enabled)
6. Click "Add Product" → Form accessible (200 OK)
7. Try to access `/admin/dashboard`:
   - Should show 403 Forbidden error (no permission)

### 5. Test as CUSTOMER
1. Logout
2. Go to `http://localhost:8080/login`
3. Enter credentials:
   ```
   Username: customer
   Password: customer123
   ```
4. Click "Sign In"
5. You should see:
   - Username "customer" displayed in navbar with (CUSTOMER) role
   - "Add Product" link NOT visible (disabled)
6. Try to access `/products/new`:
   - Should redirect or show 403 Forbidden error
7. Visit `/products` → Product browsing works ✓

### 6. Test Login Failure
1. Go to `http://localhost:8080/login`
2. Enter invalid credentials:
   ```
   Username: invalid
   Password: wrongpassword
   ```
3. Click "Sign In"
4. Should show error message: "Invalid username or password"

### 7. Test Password Encoding
1. Access H2 Database Console: `http://localhost:8080/h2-console`
2. JDBC URL: `jdbc:h2:mem:mainmarketdb`
3. Run SQL query:
   ```sql
   SELECT USERNAME, PASSWORD FROM USERS LIMIT 1;
   ```
4. Notice password is NOT in plain text (starts with `$2a$` for Bcrypt hash)

### 8. Logout Functionality
1. Click "Logout" in navbar
2. Should redirect to home page
3. Navbar should show "Login" and "Register" links
4. Should NOT see username or role display

## Expected Behaviors by Role

### ADMIN Can:
- ✅ View and edit home page
- ✅ Create new products
- ✅ Access admin dashboard (`/admin/dashboard`)
- ✅ View user management (statistics, list)
- ✅ Browse products

### STAFF Can:
- ✅ Create new products
- ✅ Browse products
- ❌ Cannot access admin dashboard
- ❌ Cannot manage users

### CUSTOMER Can:
- ✅ Browse products
- ✅ View home page
- ❌ Cannot add products
- ❌ Cannot access admin dashboard

## Troubleshooting

### Issue: Can't login
**Solution:** Make sure username and password match demo credentials exactly:
- admin/admin123
- staff/staff123
- customer/customer123

### Issue: Database shows plain text passwords
**Solution:** Make sure you're using the built application. Rebuild with:
```bash
mvn clean package -DskipTests
```

### Issue: Admin dashboard shows no users
**Solution:** Make sure H2 `spring.jpa.defer-datasource-initialization=true` is set in application.properties

### Issue: "Add Product" button visible for all users
**Solution:** Log out and back in. Browser may be caching the page. Do hard refresh (Ctrl+Shift+R or Cmd+Shift+R)

### Issue: Role badges not showing in navbar
**Solution:** Clear browser cache or use incognito/private browsing mode

## Key Security Validations

1. **Passwords are NOT plain text:**
   - Check H2 console: all passwords start with `$2a$`
   - This indicates Bcrypt hashing with cost factor 10

2. **Role-based access works:**
   - Customer cannot see Add Product link
   - Customer cannot POST to `/products`
   - Staff cannot access `/admin/dashboard`

3. **Authentication required:**
   - Cannot access protected endpoints without login
   - Invalid credentials show generic error message (no user enumeration)

4. **Session management:**
   - Logout clears session
   - Cannot reuse old session after logout

## Security Features Demonstrated

- ✅ Bcrypt password hashing
- ✅ Role-based access control (3 levels)
- ✅ Method-level security with @PreAuthorize
- ✅ URL-pattern security rules
- ✅ UI conditional rendering based on role
- ✅ Form login/logout flow
- ✅ Session management
- ✅ User registration with validation
- ✅ Database-backed user storage

## Documentation

See [SECURITY_IMPLEMENTATION.md](./SECURITY_IMPLEMENTATION.md) for comprehensive documentation of:
- Architecture overview
- Component descriptions
- Configuration details
- Database schema
- Testing procedures
- Security best practices
- Future enhancements
