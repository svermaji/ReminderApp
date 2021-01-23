package com.svws.web;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: mmodi
 * Date: Feb 4, 2007
 * Time: 7:46:14 PM
 */
public class UserSession
{
    private int userId = 0;
    private String userName, firstName, lastName, email;

    private UserSession(int userId, String userName, String firstName, String lastName, String email) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        describe ();
    }

    public static UserSession getUserSession (HttpServletRequest req)
    {
        final String LOGINFO = LogInfo.getLogInfo ( "UserSession", "getUserSession");
        Vector rec = new Vector();
        try
        {
            HttpSession sessionInfo = (HttpSession) SessionManager.getSessionInManager (req);

            if (sessionInfo == null)
            {
                Debug.warning ( LOGINFO + "no session exists. Returning null");
                return null;
            }

            rec = (Vector) sessionInfo.getAttribute ("vUserInfo");
            if (rec == null)
            {
                Debug.error (LOGINFO + "no user information exists. Returning null");
                return null;
            }
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + "Unable to retrieve user information from session.", e);
        }

        return new UserSession (
                Integer.parseInt((String) rec.get (0)),
                (String) rec.get (1),
                (String) rec.get (3),
                (String) rec.get (4),
                (String) rec.get (13)
                );
    }

    public void describe ()
    {
        final String LOGINFO = LogInfo.getLogInfo ("UserSession", "describe" );
        Debug.info (LOGINFO + "Values in User Session are: " +
                "userId [" + userId + "]" +
                ", userName [" + userName + "]" +
                ", firstName [" + firstName + "]" +
                ", lastName [" + lastName + "]" +
                " and email [" + email + "]");
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
