package ama;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 *
 * @author ama
 */
public class GameListener {

    @PersistenceContext
    EntityManager em;

    @PrePersist
    public void prePersist(Game object) {
        System.out.println("prepersist" + object);
        object.setUpdateby(object.getUpdateby() + ":" + "prePersist");
    }

    @PostPersist
    public void postPersist(Game object) {
        System.out.println("postpersist" + object);
        object.setUpdateby(object.getUpdateby() + ":" + "postPersist");
    }

    @PreUpdate
    public void preUpdate(Game object) {
        System.out.println("preUpdate" + object);
        object.setUpdateby(object.getUpdateby() + ":" + "preUpdate");
    }

    @PostUpdate
    public void postUpdate(Game object) {
        System.out.println("postUpdate" + object);
        object.setUpdateby(object.getUpdateby() + ":" + "postUpdate");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("preDestroy");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("postConstruct");
    }
}
