import dao.EmployeeDAO;
import dao.EmployeeDAOImpl;
import model.City;
import model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        final String user = "postgres";
        final String password = "1234";
        final String url = "jdbc:postgresql://localhost:5432/skypro";

        /*try (final Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee WHERE id = (?)")) {
            statement.setInt(1, 6);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String firstName = "First name: " + resultSet.getString("first_name");
                String lastName = "Last name: " + resultSet.getString("last_name");
                String gender = "Gender: " + resultSet.getString("gender");
                int age = resultSet.getInt(5);

                System.out.println(firstName);
                System.out.println(lastName);
                System.out.println(gender);
                System.out.println(age);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            EmployeeDAO employeeDAO = new EmployeeDAOImpl(connection);

            City city = new City(1, "L.N.Tolstoy");
            Employee employee1 = new Employee("Peter", "Kravchenko", "male", 68, city);

            employeeDAO.create(employee1);

            List<Employee> list = new ArrayList<>(employeeDAO.getAllEmployees());

            for (Employee employee : list) {
                System.out.println(employee);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
