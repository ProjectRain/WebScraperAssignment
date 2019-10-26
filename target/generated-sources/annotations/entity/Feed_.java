package entity;

import entity.Host;
import entity.Image;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-10-25T19:06:51")
@StaticMetamodel(Feed.class)
public class Feed_ { 

    public static volatile SingularAttribute<Feed, String> path;
    public static volatile SingularAttribute<Feed, String> name;
    public static volatile SingularAttribute<Feed, Host> hostid;
    public static volatile SingularAttribute<Feed, Integer> id;
    public static volatile SingularAttribute<Feed, String> type;
    public static volatile ListAttribute<Feed, Image> imageList;

}