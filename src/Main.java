import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);
    private static int id;
    private static String title, author;
    private static BookBean result;
    public static List<BookBean> result_lbb;

    public static void main(String[] args) {

        boolean finish = false;
        String command;

        while (!finish) {
            System.out.println("+------------------+");
            System.out.println("| 書籍管理システム |");
            System.out.println("+------------------+");

            System.out.println("操作を選んでください。");
            System.out.println("(1:一覧 2:検索 3:追加 4:編集 5:削除 q:終了)");
            System.out.print(">> ");
            command = sc.nextLine();

            switch (command) {
                case "q" -> finish = true;
                case "1" -> all();
                case "2" -> search();
                case "3" -> add();
                case "4" -> update();
                case "5" -> delete();
                default -> {
                    System.out.println("1~4の操作の中から選択してください。");
                    System.out.println("終了する場合は「q」を入力してください。" + "\n");
                }
            }
        }
    }

    //    ---------- ここから「一覧」 ----------
    private static void all() {
        System.out.println("\n" + "表示方法を選んでください。");
        System.out.println("(1:ID順 2:タイトル順 3:著者順)");
        System.out.print(">> ");
        int how = Integer.parseInt(sc.nextLine());
        switch (how) {
            case 1 -> all_id();
            case 2 -> all_title();
            case 3 -> all_author();
            default -> System.out.println("1~3の操作の中から選択してください。" + "\n");
        }
    }
    private static void all_id() {
        result_lbb = BookDAO.selectAll_id();
        showAll(result_lbb);
    }

    private static void all_title() {
        result_lbb = BookDAO.selectAll_title();
        showAll(result_lbb);
    }

    private static void all_author() {
        result_lbb = BookDAO.selectAll_author();
        showAll(result_lbb);
    }

    private static void showAll(List<BookBean> result) {
        System.out.println("\n" + "--------------------");
        System.out.println("ID : タイトル / 著者");
        System.out.println("--------------------");

        if (result.size() != 0) {
            for (BookBean b : result) {
                System.out.println(b.getId() + " : " + b.getTitle() + " / " + b.getAuthor());
            }
            System.out.println("--------------------");
            System.out.println("現在" + result.size() + "件のデータが登録されています。" + "\n");

        } else {
            System.out.println("書籍データが登録されていません。" + "\n");
        }
    }
    //    ---------- ここまで「一覧」 ----------

    //    ---------- ここから「検索」 ----------
    private static void search() {
        System.out.println("\n" + "検索方法を選んでください。");
        System.out.println("(1:ID一致 2:タイトル・著者あいまい)");
        System.out.print(">> ");
        int how = Integer.parseInt(sc.nextLine());
        switch (how) {
            case 1 -> search_id();
            case 2 -> search_TnA();
            default -> System.out.println("1~3の操作の中から選択してください。" + "\n");
        }
    }

    private static void search_id() {
        System.out.println("\n" + "検索する書籍の情報を入力してください。");
        System.out.println("--------------------");
        System.out.print("ID : ");
        id = Integer.parseInt(sc.nextLine());
        result = BookDAO.selectById(id);
        showSearchResult_id(result);
    }

    private static void search_TnA() {
        System.out.println("\n" + "検索する書籍の情報を入力してください(空欄可)。");
        System.out.println("--------------------");
        System.out.print("タイトル : ");
        title = sc.nextLine();
        System.out.print("著者 : ");
        author = sc.nextLine();
        result_lbb = BookDAO.selectLikeTnA(title, author);
        showSearchResult_else(result_lbb);
    }

    private static void showSearchResult_id(BookBean result) {
        System.out.println("\n" + "----- 検索結果 -----");
        try {
            System.out.println("ID : " + result.getId());
            System.out.println("タイトル : " + result.getTitle());
            System.out.println("著者 : " + result.getAuthor() + "\n");
        } catch (NullPointerException e) {
            System.out.println("お探しの書籍は見つかりませんでした。" + "\n");
        }
    }

    private static void showSearchResult_else(List<BookBean> result) {
        System.out.println("\n" + "----- 検索結果 -----");
        if (result.size() != 0) {
            System.out.println("ID : タイトル / 著者");
            System.out.println("--------------------");
            for (BookBean b : result) {
                System.out.println(b.getId() + " : " + b.getTitle() + " / " + b.getAuthor());
            }
            System.out.println("--------------------");
            System.out.println(result.size() + "件のデータがヒットしました。" + "\n");
        } else {
            System.out.println("お探しの書籍は見つかりませんでした。" + "\n");
        }
    }
    //    ---------- ここまで「検索」 ----------

    //    ---------- ここから「追加」と「編集」と「削除」 ----------
    private static void add() {
        System.out.println("\n" + "追加したい書籍の情報を入力してください。");
        System.out.println("--------------------");
        try {
            System.out.print("ID : ");
            id = Integer.parseInt(sc.nextLine());

            System.out.print("タイトル : ");
            title = sc.nextLine();
            if(title.equals("")) {
                throw new EmptyInput();
            }

            System.out.print("著者 : ");
            author = sc.nextLine();
            if(author.equals("")) {
                throw new EmptyInput();
            }

            int r = BookDAO.insert(id, title, author);
            if (r != 0) {
                System.out.println("--------------------");
                System.out.println("書籍の追加に成功しました。");
                result_lbb = BookDAO.selectAll_id();
                System.out.println("現在" + result_lbb.size() + "件のデータが登録されています。" + "\n");
            } else {
                System.out.println("データの追加に失敗しました。" + "\n");
            }
        } catch (NumberFormatException e) {
            System.out.println("数字を入力してください。" + "\n");
        } catch (EmptyInput e) {
            System.out.println("文字を入力してください。" + "\n");
        }
    }

    public static void update() {
        System.out.println("\n" + "編集したい書籍のIDを入力してください。");
        System.out.print(">> ");
        id = Integer.parseInt(sc.nextLine());
        result = BookDAO.selectById(id);
        if (result != null) {
            System.out.println("\n" + "このデータを編集しますか？(yes/no)");
            System.out.println("※IDを編集したい場合は、一度該当データを削除した後に作り直してください。");
            String answer = dataCheck();
            if (answer.equals("yes")) {

                System.out.println("\n" + "新しい書籍の情報を入力してください。");
                System.out.println("--------------------");
                System.out.println("ID : " + id);

                try {
                    System.out.print("タイトル : ");
                    title = sc.nextLine();
                    if(title.equals("")) {
                        throw new EmptyInput();
                    }

                    System.out.print("著者 : ");
                    author = sc.nextLine();
                    if(author.equals("")) {
                        throw new EmptyInput();
                    }
                    BookDAO.update(id, title, author);
                    System.out.println("--------------------");
                    System.out.println("データの編集に成功しました。" + "\n");
                } catch (EmptyInput e) {
                    System.out.println("文字を入力してください。" + "\n");
                }

            } else if (answer.equals("no")) {
                System.out.println("編集処理を中止しました。" + "\n");
            } else {
                System.out.println("yesまたはnoを入力してください。" + "\n");
            }
        } else {
            System.out.println("指定された書籍は見つかりませんでした。" + "\n");
        }
    }
    public static void delete() {
        System.out.println("\n" + "削除したい書籍のIDを入力してください。");
        System.out.print(">> ");
        id = Integer.parseInt(sc.nextLine());
        result = BookDAO.selectById(id);
        if (result != null) {
            System.out.println("\n" + "このデータを削除しますか？(yes/no)");
            String answer = dataCheck();
            if (answer.equals("yes")) {
                int r = BookDAO.delete(id);
                if (r != 0) {
                    System.out.println("書籍の削除に成功しました。");
                    result_lbb = BookDAO.selectAll_id();
                    System.out.println("現在" + result_lbb.size() + "件のデータが登録されています。" + "\n");
                } else {
                    System.out.println("書籍の削除に失敗しました。" + "\n");
                }
            } else if (answer.equals("no")) {
                System.out.println("削除処理を中止しました。" + "\n");
            } else {
                System.out.println("yesまたはnoを入力してください。" + "\n");
            }
        } else {
            System.out.println("指定された書籍は見つかりませんでした。" + "\n");
        }
    }
    public static String dataCheck() {
        System.out.println("--------------------");
        System.out.println("ID : " + result.getId());
        System.out.println("タイトル : " + result.getTitle());
        System.out.println("著者 : " + result.getAuthor());
        System.out.println("--------------------");
        System.out.print(">> ");
        return sc.nextLine();
    }

    public static class EmptyInput extends Exception{
        EmptyInput() {
            super();
        }
    }
    //    ---------- ここまで「追加」と「編集」と「削除」 ----------
}