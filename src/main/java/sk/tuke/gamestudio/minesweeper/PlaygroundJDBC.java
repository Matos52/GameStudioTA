package sk.tuke.gamestudio.minesweeper;

import java.util.Date;

import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.ScoreServiceJDBC;

public class PlaygroundJDBC {

    public static void main(String[] args) throws Exception {

        ScoreService service = new ScoreServiceJDBC();
//        service.reset();
        service.addScore(new Score("sk/tuke/gamestudio", "David", 359, new Date()));
        service.addScore(new Score("sk/tuke/gamestudio", "Matej", 136, new Date()));
        service.addScore(new Score("sk/tuke/gamestudio", "Oliver", 259, new Date()));

        var scores = service.getBestScores("sk/tuke/gamestudio");
        System.out.println(scores);
    }

//    public static void main(String[] args) throws Exception {
//
//        try(var connection = DriverManager.getConnection("jdbc:postgresql://localhost/gamestudio", "postgres", "postgres");
//            var statement = connection.createStatement();
//            var rs = statement.executeQuery("SELECT game, username, points, played_on FROM score WHERE game='minesweeper' ORDER BY points DESC LIMIT 5")
//                ) {
//
//            while(rs.next()) {
//                System.out.printf("%s, %s, %d, %s \n",rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4));
//            }
//
//            System.out.println("Pripojenie uspesne");
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
