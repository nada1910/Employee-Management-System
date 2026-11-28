# Employee Management System — Farah Ahmed

---

## 1. Assigned Files & Deliverables

### Owned Classes & Interfaces
| File | Role | Summary |
| :--- | :--- | :--- |
| `DepartmentNames.java` | Interface | Defines the 5 default department name constants (`IT`, `HR`, `FINANCE`, `MARKETING`, `SALES`) and `getName()` |
| `Department.java` | Model (`implements DepartmentNames`) | Represents a department with immutable `id` and validated `name`; overrides `equals`, `hashCode`, and `toString` |
| `SalaryRates.java` | Model | Stores department-level Full-Time monthly salaries and Part-Time hourly rates using two `HashMap<Department, Double>` maps |
| `DepartmentService.java` | Service | Manages the department list, seeds default departments on first launch, validates & adds new departments, updates rates, and persists via `FileHandler` |
| `DepartmentManagePanel.java` | GUI (`Refreshable`) | Displays all departments and rates in a `JTable`, allows adding new departments, and updates rates for a selected department |
| `SalaryInfoPanel.java` | GUI (`Refreshable`) | Displays a read-only table of department salary rates alongside the Full-Time and Part-Time salary calculation formulas |
| `AboutAuthorPanel.java` | GUI | Displays the read-only team authors table and a Back to Home button |
| `UML_ClassDiagram.drawio` / `.png` | Deliverable | Full UML Class Diagram covering all system classes, attributes, methods, visibility, inheritance, interfaces, aggregation, composition, and associations |

### Methods Contributed to Shared Files
| Shared File | Methods Implemented | Purpose |
| :--- | :--- | :--- |
| `Validator.java` | `validateDepartment(Department d, List<Department> all)` | Ensures the selected department is not `null` and exists in the registered department list |
| `Validator.java` | `validateDepartmentUnique(int id, String name, List<Department> all)` | Validates non-empty name (no commas) and rejects duplicate department IDs or case-insensitive duplicate names |
| `Validator.java` | `validateId(int id, String fieldName)` | Ensures positive integer IDs with a custom field label in error messages |

---

## 2. Component Architecture & Behavior

### Backend

#### `DepartmentNames` & `Department`
- `DepartmentNames` provides standard constants for the 5 default departments (`"IT"`, `"Human Resources"`, `"Finance"`, `"Marketing"`, `"Sales"`) and declares `String getName()`.
- `Department` encapsulates a `final int id` (`> 0`) and `String name` (trimmed, non-empty, no commas), validated through `Validator` in both the constructor and `setName(...)`.
- **`equals(Object o)` & `hashCode()`:** Based strictly on `id` so `Department` works reliably as a key inside `SalaryRates`'s `HashMap`s, `List.contains(...)` checks, and `JComboBox<Department>` selection matching.
- **`toString()`:** Returns `name`, allowing `JComboBox<Department>` to display readable department names directly.

#### `SalaryRates`
- Maintains two `Map<Department, Double>` (`HashMap`) structures: `fullTimeSalary` and `partTimeHourly`.
- `getFullTimeSalary(d)` and `getHourlyRate(d)` safely return `0.0` if `d` is `null` or not present (`getOrDefault`).
- `setFullTimeSalary(d, value)` and `setHourlyRate(d, value)` reject `null` departments and negative values by throwing `ValidationException`.

#### `DepartmentService`
- **Startup & Seeding:** Loads saved departments and rates via `fileHandler.loadDepartments(rates)`. If the file is missing or empty on first launch, `seedDefaults()` populates the 5 default departments and saves them immediately to `data/departments.txt`:

| ID | Department | Default Full-Time Monthly Salary | Default Part-Time Hourly Rate |
| :---: | :--- | :---: | :---: |
| 1 | IT | `50000.00 EGP` | `90.00 EGP` |
| 2 | Human Resources | `15000.00 EGP` | `60.00 EGP` |
| 3 | Finance | `20000.00 EGP` | `75.00 EGP` |
| 4 | Marketing | `18000.00 EGP` | `70.00 EGP` |
| 5 | Sales | `16000.00 EGP` | `65.00 EGP` |

