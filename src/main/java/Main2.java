import model.dao.DepartmentDao;
import model.dao.FactoryDao;

import model.entities.Department;

import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        DepartmentDao departmentDao = FactoryDao.createDepartmentDao();

        System.out.println("=== find by id ===");
        Department departmentById = departmentDao.findById(2);
        System.out.println(departmentById);

        System.out.println("\n=== find all ===");
        List<Department> departmentList = departmentDao.findAll();
        for (Department department : departmentList){
            System.out.println(department);
        }

        System.out.println("\n=== insert ===");
        Department newDepartment = new Department(null, "Food");
        departmentDao.insert(newDepartment);
        System.out.println("Id get " + newDepartment.getId());

        System.out.println("\n=== update ===");
        Department updated = departmentDao.findById(2);
        updated.setName("videogames");
        departmentDao.update(updated);
        System.out.println("updated " + updated.getName());

        System.out.println("\n=== deleted ===");
        departmentDao.deleteById(6);
        System.out.println("deleted!!");
    }
}