import java.util.HashMap;
import java.util.Map;

public class SalaryRates {
    private Map<Department, Double> fullTimeSalary = new HashMap<>();
    private Map<Department, Double> partTimeHourly = new HashMap<>();

    public double getFullTimeSalary(Department d) {
        if(d == null) {
            return 0.0;
        }
        return fullTimeSalary.getOrDefault(d, 0.0); // return 0.0 if missing
    }
    public double getHourlyRate(Department d) {
        if(d == null) {
            return 0.0;
        }
        return partTimeHourly.getOrDefault(d, 0.0);
    }

    public void setFullTimeSalary(Department d, double value) throws ValidationException{
        if(d == null) {
            throw new ValidationException("Department must not be null.");
        }
        if(value < 0) {
            throw new ValidationException("Full-time monthly salary must not be negative.");
        }
        fullTimeSalary.put(d, value);
    }
    public void setHourlyRate(Department d, double value) throws ValidationException {
        if (d == null) {
            throw new ValidationException("Department must not be null.");
        }
        if (value < 0) {
            throw new ValidationException("Part-Time hourly rate must not be negative.");
        }
        partTimeHourly.put(d, value);
    }
}
