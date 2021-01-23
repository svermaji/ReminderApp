package com.svws.email;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.utils.*;
import com.svws.common.Base64Encoder;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;
import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.security.Security;


/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 29, 2007
 * Time: 5:49:19 PM
 */
public final class ManageEmails
{

    private static Properties prop;
    private static Session session;
    private static MimeMessage message;

    private static String username = "mail.username";
    private static String password = "mail.password";

    static
    {
        final String LOGINFO = LogInfo.getLogInfo ( "ManageEmails", "static");

        try
        {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            prop = ReadSource.getResourceProperties ( ServletConstants.EMAIL_PROPERTIES, true );


/*
                    java.io.File f = new java.io.File ("E:\\i312\\tomcat\\webapps\\svws\\resources\\properties\\email.properties");
                    java.io.FileInputStream fis = new java.io.FileInputStream ( f );
                    prop = new Properties ( );
                    prop.load ( fis );


                    Iterator itar = prop.keySet ().iterator ();
                    while (itar.hasNext ())
                    {
                        String key = (String) itar.next ();
                        String val= prop.getProperty ( key );
                        Debug.info (LOGINFO + "key [" + key + "] and value [" + val + "]." );
                    }
*/

            Debug.info ( LOGINFO + "Email properties populated.");

            final String usernameProp = prop.getProperty(username);
            final String pwdProp = Base64Encoder.decode(prop.getProperty(password));
            Authenticator auth = new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usernameProp, pwdProp);
                    }
                };
            Debug.info ( LOGINFO + "Authentication information obtained. Now obtaining session instance for email properties....");
            session = Session.getInstance (prop, auth);
            Debug.info ( LOGINFO + "All values populated successfully.");
        }
