package dao;

import model.City;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {
    private final Connection connection;
    public EmployeeDAOImpl(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void create(Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO employee (first_name,last_name, gender , age, city_id) VALUES ((?), (?), (?), (?),(?))")) {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, employee.getCity().getCityId());
            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Employee getById(int id) {
        Employee employee = new Employee();
        try (PreparedStatement statement = connection.prepareStatement
                ("SELECT * FROM employee INNER JOIN city ON employee.city_id = city.city_id AND id=(?)")){
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employee.setId(resultSet.getInt("employee_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setGender(resultSet.getString("gender"));
                employee.setAge(resultSet.getInt("age"));
                employee.setCity(new City(resultSet.getInt("city_id"),
                        resultSet.getString("city_name")));
            }
        } catch (SQLException ignored) {
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List <Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM employee LEFT JOIN city ON employee.city_id=city.city_id")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                City city = new City(resultSet.getInt("city_id"),
                        resultSet.getString("city_name"));
                employees.add((new Employee(id, firstName, lastName, gender, age, city)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public void updateById(int id, Employee employee) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE employee SET (first_name = (?), last_name = (?) , gender = (?) , age = (?), city_id = (?)) WHERE id=(?)")) {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getGender());
            statement.setInt(4, employee.getAge());
            statement.setInt(5, id);
            statement.setInt(6, employee.getCity().getCityId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM employee WHERE id=(?)")) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
