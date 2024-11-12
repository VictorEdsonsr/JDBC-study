import db.DB;
import model.dao.FactoryDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SellerDao sellerDao = FactoryDao.createSellerDao();
        Department department = new Department(2, null);

        System.out.println("=== find by id ===");
        Seller sellerid = sellerDao.findById(3);
        System.out.println(sellerid);

        System.out.println("\n=== find by Department ===");
        List<Seller> sellerListByDepartment = sellerDao.findByDepartment(department);

        for(Seller seller : sellerListByDepartment){
            System.out.println(seller);
        }


        System.out.println("\n=== find all ===");
        List<Seller> sellerList = sellerDao.findAll();

        for(Seller seller : sellerList){
            System.out.println(seller);
        }

        System.out.println("\n=== insert ===");
        Seller newSeller = new Seller(null, "victor", "victor@gmail.com", LocalDate.now(), 3000.00, department);
        sellerDao.insert(newSeller);
        System.out.println("Id getted " + newSeller.getId());

        System.out.println("\n=== update ===");
        Seller updateSeller = sellerDao.findById(3);
        updateSeller.setName("thalyta");
        sellerDao.update(updateSeller);
        System.out.println("Seller updated " + updateSeller.getId());


        System.out.println("\n=== delete ===");
        sellerDao.deleteById(10);
        System.out.println("Deleted!!");

    }
}