/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model_entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author helder
 */

/*implemento Serializable se eu quiser enviar meu objeto via rede
ou gravar em arquivo. Essa interface Serializable é apenas uma marcação
pois não existe nenhum método para ser implementado, serve apenas para que
a JVM saiba que aquela determinada Classe está hábil para ser serializada
*/
public class Department implements Serializable{
    private Integer id;
    private String name;
    
    public Department(){
        
    }

    public Department(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Para comparar os objetos por conteúdo e não por endereço
    @Override //implementação de um método da superclasse na subclasse
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    //Para comparar os objetos por conteúdo e não por endereço
    @Override //implementação de um método da superclasse na subclasse
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Department other = (Department) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Department{" + "id=" + id + ", name=" + name + '}';
    }
    
    
    
    
}
