package com.svws.events;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 23, 2007
 * Time: 3:53:43 PM
 */
/**
 * This class is a bean for table "sv_events"
 */
public class Event
{
    private int event_id;
    private String user_name;
    private String event_type;
    private String event_user;

    private String event_desc;
    private String receiver_email;
    private String event_date;

    private String email_send_date;
    private int email_mode, active;

    public int getEvent_id ()
    {
        return event_id;
    }

    public void setEvent_id ( int event_id )
    {
        this.event_id = event_id;
    }

    public String getUser_name ()
    {
        return user_name;
    }

    public void setUser_name ( String user_name )
    {
        this.user_name = user_name;
    }

    public String getEvent_type ()
    {
        return event_type;
    }

    public void setEvent_type ( String event_type )
    {
        this.event_type = event_type;
    }

    public String getEvent_desc ()
    {
        return event_desc;
    }

    public void setEvent_desc ( String event_desc )
    {
        this.event_desc = event_desc;
    }

    public String getReceiver_email ()
    {
        return receiver_email;
    }

    public void setReceiver_email ( String receiver_email )
    {
        this.receiver_email = receiver_email;
    }

    public String getEvent_date ()
    {
        return event_date;
    }

    public void setEvent_date ( String event_date )
    {
        this.event_date = event_date;
    }

    public String getEmail_send_date ()
    {
        return email_send_date;
    }

    public void setEmail_send_date ( String email_send_date )
    {
        this.email_send_date = email_send_date;
    }

    public int getEmail_mode ()
    {
        return email_mode;
    }

    public void setEmail_mode ( int email_mode )
    {
        this.email_mode = email_mode;
    }

    public int getActive ()
    {
        return active;
    }

    public void setActive ( int active )
    {
        this.active = active;
    }
    
    public String getEvent_user ()                            
    {
        return event_user;
    }

    public void setEvent_user ( String event_user )
    {
        this.event_user = event_user;
    }

}
