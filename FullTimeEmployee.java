public class FullTimeEmployee extends Employee{
    
    // Variables
    private double bonus = 0;
    
    public FullTimeEmployee(String name, int age, Department department, String position, double bonus) throws ValidationException
    {
        super(name, age, department, position);
        Validator.validateNonNegative(bonus, "Bonus");
        this.bonus = bonus;
    }
    
    public FullTimeEmployee(int id, String name, int age, Department department, String position, double bonus) throws ValidationException
    {
        super(id, name, age, department, position);
        Validator.validateNonNegative(bonus, "Bonus");
        this.bonus = bonus;
    }
    
    // Getters
    public double getBonus(){return this.bonus;}
   
    //  Setters
    public void setBonus(double bonus) throws ValidationException
    {
        Validator.validateNonNegative(bonus, "Bonus");
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary(SalaryRates rates) {
        return rates.getFullTimeSalary(getDepartment()) + this.bonus;
    }

    @Override
    public String getType() {
        return "Full-Time";
    }

    @Override
    public String toCsv() {
        return "FULL," + super.commonCsv() +","+this.bonus;
    }
    
}
