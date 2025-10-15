# 🩺 Telehealth System (THS)

## Team Details
| Name | StudentID |
|------------|----------|
| Ketul Pareshbhai Patel | 12262547  |
| Thitichuda Uechitarporn | 12270885 |
| Parth Lalajibhai Patel | 12281818  |


## ⚙️ System Requirements

| Component | Version |
|------------|----------|
| Java Development Kit | JDK 24  |
| JavaFX SDK | 24.0.2 |
| MySQL Server | 8.0  |
| IDE | NetBeans 19+  |

---
---

## 🚀 How to Run the Project

### 1. Setup JavaFX
Download and extract **JavaFX SDK 24.0.2** to:
```
C:\javafx-sdk-24.0.2
```

In **NetBeans:**
- Go to `Project Properties → Run`.
- Add the following to **VM Options**:
  ```
  --module-path "C:\javafx-sdk-24.0.2\lib" --add-modules javafx.controls,javafx.fxml
  ```

---

### 2. Setup Database (MySQL)

1. Open **MySQL Workbench**   
2. Run the file:  
   ```sql
   SOURCE /path/to/ths.sql;
   ```
3. Verify:
   ```sql
   USE ths;
   SELECT email, role FROM user_accounts;
   ```

**Default Credentials:**
| Role | Email | Password |
|------|--------|-----------|
| Admin | admin@ths.com | admin123 |
| Doctor | drjane@example.com | doc123 |
| Patient | john@example.com | john123 |

---

### 3. Configure Database Connection
Edit your connection configuration file or constants (usually in `infra/DatabaseConnection.java`):

```java
private static final String URL = "jdbc:mysql://localhost:3306/ths";
private static final String USER = "ths_user";
private static final String PASSWORD = "ths_pass";
```

---

### 4. Build and Run
In NetBeans:
```
Clean and Build → Run Project
```

The application will start with the **Login Screen**.  
After successful login, it automatically loads the appropriate dashboard based on user role.

---
