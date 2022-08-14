package sk.tuke.gamestudio.entity;

import javax.persistence.*;


@Entity
public class Student {

    @Id
    @GeneratedValue
    private long ident;
    private String first_name;
    private String last_name;

    @ManyToOne
    @JoinColumn(name = "StudyGroup.ident", nullable = false)
    private StudyGroup studyGroup;

    public Student() {
    }

    public Student(String first_name, String last_name, StudyGroup studyGroup) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.studyGroup = studyGroup;
    }

    public long getIdent() {
        return ident;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    @Override
    public String toString() {
        return "Student{" +
                "ident=" + ident +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", studyGroup=" + studyGroup +
                '}';
    }
}
