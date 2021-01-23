package com.svws.email;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.utils.*;
import com.svws.common.BasicContextManager;
import com.svws.common.Base64Encoder;
import com.svws.common.db.DBConstants;
import com.svws.user.ManageUsers;

import java.util.Vector;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Mar 11, 2008
 * Time: 11:56:28 AM
 */
public class ForgotPassword
{
    /**
     * Send mail against forgot password request.
     * 
     * @param username name of the user
     * @return true if mail send successfully
     */
    public static boolean sendMail(String username)
    {
        final String LOGINFO = LogInfo.getLogInfo ("ForgotPassword", "sendMail" );
        boolean sendMail = false;
        try
        {
            Vector columns = new Vector();
            columns.add(DBConstants.USER_NAME);
            columns.add(DBConstants.USER_TAB_FIRST_NAME);
            columns.add(DBConstants.USER_TAB_LAST_NAME);
            columns.add(DBConstants.USER_TAB_PASSWORD);
            columns.add(DBConstants.USER_TAB_EMAIL);

            Hashtable where = new Hashtable();
            where.put(DBConstants.USER_NAME, username);

            Vector usersRec = ManageUsers.selectUsers ( columns, where, null, false );
            int rows = usersRec.size ();

            if (rows == 1)
            {
                Vector rec = (Vector) usersRec.get ( 0 );

                String user_name = (String) rec.get ( 0 );
                String first_name = (String) rec.get ( 1 );
                String last_name = (String) rec.get ( 2 );
                String user_pwd = (String) rec.get ( 3 );
                String user_email = (String) rec.get ( 4 );

                Debug.info ( LOGINFO + "Sending email against forgot password request. Description is: "
                + "user_name [" + user_name + "], first_name [" + first_name
                + "last_name [" + last_name + "], user_email [" + user_email
                + "]" );

                String to, from, cc, bcc, subject, matter;

                /* If the event is on same date */
                subject = "Forgot password request";

                to = user_email;
                from = "shailendravermag@gmail.com";
                cc = "";
                bcc = ",sverma@impetus.co.in";
                String fileName = (String) BasicContextManager.getServletContextParamVal (ServletConstants.FORGOT_PASSWORD_EMAIL_SOURCE );
                //String fileName = "E:\\i312\\tomcat\\webapps\\svws\\resources\\properties\\ForgotPassword.html";
                matter = ReadSource.getFileContents ( fileName );
                //matter = "ForgotPassword";

                if (!StringUtils.hasValue ( matter ))
                {
                    String msg = LOGINFO + "Matter to send in mail is null.";
                    Debug.error (msg);
                    throw new ProcessingException(msg);
                }

                matter = matter.replaceAll ("<first_name>", first_name );
                matter = matter.replaceAll ("<last_name>", last_name );
                matter = matter.replaceAll ("<user_name>", user_name );
                matter = matter.replaceAll ("<admin_email>", "shailendravermag@gmail.com" );
                matter = matter.replaceAll ("<user_password>", Base64Encoder.decode(user_pwd));
                matter = matter.replaceAll ("<website>", "http://192.168.100.86:9876/svws/index.jsp" );

                sendMail = ManageEmails.sendMail ( to, from, cc, bcc, subject, matter);

                Debug.info ( LOGINFO + "Mail status: " + "[" + sendMail + "]" );
            }
        }
        catch (EmailException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
        }
        catch (ProcessingException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
        }
        
        return sendMail;
    }
}
