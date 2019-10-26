package logic;

import dao.ImageDAO;
import entity.Feed;
import entity.Image;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author roble
 */
public class ImageLogic extends GenericLogic<Image, ImageDAO>{
     
    public static final String ID = "id";
    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String FEED_ID = "feedID";
    public FeedLogic feedLogic;


    public ImageLogic() {
        super(new ImageDAO());
        feedLogic = new FeedLogic();
    }
    
    @Override
    public List<Image> getAll(){
        return get(()-> dao().findAll());
    }
    
    @Override
    public Image getWithId( int id){
        return get(()-> dao().findById(id));
    }
    
    
    public List<Image> getImagesWithFeedId(int feedId){
        return get(()-> dao().findByFeedid(feedId));
    }
    
    public List<Image> getImagesWithName(String name){
        return get(()-> dao().findByName(name));
    }
    
    public Image getImageWithPath(String path){
        return get(()-> dao().findByPath(path));
    }
    
    public List<Image> getImagesWithDate(Date date){
        return get(()-> dao().findByDate(date));
    }
    
    @Override
    public List<Image> search( String search){
        return get(()-> dao().findContaining(search));
    }

    @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        //never create another logic within another logic.
        //create a new Entity object
        Image image = new Image();
        
        //ID is generated so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        if(parameterMap.containsKey(ID)){
            //everything in the map is string so it must first be converted to 
            //appropriate type. have in mind also that values are stored in an
            //array of String, almost always the value is at index zero unless
            //you have used duplicated key/name somewhere.
            image.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        //have in mind also that values are stored in an
        //array of String, almost always the value is at index zero unless
        //you have used duplicated key/name somewhere.
        validateString(parameterMap, NAME, 255);
        image.setName(parameterMap.get(NAME)[0]); 
        
        validateString(parameterMap, PATH, 255);
        image.setPath(parameterMap.get(PATH)[0]);
             
        validateString(parameterMap, FEED_ID, 45);
        int feedId = Integer.parseInt(parameterMap.get(FEED_ID)[0]);
        Feed imageFeed = feedLogic.getWithId(feedId);
        image.setFeedid(imageFeed);
        
        image.setDate(new Date(Long.parseLong(parameterMap.get(DATE)[0])));
        return image;
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
        return Arrays.asList("ID", "Path", "Name", "Date", "Feed ID");
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
        return Arrays.asList(ID, PATH, NAME, DATE, FEED_ID);
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
    public List<?> extractDataAsList( Image e) {
        return Arrays.asList(e.getId(), e.getPath(), e.getName(), e.getDate(), e.getFeedid());
    }
}

