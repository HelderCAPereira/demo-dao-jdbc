/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.Date;
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
    }  
    
}
