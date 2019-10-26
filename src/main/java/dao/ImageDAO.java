/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Image;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author roble
 */
public class ImageDAO extends GenericDAO<Image> {
    

    public ImageDAO(){
        super(Image.class);
    }
    
    public List<Image> findAll() {
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        return findResults("Image.findAll", null);
    }
    public Image findById(int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        //first argument is a name given to a named query defined in appropriate entity
        //second argument is map used for parameter substitution.
        //parameters are names starting with : in named queries, :[name]
        //in this case the parameter is named "id and value for it is saved in map
        return findResult("Image.findById", map);
    }
    
    public List<Image> findByName(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResults("Image.findByName", map);
    }
    
    public List<Image> findByFeedid(int feedID) {
        Map<String, Object> map = new HashMap<>();
        map.put("Feed_id", feedID);
        return findResults("Image.findByFeedid", map);
    }
    
    public Image findByPath(String patch) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", patch);
        return findResult("Image.findByPath", map);
    }
    
    public List<Image> findByDate(Date date) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", date);
        return findResults("Image.findByDate", map);
    }
    
    public List<Image> findContaining (String search){
        Map<String, Object> map = new HashMap<>();
        map.put("search", search);
        return findResults( "Image.findContaining", map);
    }
}
