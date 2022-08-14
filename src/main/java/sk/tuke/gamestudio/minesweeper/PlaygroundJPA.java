package sk.tuke.gamestudio.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.entity.*;
import sk.tuke.gamestudio.service.StudentServiceJPA;
import sk.tuke.gamestudio.service.StudyGroupServiceJPA;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
public class PlaygroundJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StudentServiceJPA studentServiceJPA;
    @Autowired
    private StudyGroupServiceJPA studyGroupServiceJPA;

    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    Pattern MENO = Pattern.compile("([A-Za-z]*)");

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

//Definicia pre studentJPA a studyGroupJPA od mojich kolegov
    //V tejto verzii nepotrebujem pouzivat entityManager lebo volam servicy priamo a v prislusnych servicoch mam managerov
    //inicializovanych, kazdy manager ma svojho, tym padom nepotrebujem ani @Transactional

    public void addNewStudyGroup() {
        System.out.println("Do you want to add a new study group? (Y) or (N)");
        String choice = readLine();
        if(choice.toLowerCase().equals("y")) {
            System.out.print("Study group name: ");
            String studyGroupName = readLine();
            try {
                studyGroupServiceJPA.addStudyGroup(new StudyGroup(studyGroupName));
            } catch(Exception e) {
                System.err.println("Problem with writing to the database.\n" +e.getMessage());
            }
        }
    }

    public void addNewStudent() {
        System.out.println("Enter a first name: ");
        String firstName = readLine();
        System.out.println("Enter a last name: ");
        String lastName = readLine();
        try {
            List<StudyGroup> studyGroups = studyGroupServiceJPA.getStudyGroups();
            System.out.println("Study groups: ");
            int noOfStudyGroups = studyGroups.size();
            for (int i = 0; i < noOfStudyGroups; i++) {
                System.out.println(i+ " " +studyGroups.get(i));
            }
            System.out.println("Group id: ");
            int group = Integer.parseInt(readLine());
            try {
                studentServiceJPA.addStudent(new Student(firstName, lastName, studyGroups.get(group)));
            } catch (Exception e) {
                System.err.println("Problem with writing to the database.\n" +e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Problem with writing to the database.\n" +e.getMessage());
        }
    }

    public void getAllStudents() {
        try {
            List<Student> students = studentServiceJPA.getStudents();
            System.out.println(students);
            int noOfStudents = students.size();
            for (int i = 0; i < noOfStudents; i++) {
                System.out.printf("%2d. %10s %10s -> %15s\n",
                        students.get(i).getIdent(),
                        students.get(i).getFirst_name(),
                        students.get(i).getLast_name(),
                        students.get(i).getStudyGroup());
            }
        } catch(Exception e) {
            System.err.println("Problem with reading from the database.\n" +e.getMessage());
        }
    }
    public void play() {
        System.out.println("Opening JPA playground");

        addNewStudyGroup();

        addNewStudent();

        getAllStudents();

        System.out.println("Closing JPA playground");
    }
}
    //Moja definicia pre studentJPA a studyGroupJPA
        //V tejto verzii pouzivam entityManager
/*
    public boolean firstName(String firstName) {
        Matcher matcher = MENO.matcher(firstName);
        if (matcher.matches()) {
            return true;
        }
        System.out.println("Nespravne zadanie, skus este raz");
        return false;
    }

    public boolean lastName(String lastName) {
//        Matcher matcher = MENO.matcher(lastName);
        if (lastName != null && !lastName.equals("")) {
            return true;
        }
        System.out.println("Nespravne zadanie, skus este raz");
        return false;
    }

    public boolean idNumber(int idNumber) {
        if (idNumber >= 1 && idNumber <= 3) {
            return true;
        }
        System.out.println("Nespravne zadanie, skus este raz, musis zadat cislo (1-3)");
        return false;
    }

    public void play() {
        System.out.println("Opening JPA playground");

//        entityManager.persist(new StudyGroup("zakladna"));
//        entityManager.persist(new StudyGroup("miernePokrocila"));
//        entityManager.persist(new StudyGroup("pokrocila"));


        String firstName;
        String lastName;
        int group;

        boolean checker = true;

        while (checker) {
            System.out.println("Zadaj krstne meno studenta: ");
            firstName = readLine();

            System.out.println("Zadaj priezvisko studenta");
            lastName = readLine();

            System.out.println("Zadaj studijnu skupinu studenta(1-zakladna, 2-mierne pokrocila, 3-pokrocila)");
            group = Integer.parseInt(readLine());

            if (firstName(firstName) && lastName(lastName) && idNumber(group)) {
                List<StudyGroup> studyGroups = entityManager.createQuery("select g from StudyGroup g").getResultList();

                int noOfGroups = studyGroups.size();

                entityManager.persist(new Student(firstName, lastName, studyGroups.get(group)));

                List<Student> students = entityManager.createQuery("select s from Student s").getResultList();

                System.out.println(students);

                checker = false;
            }
        }

        System.out.println("Closing JPA playground");
    }
}
 */


//Pridanie a vypis hodnot - Score, Comment, Ratting
/*
    public void play() {
        String game = "minesweeper";
        String user = "Feri";
        int ratingValue = 4;

//        entityManager.persist(new Rating(game, user, ratingValue, new Date()));

        Rating rating2Write = null;

        try {
            rating2Write = (Rating) entityManager.createQuery("select r from Rating r where r.userName = :user and r.game = :game")
                    .setParameter("user", user)
                    .setParameter("game", game)
                    .getSingleResult();

            rating2Write.setRating(ratingValue);
            rating2Write.setRatedOn(new Date());

        } catch (NoResultException e) {
            rating2Write = new Rating(game,user,ratingValue,new Date());
            entityManager.persist(rating2Write);
        }

        System.out.println(rating2Write);

        //The EntityManager. persist() operation is used to insert a new object into the database.
        // persist does not directly insert the object into the database: it just registers it as
        // new in the persistence context (transaction).
        entityManager.persist(new Score(game, "Stevo", 10, new Date()));
        entityManager.persist(new Score(game, "Stevoo", 10, new Date()));

        entityManager.persist(new Comment(game, "Matej", "Hahaha", new Date()));
        entityManager.persist(new Comment(game, "Filip", "Kukuku", new Date()));

        entityManager.persist(new Rating(game, "Matej", 4, new Date()));
        entityManager.persist(new Rating(game, "Edo", 2, new Date()));

        List<Score> bestScores = entityManager
                .createQuery("Select s from Score s where s.game = :myGame order by s.points desc")
                .setParameter("myGame", game)
                .getResultList();

        System.out.println(bestScores);

        List<Comment> getComments = entityManager
                .createQuery("Select c from Comment c where c.game = :myGame order by c.commentedOn desc")
                .setParameter("myGame", game)
                .getResultList();

        System.out.println(getComments);

        double avgRating = (Double) entityManager.createQuery("select AVG(CAST(r.rating as integer)) from Rating r where r.game = :myGame")
                .setParameter("myGame", game).getSingleResult();

        System.out.printf("%.0f\n", avgRating);

        System.out.println("Closing JPA playground");
    }
}
 */
