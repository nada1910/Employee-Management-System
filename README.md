# Employee Management System: Amr's Part

## 1. My Classes

| Class | Type | Purpose |
|-------|------|---------|
| `FileHandler` | File handling | Saves and loads employees and departments using TXT files |
| `ViewEmployeesPanel` | GUI (`Refreshable`) | Displays all employees, filters by department, and allows updating and deleting employees |
| `EmployeeService` | Service | Manages employee data, calculates salaries, and handles updating and deleting employees |
| `ValidationException` | Exception | Handles validation and file-related errors and displays meaningful messages to the user |

**My methods in shared files**

| File | Methods I wrote |
|------|-----------------|
| `Validator` | `validateSelection(int selectedRow)`, `validateSelectionD(int selectedRow)`, `validateName(String name)`, `validateNameD(String name)`, `validateForUpdate(Employee e, EmployeeService es)` |
| `EmployeeService` | `getAll()`, `getSalary(Employee e)`, `deleteEmployee(int id)`, `updateEmployee(Employee updated)` |

## 2. How Each Part Works

### FileHandler

- Uses the `data` folder to store the application's data.
- Uses two TXT files:
  - `employees.txt` stores employee information.
  - `departments.txt` stores department information.

**Methods:**

- `ensureDataFolder()` creates the data folder if it does not exist.
- `saveDepartments(List<Department> depts, SalaryRates rates)` saves department IDs, names, full-time salaries, and hourly rates into `departments.txt`.
- `loadDepartments(SalaryRates rates)` reads departments from the TXT file and restores their salary rates. Invalid lines are skipped.
- `saveEmployees(List<Employee> list)` saves all employees using their `toCsv()` method.
- `loadEmployees(List<Department> depts)` reads employees from the file and converts each valid line into a `FullTimeEmployee` or `PartTimeEmployee`.
- `parseLine(String line, List<Department> depts)` checks the employee data and creates the correct employee object.
- `getWarnings()` returns warnings about invalid or duplicate employee records.
- If invalid employee records or duplicate IDs are found, they are skipped, and a backup file named `employees.bak` is created.

### ViewEmployeesPanel

- Displays employees in a table with the columns **ID, Name, Age, Department, Position, Type, Salary**.
- Uses `EmployeeService` to retrieve employee information and calculate salaries.
- Uses a department filter to display employees from a selected department or all departments.

**How it works:**

- `refresh()` reloads the department lists, clears the selection and editing fields, and displays all employees.
- `loadTable(List<Employee> list)` fills the table with employee information and calculates each employee's salary using `es.getSalary(e)`.
- `filterByDepartment()` filters employees according to the selected department.
- `onRowSelected(ListSelectionEvent event)` gets the selected employee and displays their information in the editing fields.
  - For full-time employees, the bonus field is displayed.
  - For part-time employees, the hours worked field is displayed.
- `onSaveChanges()` validates the entered information, creates an updated employee object, and calls `es.updateEmployee(updated)`.
- `onDelete()` asks for confirmation before deleting the selected employee using `es.deleteEmployee(id)`.
- `clearForm()` clears all editing fields, resets the selected employee ID, hides the bonus and hours fields, and disables the Save Changes and Delete buttons.

**Buttons:**

- **Save Changes:** Saves the updated employee information.
- **Delete:** Deletes the selected employee after confirmation.
- **Back to Home:** Returns to the home screen.

Validation errors are caught and displayed using `JOptionPane`.

### EmployeeService

**Methods:**

- `getAll()` returns a new list containing all employees. This prevents external code from directly modifying the original employee list.
- `getSalary(Employee e)` calculates the employee's salary using the department salary rates.
- `deleteEmployee(int id)`:
  1. Searches for the employee using their ID.
  2. If the employee does not exist, throws a `ValidationException`.
  3. Removes the employee from the list.
  4. Saves the updated list to the TXT file.
  5. If saving fails, restores the deleted employee to the list.

