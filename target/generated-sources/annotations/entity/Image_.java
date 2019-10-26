package entity;

import entity.Feed;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-10-25T19:06:51")
@StaticMetamodel(Image.class)
public class Image_ { 

    public static volatile SingularAttribute<Image, Date> date;
    public static volatile SingularAttribute<Image, String> path;
    public static volatile SingularAttribute<Image, Feed> feedid;
    public static volatile SingularAttribute<Image, String> name;
    public static volatile SingularAttribute<Image, Integer> id;

}