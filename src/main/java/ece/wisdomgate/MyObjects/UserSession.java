package ece.wisdomgate.MyObjects;

public class UserSession {
    private static User loggedInUser; // Store the reference to the logged-in user

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

}
