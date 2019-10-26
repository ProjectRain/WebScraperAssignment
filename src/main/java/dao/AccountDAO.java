/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Account;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author roble
 */
public class AccountDAO extends GenericDAO<Account> {
         

    public AccountDAO(){
        super(Account.class);
    }
    
    public List<Account> findAll() {
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults("Account.findAll", null);
    }
    public Account findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id and value for it is saved in map
        return findResult("Account.findById", map);
    }
    
    public Account findByDisplayName(String displayName) {
        Map<String, Object> map = new HashMap<>();
        map.put("displayName", displayName);
        return findResult("Account.findByDisplayName", map);
    }
    
    public Account findByUser(String user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        return findResult("Account.findByUser", map);
    }
    
      public List<Account> findByPassword(String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("password", password);
        return findResults("Account.findByPassword", map);
    }
    
    public List<Account> findContaining(String search){
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults( "Account.findContaining", map);
    }
    
    public Account validateUser(String username, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return findResult("Account.validateUser", map);
    }
}
