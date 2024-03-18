package ece.wisdomgate.MyObjects;

/**
 * The `Admin` class represents the administrator of the WisdomGate system.
 * It provides methods to access the administrator's username and password.
 */
public class Admin {

    /**
     * The default username for the administrator.
     */
    private static final String USERNAME = "medialab";

    /**
     * The default password for the administrator.
     */
    private static final String PASSWORD = "medialab_2024";

    /**
     * Returns the username of the administrator.
     *
     * @return The administrator's username.
     */
    public static String getUsername() {
        return USERNAME;
    }

    /**
     * Returns the password of the administrator.
     *
     * @return The administrator's password.
     */
    public static String getPassword() {
        return PASSWORD;
    }

}
