package scraper;

import common.FileUtility;
import entity.Feed;
import entity.Image;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import logic.FeedLogic;
import logic.ImageLogic;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;

/**
 * a wrapper class for jraw library to access Reddit API. visit this site for
 * more info about jraw: https://mattbdean.gitbooks.io/jraw/
 *
 * @author Shariar
 */
public class Scraper {

    /**
     * you need to initialize the next three variables based on the given
     * instructions
     */
    private static final String CLIENT_ID = "VEoJcYs1PgJDtQ";
    private static final String CLIENT_SECRET = "ktfEViayTRI5YqSlcZ60VDr8nCg";
    private static final String REDDIT_USER = "cst8288nur00068";

    /**
     * replace yourname with your name in the text below, no spaces and
     * lowercase
     */
    private static final String APPID = "cst8288nur00068";

    private static final String PLATFORM = "ScraperWebBot";
    private static final String VERSION = "v0.1";

    private RedditClient reddit;
    private DefaultPaginator<Submission> paginator;
    private List<Listing<Submission>> pages;
    private Listing<Submission> currentPage;
    private static ImageLogic imageLogic = new ImageLogic();
    private static FeedLogic feedLogic = new FeedLogic();
    private static Map scraperMap = new HashMap<String, Scraper>();
    
    

    private void hasAuthenticated() {
        if (reddit == null) {
            throw new IllegalStateException("authenticate() Method in "
                    + "Scrapper must be called ONCE before everything "
                    + "regarding Scraper");
        }
    }

    private void hasPagesBeenConfiged() {
        if (paginator == null) {
            throw new IllegalStateException("buildRedditPagesConfig() Method "
                    + "in Scrapper must be called ONCE before requestNumberOfPages()");
        }
    }

    private void hasPagesBeenRequested() {
        if (pages == null) {
            throw new IllegalStateException("requestNumberOfPages() Method "
                    + "in Scrapper must be called ONCE before proccessPageNumber()");
        }
    }

    private void hasNextPageRequested() {
        if (currentPage == null) {
            throw new IllegalStateException("requestNextPage() Method "
                    + "in Scrapper must be called ONCE before proccessNextPage()");
        }
    }

