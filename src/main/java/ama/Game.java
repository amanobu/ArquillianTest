package ama;

import java.io.Serializable;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author ama
 */
@Entity
@EntityListeners({GameListener.class})
public class Game implements Serializable {

    private Long id;
    private String title;
    private String updateby;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Size(min = 0, max = 100)
    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(min = 3, max = 50)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Game(String title) {
        this.title = title;
    }

    public Game() {
    }

    @Override
    public String toString() {
        return "Game@" + hashCode() + "[id = " + id + "; title = " + title + "; updateby = " + updateby + "]";
    }
    

}
