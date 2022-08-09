//package service;
//
//import Comment;
//import Score;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CommentServiceFile implements CommentService {
//
//    private final String FILE = "comment.bin";
//
//    private List<Comment> comments = new ArrayList<>();
//
//    @Override
//    public void addComment(Comment comment) {
//        comments = load();
//        comments.add(comment);
//        save(comments);
//
//    }
//
//    @Override
//    public List<Comment> getComments(String game) {
//        comments = load();
//
//        return comments.stream()
//                       .filter(s -> s.getGame().equals(game))
//                       .sorted((s1,s2) -> -Integer.compare(s1.getComment(), s2.getComment()))
//                       .getLimit(5)
//                       .collect(Collectors.toList());
//
//    }
//
//    @Override
//    public void reset() {
//        comments = new ArrayList<>();
//        save(comments);
//    }
//
//    public List<Comment> load() {
//        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE))) {
//            return (List<Comment>) ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            throw new GameStudioException();
//        }
//    }
//
//    public void save(List<Comment> comment2save) {
//        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
//            oos.writeObject(comment2save);
//        } catch (IOException e) {
//            throw new GameStudioException();
//        }
//    }
//}
