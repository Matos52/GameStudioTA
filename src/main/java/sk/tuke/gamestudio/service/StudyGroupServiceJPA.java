package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Student;
import sk.tuke.gamestudio.entity.StudyGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class StudyGroupServiceJPA {

    @PersistenceContext
    private EntityManager entityManager;


    public void addStudyGroup(StudyGroup studyGroup) {
        entityManager.persist(studyGroup);
    }


    public List<StudyGroup> getStudyGroups() {

        List<StudyGroup> studyGroups = entityManager
                .createQuery("Select g from StudyGroup g")
                .getResultList();

        return studyGroups;
    }


    public void reset() {
        entityManager.createNativeQuery("DELETE FROM study_group").executeUpdate();
    }
}
