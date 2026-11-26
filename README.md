# Employee-Management-System
# Employee Management System: Nada's Part

Programming II (Fall 2026), Lab 7
**Author:** Nada, Student ID: `[ID]`, Group: `[GROUP NUMBER]`

This README describes only the classes I implemented. The employee classes, department classes, file handling and the remaining panels were done by my teammates.

## 1. My Classes

| Class | Type | Purpose |
|-------|------|---------|
| `Manager` | model | Holds the fixed login credentials and the logged-in state |
| `LoginPanel` | GUI (`Refreshable`) | Login screen with the animated monster |
| `SearchPanel` | GUI (`Refreshable`) | Search employees by ID or by department |
| `Theme` | GUI utility | Shared colours, fonts, rounded buttons, table styling, background image |
| `Refreshable` | interface | `void refresh()`, called by `MainFrame` every time a panel is shown |
| `MainFrame` | GUI (`extends JFrame`) | Main window; uses a `CardLayout` to switch between panels |
| `Main` | entry point | Creates the services and opens the application |

**My methods in shared files**

| File | Methods I wrote |
|------|-----------------|
| `Validator` | `validateCredentials(String user, String pass)`, `validateSearchQuery(String text)` |
| `EmployeeService` | `searchById(int id)`, `searchByDepartment(String deptName)` |

## 2. How Each Part Works

### Manager
- Stores the username and password as constants and a `loggedIn` flag.
- `login(user, pass)` returns `true` only if both match, and then sets `loggedIn = true`.
- `logout()` and `isLoggedIn()` complete the class.

### LoginPanel
- Fields: username, password, **Login** and **Exit** buttons.
- Pressing Enter in the username field moves to the password field. Pressing Enter in the password field logs in.
- `onLogin()` first calls `Validator.validateCredentials`. If the input is empty, a warning dialog shows the message. Otherwise it calls `manager.login`:
  - **Success:** the monster turns happy and the dashboard opens after a short delay.
  - **Failure:** the message "Incorrect username or password." is shown and the password field is cleared.
- The monster is drawn from three GIFs (`monster_idle.gif`, `monster_angry.gif`, `monster_happy.gif`). It reacts to the result and scales with the window size. The input fields sit on the monster's sign.
- If an image is missing, the panel still works without the monster.
- `refresh()` clears both fields and resets the monster, so no old password remains after logout.

### SearchPanel
- A `JComboBox` selects the search type (**Employee ID** or **Department**). The text field and Search button stay disabled until a type is chosen.
- `onSearch()`:
  1. `Validator.validateSearchQuery` checks that the query is not empty.
  2. By ID: `Validator.parseInt` converts the text, then `es.searchById(id)` is called. A `null` result becomes an empty list.
  3. By department: `es.searchByDepartment(query)` is called. The match ignores letter case.
  4. The results are shown in a table with the columns **ID, Name, Age, Department, Position, Type, Salary**. The salary comes from `es.getSalary(e)` and is shown as `%.2f EGP`.
  5. If nothing is found, the message "No matching employees found." is shown.
- Invalid input is caught as a `ValidationException` and shown in a warning dialog. No data is changed.
- `refresh()` resets the search type, which also clears and disables the field, and empties the table.

### Theme
- Colour constants (`PRIMARY`, `ACCENT`, `BACKGROUND`, `SURFACE`, `TEXT`, `DANGER`, `SUCCESS`, `WARNING`, `SELECTION`) and fonts (`TITLE_FONT`, `LABEL_FONT`, `BODY_FONT`).
- `styleButton`, `styleDangerButton` and `styleTable` give every panel the same look.
- A custom `RoundedButtonUI` draws rounded buttons with hover, pressed and focus states, because the Windows look and feel ignores `setBackground`.
- `BackgroundPanel` draws `bg.jpg` scaled to cover the panel, with a plain colour as fallback.

### MainFrame
- Creates a `CardLayout` container and registers every panel under a name constant (`LOGIN`, `HOME`, `ADD`, `VIEW`, `SEARCH`, `DEPT`, `SALARY`, `MANUAL`, `ABOUT`).
- `showPanel(name)` redirects to the login screen if nobody is logged in. This blocks access to every other screen without logging in. It also calls `refresh()` on the panel if it implements `Refreshable`.
- `logout()` calls `manager.logout()` and returns to the login screen.

### Main
1. `FileHandler.ensureDataFolder()`
2. `new DepartmentService(fileHandler)`
3. `new EmployeeService(fileHandler, ds)`
4. `new Manager()`
5. `new MainFrame(es, ds, manager)` and `setVisible(true)`

All of this runs inside `SwingUtilities.invokeLater`. Any startup exception is shown in a dialog and the program exits cleanly.

## 3. Login Credentials

| Username | Password   |
|----------|------------|
| `nada`  | `nadaa1910` |

## 4. Classes From My Teammates That My Code Uses

| Used by | Needs | From |
|---------|-------|------|
| `SearchPanel`, `Main`, `MainFrame` | `EmployeeService` (`searchById`, `searchByDepartment`, `getSalary`) | Farah Elsaid / Amr |
| `Main`, `MainFrame` | `DepartmentService`, `FileHandler` | Farah Ahmed / Amr |
| `LoginPanel`, `SearchPanel` | `Validator.parseInt`, `ValidationException` | Farah Elsaid / Amr |
| `MainFrame` | `HomePanel`, `AddEmployeePanel`, `ViewEmployeesPanel`, `DepartmentManagePanel`, `SalaryInfoPanel`, `UserManualPanel`, `AboutAuthorPanel` | Farah Elsaid, Farah Ahmed, Amr |

## 5. Running and Testing My Part

**Requirements:** JDK 8 or later.

1. Put the images in the resources folder: `bg.jpg` and `images/monster_idle.gif`, `images/monster_angry.gif`, `images/monster_happy.gif`.
2. Build the project (NetBeans: **Clean and Build**).
3. Run `Main.java`.

**Test cases**

| Test | Expected result |
|------|-----------------|
| Login with empty fields | Warning dialog, angry monster |
| Login with wrong credentials | "Incorrect username or password.", password cleared, angry monster |
| Login with `nada` / `nadaa1910` | Happy monster, then the dashboard opens |
| Log out, then reopen another panel | Login screen is shown again |
| Search with no type selected | Field and Search button are disabled |
| Search by ID with letters (e.g. `abc`) | Invalid input warning |
| Search by an ID that does not exist | "No matching employees found." |
| Search by department in a different letter case (e.g. `it`) | Employees of IT are listed |
| Press Clear or leave and return to the panel | Table and field are empty |
