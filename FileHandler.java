import java.io.*;
import java.nio.file.*;
import java.util.*;
public class FileHandler{
    private static final String DATA_DIR = "data";
    private static final String EMP_FILE = DATA_DIR + "/employees.txt";
    private static final String DEPT_FILE = DATA_DIR +"/departments.txt";

    public void ensureDataFolder() throws IOException{
        Files.createDirectories(Paths.get(DATA_DIR));
    }

    public void saveDepartments(List<Department> depts, SalaryRates rates) throws ValidationException{
        try{
                ensureDataFolder();
            try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(DEPT_FILE))){
                for (Department dept : depts) {
                    writer.write(String.valueOf(dept.getId())+",");
                    writer.write(dept.getName()+",");
                    writer.write(String.valueOf(rates.getFullTimeSalary(dept)) + ",");
                    writer.write(String.valueOf(rates.getHourlyRate(dept)));  
                    writer.newLine();
                }
            }
        }    
            catch (IOException e) {
                throw new ValidationException("Error saving departments: " + e.getMessage());
            }
    }

    public List<Employee> loadEmployees(List<Department> depts) throws ValidationException {
        List<Employee> employees = new ArrayList<>();

        if (!Files.exists(Paths.get(EMP_FILE))) {
            return employees;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(EMP_FILE));

            for (String line : lines) {
                Employee emp = parseLine(line, depts);

                if (emp != null) {
                    employees.add(emp);
                }
            }

        } 
        catch (IOException e) {
            throw new ValidationException("Error loading employees: " + e.getMessage());
        }
        return employees;
    }    
   
    public void saveEmployees(List<Employee> list) throws ValidationException{
        try{
            ensureDataFolder();
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(EMP_FILE))) {
                for (Employee emp : list) {
                    writer.write(emp.toCsv());
                    writer.newLine();
                }
            }
        }
            catch (IOException e) {
                throw new ValidationException("Error saving employees: " + e.getMessage());
            }
    }    

    public List<Department> loadDepartments(SalaryRates rates) throws ValidationException {
        List<Department> departments = new ArrayList<>();

        if (!Files.exists(Paths.get(DEPT_FILE))) {
            return departments;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(DEPT_FILE));

            for (String line : lines) {
                try {
                    String[] data = line.split(",", -1);

                    if (data.length != 4) {
                        continue;
                    }

                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    double monthlySalary = Double.parseDouble(data[2]);
                    double hourlyRate = Double.parseDouble(data[3]);
                    Department dept = new Department(id, name);
                    rates.setFullTimeSalary(dept, monthlySalary);
                    rates.setHourlyRate(dept, hourlyRate);
                    departments.add(dept);
                } 
                catch (NumberFormatException e) {
                    continue;
                }
            }

        } 
        catch (IOException e) {
            throw new ValidationException("Error loading departments: " + e.getMessage());
        }

        return departments;
    }    

    private Employee parseLine(String line, List<Department> depts) {
        try {
            String[] data = line.split(",",-1);

            if (data.length != 7) {
                return null;
            }

            int id = Integer.parseInt(data[1]);
            String fullName = data[2];
            int age = Integer.parseInt(data[3]);
            int deptId = Integer.parseInt(data[4]);
            String position = data[5];
            double value = Double.parseDouble(data[6]);

            Department department = null;

            for (Department dept : depts) {
                if (dept.getId() == deptId) {
                    department = dept;
                    break;
                }
            }

            if (!data[0].equals("FULL") && !data[0].equals("PART")) {
                return null;
            }

            if (department == null) {
                return null;
            }

            if (data[0].equals("FULL")) {
                return new FullTimeEmployee(id, fullName, age, department, position, value);
            }

            if (data[0].equals("PART")) {
                return new PartTimeEmployee(id, fullName, age, department, position, value);
            }

            return null;

        } catch (NumberFormatException e) {
            return null;         //Check whether your Employee, FullTimeEmployee, and PartTimeEmployee constructors declare throws ValidationException.
        }
    }
}