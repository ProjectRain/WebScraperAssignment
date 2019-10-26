package entity;

import entity.Feed;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-10-25T19:06:51")
@StaticMetamodel(Host.class)
public class Host_ { 

    public static volatile ListAttribute<Host, Feed> feedList;
    public static volatile SingularAttribute<Host, String> name;
    public static volatile SingularAttribute<Host, Integer> id;

}