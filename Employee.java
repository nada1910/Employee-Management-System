public abstract class Employee {
    
    // Variables
    private static int nextId = 1;         
    private final int id;
    private String name;
    private int age;
    private Department department;
    private String position;
    
    // Constructor
    public Employee(String name, int age, Department department, String position) throws ValidationException
    {
        validateCommon(name, age, department, position);
        this.id = nextId++;               
        this.name = name.trim();
        this.age = age;
        this.department = department;
        this.position = position.trim();
    }
    
    // Loaded from files to keep track for ids 
    public Employee(int id, String name, int age, Department department, String position) throws ValidationException
    {
        Validator.validateId(id);
        validateCommon(name, age, department, position);
        this.id = id;
        if (id >= nextId) nextId = id + 1; 
        this.name = name.trim();
        this.age = age;
        this.department = department;
        this.position = position.trim();
    }
    
    private static void validateCommon(String name, int age, Department department, String position) throws ValidationException
    {
        Validator.requireNotEmpty(name, "Full Name");
        Validator.validateAge(age);
        if (department == null)
            throw new ValidationException("Please select a department.");
        Validator.requireNotEmpty(position, "Position");
    }
    
    // Getters
    public int getId(){return this.id;}
    public int getAge(){return this.age;}
    public String getName(){return this.name;}
    public String getPosition(){return this.position;}
    public Department getDepartment(){return department;}
    
    //  Setters
    public void setAge(int age) throws ValidationException
    {
        Validator.validateAge(age);
        this.age = age;
    }
    public void setName(String name) throws ValidationException
    {
        Validator.requireNotEmpty(name, "Full Name");
        this.name = name.trim();
    }
    public void setPosition(String position) throws ValidationException
    {
        Validator.requireNotEmpty(position, "Position");
        this.position = position.trim();
    }
    public void setDepartment(Department dep) throws ValidationException
    {
        if (dep == null)
            throw new ValidationException("Please select a department.");
        this.department = dep;
    }
    
    // Abstract classes
    public abstract double calculateSalary(SalaryRates rates);
    public abstract String getType();
    public abstract String toCsv();
    public String commonCsv()
    {
        return this.id+","+this.name+","+this.age+","+department.getId()+","+this.position;
    }
    
            
    
    
}
