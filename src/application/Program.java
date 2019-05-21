/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.Date;
import java.util.List;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model_entities.Department;
import model_entities.Seller;
import model.dao.SellerDao;

/**
 *
 * @author helder
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Dessa forma meu programa não conhece a implementação, conhece apenas a 
        //interface
        SellerDao sellerDao = DaoFactory.createSellerDao();
        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);
        
        System.out.println("=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for(Seller obj : list){
            System.out.println(obj);
        }
        
        System.out.println("=== TEST 3: seller findALL ===");
        list = sellerDao.findAll();
        for(Seller obj : list){
            System.out.println(obj);
        }
        
        System.out.println("=== TEST 4: seller insert ===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());
        
        System.out.println("=== TEST 5: seller udate ===");
        seller = sellerDao.findById(2);
        seller.setName("Martha Waine");
        sellerDao.update(seller);
        System.out.println("Update Completed");
        
        System.out.println("=== TEST 6: seller delete ===");
        sellerDao.deleteById(20);
        System.out.println("Delete Completed");
    }  
    
}
