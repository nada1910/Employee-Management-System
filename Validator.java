import java.util.List;

public class Validator {
    public static void requireNotEmpty(String value, String fieldName) throws ValidationException {
        if(value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " must not be  empty.");
        }
        if(value.contains(",")) {
            throw new ValidationException(fieldName + " must not contain commas.");
        }
    }
    public static void validateId(int id, String fieldName) throws ValidationException {
        if(id <= 0) {
            throw new ValidationException(fieldName + " must be a positive whole number");
        }
    }

    public static void validateDepartment(Department d, List<Department> all) throws ValidationException {
        if(d == null) {
            throw new ValidationException("Please select a department.");
        }
        if(all == null || !all.contains(d)) {
            throw new ValidationException("The selected department does not exist.");
        }
    }
    public static void  validateDepartmentUnique(int id, String name, List<Department> all) throws ValidationException {
        requireNotEmpty(name, "Department Name");
        String trimmed = name.trim();
        if(all != null) {
            for(Department existing: all) {
                if(existing.getName().equalsIgnoreCase(trimmed)) {
                    throw new ValidationException("Department " + trimmed +  " already exists.");
                }
                if(existing.getId() == id) {
                    throw new ValidationException("Department ID " + id +  " already exists.");
                }
            }
        }
    }
}