- **`addDepartment(name, monthly, hourly)`:** Auto-generates `nextId = maxId + 1`, checks uniqueness (ID & case-insensitive name), validates non-negative rates, creates and stores the `Department`, sets its rates, and persists via `FileHandler`.
- **`updateRates(d, monthly, hourly)`:** Validates that `d` exists and both rates are non-negative before updating `SalaryRates` and saving. Because employees calculate their salary dynamically from `SalaryRates`, all employees in department `d` reflect the updated rates immediately.
- **`getAll()` & `findById(id)`:** `getAll()` returns a defensive copy (`new ArrayList<>(departments)`) to preserve encapsulation; `findById(id)` returns the matching `Department` or `null`.

---

### Frontend

#### `DepartmentManagePanel` *(implements `Refreshable`)*
- Displays a read-only `JTable` with columns **ID, Department, Full-Time Monthly Salary, Part-Time Hourly Rate** formatted as `%.2f EGP`.
- Selecting a table row triggers `onRowSelected()`, populating the form fields with that department's name and current rates.
- **Add Department (`onAddDepartment`):** Parses numeric inputs via `Validator.parseDouble`, delegates creation/validation to `ds.addDepartment(...)`, refreshes the table, and displays a `JOptionPane` confirmation or error message.
- **Update Rates (`onUpdateRates`):** Verifies a row is selected via `Validator.validateSelection(...)`, parses both rates, calls `ds.updateRates(...)`, refreshes the table, and shows feedback via `JOptionPane`.
- **`refresh()`:** Reloads all departments/rates from `DepartmentService` and clears table selection and input fields whenever the panel is shown.

#### `SalaryInfoPanel` *(implements `Refreshable`)*
- Displays a read-only `JTable` of all department rates and a read-only `JTextArea` summarizing the salary formulas:
  - **Full-Time:** `Monthly Salary = Department Full-Time Monthly Salary + Monthly Bonus`
  - **Part-Time:** `Monthly Salary = Department Part-Time Hourly Rate × Hours Worked`
- `refresh()` calls `loadInfo()` so any newly added department or updated rate appears immediately.

#### `AboutAuthorPanel`
- Displays a read-only `JTable` listing the project authors and a **Back to Home** button navigating to `MainFrame.HOME`.

---

## 3. OOP Principles Demonstrated

- **Encapsulation:** Private fields in `Department`, `SalaryRates`, and `DepartmentService`, defensive copies in `getAll()`, and validation before mutating state.
- **Abstraction:** `DepartmentNames` and `Refreshable` interfaces define clean contracts separated from concrete implementations.
- **Aggregation & Composition:** `Department` exists independently of `Employee` (aggregation), while `DepartmentService` owns and manages `SalaryRates` (composition).
- **GUI / Backend Separation:** All business rules, uniqueness checks, and file persistence stay in `DepartmentService` and `Validator`; GUI panels only handle layout, events, and `JOptionPane` dialogs.

---

## 4. Dependencies on Teammates' Classes

| My Class | Uses | Owned By |
| :--- | :--- | :--- |
| `DepartmentService` | `FileHandler` (`loadDepartments`, `saveDepartments`) | Amr |
| `Department`, `DepartmentService`, `DepartmentManagePanel` | `ValidationException`, `Validator` (`parseDouble`, `validateNonNegative`, `validateSelection`) | Amr / Farah Elsaid |
| `DepartmentManagePanel`, `SalaryInfoPanel`, `AboutAuthorPanel` | `MainFrame` (`showPanel`, `MainFrame.HOME`), `Theme`, `Refreshable` | Nada |

---

## 5. Testing Checklist

| Scenario | Steps | Expected Result |
| :--- | :--- | :--- |
| **First-launch seeding** | Delete `data/departments.txt` and start the app | 5 default departments are created and saved automatically |
| **Add valid department** | Open **Departments**, enter `"Operations"`, `22000`, `85`, click **Add Department** | Added with next ID, appears in table, saved to `data/departments.txt`, visible in **Add Employee** dropdown & **Salary Info** |
| **Duplicate department (case-insensitive)** | Try adding `"it"` or `"FINANCE"` | Error dialog: department already exists; no duplicate added |
| **Invalid inputs** | Submit empty name, name containing `,`, letters in rate fields, or negative numbers | Informative `JOptionPane` error; stored data remains unchanged |
| **Update rates without selection** | Click **Update Rates** with no row selected | Error dialog prompting the user to select a row |
| **Update rates & live salary effect** | Select `IT`, change monthly salary, click **Update Rates**, then open **View Employees** and **Salary Info** | Table and file update; all `IT` employees immediately show recalculated salaries |
| **Persistence across restart** | Add/update a department, close and reopen the app | Changes are reloaded accurately from `data/departments.txt` |
