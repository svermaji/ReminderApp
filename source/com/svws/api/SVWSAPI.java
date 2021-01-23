package com.svws.api;

import com.svws.common.db.DBConstants;
import com.svws.common.db.DBUtil;
import com.svws.common.utils.ProcessingException;
import com.svws.common.log.Debug;
import com.svws.events.ManageEvents;
import com.svws.events.EventException;

import java.util.Vector;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jul 14, 2008
 * Time: 6:33:30 PM
 */
public class SVWSAPI 
{
    public String getEvents (String user)
    {
        String sql = "select " + DBConstants.EVENT_TAB_EVENT_ID  // 0
            + ", " + DBConstants.EVENT_TAB_ACTIVE                // 1
            + ", " + DBConstants.EVENT_TAB_EMAIL_MODE            // 2
            + ", " + DBConstants.EVENT_TAB_EVENT_DATE            // 3
            + ", " + DBConstants.EVENT_TAB_EVENT_TYPE
            + ", " + DBConstants.EVENT_TAB_EVENT_USER
            + ", " + DBConstants.EVENT_TAB_RECEIVER_MAIL
            + " from sv_events where "
            + DBConstants.USER_NAME + " = '" + user + "' "
            + " order by to_char(to_date(event_date), 'MM'), to_char(to_date(event_date), 'DD')";

        String result = ""; 
        try
        {
            Vector vRecords = ManageEvents.selectEvents ( sql );
            result = DBUtil.getDBResultAsStr(vRecords);
        }
        catch (EventException e)
        {
            Debug.error(e.toString(), e);
        }
        catch (ProcessingException e)
        {
            Debug.error(e.toString(), e);
        }
        catch (Exception e)
        {
            Debug.error(e.toString(), e);
        }
        return result;
    }
}
