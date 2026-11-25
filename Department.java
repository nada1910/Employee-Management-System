public class Department implements DepartmentNames {
    final private int id;
    private String name;

    public Department(int id, String name) throws ValidationException {
        Validator.validateId(id, "ID");
        Validator.requireNotEmpty(name, "Department Name");
        this.id = id;
        this.name = name.trim();
    }

    public int getId() {
        return this.id;
    }
    @Override
    public String getName() {
        return this.name;
    }
    public void setName(String name) throws ValidationException {
        Validator.requireNotEmpty(name, "Department Name");
        this.name = name.trim();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Department)) {
            return false;
        }
        Department other = (Department) o;
        return this.id == other.id;
    }
    public int hashCode() {
        return Integer.hashCode(this.id);
    }
    public String toString() {
        return this.name;
    }
}
