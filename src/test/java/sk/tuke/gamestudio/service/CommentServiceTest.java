package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.CommentServiceJDBC;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {

    private CommentService commentService = new CommentServiceJDBC();

    private static final String GAME = "minesweeper";

    @Test
    public void testCommentReset() {
        commentService.addComment(new Comment(GAME, "Matej", "Ako sa mas", new Date()));
        commentService.reset();
        assertEquals(0, commentService.getComments(GAME).size());
    }


    @Test
    public void testAddComment() {
        commentService.reset();
        var date = new Date();

        commentService.addComment(new Comment(GAME, "Matej", "Ako sa mas", date));

        var comments = commentService.getComments(GAME);
        assertEquals(1, comments.size());

        assertEquals(GAME, comments.get(0).getGame());
        assertEquals("Matej", comments.get(0).getUserName());
        assertEquals("Ako sa mas", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
    }

    @Test
    public void testGetComments() {
        commentService.reset();
        var date = new Date();
        commentService.addComment(new Comment(GAME, "Peto", "Moze byt", date));
        commentService.addComment(new Comment(GAME, "Katka", "Nemoze byt", date));
        commentService.addComment(new Comment(GAME, "Oliver", "Chod domov", date));
        commentService.addComment(new Comment(GAME, "David", "Podme von", date));

        var comments = commentService.getComments(GAME);

        assertEquals(3, comments.size());

        assertEquals(GAME, comments.get(0).getGame());
        assertEquals("Katka", comments.get(0).getUserName());
        assertEquals("Nemoze byt", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());

        assertEquals(GAME, comments.get(1).getGame());
        assertEquals("Peto", comments.get(1).getUserName());
        assertEquals("Moze byt", comments.get(1).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());

    }
}







