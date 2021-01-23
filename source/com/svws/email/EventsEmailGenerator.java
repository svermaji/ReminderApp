package com.svws.email;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.BasicContextManager;
import com.svws.events.ManageEvents;
import com.svws.events.EventException;
import com.svws.common.utils.*;

import java.util.*;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 31, 2007
 * Time: 7:16:44 PM
 */
public class EventsEmailGenerator
{
    private static Thread reloadThread;
    private static EventsEmailGenerator emailGenerator = null;
    private static ArrayList mailsSend = null;

    static
    {
        final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator", "static block" );
        try
        {
            Debug.info ( LOGINFO + "Instantiating EventsEmailGenerator object...");
            EventsEmailGenerator.getInstance ();
        }
        catch (ProcessingException e)
        {
            Debug.error (LOGINFO + "Unable to instantiate EventsEmailGenerator object", e);
        }
        catch (Exception e)
        {
            Debug.error (LOGINFO + "Unknown error occured", e);
        }
    }
    /**
     * Private constructor
     */
    private EventsEmailGenerator()
    {
    }

    /**
     * Create instance of EventsEmailGenerator for first time and then
     * returns from cache for the next time.
     *
     * @return EventsEmailGenerator object
     * @throws ProcessingException
     */
    public static EventsEmailGenerator getInstance ()  throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator", "getInstance" );
        if (emailGenerator == null)
        {
            emailGenerator = new EventsEmailGenerator();
            Debug.info ( LOGINFO + "Object created. Calling init()...");
            emailGenerator.init ();
        }
        return emailGenerator;
    }

    /**
     * Initialize the EventsEmailGenerator
     * @throws ProcessingException
     */
    private void init () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator", "init" );
        int timeInHrs = 24;
        int minDefault = 6;
        int maxDefault = 24;
        Object prm = BasicContextManager.getServletContextParamVal (ServletConstants.CHECK_FOR_EVENTS_IN_HRS, new Integer ( maxDefault ));
        timeInHrs = Integer.parseInt((String) prm);
        //timeInHrs = 24;
        if (timeInHrs > maxDefault || timeInHrs < minDefault)
        {
            Debug.info ( LOGINFO + "Setting time for checking for events to default as it is out of bounds with value [" + timeInHrs + "]");
            timeInHrs = maxDefault;
        }

        int defaultDaysNotification = 4;
        int daysNotification = 4;
        prm = BasicContextManager.getServletContextParamVal (ServletConstants.DAYS_BEFORE_EVENT_FOR_NOTIFICATION, new Integer ( defaultDaysNotification ));
        daysNotification = Integer.parseInt((String) prm);
        //daysNotification = 3;
        if (daysNotification > 7 || daysNotification < 1)
        {
            Debug.info ( LOGINFO + "Setting days of Notification for events to default as it is out of bounds with value [" + timeInHrs + "]");
            daysNotification = defaultDaysNotification;
        }

        Debug.info ( LOGINFO + "timeInHrs as [" + timeInHrs + "] and daysNotification as [" + daysNotification + "]");

        int MINS = 60;
        startReloadThread ( timeInHrs * MINS );

        checkEventsToMail (daysNotification);
    }

    private static void checkEventsToMail (int daysNotification) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator", "checkEventsToMail" );
        try
        {
            // EVENT_ID - 0, USER_NAME - 1, EVENT_DATE - 2, EVENT_TYPE - 3, EVENT_DESC - 4, RECEIVER_EMAIL - 5, EVENT_USER - 6
            // EMAIL_MODE - 7, ACTIVE - 8, EMAIL_SEND_DATE - 9, FIRST_NAME - 10, LAST_NAME - 11, EMAIL - 12
            String sql = "SELECT E.*, U.FIRST_NAME, U.LAST_NAME, U.EMAIL FROM SV_EVENTS E, SV_USERS U WHERE E.USER_NAME=U.USER_NAME AND E.ACTIVE=1";

            Replacements replacements = Replacements.getInstance();

            Vector eventsRec = ManageEvents.selectEvents ( sql );
            int rows = eventsRec.size ();
            ArrayList event_ids_to_update = new ArrayList ( );
            mailsSend = new ArrayList ( );
            if (rows != 0)
            {
                for (int row = 0; row < rows; row++)
                {
                    Vector rec = (Vector) eventsRec.get ( row );
                    int event_id = Integer.parseInt((String) rec.get ( 0 ));
                    String user_name = (String) rec.get ( 1 );
                    String event_date = (String) rec.get ( 2 );
                    String event_type = (String) rec.get ( 3 );
                    String event_desc = (String) rec.get ( 4 );
                    String receiver_email = (String) rec.get ( 5 );
                    String event_user = (String) rec.get ( 6 );
                    String email_mode = (String) rec.get ( 7 );
                    String active = (String) rec.get ( 8 );
                    String email_send_date = (String) rec.get ( 9 );
                    String first_name = (String) rec.get ( 10 );
                    String last_name = (String) rec.get ( 11 );
                    String user_email = (String) rec.get ( 12 );

                    Debug.info ( LOGINFO + "Checking event number [" + (row+1) + "] for sending email. Description is: "
                        + "event id [" + event_id + "], user_name [" + user_name + "], event_date [" + event_date
                        + "], event_type [" + event_type + "], event_desc [" + event_desc
                        + "], event_user [" + event_user + "], email_mode [" + email_mode
                        + "]" );
                    
                    int eventDateDiff = getDateDiff ( event_date );
                    Debug.info ( LOGINFO + "Event date [" + event_date + "] is [" + eventDateDiff + "] days far from current date.");
                    int emailMode = 1;
                    if (StringUtils.hasValue ( email_mode ))
                    {
                        try
                        {
                            emailMode = Integer.parseInt ( email_mode );
                        }
                        catch (NumberFormatException e)
                        {
                            emailMode = 1;
                            Debug.warning ( LOGINFO + "Error while retrieving value for email mode column. Considering 1 as default value." + e.getMessage ());
                        }
                        catch (Exception e)
                        {
                            emailMode = 1;
                            Debug.warning ( LOGINFO + "Error while retrieving value for email mode column. Considering 1 as default value." + e.getMessage ());
                        }
                    }

                    int sendMailDateDiff = -1;
                    if (StringUtils.hasValue ( email_send_date, true ))
                    {
                        sendMailDateDiff = getDateDiff ( email_send_date );

                        Debug.info ( LOGINFO + "Last mail for this event was sent before [" + sendMailDateDiff + "] days.");
                    }
                    else Debug.info ( LOGINFO + "Unable to find when did last mail for this event was sent assuming [" + sendMailDateDiff + "].");

                    // First line is Mode-1, Second is Mode-2 and Third is Mode-3
                    boolean eventEligible = (emailMode == EventEmailModeConstants.DAILY_ONCE_TILL_EVENT_DATE && sendMailDateDiff < 0)
                                            || (emailMode == EventEmailModeConstants.ONE_TIME_TO_EVENT_DATE && sendMailDateDiff < 0  && eventDateDiff <= 1)
                                            || (emailMode == EventEmailModeConstants.DIRECTLY_TO_EVENT_PERSON && sendMailDateDiff < 0 && eventDateDiff == 0);

                    Debug.info ( LOGINFO + "Event is eligible to check if need to send mail is [" + eventEligible + "].");
                    
                    boolean sendMail = false;
                    
                    if (eventEligible)
                    {
                        String to, from, cc, bcc, subject, matter;
                        to =  from = cc =  bcc =  subject =  matter = null;

                        /* If the event is on same date */
                        subject = " - " + ManageEvents.getEventAlias ( event_type ) + " - " + event_user;
                        if (eventDateDiff == 0)
                        {
                            to = receiver_email;
                            bcc = from = user_email;
                            bcc += "," + replacements.getReplacement ("<admin_email_bcc>");
                            subject = replacements.getReplacement ("<congrates_heading>") + subject;
                            String fileName = (String) BasicContextManager.getServletContextParamVal (ServletConstants.ON_DAY_GREETING_EMAIL_SOURCE );
                            //String fileName = "E:\\i312\\tomcat\\webapps\\svws\\resources\\properties\\OnDayGreetingMessage.html";
                            matter = ReadSource.getFileContents ( fileName );
                            //matter = "OnDayGreetingMessage";
                            sendMail = true;
                        }
                        else if (eventDateDiff <= daysNotification && eventDateDiff > 0)
                        {
                            to = user_email;
                            from = replacements.getReplacement ("<admin_email>");
                            bcc = replacements.getReplacement ("<admin_email_bcc>");
                            subject = replacements.getReplacement ("<reminder_heading>") + subject;
                            String fileName = (String) BasicContextManager.getServletContextParamVal (ServletConstants.GREETING_EMAIL_SOURCE );
                            //String fileName = "E:\\i312\\tomcat\\webapps\\svws\\resources\\properties\\GreetingMessage.html";
                            matter = ReadSource.getFileContents ( fileName );
                            //matter = "GreetingMessage";
                            sendMail = true;
                        }

                        if (sendMail)
                        {
                            if (!StringUtils.hasValue ( matter ))
                            {
                                String msg = LOGINFO + "Matter to send in mail is null.";
                                Debug.error (msg);
                                throw new ProcessingException (msg);
                            }

                            //todo use event_user
                            matter = matter.replaceAll ("<first_name>", first_name );
                            matter = matter.replaceAll ("<last_name>", last_name );
                            matter = matter.replaceAll ("<user_name>", user_name );
                            matter = matter.replaceAll ("<event_user>", event_user );
                            matter = matter.replaceAll ("<event_desc>", event_desc );
                            matter = matter.replaceAll ("<event_type>", ManageEvents.getEventAlias ( event_type ));
                            matter = matter.replaceAll ("<event_date>", event_date );
                            matter = matter.replaceAll ("<admin_email>", replacements.getReplacement ("<admin_email>") );
                            matter = matter.replaceAll ("<receiver_email>", receiver_email );
                            matter = matter.replaceAll ("<website>", replacements.getReplacement ("<website>") );
                            matter = matter.replaceAll ("<today_date>", DateUtils.getFormattedDate(UtilConstants.DATE_FORMAT_DD_MMM_YYYY));

                            boolean status = ManageEmails.sendMail ( to, from, cc, bcc, subject, matter);

                            if (status)
                            {
                                mailsSend.add ( "event id [" + event_id + "], user_name [" + user_name + "], event_date [" + event_date
                                    + "], event_type [" + event_type + "], event_desc [" + event_desc
                                    + "], event_user [" + event_user + "], email_mode [" + email_mode
                                    + "]" );
                                Debug.info ( LOGINFO + "Email send successfully for event id [" + event_id + "].");
                                event_ids_to_update.add ( new Integer (event_id) );
                            }
                            else
                                Debug.warning ( LOGINFO + "Email could not send.");
                        }
                    }

                    if (!sendMail)
                        Debug.info ( LOGINFO + "Email skipped for this event as per conditions.");
                }
            }
            else
                Debug.info ( LOGINFO + "No event found to mail.");


            int totalSendMails = mailsSend.size ();
            Debug.info ( LOGINFO + "Total [" + totalSendMails + "] email(s) send against events.");

            if (totalSendMails > 0)
            {
                Debug.info ( LOGINFO + "Below is the information: ");

                for (int num = 0; num < totalSendMails; num++)
                {
                    Debug.info ( LOGINFO + "Email [" + (num+1) + "]: " + mailsSend.get ( num ));
                }

                Debug.info ( LOGINFO + "Updating send date of emails for events...");

                StringBuffer updatesql = new StringBuffer ("UPDATE SV_EVENTS SET EMAIL_SEND_DATE = \'");
                updatesql.append ( DateUtils.getFormattedDate(UtilConstants.DATE_FORMAT_DD_MMM_YYYY) ).append ( "\' WHERE EVENT_ID IN (" );

                int len = event_ids_to_update.size ();

                for (int num = 0; num < len; num++)
                {
                    if (num != 0)
                        updatesql.append ( ", " );
                    updatesql.append ( ((Integer) event_ids_to_update.get ( num )).intValue ());
                }
                updatesql.append ( ") " );

                ManageEvents.updateEvents ( updatesql.toString () );
            }
        }
        catch (EventException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
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
            throw new ProcessingException ( msg );
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
        }
    }

    /**
     * Get date difference with current date.
     * 
     * @param event_date date of event
     * @return int difference in days
     * @throws ParseException on error
     * @throws ProcessingException on error
     */
    public static int getDateDiff ( String event_date ) throws ParseException, ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator", "getDateDiff" );

        Debug.info ( LOGINFO + "Computing date difference for date [" + event_date + "].");

        Calendar currentCal = Calendar.getInstance ();
        int HRS = currentCal.get ( Calendar.HOUR_OF_DAY ), MINS = currentCal.get ( Calendar.MINUTE );
        Date currentDate = currentCal.getTime ();
        int year = currentCal.get ( Calendar.YEAR );

        // here not taking from DateUtils as we need DateFormat obj
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Date temp_event_date = df.parse(event_date + " " + HRS + ":" + MINS);
        Calendar cal = df.getCalendar ();

        int day = cal.get ( Calendar.DAY_OF_MONTH );
        int month = cal.get ( Calendar.MONTH );

        temp_event_date = new GregorianCalendar (year, month, day, HRS, MINS).getTime ();

        long t1 = temp_event_date.getTime ();
        long t2 = currentDate.getTime ();
        long daySecs = 1000*60*60*24;

        int diff = Math.round ((float) (t1 - t2) / daySecs);

        Debug.info ( LOGINFO + "for event_date [" + event_date + "], the date difference from current date ["+ DateUtils.getFormattedDate(UtilConstants.DATE_FORMAT_DD_MMM_YYYY) +"] is [" + diff + "]");

        return diff;
    }

    private static synchronized void startReloadThread (long reloadInterval)
    {
        final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator", "startReloadThread" );
        // Start the background reload thread.
        if (reloadThread == null)
        {
            Debug.info ( LOGINFO + "Creating reloadThread first time for interval [" + reloadInterval + "] minutes");
            reloadThread = new Thread(new ReloadThread(reloadInterval));
            Debug.info ( LOGINFO + "Thread created. Setting Daemon...");
            reloadThread.setDaemon(true);
            Debug.info ( LOGINFO + "Thread set to be Daemon. Starting thread...");
            reloadThread.start();
            Debug.info ( LOGINFO + "Thread started.");
        }
    }

    private static class ReloadThread implements Runnable
    {
        private long reloadInterval = 5;
        private static final long MSEC_PER_MINUTE = 60 * 1000;

        public ReloadThread(long reloadInterval) {
            final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator.ReloadThread", "constructor" );
            if (reloadInterval < 5)
            {
                Debug.info ( LOGINFO + "Parameter reloadInterval passed as [" + reloadInterval + "].");
                Debug.info ( LOGINFO + "As it is less than 5 minutes, setting reloadInterval to 5.");
                this.reloadInterval = MSEC_PER_MINUTE * 5;
            }
            else
                this.reloadInterval = MSEC_PER_MINUTE * reloadInterval;
        }

        public void run()
        {
            final String LOGINFO = LogInfo.getLogInfo ("EventsEmailGenerator.ReloadThread", "run" );
            try
            {
                do
                {
                    Debug.info ( LOGINFO + "Thread is about to sleep for interval [" + reloadInterval + "] msec i.e [" + (reloadInterval / MSEC_PER_MINUTE) + "] mins.");
                    Thread.sleep(reloadInterval);
                    Debug.info ( LOGINFO + "Thread is awake after interval [" + reloadInterval + "] msec i.e [" + (reloadInterval / MSEC_PER_MINUTE) + "] mins.");
                    if (Thread.currentThread().isInterrupted()) {
                        Debug.info ( LOGINFO + "Thread interrupted. Breaking loop...");
                        break;
                    }
                    Debug.info ( LOGINFO + "Flushing properties...");
                    emailGenerator = null;
                    Debug.info (LOGINFO + "Flushed successfully. Creating new instance...");
                    emailGenerator = getInstance();
                    emailGenerator.init ();
                    Debug.info (LOGINFO + "Events done after interval [" + reloadInterval + "].");
                } while (true);
            }
            catch (Exception e)
            {
                String msg = LOGINFO + e.getMessage ();
                Debug.error (msg, e);
            }
        }
    }

    public static void main ( String[] args ) throws ProcessingException
    {
        EventsEmailGenerator.getInstance ();
    }
}
