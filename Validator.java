public class Validator {
    
    public static int parseInt(String text, String fieldName) throws ValidationException
    {
        if (text == null || text.trim().isEmpty())
            throw new ValidationException(fieldName + " is required.");
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a whole number.");
        }
    }
    
    public static double parseDouble(String text, String fieldName) throws ValidationException
    {
        if (text == null || text.trim().isEmpty())
            throw new ValidationException(fieldName + " is required.");
        try {
            double value = Double.parseDouble(text.trim());
            if (Double.isNaN(value) || Double.isInfinite(value))
                throw new ValidationException(fieldName + " must be a valid number.");
            return value;
        } catch (NumberFormatException e) {
            throw new ValidationException(fieldName + " must be a valid number.");
        }
    }
    
    public static void requireNotEmpty(String value, String fieldName) throws ValidationException
    {
        if (value == null || value.trim().isEmpty())
            throw new ValidationException(fieldName + " must not be empty.");
        if (value.contains(","))
            throw new ValidationException(fieldName + " must not contain commas.");
    }
    
    public static void validateId(int id) throws ValidationException
    {
        if (id <= 0)
            throw new ValidationException("Employee ID must be a positive whole number.");
    }
    
    // Age must be > 0
    public static void validateAge(int age) throws ValidationException
    {
        if (age <= 20)
            throw new ValidationException("Age must be greater than 20.");
    }
    
    public static void validateNonNegative(double v, String fieldName) throws ValidationException
    {
        if (v < 0)
            throw new ValidationException(fieldName + " must not be negative.");
    }
    
    public static void validateType(String type) throws ValidationException
    {
        if (!"Full-Time".equals(type) && !"Part-Time".equals(type))
            throw new ValidationException("Employee type must be Full-Time or Part-Time.");
    }
    
    public static void validateDepartment(Department dep, List<Department> known) throws ValidationException
    {
        if (dep == null)
            throw new ValidationException("Please select a department.");
        if (known == null || !known.contains(dep))
            throw new ValidationException("The selected department does not exist.");
    }
    
    public static void validateEmployee(Employee e) throws ValidationException
    {
        if (e == null)
            throw new ValidationException("Employee must not be null.");

        validateId(e.getId());
        requireNotEmpty(e.getName(), "Full Name");
        validateAge(e.getAge());
        requireNotEmpty(e.getPosition(), "Position");
        validateType(e.getType());

        if (e instanceof FullTimeEmployee)
            validateNonNegative(((FullTimeEmployee) e).getBonus(), "Bonus");
        else if (e instanceof PartTimeEmployee)
            validateNonNegative(((PartTimeEmployee) e).getHoursWorked(), "Hours Worked");
    }
    
}