- `updateEmployee(Employee updated)`:
  1. Searches for the employee using their ID.
  2. If the employee does not exist, throws a `ValidationException`.
  3. Saves the old employee object temporarily.
  4. Replaces the old employee with the updated employee.
  5. Saves the updated list to the TXT file.
  6. If saving fails, restores the old employee information.

Both `deleteEmployee()` and `updateEmployee()` protect the employee list from being left in an incorrect state if saving fails.

### Validator

**Methods I added:**

- `validateSelection(int selectedRow)` checks whether an employee row is selected. If no row is selected (`selectedRow < 0`), it throws a `ValidationException` with the message "Please select an employee."

- `validateSelectionD(int selectedRow)` checks whether a department row is selected. If no row is selected, it throws a `ValidationException` with the message "Please select a department."

- `validateName(String name)` checks that the employee's full name is not null or empty and contains only English letters and spaces.

- `validateNameD(String name)` checks that the department name is not null or empty and contains only English letters and spaces.

- `validateForUpdate(Employee e, EmployeeService es)`:
  1. Calls `validateEmployee(e)` to validate the employee information.
  2. Checks whether the employee service is available.
  3. Checks whether the employee ID still exists.
  4. Throws a `ValidationException` if any of these conditions fail.

### ValidationException

- Extends Java's `Exception` class.
- Has a constructor `ValidationException(String message)`.
- Passes the error message to the parent `Exception` class using `super(message)`.
- Allows the GUI to display meaningful error messages to the user.

## 3. Data Storage

| File | Stored Information |
|------|-------------------|
| `employees.txt` | Employee ID, name, age, department ID, position, and salary-related information |
| `departments.txt` | Department ID, name, full-time salary, and hourly rate |
| `employees.bak` | Backup of employee data when invalid or duplicate employee records are found |

All files are stored inside the `data` folder.

## 4. Classes From My Teammates That My Code Uses

| Used by | Needs | From |
|---------|-------|------|
| `FileHandler` | `Employee`, `FullTimeEmployee`, `PartTimeEmployee` | Amr / Team |
| `FileHandler` | `Department`, `SalaryRates` | Team |
| `ViewEmployeesPanel` | `MainFrame`, `Theme`, `Refreshable` | Team |
| `ViewEmployeesPanel` | `EmployeeService`, `DepartmentService` | Amr / Team |
| `ViewEmployeesPanel` | `Validator`, `ValidationException` | Team / Amr |
| `EmployeeService` | `FileHandler`, `DepartmentService` | Team |
| `Validator` | `Employee`, `EmployeeService`, `ValidationException` | Team / Amr |

## 5. Running and Testing My Part

**Requirements:** JDK 25 or later (for the current project configuration).

1. Build the project using NetBeans: **Clean and Build**.
2. Run `Main.java`.
3. Log in and open the **View Employees** screen.
4. Test adding, filtering, updating, and deleting employees.

**Test cases**

| Test | Expected result |
|------|-----------------|
| Open View Employees | All employees are displayed in the table |
| Filter by a department | Only employees from that department are displayed |
| Select a full-time employee | Employee information and bonus field are displayed |
| Select a part-time employee | Employee information and hours worked field are displayed |
| Save changes with valid information | Employee information is updated successfully |
| Save changes with an invalid name | Validation error is displayed |
| Save changes with an invalid age | Validation error is displayed |
| Click Save Changes without selecting an employee | Warning message is displayed |
| Delete an employee | Confirmation message appears before deletion |
| Delete a nonexistent employee | Validation error is displayed |
| Update a nonexistent employee | Validation error is displayed |
| Save employees successfully | Updated employee information is stored in `employees.txt` |
| File saving fails during an update | Previous employee information is restored |
| File saving fails during deletion | Deleted employee is restored |
| Load employees with duplicate IDs | Duplicate records are skipped and a backup is created |
| Load employees with invalid data | Invalid records are skipped and warnings are recorded |
| Return to View Employees after changing data | Table is refreshed with the latest information |
