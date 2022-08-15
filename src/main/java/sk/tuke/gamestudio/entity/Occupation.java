package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Occupation {

    //primarny kluc
    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length=32, unique = true)
    private String occupation;

    @OneToMany(mappedBy = "ident")
    private List<Player> players;

    public Occupation() {
    }

    public Occupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "ident=" + ident +
                ", occupation='" + occupation + '\'' +
                '}';
    }
}
