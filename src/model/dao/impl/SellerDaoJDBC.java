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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author helder
 */
public class SellerDaoJDBC implements SellerDao{

    private Connection conn;
    
    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }
    
    //método apra inserção de dados no banco de dados
    @Override
    public void insert(Seller obj) {
        /*
        PreparedStatement st - cria um objeto para armazenar uma instrução sql
        */
        PreparedStatement st = null; 
        
        
        try{
            /*
            digita uma operação sql para inserção de um dado na tabela seller
            e pede para retornar a chave gerada na tabela da nova inserção
            */
            st = conn.prepareStatement("insert into seller "
                + "(Name, Email, BirthDate, BaseSalary, Department) "
                + "values "
                + "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            
            /*
            Estou falando que o terceiro interrogação é para substituir por um date
            gettime() retorna o número em milissegundos desde 1970
            
            */
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            
            //Essa linha executa a instrução sql e retorna o código que se maior
            //que zero foi feita uma inserção com sucesso
            int rowsAffected = st.executeUpdate();
            
            if(rowsAffected > 0){
                
                //esse comando resulta na obtenção da linha que foi inserida
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }else{
                throw new DbException("Unexpected error! No rows affected!");
            }
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            
        }
    }

    @Override
    public void update(Seller obj) {
       /*
        PreparedStatement st - cria um objeto para armazenar uma instrução sql
        */
        PreparedStatement st = null; 
        
        
        try{
            /*
            digita uma operação sql para inserção de um dado na tabela seller
            e pede para retornar a chave gerada na tabela da nova inserção
            */
            st = conn.prepareStatement(
                "update seller "
                + " set Name = ?, Email = ?, BirthDate =?, BaseSalary =?, Department = ? "
                + "where id = ?");
                   
            
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            
            /*
            Estou falando que o terceiro interrogação é para substituir por um date
            gettime() retorna o número em milissegundos desde 1970
            
            */
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());
            

            st.executeUpdate();
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            
        }        
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
        PreparedStatement st = null; // isso é um framework
        ResultSet rs = null; //isso é um framework
        
        try{
            /*
            Inserção de um comando sql
            No caso estamos selecinando todos os campos da tabela seller e o campo
            Name da tabela department que na nova tabela será chamada DepName
            A ligação entre as tabelas seller e department será via o campo Id
            Estou gerando uma nova tabela contendo todos os campos do Seller mais 
            o campo Name da tabela department
            */
            st = conn.prepareStatement(
                    "Select seller.*, department.Name as DepName "
                    +"from seller inner join department "
                    +"on seller.Department = department.Id "
                    + "order by name");

            //Esse resultado vem para mim na forma de tabela e preciso
            //representá-lo na forma de objeto
            rs = st.executeQuery();
            
            /*
            List é uma inteface e a classe ArrayList está implementando essa interface List
            List aceita elementos duplicados
            Cada nó da lista possui o endereço para acessar o próximo nó
            Os tipos de dados contidos na lista tem que ser iguais
            */
            List <Seller> list = new ArrayList<>();
            
            /*
            Map é uma coleção de pares chave/valor, ou seja, todo valor inserido na lista
            tem que inserir uma cahve associada a ele
            Não se admite repetições do objeto chave;
            */
            Map<Integer, Department> map = new HashMap<>();
            

            /*
            Verifica se existe elementos resultantes da execução do sql, 
            se existe ele captura a primeira linha da tabela e retorna true
            */
            while(rs.next()){
                
                /*
                procura um objeto department dentro do map, baseado na chave Department
                que é um campo com valor inteiro, se existir é gerado um objeto
                do tipo department e atribuído a dep, caso contrário retorna null
                Faça isso para não ter de criar vários objeto do tipo Department na
                memória
                */
                Department dep = map.get(rs.getInt("Department"));
                if(dep == null){
                    
                    //chama um método para criar o objeto Department 
                    dep = instantiateDepartment(rs);
                    /*
                    Inser o objeto dep no Map, sendo que a chave é o 
                    indice Department, campo da classe department
                    */
                    map.put(rs.getInt("Department"), dep);
                }
                
                //chama um objeto que cria o objeto Seller
                Seller obj = instantiateSeller(rs, dep);
                
                //adiciona o objeto obj na lista
                list.add(obj);    
            }
            return list;
            
        }catch(SQLException e){
            throw new DbException(e.getMessage()); //fazendo minha mensagem personalizada
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        } 

    }

    //criando o objeto Department
    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Department"));
        dep.setName(rs.getString("DepName"));
        return dep;
                
    }

    //Método para criar o objeto Seller
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

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null; // isso é um framework
        ResultSet rs = null; //isso é um framework
        
        try{
            /*
            Inserção de um comando sql
            No caso estamos selecinando todos os campos da tabela seller e o campo
            Name da tabela department que na nova tabela será chamada DepName
            A ligação entre as tabelas seller e department será via o campo Id
            No caso a linha que quero obter é aquela que estou informando como 
            uma ?
            st.getInt(1,department.getId()) - estou falando que o ? vale o retorno
            do método department.getId();
            Na prática é obtido uma tabela com os dados da tabelas seller  mais 
            o campo Name da tabela department, no qual o campo seller.Department
            for igua ao valor informado.
            */
            st = conn.prepareStatement(
                    "Select seller.*, department.Name as DepName "
                    +"from seller inner join department "
                    +"on seller.Department = department.Id "
                    +"where Department = ? " 
                    + "order by name");
            st.setInt(1, department.getId());

            //Esse resultado vem para mim na forma de tabela e preciso
            //representá-lo na forma de objeto
            rs = st.executeQuery();
            
            /*
            List é uma inteface e a classe ArrayList está implementando essa interface List
            List aceita elementos duplicados
            Cada nó da lista possui o endereço para acessar o próximo nó
            Os tipos de dados contidos na lista tem que ser iguais
            */
            List <Seller> list = new ArrayList<>();
            
            /*
            Map é uma coleção de pares chave/valor, ou seja, todo valor inserido na lista
            tem que inserir uma cahve associada a ele
            Não se admite repetições do objeto chave;
            */
            Map<Integer, Department> map = new HashMap<>();
            

            /*
            Verifica se existe elementos resultantes da execução do sql, 
            se existe ele captura a primeira linha da tabela e retorna true
            */
            while(rs.next()){
                
                /*
                procura um objeto department dentro do map, baseado na chave Department
                que é um campo com valor inteiro, se existir é gerado um objeto
                do tipo department e atribuído a dep, caso contrário retorna null
                Faça isso para não ter de criar vários objeto do tipo Department na
                memória
                */
                Department dep = map.get(rs.getInt("Department"));
                if(dep == null){
                    
                    //chama um método para criar o objeto Department 
                    dep = instantiateDepartment(rs);
                    /*
                    Inser o objeto dep no Map, sendo que a chave é o 
                    indice Department, campo da classe department
                    */
                    map.put(rs.getInt("Department"), dep);
                }
                
                //chama um objeto que cria o objeto Seller
                Seller obj = instantiateSeller(rs, dep);
                
                //adiciona o objeto obj na lista
                list.add(obj);    
            }
            return list;
            
        }catch(SQLException e){
            throw new DbException(e.getMessage()); //fazendo minha mensagem personalizada
        }finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        } 
    }
    
}
