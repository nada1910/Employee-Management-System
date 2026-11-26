
package com.mycompany.employeemanagmentsystem;


public class EmployeeService1 {
     public Employee searchById(int id) {

        return getById(id);

    }

    public List<Employee> searchByDepartment(String deptName) {
        List<Employee> found = new ArrayList<>();
        if (deptName == null) {
            return found;
        }
        String wanted = deptName.trim();
        for (Employee e : employees) {
            if (e.getDepartment().getName().equalsIgnoreCase(wanted)) {
                found.add(e);
            }
        }
        return found;
    }
    
}
