package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.UserAccount;

public class SessionUtils {
    public static UserAccount getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object user = session.getAttribute("currentUser");
        if (user == null) {
            return null;
        }
        return (UserAccount) user;
    } 
}
