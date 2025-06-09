// util/UserAuthentication.java
package com.inventory.util;

import com.inventory.ui.*;
import com.inventory.dao.UsersDAOImpl;
import com.inventory.model.User;
// import java.security.SecureRandom;
import java.sql.SQLException;
// import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

public class UserAuthentication {

    public static int authenticateUser(String username, String password) throws SQLException {
        UsersDAOImpl usersDAO = new UsersDAOImpl();
        User user = usersDAO.getUserByUsername(username);

        if (user != null) {
            //  IMPORTANT:  THIS IS INSECURE!  Never store or check passwords in plain text.
            if (user.getPassword().equals(password)) {
                return user.getRoleID();
            }
        }
        return 0;
    }
    
    /*
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    */
    
    public static String hashPassword(String password, String salt) {
        return BCrypt.hashpw(password + salt, BCrypt.gensalt());
    }
    
    public static boolean verifyPassword(String plainPassword, String hashedPasswordWithSalt) {
        return BCrypt.checkpw(plainPassword, hashedPasswordWithSalt);
    }
}