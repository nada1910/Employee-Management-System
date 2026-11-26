import java.util.*;
public class EmployeeService {
    
    private final List<Employee> employees = new ArrayList<>();
    private final FileHandler fileHandler;
    private final DepartmentService departmentService;

    public List<Employee> getAll(){
        return new ArrayList<>(employees);
    }
    
    public double getSalary(Employee e){
        return e.calculateSalary(departmentService.getRates()); 
    }

    public Employee getById(int id){
        for(Employee e : employees){
            if(e.getId() == id)
                return e;
            }
        return null;
    }
    
    public void deleteEmployee(int id) throws ValidationException{
        Employee e = getById(id);

        if(e == null)
            throw new ValidationException("Employee not found");

        employees.remove(e);

        try{
            fileHandler.saveEmployees(employees);
        }
        catch(ValidationException ex){
            employees.add(e);
            throw ex;
        }
    }
    public void updateEmployee(Employee updated) throws ValidationException{
        int index = -1;

        for(int i = 0; i < employees.size(); i++){
            if(employees.get(i).getId() == updated.getId()){
                index = i;
                break;
            }
        }

        if(index == -1)
            throw new ValidationException("Employee not found");

        Employee old = employees.get(index);
        employees.set(index, updated);

        try{
            fileHandler.saveEmployees(employees);
        }
        catch(ValidationException ex){
            employees.set(index, old);
            throw ex;
        }
    }
    
}