package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingServiceJDBC implements RatingService {

//    CREATE TABLE rating (
//    game VARCHAR(64) NOT NULL,
//    username VARCHAR(64) NOT NULL,
//    rating INTEGER NOT NULL,
//    rated_on TIMESTAMP NOT NULL,
//    CHECK(rating > 0 AND rating < 6),
//    UNIQUE(game, username)
//);

    private static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    private static final String JDBC_USER = "postgres";
    private static final String JDBC_PASSWORD = "postgres";

    private static final String STATEMENT_SET_RATING = "INSERT INTO rating VALUES(?,?,?,?)";
    private static final String STATEMENT_RESET = "DELETE FROM rating";
    private static final String STATEMENT_GET_RATING = "SELECT rating FROM rating WHERE game = ? AND username = ?";
    private static final String STATEMENT_AVG_RATING = "SELECT ROUND(AVG(rating)) FROM rating WHERE game = ?";
    private static final String STATEMENT_UPDATE_RATING = "UPDATE rating SET rating=?, rated_on=? WHERE game=? AND username=?";
    private static final String STATEMENT_SELECT_RATING = "SELECT FROM rating WHERE game=? AND username=?";

    private void updateRating(Rating rating) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(STATEMENT_UPDATE_RATING))
        {
            statement.setInt(1, rating.getRating());
            statement.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
            statement.setString(3, rating.getGame());
            statement.setString(4, rating.getUserName());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    private void setNewRating(Rating rating) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(STATEMENT_SET_RATING))
        {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getUserName());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }



    public void setRating(Rating rating) {
        try (var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             var statement = connection.prepareStatement(STATEMENT_SELECT_RATING))
        {
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getUserName());

            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    updateRating(rating);
                } else {
                    setNewRating(rating);
                }
            }
        } catch (SQLException e) {
            throw new GameStudioException(e);
        }
    }

    @Override
    public int getAverageRating(String game) {
        try(var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            var statement = connection.prepareStatement(STATEMENT_AVG_RATING);
        ) {
            statement.setString(1,game);
            try(var rs = statement.executeQuery()) {
                int avgRating = 0;
                if(rs.next()) {
                    avgRating = rs.getInt(1);
                }
                return avgRating;
            }
        } catch(SQLException e) {
            throw new GameStudioException();
        }
    }

    @Override
    public int getRating(String game, String username) {

        try(var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            var statement = connection.prepareStatement(STATEMENT_GET_RATING);
        ) {
            statement.setString(1,game);
            statement.setString(2,username);
            try(var rs = statement.executeQuery()) {
                int rating = 0;
                if(rs.next()) {
                    rating = rs.getInt(1);
                }
                return rating;
            }

        } catch (SQLException e) {
            throw new GameStudioException();
        }
    }

    @Override
    public void reset() {
        try(var connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            var statement = connection.createStatement();
            ) {
            statement.executeUpdate(STATEMENT_RESET);
        } catch(SQLException e) {
            throw new GameStudioException();
        }
    }
}
