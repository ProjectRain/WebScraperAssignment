package logic;

import dao.FeedDAO;
import entity.Feed;
import entity.Host;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author roble
 */
public class FeedLogic extends GenericLogic<Feed, FeedDAO>{
         
    public static final String ID = "id";
    public static final String PATH = "path";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String HOST_ID = "hostid";
    public HostLogic hostLogic;

    public FeedLogic() {
        super(new FeedDAO());
        hostLogic = new HostLogic();
    }
    
    @Override
    public List<Feed> getAll(){
        return get(()-> dao().findAll());
    }
    
    @Override
    public Feed getWithId(int id){
        return get(()-> dao().findById(id));
    }    
    
    public List<Feed> getFeedsWithHostID(int hostId){
        return get(()-> dao().findByHostid(hostId));
    }
    
    public Feed getFeedWithPath(String path){
        return get(()-> dao().findByPath(path));
    }
    
    public List<Feed> getFeedsWithType(String type){
        return get(()-> dao().findByType(type));
    }
    
    public List<Feed> getFeedsWithName(String name){
        return get(()-> dao().findByName(name));
    }
    
    @Override
    public List<Feed> search(String search){
        return get(()-> dao().findContaining(search));
    }

    @Override
    public Feed createEntity(Map<String, String[]> parameterMap) {
        //never create another logic within another logic.
        //create a new Entity object
        Feed feed = new Feed();
        
        //ID is generated so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        if(parameterMap.containsKey(ID)){
            //everything in the map is string so it must first be converted to 
            //appropriate type. have in mind also that values are stored in an
            //array of String, almost always the value is at index zero unless
            //you have used duplicated key/name somewhere.
            feed.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        //have in mind also that values are stored in an
        //array of String, almost always the value is at index zero unless
        //you have used duplicated key/name somewhere.
        validateString(parameterMap, PATH, 255);
        feed.setPath(parameterMap.get(PATH)[0]);
        
        validateString(parameterMap, TYPE, 45);
        feed.setType(parameterMap.get(TYPE)[0]);
        
        validateString(parameterMap, NAME, 45);
        feed.setName(parameterMap.get(NAME)[0]);
        
        validateString(parameterMap, HOST_ID, 45);
        Host testHost = hostLogic.getWithId(Integer.parseInt(parameterMap.get(HOST_ID)[0]));
        System.out.println(testHost);
        feed.setHostid(testHost);
        
        return feed;
    } 
    
    /**
     * this method is used to send a list of all names to be used form table
     * column headers. by having all names in one location there is less chance of mistakes.
     * 
     * this list must be in the same order as getColumnCodes and extractDataAsList
     * 
     * @return list of all column display names.
     */
    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Path", "Type", "Name", "Host ID");
    }
    
    /**
     * this method returns a list of column names that match the official column
     * names in the db. by having all names in one location there is less chance of mistakes.
     * 
     * this list must be in the same order as getColumnNames and extractDataAsList
     * 
     * @return list of all column names in DB.
     */
    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, PATH, TYPE, NAME, HOST_ID);
    }

    /**
     * return the list of values of all columns (variables) in given entity.
     * 
     * this list must be in the same order as getColumnNames and getColumnCodes
     * 
     * @param e - given Entity to extract data from.
     * @return list of extracted values
     */
    @Override
    public List<?> extractDataAsList( Feed e) {
        return Arrays.asList(e.getId(), e.getPath(), e.getType(), e.getName(), e.getHostid());
    }
}

