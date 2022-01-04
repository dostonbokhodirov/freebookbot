package uz.mvp.repository.authuser;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.PConfig;
import uz.mvp.entity.user.User;
import uz.mvp.repository.AbstractRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Sun 9:02 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUserRepository extends AbstractRepository {
    private static final AuthUserRepository instance = new AuthUserRepository();

    public Integer save(String key, String value, String chatId) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(PConfig.get("users.update.query").formatted(key, value, chatId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(String chatId) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(PConfig.get("users.insert.chatId").formatted(chatId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer update(String key, String value, String number) {
        Connection connection = getConnection();
        try {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(PConfig.get("users.update.number.query").formatted(key, value, number));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findRoleById(String chatId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.role").formatted("id", chatId));
            return resultSet.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findRoleByNumber(String phoneNumber) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.role").formatted("phone_number", phoneNumber));
            return resultSet.getString("role");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findFieldByChatID(String chatId, String columnName) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.item").formatted(columnName, chatId));
            return resultSet.getString(columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isHaveUser(String chatId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.chatId").formatted(chatId));
            return chatId.equals(resultSet.getString("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLanguage(String chatId) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.item").formatted("language", chatId));
            return resultSet.getString("language");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<User> getUsers(Integer limit, Integer offset) {
        ArrayList<User> users = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.query").formatted(limit, String.valueOf(offset * limit)));
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getString("id"))
                        .fullName(resultSet.getString("full_name"))
                        .age(resultSet.getInt("age"))
                        .role(resultSet.getString("role"))
                        .userName(resultSet.getString("user_name"))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public StringBuilder getUser(String chatId, String id) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.user.query").formatted(id));
            String userId = resultSet.getString("id");
            String fullName = resultSet.getString("full_name");
            Integer age = resultSet.getInt("age");
            String gender = resultSet.getString("gender");
            String phoneNumber = resultSet.getString("phone_number");
            String language = resultSet.getString("language");
            String role = resultSet.getString("role");
            String userName = resultSet.getString("user_name");
            String createdAt = resultSet.getString("created_at");
            stringBuilder.append("ID: ").append(userId).append("\n")
                    .append(LangConfig.get(chatId, "user.full.name")).append(" <b>").append(fullName).append("</b>\n")
                    .append(LangConfig.get(chatId, "user.age")).append(" <b>").append(age).append("</b>\n")
                    .append(LangConfig.get(chatId, "user.gender")).append(" <code>").append(gender).append("</code>\n")
                    .append(LangConfig.get(chatId, "user.phone.number")).append(" <b>").append(phoneNumber).append("</b>\n")
                    .append(LangConfig.get(chatId, "user.language")).append(" <code>").append(language).append("</code>\n")
                    .append(LangConfig.get(chatId, "user.role")).append(" <code>").append(role).append("</code>\n")
                    .append(LangConfig.get(chatId, "user.name")).append(" @").append(userName).append("\n")
                    .append(LangConfig.get(chatId, "user.created.at")).append(" <code>").append(createdAt).append("</code>\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }

    public ArrayList<Integer> getUsersId(String role) {
        ArrayList<Integer> list = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.role.query").formatted(role));
            while (resultSet.next()) {
                list.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Integer> getUsersId() {
        ArrayList<Integer> list = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(PConfig.get("users.select.all.query"));
            while (resultSet.next()) {
                list.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static AuthUserRepository getInstance() {
        return instance;
    }
}