/*
        catch (ProcessingException e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
*/
        catch (Exception e)
        {
            Debug.error (LOGINFO + e.getMessage (), e);
        }
    }

    public static boolean sendMail (String to, String from, String cc, String bcc, String subject, String matter) throws EmailException
    {
        final String LOGINFO = LogInfo.getLogInfo ( "ManageEmails", "sendMail");
        boolean status = false;
        try
        {
            Debug.info ( LOGINFO + "Obtaining MimeMessage settings for email properties....");
            message = new MimeMessage(session);
            Debug.info ( LOGINFO + "Setting value for [from] as [" + from + "] for email");
            InternetAddress fromAddr = new InternetAddress (from);
            message.setFrom(fromAddr);

            Debug.info ( LOGINFO + "Setting value for [to] as [" + to + "] for email");
            InternetAddress[] toAddrs = InternetAddress.parse (to) ;
            message.addRecipients(Message.RecipientType.TO, toAddrs);

/*


            javax.mail.Address[] toAddrs1 = message.getAllRecipients ();

            for (int i = 0; i < toAddrs1.length; i++)

            {

            javax.mail.Address toAddrr = toAddrs1[i];

            Debug.info ( LOGINFO + "Getting receipent as [" + toAddrr + "] for email");

            }
*/


            if (StringUtils.hasValue ( cc ))
            {
                Debug.info ( LOGINFO + "Setting value for [cc] as [" + cc + "] for email");
                InternetAddress[] ccAddrs = InternetAddress.parse (cc);
                message.addRecipients(Message.RecipientType.CC, ccAddrs);
            }
            if (StringUtils.hasValue ( bcc ))
            {
                Debug.info ( LOGINFO + "Setting value for [bcc] as [" + bcc + "] for email");
                InternetAddress[] bccAddrs = InternetAddress.parse (bcc);
                message.addRecipients(Message.RecipientType.BCC, bccAddrs);
            }

            Debug.info ( LOGINFO + "Setting value for [subject] as [" + subject + "] for email");
            message.setSubject (subject);

            Debug.info ( LOGINFO + "Setting value for [matter] as [" + matter + "] for email");
            message.setContent ( matter, "text/html");
            //message.setText (matter);

            Debug.info ( LOGINFO + "Sending email...");
            Transport.send(message);
            Debug.info ( LOGINFO + "Email send successfully.");

            status = true;
        }
        catch (MessagingException e)
        {
            status = false;
            Debug.error (LOGINFO + e.getMessage (), e);
            throw new EmailException (e.getMessage ());
        }
/*
        catch (ProcessingException e)
        {
            status = false;
            Debug.error (LOGINFO + e.getMessage (), e);
        }
*/
        catch (Exception e)
        {
            status = false;
            Debug.error (LOGINFO + e.getMessage (), e);
        }
        return status;
    }

    /**
     * TODO: check user from session
     */
    public static boolean sendMail (String to, String cc, String bcc, String subject, String matter) throws EmailException, Exception
    {
        return sendMail ( to, "", cc, bcc, subject, matter);
    }

    /**
     * Unit test point.
     *
     * @param args
     * @throws EmailException
     */
    public static void main ( String[] args ) throws EmailException, ProcessingException
    {
        String to = "sverma@impetus.co.in";
        String from = "sverma@impetus.co.in";
        String cc = "";
        String bcc = "";
        String subject = "Reminder";
        String matter = "<html>\n" +
            "<body bgcolor=\"F0F5FF\">\n" +
            "<table border=\"0\" width=\"100%\">\n" +
            "<tr><td align=\"center\" width=\"10%\">&nbsp;</td>\n" +
            "    <td align=\"center\"><font face=\"Verdana\" size=\"4\" color=\"64AFFF\"><b>Reminder</b></font></td></tr>\n" +
            "<tr><td align=\"center\" width=\"10%\">&nbsp;</td>\n" +
            "      <td class=\"text\">\n" +
            "      <font face=\"Verdana\" size=\"2\" color=\"64AFFF\">\n" +
            "            <br>Hi <b><first_name> <last_name></b>,\n" +
            "            <br>\n" +
            "            <br>This is a reminder.\n" +
            "            <br>\n" +
            "            <br>Today's date is <b><today_date></b>.\n" +
            "            <br>\n" +
            "            <br>There is event of <b><event_type></b> on <b><event_date></b> in near future.\n" +
            "            <br>\n" +
            "            <br>Event description is:\n" +
            "            <br>\n" +
            "            <br><b><event_desc></b>\n" +
            "            <br>\n" +
            "            <br>Receivers for this event stored by you are <b><receiver_email></b>.\n" +
            "            <br>\n" +
            "            <br>You are receiving this email as you are signed up as <b><user_name></b> to <b><website></b>.\n" +
            "            <br>For further any information either contact to administrator or log on to <b><website></b>.\n" +
            "            <br>\n" +
            "            <br>Thanks\n" +
            "            <br>Shailendra Verma (Administrator)\n" +
            "            <br><b><admin_email></b>\n" +
            "            <br><b><website></b>\n" +
            "        </font>\n" +
            "        </td></tr></table>\n" +
            "</body>\n" +
            "</html>";

        //subject = "Congratulations";
        /*matter = "<html>\n" +
            "<body bgcolor=\"F0F5FF\">\n" +
            "<table border=\"0\" width=\"100%\">\n" +
            "<tr><td align=\"center\" width=\"10%\">&nbsp;</td>\n" +
            "      <td align=\"center\"><font face=\"Verdana\" size=\"4\" color=\"64AFFF\"><b>Congratulations</b></font></td></tr>\n" +
            "<tr><td align=\"center\" width=\"10%\">&nbsp;</td>\n" +
            "      <td class=\"text\">\n" +
            "      <font face=\"Verdana\" size=\"2\" color=\"64AFFF\">\n" +
            "            <br>Hi,\n" +
            "            <br>\n" +
            "            <br>Congratulations you on the event of <b><event_type></b> on <b><event_date></b>.\n" +
            "            <br>Please accept greetings from my side.\n" +
            "            <br>\n" +
            "            <br>Thanks\n" +
            "            <br><b><first_name> <last_name></b>\n" +
            "            <br>\n" +
            "            <br>--------------------------------------------------\n" +
            "            <br>\n" +
            "            <br>Hi,\n" +
            "            <br>\n" +
            "            <br>Congratulations.\n" +
            "            <br>\n" +
            "            <br>This is automatic mail generation program from <b><website></b>, authour is <b>Shailendra Verma (shailendravermag@yahoo.com);</b>\n" +
            "            <br>\n" +
            "            <br>Thanks\n" +
            "            <br>Shailendra Verma (Administrator)\n" +
            "        </font>\n" +
            "        </td></tr></table>\n" +
            "</body>\n" +
            "</html>\n" +
            "\n" +
            "\n" +
            "Hi,\n" +
            "\n" +
            "Congratulations.\n" +
            "\n" +
            "This is automatic mail generation program from <website>, authour is Shailendra Verma (shailendravermag@yahoo.com);\n" +
            "\n" +
            "Thanks\n" +
            "Shailendra Verma (Administrator)";*/

        matter = matter.replaceAll ("<first_name>", "Shailendra" );
        matter = matter.replaceAll ("<last_name>", "Verma" );
        matter = matter.replaceAll ("<user_name>", "sv" );
        matter = matter.replaceAll ("<event_desc>", "test" );
        matter = matter.replaceAll ("<event_type>", "test");
        matter = matter.replaceAll ("<event_date>", "09-FEB-2007" );
        matter = matter.replaceAll ("<admin_email>", "shailendravermag@yahoo.com" );
        matter = matter.replaceAll ("<receiver_email>", "sverma@impetus.co.in" );
        matter = matter.replaceAll ("<website>", "http://myjavaserver.com/~shailendra" );
        matter = matter.replaceAll ("<today_date>", DateUtils.getFormattedDate(UtilConstants.DATE_FORMAT_DD_MMM_YYYY));

        boolean status = sendMail ( to, from, cc, bcc, subject, matter);
        System.out.println ( "status = " + status );
    }
}
