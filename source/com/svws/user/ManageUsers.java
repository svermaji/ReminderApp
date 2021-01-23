package com.svws.user;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBUtil;
import com.svws.common.db.DBConstants;
import com.svws.common.db.DBException;
import com.svws.common.Base64Encoder;

import java.util.Vector;
import java.util.Hashtable;

/**
 * Created by
 * User: sverma
 * Date: Mar 8, 2006
 * Time: 4:55:45 PM
 */
/**
 * This class contains the operation related to user
 */
public final class ManageUsers
{
    private ManageUsers ()
    {
    }

    public static boolean handleUser (User user) throws UserException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageUsers", "handleUser" );
        try
        {
            Hashtable attributes = new Hashtable ();
            Debug.info (LOGINFO + "adding values from bean in attributes...");
            if (user.getUserId () == 0)
            {
                attributes.put (DBConstants.USER_NAME, user.getUserName ());
                Debug.info (LOGINFO + "added as [" + user.getUserName () + "]" );
            }
            attributes.put (DBConstants.USER_TAB_PASSWORD, Base64Encoder.encode ( user.getPassword ()));
            Debug.info (LOGINFO + "added as [" + Base64Encoder.encode ( user.getPassword ()) + "]");
            attributes.put (DBConstants.USER_TAB_FIRST_NAME, user.getFirst_name ());
            Debug.info (LOGINFO + "added as [" + user.getFirst_name () + "]" );
            attributes.put (DBConstants.USER_TAB_LAST_NAME, user.getLast_name ());
            Debug.info (LOGINFO + "added as [" + user.getLast_name () + "]" );
            attributes.put (DBConstants.USER_TAB_GENDER, user.getGender ());
            Debug.info (LOGINFO + "added as [" + user.getGender () + "]" );
            attributes.put (DBConstants.USER_TAB_ADDR, user.getAddress ());
            Debug.info (LOGINFO + "added as [" + user.getAddress () + "]" );
            attributes.put (DBConstants.USER_TAB_PHONE, user.getPhone ());
            Debug.info (LOGINFO + "added as [" + user.getPhone () + "]" );
            attributes.put (DBConstants.USER_TAB_BIRTH_DATE, user.getBirth_date ());
            Debug.info (LOGINFO + "added as [" + user.getBirth_date () + "]" );
            attributes.put (DBConstants.USER_TAB_CITY, user.getCity ());
            Debug.info (LOGINFO + "added as [" + user.getCity () + "]" );
            attributes.put (DBConstants.USER_TAB_ZIP, user.getZip ());
            Debug.info (LOGINFO + "added as [" + user.getZip () + "]" );
            attributes.put (DBConstants.USER_TAB_STATE, user.getState ());
            Debug.info (LOGINFO + "added as [" + user.getState () + "]" );
            attributes.put (DBConstants.USER_TAB_COUNTRY, user.getCountry ());
            Debug.info (LOGINFO + "added as [" + user.getCountry () + "]" );
            attributes.put (DBConstants.USER_TAB_EMAIL, user.getEmail ());
            Debug.info (LOGINFO + "added as [" + user.getEmail () + "]" );

            if (user.getUserId () > 0)
            {
                Hashtable where = new Hashtable ();
                where.put (DBConstants.USER_TAB_USER_ID, new Integer ( user.getUserId ()));
                where.put (DBConstants.USER_NAME, user.getUserName ());
                return updateUsers (attributes, where);
            }
            else
            {
                return insertUsers (attributes);
            }
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new UserException (e.getMessage ());
        }
    }

    public static boolean insertUsers (Hashtable attributes) throws UserException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageUsers", "insertUsers" );
        try
        {
            Hashtable where = new Hashtable ();
            where.put (DBConstants.USER_NAME, attributes.get ( DBConstants.USER_NAME ));
            Vector vRec = selectUsers (null, where, null, false );
            if (vRec.size () > 0)
            {
                Debug.error ( LOGINFO + "Record exists." );
                throw new UserException (LOGINFO + "User ["+ attributes.get ( DBConstants.USER_NAME ) +"] already created. Select another.");
            }
            Debug.info (LOGINFO + "Inserting..." );
            int user_id = getNextValOfSequence ();
            attributes.put ( DBConstants.USER_TAB_USER_ID, user_id+"");
            return DBUtil.insertRow (DBConstants.USER_TABLE, attributes);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new UserException (e.getMessage ());
        }
    }

    /**
     * Obtains the next value of sequence "SEQ_SV_USERS"
     * @return int
     * @throws DBException on error
     */
    private static int getNextValOfSequence () throws DBException
    {
        return DBUtil.getNextValOfSequence ("SEQ_SV_USERS");
    }

    public static boolean updateUsers (Hashtable attributes, Hashtable where) throws UserException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageUsers", "updateUsers" );
        try
        {
            Debug.info (LOGINFO + "Updating...." );
            return DBUtil.updateRows (DBConstants.USER_TABLE, attributes, where);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new UserException (e.getMessage ());
        }
    }

    public static boolean deleteUsers (Hashtable where) throws UserException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageUsers", "deleteUsers" );
        try
        {
            Debug.info (LOGINFO + "Deleting...." );
            return DBUtil.deleteRows (DBConstants.USER_TABLE, where);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new UserException (e.getMessage ());
        }
    }

    public static Vector selectUsers (Vector columns, Hashtable where, Vector orderBy, boolean ascending) throws UserException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageUsers", "selectUsers" );
        try
        {
            Debug.info (LOGINFO + "Selecting..." );
            return DBUtil.selectRecords (DBConstants.USER_TABLE, columns, where, orderBy, ascending);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new UserException (e.getMessage ());
        }
    }

    public static void main ( String[] args ) throws UserException
    {
        User bean = new User ();
        bean.setFirst_name ( "kk" );
        bean.setLast_name ( "kk" );
        bean.setEmail ( "tt@tt.com" );
        bean.setPassword ( "kk" );
        bean.setUserId ( 7 );
        bean.setUserName ( "kk" );
        bean.setGender ( "M" );
        bean.setCountry ( "India" );
        handleUser ( bean );
    }
}