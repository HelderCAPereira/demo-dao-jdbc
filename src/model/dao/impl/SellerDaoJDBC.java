/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import model.dao.SellerDao;
import model_entities.Department;
import model_entities.Seller;
import db.DbException;
import db.DB;

/**
 *
 * @author helder
 */
public class SellerDaoJDBC implements SellerDao{

    private Connection conn;
    
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }
    @Override
    public void insert(Seller obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Seller obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null; // isso é um framework
        ResultSet rs = null; //isso é um framework
        
        try{
            /*
            Inserção de um comando sql
            No caso estamos selecinando todos os campos da tabela seller e o campo
            Name da tabela department que na nova tabela será chamada gerada DepName
            A ligação entre as tableas seller e department será via o campo Id
            No caso a linha que quero obter é aquela que estou informando como 
            uma ?
            st.getInt(1,id) - estou falando que o ? vale 1
            */
            st = conn.prepareStatement(
                    "Select seller.*, department.Name as DepName "
                    +"from seller inner join department "
                    +"on seller.Department = department.Id "
                    +"where seller.Id = ?");
            st.setInt(1, id);
            
            //Esse resultado vem para mim na forma de tabela e preciso
            //representá-lo na forma de objeto
            rs = st.executeQuery();
            
            //uso esse if para ver se veio algum dado da minha consulta
            if(rs.next()){
                //Havendo dados retornados eu estou criando um objeto
                //e atribuindo dados para o objeto
                Department dep = instantiateDepartment(rs);
                Seller obj = instantiateSeller(rs, dep);
                return obj;
                
            }else{
                return null;
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage()); //fazendo minha mensagem personalizada
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }


    @Override
    public List<Seller> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Department"));
        dep.setName(rs.getString("DepName"));
        return dep;
                
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller obj = new Seller();
        obj.setId(rs.getInt("Id"));
        obj.setName(rs.getString("Name"));
        obj.setEmail(rs.getString("Email"));
        obj.setBaseSalary(rs.getDouble("BaseSalary"));
        obj.setBirthDate(rs.getDate("BirthDate"));
        obj.setDepartment(dep); //a associação aqui é por objeto e não por campo
        return obj;
    }
    
}
