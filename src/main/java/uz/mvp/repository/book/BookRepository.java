package uz.mvp.repository.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mvp.configs.PConfig;
import uz.mvp.entity.book.Book;
import uz.mvp.repository.AbstractRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Doston Bokhodirov, Wed 5:04 PM. 12/22/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookRepository extends AbstractRepository {
    private static final BookRepository instance = new BookRepository();

    public void save(String columnName, String value, String fileId) {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(PConfig.get("books.update.query").formatted(columnName, value, fileId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(String columnName, Integer value, String fileId) {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(PConfig.get("books.update.query").formatted(columnName, value, fileId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(String fileId) {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(PConfig.get("books.insert.fileId").formatted(fileId));
            statement.execute(PConfig.get("ID.insert.fileId").formatted(fileId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getBooksByName(String text, Integer limit, Integer offset) {
        text = "%" + text + "%";
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.like.query")
                    .formatted(text, limit, String.valueOf(offset * limit)));
            while (resultSet.next()) {
                Book book = Book.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .ownerId(resultSet.getString("owner_id"))
                        .size(resultSet.getString("size"))
                        .uploadedAt(resultSet.getString("uploaded_at"))
                        .downloadsCount(resultSet.getInt("downloads_count"))
                        .build();
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<Book> getBooksByGenre(String genre, Integer limit, Integer offset) {
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.genre.query")
                    .formatted(genre, limit, String.valueOf(offset * limit)));
            while (resultSet.next()) {
                Book book = Book.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .ownerId(resultSet.getString("owner_id"))
                        .size(resultSet.getString("size"))
                        .uploadedAt(resultSet.getString("uploaded_at"))
                        .downloadsCount(resultSet.getInt("downloads_count"))
                        .build();
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<String> getDownloadedBooksId(String chatId, Integer limit, Integer offset) {
        ArrayList<String> books = new ArrayList<>();
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("downloads.select.id.query")
                    .formatted(chatId, limit, String.valueOf(offset * limit)));
            while (resultSet.next()) {
                books.add(resultSet.getString("book_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<Book> getDownloadedBooks(String chatId, Integer limit, Integer offset) {
        ArrayList<String> booksId = getDownloadedBooksId(chatId, limit, offset);
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = getConnection();
        for (String id : booksId) {
            try (
                    Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.downloaded.query")
                        .formatted(id, chatId, limit, String.valueOf(offset * limit)));
                Book book = Book.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .ownerId(resultSet.getString("owner_id"))
                        .size(resultSet.getString("size"))
                        .uploadedAt(resultSet.getString("uploaded_at"))
                        .downloadsCount(resultSet.getInt("downloads_count"))
                        .build();
                books.add(book);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return books;
    }

    public ArrayList<Book> getUploadedBooks(String chatId, Integer limit, Integer offset) {
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.uploaded.query")
                    .formatted(chatId, limit, String.valueOf(offset * limit)));
            while (resultSet.next()) {
                Book book = Book.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .ownerId(resultSet.getString("owner_id"))
                        .size(resultSet.getString("size"))
                        .uploadedAt(resultSet.getString("uploaded_at"))
                        .downloadsCount(resultSet.getInt("downloads_count"))
                        .build();
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public ArrayList<Book> getTopBooks(Integer limit, Integer offset) {
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.top.query")
                    .formatted(limit, String.valueOf(offset * limit)));
            while (resultSet.next()) {
                Book book = Book.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .ownerId(resultSet.getString("owner_id"))
                        .size(resultSet.getString("size"))
                        .uploadedAt(resultSet.getString("uploaded_at"))
                        .downloadsCount(resultSet.getInt("downloads_count"))
                        .build();
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public Integer getDownloadsCount(String bookId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.downloads_count,query").formatted(bookId));
            return resultSet.getInt("downloads_count");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId(String bookId) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(PConfig.get("ID.select.id").formatted(bookId));
            return resultSet.getString("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBookId(String id) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(PConfig.get("ID.select.bookId").formatted(id));
            return resultSet.getString("book_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> getIdBooks() {
        ArrayList<String> list = new ArrayList<>();
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.all.id.query"));
            while (resultSet.next()) {
                list.add(resultSet.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.all.query"));
            while (resultSet.next()) {
                Book book = Book.builder()
                        .id(resultSet.getString("id"))
                        .name(resultSet.getString("name"))
                        .build();
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean isHaveBookWithUser(String chatId, String bookId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("downloads.select.query").formatted(chatId, bookId));
            return Objects.nonNull(resultSet.getString("book_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addDownloads(String chatId, String bookId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(PConfig.get("downloads.insert").formatted(chatId, bookId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String name) {
        name = "%" + name + "%";
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(PConfig.get("books.delete.query").formatted(name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String findFieldByFileName(String fileName, String columnName) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("books.select.item").formatted(columnName, fileName));
            return resultSet.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BookRepository getInstance() {
        return instance;
    }
}
