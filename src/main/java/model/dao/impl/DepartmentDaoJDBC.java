package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }


    @Override
    public void insert(Department department) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = conn.prepareStatement("INSERT INTO department " +
                    "(Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, department.getName());

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                resultSet = statement.getGeneratedKeys();

                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    department.setId(id);
                }
            }
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            statement.setString(1, department.getName());
            statement.setInt(2,department.getId());

            statement.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatment(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;
        try{
            statement = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            statement.setInt(1, id);

            statement.executeUpdate();

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatment(statement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = conn.prepareStatement("SELECT * FROM department " +
                    "WHERE Id = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                return instantianteDepartment(resultSet);
            }
            return null;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = conn.prepareStatement("SELECT * FROM department");
            resultSet = statement.executeQuery();

            List<Department> departmentList = new ArrayList<>();

            while(resultSet.next()){
                Department department = instantianteDepartment(resultSet);
                departmentList.add(department);
            }

            return departmentList;
        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    private Department instantianteDepartment(ResultSet resultSet) throws SQLException{
        Department department = new Department();
        department.setId(resultSet.getInt("Id"));
        department.setName(resultSet.getString("Name"));

        return department;
    }
}
