package sk.tuke.gamestudio.entity;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "UniqueUserNameAndFullName", columnNames = {"userName", "fullName"})})
public class Player {

    @Id
    @GeneratedValue
    private long ident;

    @Column(nullable = false, length=32, unique = true)
    private String userName;

    @Column(nullable = false, length=128)
    private String fullName;

    @Column(columnDefinition = "INT CHECK(self_evaluation BETWEEN 1 AND 10) NOT NULL")
    private int selfEvaluation;

    @ManyToOne
    @JoinColumn(name = "Country.ident", nullable = false)
    private Country country;

    @ManyToOne
    @JoinColumn(name = "Occupation.ident", nullable = false)
    private Occupation occupation;

    public Player() {
    }

    public Player(String userName, String fullName, int selfEvaluation, Country country, Occupation occupation) {
        this.userName = userName;
        this.fullName = fullName;
        this.selfEvaluation = selfEvaluation;
        this.country = country;
        this.occupation = occupation;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getSelfEvaluation() {
        return selfEvaluation;
    }

    public void setSelfEvaluation(int selfEvaluation) {
        this.selfEvaluation = selfEvaluation;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }

    @Override
    public String toString() {
        return "Player{" +
                "ident=" + ident +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", selfEvaluation=" + selfEvaluation +
                ", country=" + country +
                ", occupation=" + occupation +
                '}';
    }
}
