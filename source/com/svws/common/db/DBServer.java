package com.svws.common.db;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.BasicContextManager;
import com.svws.common.utils.ServletConstants;
import com.svws.common.utils.ProcessingException;
import com.svws.common.utils.ReadSource;

import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * This class is the Database Server that interacts
 * with the Database and creates the connection, statement, resulteset
 * and resultset meta data.
 */
public class DBServer{

    private static Thread reloadThread;
    private static Driver driver;
    private static Properties DBProperties;
    private static StringBuffer url;
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;
    private static ResultSetMetaData rsmd;
    private static Vector vColNames;
    private static Vector vRecords;
    private static Vector vTempRecord;

    //Read DB settings from proprety file
    static
    {
        init ();
    }

    /**
     * initialize method
     */
    private static void init ()
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "static block" );
        try
        {
            Properties prop = ReadSource.getResourceProperties ( ServletConstants.DB_PROPERTIES, true );

            //File f = new File ("d:\\tomcat-5.5.26\\webapps\\svws\\resources\\properties\\db.properties");
            //FileInputStream fis = new FileInputStream ( f );
            //Properties prop = new Properties ( );
            //prop.load ( fis );

            DBProperties = new Properties();
            if (prop.getProperty("driver_name")!=null)
            {
                //load the databasae driver
                DBProperties.setProperty("user",(String)prop.getProperty("user") );
                DBProperties.setProperty("password",(String)prop.getProperty("password") );
                driver = (Driver)Class.forName( (String)prop.getProperty("driver_name") ).newInstance();

                //create database url
                url = new StringBuffer("");
                url.append((String)prop.getProperty("database_url"));
                url.append(":@");
                url.append((String)prop.getProperty("server"));
                url.append(":");
                url.append((String)prop.getProperty("port"));
                url.append(":");
                url.append((String)prop.getProperty("sid"));
            }
            else
            {
                DBProperties.setProperty("class", (String)prop.getProperty("class") );
                DBProperties.setProperty("hdbc_url", (String)prop.getProperty("jdbc_url") );
            }

            Debug.info ( LOGINFO + "DB URL is [" + url.toString() + "]");
            int defaultReloadTime = 30;
            //Object prm = BasicContextManager.getServletContextParamVal (ServletConstants.RELOAD_TIME_IN_MIN, new Integer(defaultReloadTime));
            //int reloadTime = Integer.parseInt((String) prm);
            int reloadTime = 30;
            if (reloadTime > defaultReloadTime) reloadTime = defaultReloadTime;
            Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");
            startReloadThread (reloadTime);
        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
    }// End of static block

    /**
     * Method used to get the connection
     *
     * @return Connection object
     */
    private static Connection getConnection()
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "getConnection" );
        try
        {
            synchronized (DBServer.class)
            {
                if (con != null)
                {
                    return con;
                }
                Debug.info ( LOGINFO + "Connecting...");
                if (url != null)
                {
                    con = driver.connect(url.toString(), DBProperties);
                }
                else
                {
                    Class.forName(DBProperties.getProperty ( "class" ));
                    con = DriverManager.getConnection(DBProperties.getProperty ( "jdbc_url" ));
                }
                Debug.info ( LOGINFO + "Connection established");
            }
        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
        return con;
    }

    /**
     * Method used to get the statement
     *
     * @return Statement object
     */
    private static Statement getStatement()
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "getStatement" );
        try
        {
            if (con == null)
            {
                con = getConnection();
            }
            if (stmt != null)
            {
                return stmt;
            }
            synchronized (DBServer.class)
            {
                stmt = con.createStatement();
                Debug.info ( LOGINFO + "Statement created");
            }
        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
        return stmt;
    }

    /**
     * Method used to get the result set
     *
     * @param qry
     * @return ResultSet object
     */
    private static ResultSet getResultSet(String qry)
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "getResultSet" );
        try
        {
            if (con == null) {
                con = getConnection();
            }
            if (stmt == null) {
                stmt = getStatement();
            }
            synchronized (DBServer.class)
            {
                rs = stmt.executeQuery(qry);
                Debug.info ( LOGINFO + "ResultSet created");
            }
        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
        return rs;
    }

    /**
     * Method used to get the result set meta data
     *
     * @return ResultSetMetaData object
     */
    private static ResultSetMetaData getResultSetMetaData()
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "getResultSetMetaData" );
        try
        {
            if (con == null) {
                con = getConnection();
            }
            if (stmt == null) {
                throw new SQLException (LOGINFO + "No Statement present");
            }
            if (rs == null) {
                throw new SQLException (LOGINFO + "No Resultset present");
            }
            synchronized (DBServer.class)
            {
                rsmd = rs.getMetaData();
                Debug.info ( LOGINFO + "ResultSetMetaData created");
            }
        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
        return rsmd;
    }

    /**
     * This method executes the query and produce the resultset
     * then convert all the records in Vector of Vectors
     * where each vector inside MAIN vector represents a row
     * @param qry to execute
     * @return Vector
     */
    public static Vector getRecords(String qry)
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "getRecords" );
        try
        {
            if (con==null) {
                con = getConnection();
            }
            if (stmt==null) {
                stmt = getStatement();
            }
            Debug.info ( LOGINFO + "Executing query [" + qry + "]");
            rs = getResultSet(qry);
            Debug.info ( LOGINFO + "Resultset created.");
            rsmd = getResultSetMetaData();
            Debug.info ( LOGINFO + "ResultsetMetaData created");
            int totalColumns = rsmd.getColumnCount();
            Debug.info ( LOGINFO + "TotalColumns are ["+totalColumns + "]");
            vColNames = new Vector();
            vRecords = new Vector();
            for (int colCount=1; colCount <= totalColumns; colCount++ )
            {
                vColNames.add(rsmd.getColumnName (colCount));
            }
            while (rs.next())
            {
                vTempRecord = new Vector();
                for (int colCount=1; colCount <= totalColumns; colCount++ )
                {
                    vTempRecord.add((colCount-1), rs.getString (vColNames.get(colCount-1).toString()));
                }
                vRecords.add(vTempRecord);
            }
            Debug.info ( LOGINFO + "Total rows are ["+vRecords.size() + "]"); // as row start from 0
        } catch(Exception e) {
            Debug.error (LOGINFO + e, e);
        }
        return vRecords;
    }

    /**
     * This method handles the query regarding insert, update
     * and delete since in this case the statement do not
     * produce any record set
     * @param qry to execute
     * @return status of successful execution
     */
    public static boolean updateRecord (String qry)
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "updateRecords" );
        boolean success=false;
        Debug.info ( LOGINFO + "Query ["+qry + "]");
        try
        {
            if (con == null) {
                con = getConnection();
            }
            if (stmt == null) {
                stmt = getStatement();
            }
            // Not taking any ResultSet as execute command returns void
            stmt.execute(qry);
            success=true;
        } catch(Exception e) {
            Debug.error (LOGINFO + e, e);
        }
        return success;
    }

    /**
     * start the reload thread
     *
     * @param reloadInterval
     */
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "startReloadThread" );
        // Start the background reload thread.
        if (reloadThread == null) {
            Debug.info ( LOGINFO + "Creating reloadThread first time for interval [" + reloadInterval + "] minutes");
            reloadThread = new Thread(new ReloadThread(reloadInterval));
            Debug.info ( LOGINFO + "Thread created. Setting Daemon...");
            reloadThread.setDaemon(true);
            Debug.info ( LOGINFO + "Thread set to be Daemon. Starting thread...");
            reloadThread.start();
            Debug.info ( LOGINFO + "Thread started.");
        }
    }

    /**
     * Inner ReloadThread class
     */
    private static class ReloadThread implements Runnable
    {
        private long reloadInterval = 5;
        private static final long MSEC_PER_MINUTE = 60 * 1000;

        /**
         * Constructor
         *
         * @param reloadInterval
         */
        public ReloadThread(long reloadInterval) {
            final String LOGINFO = LogInfo.getLogInfo ("DBServer.ReloadThread", "constructor" );
            if (reloadInterval < 5)
            {
                Debug.info ( LOGINFO + "Parameter reloadInterval passed as [" + reloadInterval + "].");
                Debug.info ( LOGINFO + "As it is less than 5 minutes, setting reloadInterval to 5.");
                this.reloadInterval = MSEC_PER_MINUTE * this.reloadInterval;
            }
            else
                this.reloadInterval = MSEC_PER_MINUTE * reloadInterval;
        }

        /**
         * run method
         */
        public void run()
        {
            final String LOGINFO = LogInfo.getLogInfo ("DBServer.ReloadThread", "run" );
            try
            {
                do
                {
                    Debug.info ( LOGINFO + "Thread is about to sleep for interval [" + reloadInterval + "] msec i.e [" + (reloadInterval / MSEC_PER_MINUTE) + "] mins.");
                    Thread.sleep(reloadInterval);
                    Debug.info ( LOGINFO + "Thread is awake after interval [" + reloadInterval + "] msec i.e [" + (reloadInterval / MSEC_PER_MINUTE) + "] mins.");
                    if (Thread.currentThread().isInterrupted())
                    {
                        Debug.info ( LOGINFO + "Thread interrupted. Breaking loop...");
                        break;
                    }
                    DBServer.flush ();
                    Debug.info ( LOGINFO + "Flushing static object of a class...");
                    DBServer.init ();
                    Debug.info (LOGINFO + "Flushed successfully.");
                } while (true);
            }
            catch (Exception e)
            {
                String msg = LOGINFO + e.getMessage ();
                Debug.error (msg, e);
            }
        }
    }

    /**
     * flushes the required variables
     * @throws ProcessingException on error
     */
    private static void flush () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("DBServer", "flush" );
        try
        {
            Debug.info (LOGINFO + "Flushing objects...");
            synchronized ( DBServer.class )
            {
                if (rs!=null)
                {
                    rs.close ();
                    rs = null;
                }
                if (stmt!=null)
                {
                    stmt.close ();
                    stmt = null;
                }
                if (con!=null)
                {
                    con.close ();
                    con = null;
                }
            }
            Debug.info (LOGINFO + "Flushed successfully.");
        }
        catch (Exception e)
        {
            String msg = LOGINFO + "Error while flushing files. " + e.getMessage ();
            Debug.error ( msg, e);
            throw new ProcessingException (msg);
        }
    }

    /**
     * test method.
     *
     * @param args
     */
    public static void main ( String[] args )
    {
        getConnection();
    }
}