    /**
     * get permission from Reddit to access their API
     *
     * @return current object of Scraper, this
     */
    public Scraper authenticate() {
        UserAgent userAgent = new UserAgent(PLATFORM, APPID, VERSION, REDDIT_USER);
        Credentials credentials = Credentials.userless(CLIENT_ID, CLIENT_SECRET, UUID.randomUUID());
        NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);
        reddit = OAuthHelper.automatic(adapter, credentials);
        return this;
    }

    /**
     * configure what subreddit to be downloaded
     *
     * @param subreddit - name of subreddit to access
     * @param postsPerPage - number of post per page to download, max 10
     * @param sort - in what order to sort the posts, ex. Sort.HOT
     * @return current object of Scraper, this
     */
    public Scraper buildRedditPagesConfig(String subreddit, int postsPerPage, Sort sort) {
        hasAuthenticated();

        paginator = reddit
                .subreddit(subreddit)
                .posts()
                .limit(postsPerPage)
                .sorting(sort.value())
                .build();
        return this;
    }

    /**
     * get the next Reddit page. first page if next hasn't been called yet.
     *
     * @return current object of Scraper, this
     */
    public Scraper requestNextPage() {
        hasAuthenticated();
        hasPagesBeenConfiged();

        currentPage = paginator.next();
        return this;
    }

    /**
     * request number of pages to be downloaded. do not go for a high number,
     * max 5
     *
     * @param totalPages - number of pages to download, max 5
     * @return current object of Scraper, this
     */
    @Deprecated
    public Scraper requestNumberOfPages(int totalPages) {
        hasAuthenticated();
        hasPagesBeenConfiged();

        pages = paginator.accumulate(totalPages);
        return this;
    }

    /**
     * start processing each page using the callback lambda. callback lambda
     * uses the Post class to access data in each post. this lambda will be
     * called for every single post one at a time.
     *
     * @param callback - callback lambda of type Post class
     * @return current object of Scraper, this
     */
    public Scraper proccessNextPage(Consumer<Post> callback) {
        hasAuthenticated();
        hasPagesBeenConfiged();
        hasNextPageRequested();

        currentPage.forEach(submission -> {
            callback.accept(new Post(submission));
        });
        return this;
    }

    /**
     * start processing each page using the callback lambda. callback lambda
     * uses the Post class to access data in each post. this lambda will be
     * called for every single post one at a time.
     *
     * @param pageNumber - page number to process, -1 for all
     * @param callback - callback lambda of type Post class
     * @return current object of Scraper, this
     */
    @Deprecated
    public Scraper proccessPageNumber(int pageNumber, Consumer<Post> callback) {
        hasAuthenticated();
        hasPagesBeenConfiged();
        hasPagesBeenRequested();

        int start = pageNumber == -1 ? 0 : pageNumber;
        int end = pageNumber == -1 ? pages.size() : pageNumber + 1;

        for (int i = start; i < end; i++) {
            Listing<Submission> firstPage = pages.get(pageNumber);
            firstPage.forEach(submission -> {
                callback.accept(new Post(submission));
            });
        }
        return this;
    }

    /**
     * old style do not use.
     * @throws IOException 
     */
    @Deprecated
    private static void exampleForReadingManyPages() throws IOException {
        FileUtility.createDirectory("img/");

        //example of how to download the first 30 hot images from Wallpaper subreddit
        Scraper scrap = new Scraper();
        scrap.authenticate()
                .buildRedditPagesConfig("Wallpaper", 30, Sort.HOT)
                .requestNumberOfPages(1)
                .proccessPageNumber(0, (Post post) -> {
                    if (post.isImage() && !post.isOver18()) {
                        String path = post.getUrl();
                        FileUtility.downloadAndSaveFile(path, "img");
                    }
                });
    }
    
    public void scrape() throws IOException{
        String savePath = new JFileChooser().getFileSystemView().getDefaultDirectory().toString()+"/Reddit Images";
        FileUtility.createDirectory(savePath);
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

        //create a lambda that accepts post
        Consumer<Post> saveImage = (Post post) -> {
            //if post is an image and SFW
            
            List<Image> existingImages = imageLogic.getImagesWithName(post.getTitle());
            if (post.isImage() && !post.isOver18() && existingImages.isEmpty()) {
                //get the path for the image which is unique
                String path = post.getUrl();
                //save it in img directory
                String outputPath = FileUtility.downloadAndSaveFile(path, savePath);
                
                if(outputPath != null && !outputPath.isEmpty()){
                    Map<String, String[]> parameterMap = new HashMap<String, String[]>();

                    System.out.println(post.getUrl());
                    List<Feed> feeds = feedLogic.getFeedsWithName(post.getSubreddit());

                    String[] feedIdValue = {Integer.toString(feeds.get(0).getId())};
                    parameterMap.put(ImageLogic.FEED_ID, feedIdValue);

                    String[] nameValue = {post.getTitle()};
                    parameterMap.put(ImageLogic.NAME, nameValue);

                    String[] postValue = {outputPath};
                    parameterMap.put(ImageLogic.PATH, postValue);

                    Date dt = new Date();
                    String[] dateValue = {Long.toString(dt.getTime())};
                    parameterMap.put(ImageLogic.DATE, dateValue);

                    Image image = imageLogic.createEntity(parameterMap);
                    imageLogic.add(image);
                }
            }
        };

        List<Feed> feeds = feedLogic.getAll();
        //create a new scraper
        for(Feed feed : feeds){
            Scraper newScraper;
            if(!scraperMap.containsKey(feed.getName())){
                Scraper scrap = new Scraper();
                newScraper = scrap.authenticate().buildRedditPagesConfig(feed.getName(), 5, Sort.HOT);
                scraperMap.put(feed.getName(), newScraper);
            }else{
                newScraper = (Scraper)scraperMap.get(feed.getName());
            }
            //get the next page 1 times and save the images.
            newScraper.requestNextPage().proccessNextPage(saveImage);
//            newScraper.requestNextPage().proccessNextPage(saveImage);
        }
        //create a new scraper
        //Scraper scrape = new Scraper();
        //authenticate and set up a page for wallpaper subreddit with 5 posts soreted by HOT order
        
    }
    /**
     * use this example for your code.
     * @throws IOException 
     */
    private static void exampleForReadingNextPage() throws IOException {
        //create a directory in your project called img
        FileUtility.createDirectory("img/");
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //create a lambda that accepts post
        Consumer<Post> saveImage = (Post post) -> {
            //if post is an image and SFW
            if (post.isImage() && !post.isOver18()) {
                //get the path for the image which is unique
                String path = post.getUrl();
                //save it in img directory
                FileUtility.downloadAndSaveFile(path, "img");
                Map<String, String[]> parameterMap = new HashMap<String, String[]>();
                
                List<Feed> feeds = feedLogic.getFeedsWithName(post.getSubreddit());
                
                String[] feedIdValue = {Integer.toString(feeds.get(0).getId())};
                parameterMap.put(ImageLogic.FEED_ID, feedIdValue);
                
                String[] nameValue = {post.getTitle()};
                parameterMap.put(ImageLogic.NAME, nameValue);
                
                String[] postValue = {path};
                parameterMap.put(ImageLogic.PATH, postValue);
                
                Date dt = new Date();
                String[] dateValue = {sdf.format(dt)};
                parameterMap.put(ImageLogic.DATE, postValue);
//                        
//                Image image = imageLogic.createEntity(parameterMap);
//                imageLogic.add(image);
            }
        };

        //create a new scraper
        Scraper scrap = new Scraper();
        //authenticate and set up a page for wallpaper subreddit with 5 posts soreted by HOT order
        scrap.authenticate().buildRedditPagesConfig("Wallpaper", 5, Sort.HOT);
        //get the next page 3 times and save the images.
        scrap.requestNextPage().proccessNextPage(saveImage);
        scrap.requestNextPage().proccessNextPage(saveImage);
        scrap.requestNextPage().proccessNextPage(saveImage);
    }

    /**
     * only run this for testing the scraper it will not run the project.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        exampleForReadingNextPage();
    }
}
