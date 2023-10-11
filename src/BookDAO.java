import java.sql.*;
import java.util.*;

public class BookDAO {
    private static final String db_url = "jdbc:sqlite:books.db";
    public static ArrayList<BookBean> results_albb;

    public static ArrayList<BookBean> selectAll_id() {
        String sql = "SELECT * FROM books ORDER BY id";
        return selectAll_tool(sql);
    }

    public static ArrayList<BookBean> selectAll_title() {
        String sql = "SELECT * FROM books ORDER BY title";
        return selectAll_tool(sql);
    }

    public static ArrayList<BookBean> selectAll_author() {
        String sql = "SELECT * FROM books ORDER BY author";
        return selectAll_tool(sql);
    }

    public static ArrayList<BookBean> selectAll_tool(String sql) {
        results_albb = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                results_albb.add(
                        new BookBean(
                                rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("author")
                        )
                );
            }
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
        }
        return results_albb;
    }

    public static BookBean selectById(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        BookBean result = null;

        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = new BookBean(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<BookBean> selectLikeTitle(String title) {
        String sql = "SELECT * FROM books WHERE title LIKE ?";
        results_albb = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    results_albb.add(
                            new BookBean(
                                    rs.getInt("id"),
                                    rs.getString("title"),
                                    rs.getString("author")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
            e.printStackTrace();
        }
        return results_albb;
    }

    public static ArrayList<BookBean> selectLikeAuthor(String author) {
        String sql = "SELECT * FROM books WHERE author LIKE ?";
        results_albb = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + author + "%");

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    results_albb.add(
                            new BookBean(
                                    rs.getInt("id"),
                                    rs.getString("title"),
                                    rs.getString("author")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
            e.printStackTrace();
        }
        return results_albb;
    }

    public static ArrayList<BookBean> selectLikeTnA(String title, String author) {
        String sql = "SELECT * FROM books WHERE title LIKE ? AND author LIKE ?";
        results_albb = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + title + "%");
            ps.setString(2, "%" + author + "%");

            try(ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    results_albb.add(
                            new BookBean(
                                    rs.getInt("id"),
                                    rs.getString("title"),
                                    rs.getString("author")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
            e.printStackTrace();
        }
        return results_albb;
    }

    public static int insert(int id, String title, String author) {
        String sql = "INSERT INTO books VALUES (?, ?, ?)";

        int r = 0;
        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, title);
            ps.setString(3, author);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("--------------------");
            if (e.getErrorCode() == 19) {
                System.out.println("そのIDは使用されています。");
            } else {
                System.out.println("SQLの実行に失敗しました。");
            }
        }
        return r;
    }

    public static void update(int id, String title, String author) {
        String sql = "UPDATE books SET title = ?, author = ? WHERE id = ?";

        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
        }
    }

    public static int delete(int id) {
        String sql = "DELETE FROM books WHERE id = ?";
        int r = 0;
        try (Connection con = DriverManager.getConnection(db_url); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            r = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLの実行に失敗しました。");
        }
        return r;
    }
}