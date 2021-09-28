package dao;

import models.User;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.sql.*;

public class UserDaoImpl implements  UserDao {
    private static UserDao userDao;

    private UserDaoImpl() {
        try{
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static UserDao getInstance(){
        if (userDao == null){
            userDao = new UserDaoImpl();
        }
        return userDao;
    }

    @Override
    public User login(User user) {
        try (Connection conn = DriverManager.getConnection(ConnectionUtil.url, ConnectionUtil.username, ConnectionUtil.password)){
            String sql = "SELECT * FROM ers_users WHERE ers_username = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUsername());

            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                User tempUser = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7));
                BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();
                if (basicPasswordEncryptor.checkPassword(user.getPassword(), tempUser.getPassword())){
                    return tempUser;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User register(User user) {
        BasicPasswordEncryptor basicPasswordEncryptor = new BasicPasswordEncryptor();

        try (Connection conn = DriverManager.getConnection(ConnectionUtil.url, ConnectionUtil.username, ConnectionUtil.password)){
            String sql = "INSERT INTO ers_users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, basicPasswordEncryptor.encryptPassword(user.getPassword()));
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getEmail());
            ps.setInt(6, user.getRoleId());

            if (ps.executeUpdate() != 0){
                return user;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
