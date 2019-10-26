package logic;

import dao.AccountDAO;
import entity.Account;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author roble
 */
public class AccountLogic extends GenericLogic<Account, AccountDAO>{
    
    public static final String DISPLAY_NAME = "displayName";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String ID = "id";


    public AccountLogic() {
        super(new AccountDAO());
    }
    
    @Override
    public List<Account> getAll(){
        return get(()-> dao().findAll());
    }
    
    @Override
    public Account getWithId( int id){
        return get(()-> dao().findById(id));
    }
    
    
    public Account getAccountWithDisplayNamed( String displayName){
        return get(()-> dao().findByDisplayName(displayName));
    }
    
    public Account getAccountWithUser( String user){
        return get(()-> dao().findByUser(user));
    }
    
    public List<Account> getAccountsWithPassword( String password){
        return get(()-> dao().findByPassword(password));
    }
    
    public Account getAccountWith( String username, String password){
        return get(()-> dao().validateUser(username, password));
    }
    
    @Override
    public List<Account> search( String search){
        return get(()-> dao().findContaining(search));
    }

    @Override
    public Account createEntity(Map<String, String[]> parameterMap) {
        //never create another logic within another logic.
        //create a new Entity object
        Account account = new Account();
        
        //ID is generated so if it exists add it to the entity object
        //otherwise it does not matter as mysql will create an if for it.
        if(parameterMap.containsKey(ID)){
            //everything in the map is string so it must first be converted to 
            //appropriate type. have in mind also that values are stored in an
            //array of String, almost always the value is at index zero unless
            //you have used duplicated key/name somewhere.
            account.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }
        //have in mind also that values are stored in an
        //array of String, almost always the value is at index zero unless
        //you have used duplicated key/name somewhere.
        validateString(parameterMap, DISPLAY_NAME, 45);
        account.setDisplayName(parameterMap.get(DISPLAY_NAME)[0]);
        
        validateString(parameterMap, USER, 45);
        account.setUser(parameterMap.get(USER)[0]);
        
        validateString(parameterMap, PASSWORD, 45);
        account.setPassword(parameterMap.get(PASSWORD)[0]);
        
        
        return account;
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
        return Arrays.asList("ID", "Display Name", "User", "Password");
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
        return Arrays.asList(ID, DISPLAY_NAME, USER, PASSWORD);
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
    public List<?> extractDataAsList( Account e) {
        return Arrays.asList(e.getId(), e.getDisplayName(), e.getUser(), e.getPassword());
    }
}
