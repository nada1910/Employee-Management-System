public class PartTimeEmployee extends Employee {
        
    // Variables
    private double hoursWorked = 6;
    
    public PartTimeEmployee(String name, int age, Department department, String position, double hoursWorked) throws ValidationException
    {
        super(name, age, department, position);
        Validator.validateNonNegative(hoursWorked, "Hours Worked");
        this.hoursWorked = hoursWorked;
    }

    public PartTimeEmployee(int id, String name, int age, Department department, String position, double hoursWorked) throws ValidationException
    {
        super(id, name, age, department, position);
        Validator.validateNonNegative(hoursWorked, "Hours Worked");
        this.hoursWorked = hoursWorked;
    }
    
    // Getters
    public double getHoursWorked(){return this.hoursWorked;}
   
    //  Setters
    public void setHoursWorked(double hoursWorked) throws ValidationException
    {
        Validator.validateNonNegative(hoursWorked, "Hours Worked");
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary(SalaryRates rates) {
        return rates.getHourlyRate(super.getDepartment()) * this.hoursWorked;
    }

    @Override
    public String getType() {
        return "Part-Time";
    }

    @Override
    public String toCsv() {
        return "PART," + super.commonCsv() +","+this.hoursWorked;
    }
    
}

