import java.util.ArrayList;
import java.util.List;


public class DepartmentService  {
    private List<Department> departments;
    private SalaryRates rates;
    private FileHandler fileHandler;

    public DepartmentService(FileHandler fh) throws ValidationException {
        this.fileHandler = fh;
        this.rates = new SalaryRates();
        List<Department> loaded = fileHandler.loadDepartments(this.rates);
        this.departments = (loaded != null) ? new ArrayList<>(loaded) : new ArrayList<>();
        if(this.departments.isEmpty()) {
            seedDefaults();
        }
    }

    private void seedDefaults() throws ValidationException {
        Object[][] defaults = { // id, name, full time salary, hourly rate
                { 1, DepartmentNames.IT,        50000.0, 90.0 },
                { 2, DepartmentNames.HR,        15000.0, 60.0 },
                { 3, DepartmentNames.FINANCE,   20000.0, 75.0 },
                { 4, DepartmentNames.MARKETING, 18000.0, 70.0 },
                { 5, DepartmentNames.SALES,     16000.0, 65.0 }
        };
        for(Object[] row: defaults) {
            Department d = new Department((int) row[0], (String) row[1]);
            departments.add(d);
            rates.setFullTimeSalary(d, (double)row[2]);
            rates.setHourlyRate(d, (double)row[3]);
        }
        fileHandler.saveDepartments(departments, rates);
    }

    public Department addDepartment(String name, double monthly, double hourly) throws ValidationException{
        int nextId = 1;
        for(Department d: departments) {
            if(d.getId() >= nextId) {
                nextId = d.getId() + 1;
            }
        }
        Validator.validateDepartmentUnique(nextId, name, departments);
        Department d = new Department(nextId, name);
        Validator.validateNonNegative(monthly, "Full-Time Monthly Salary");
        Validator.validateNonNegative(hourly, "Part-Time Hourly Rate");
        rates.setFullTimeSalary(d, monthly);
        rates.setHourlyRate(d, hourly);
        departments.add(d);
        fileHandler.saveDepartments(departments, rates);
        return d;
    }

    public void updateRates(Department d, double monthly, double hourly) throws ValidationException {
        Validator.validateDepartment(d, departments);
        Validator.validateNonNegative(monthly, "Full-Time Monthly Salary");
        Validator.validateNonNegative(hourly, "Part-Time Hourly Rate");
        rates.setFullTimeSalary(d, monthly);
        rates.setHourlyRate(d, hourly);
        fileHandler.saveDepartments(departments, rates);
    }

    public List<Department> getAll() {
        return new ArrayList<>(departments);
    }

    public Department findById(int id) {
        for (Department d : departments) {
            if (d.getId() == id) {
                return d;
            }
        }
        return null;
    }

    public SalaryRates getRates() {
        return rates;
    }
}
