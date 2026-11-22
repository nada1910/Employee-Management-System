import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    
    // Variables
    private final List<Employee> employees = new ArrayList();
    private final FileHandler fileHandler;
    private final DepartmentService departmentService;
    
    public EmployeeService(FileHandler fh, DepartmentService ds) throws ValidationException
    {
        this.fileHandler = fh;
        this.departmentService = ds;
        this.employees.addAll(fileHandler.loadEmployees(departmentService.getAll()));
    }
    
    public void addEmployee(Employee e) throws ValidationException
    {
        Validator.validateEmployee(e);
        
        if(idExists(e.getId()))
            throw new ValidationException("An employee with this id already exists");
        
        employees.add(e);
        try{
            fileHandler.saveEmployees(employees);
        } catch (ValidationException ex) {
            employees.remove(e);   
            throw ex;
            
        }
    }
    
    public boolean idExists(int id)
    {
        for (Employee e : employees) {
            if (e.getId() == id) return true;
        }
        return false;
    }
    
}
