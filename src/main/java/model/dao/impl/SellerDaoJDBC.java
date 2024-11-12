package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = conn.prepareStatement("INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, Date.valueOf(seller.getBirthDate()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = statement.executeUpdate();

            if(rowsAffected > 0){
                resultSet = statement.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("UPDATE seller " +
                    "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                    "WHERE Id = ?");

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, Date.valueOf(seller.getBirthDate()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());


            statement.setInt(6, seller.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatment(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement("DELETE FROM seller " +
                    "WHERE Id = ?");

            statement.setInt(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatment(statement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE seller.Id = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next()){
                Department department = instantianteDepartment(resultSet);
                Seller seller = instantianteSeller(department,resultSet);
                return seller;
            }

            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            statement = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "WHERE DepartmentId = ? " +
                    "ORDER BY Name");
            statement.setInt(1, department.getId());
            resultSet = statement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while(resultSet.next()){
                Department departmentInstantiate = departmentMap.get(resultSet.getInt("DepartmentId"));

                if(departmentInstantiate == null){
                    departmentInstantiate = instantianteDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), departmentInstantiate);
                }

                Seller seller = instantianteSeller(departmentInstantiate,resultSet);
                sellerList.add(seller);
            }

            return sellerList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try{
            statement = conn.prepareStatement("SELECT seller.*,department.Name as DepName " +
                    "FROM seller INNER JOIN department " +
                    "ON seller.DepartmentId = department.Id " +
                    "ORDER BY Name");

            resultSet = statement.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while(resultSet.next()){
                Department departmentInstantiate = departmentMap.get(resultSet.getInt("DepartmentId"));

                if(departmentInstantiate == null){
                    departmentInstantiate = instantianteDepartment(resultSet);
                    departmentMap.put(resultSet.getInt("DepartmentId"), departmentInstantiate);
                }

                Seller seller = instantianteSeller(departmentInstantiate,resultSet);
                sellerList.add(seller);
            }

            return sellerList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatment(statement);
        }
    }

    private Department instantianteDepartment(ResultSet resultSet) throws SQLException{
        Department department =  new Department();
        department.setName(resultSet.getString("DepName"));
        department.setId(resultSet.getInt("DepartmentId"));

        return department;
    }

    private Seller instantianteSeller(Department department, ResultSet resultSet) throws SQLException{
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
        seller.setDepartment(department);

        return seller;
    }
}
