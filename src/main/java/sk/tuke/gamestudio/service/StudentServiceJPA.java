package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class StudentServiceJPA {

    @PersistenceContext
    private EntityManager entityManager;


    public void addStudent(Student student) {
        entityManager.persist(student);
    }


    public List<Student> getStudents() {

        List<Student> students = entityManager
                .createQuery("Select s from Student s")
                .getResultList();

        return students;
    }


    public void reset() {
        entityManager.createNativeQuery("DELETE FROM student").executeUpdate();
    }
}

