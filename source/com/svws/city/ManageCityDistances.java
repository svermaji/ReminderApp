package com.svws.city;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.db.DBUtil;
import com.svws.common.db.DBConstants;
import com.svws.common.db.DBException;
import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ProcessingException;

import java.util.*;

/**
 * Created by
 * User: sverma
 * Date: Mar 8, 2006
 * Time: 4:55:45 PM
 */
/**
 * This class contains the operation related to events
 */
public final class ManageCityDistances
{

    final static String KEY_SEP = "-";
    final static String SPACE = " ";
    final static String COMMA = ",";
    private static Thread reloadThread;
    private static ManageCityDistances thisObj;

    // this cache will hold the from_city as key and set of to_cities as value
    private static HashMap toFromCities = null;
    private static ArrayList cities = null;
    // this cache will hold the <from_city>-<to_city> as key and <distance>-<time> as value
    private static HashMap citiesDistance = null;

    /**
     * Private constructor
     */
    private ManageCityDistances ()
    {
    }

    /**
     * Static block to initiate the class at loading time
     * and start timer.
     */
    public static ManageCityDistances getInstance () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances", "getInstance" );

        if (thisObj == null)
        {
            try {
                int defaultReloadTime = 30;
                //Object prm = BasicContextManager.getServletContextParamVal (ServletConstants.RELOAD_TIME_IN_MIN, new Integer ( defaultReloadTime ));
                //int reloadTime = Integer.parseInt((String) prm);
                int reloadTime = 30;
                if (reloadTime > 40 && reloadTime < 10)
                    reloadTime = 30;

                Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");

                startReloadThread (reloadTime);
                init ();
                thisObj = new ManageCityDistances();
            }
            catch(Exception e)
            {
                Debug.error (LOGINFO + e, e);
            }
        }
        return thisObj;
    }

    /**
     * Initialize method
     */
    private static void init () throws CityDistancesException, ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances", "init" );

        //initializing two caches toFromCities and citiesDistance
        cities = new ArrayList();
        toFromCities = new HashMap();
        citiesDistance = new HashMap();

        Vector orderBy = new Vector();
        orderBy.add(DBConstants.CITY_DISTANCES_TAB_KMS);
        Vector records = selectCityDistances (null, null, orderBy, true);

        for (int row = 0; row < records.size(); row++)
        {
            Vector rec = (Vector) records.get(row);
            int cityDistanceId = StringUtils.getInteger( (String) rec.get (0)); // column-1 CITY_DISTANCE_ID
            String fromCity = (String) rec.get (1); // column-2 FROM_CITY
            String toCity = (String) rec.get (2); // column-3 TO_CITY
            String appxTimeHrs = (String) rec.get (3); // column-4 APPX_TIME_HRS
            int kms = StringUtils.getInteger ((String) rec.get (4)); // column-5 KMS

            // storing as processed information

            String key = fromCity + KEY_SEP + toCity;
            String val = kms + " Kms";

            if (StringUtils.hasValue(appxTimeHrs, true))
                val += COMMA + SPACE + appxTimeHrs;

            citiesDistance.put(key.toLowerCase(), val);

            if (!cities.contains(toCity))
                cities.add(toCity);
            if (!cities.contains(fromCity))
                cities.add(fromCity);

            // if fromCity is already present then its set will be updated else new one will be made
            if (toFromCities.containsKey(fromCity))
            {
                HashSet temp = (HashSet) toFromCities.get(fromCity);
                if (!temp.contains(toCity))
                    temp.add(toCity);
            }
            else
            {
                HashSet temp = new HashSet ();
                temp.add(toCity);
                toFromCities.put(fromCity.toLowerCase(), temp);
            }
        }

        Debug.info (LOGINFO + "Cities Distance hash map loaded with total [" + citiesDistance.size() + "] values." );
    }


    /**
     * Selects the CityDistances based on criteria.
     * Making private to not allow operation from outer class
     *
     * @param columns as vector
     * @param where clause
     * @param orderBy as vector
     * @param ascending boolean
     * @return Vector of CityDistances
     * @throws CityDistancesException on error
     */
    public static Vector selectCityDistances (Vector columns, Hashtable where, Vector orderBy, boolean ascending) throws CityDistancesException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances", "selectCityDistances" );
        try
        {
            Debug.info (LOGINFO + "Record selected." );
            return DBUtil.selectRecords (DBConstants.CITY_DISTANCES_TABLE, columns, where, orderBy, ascending);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new CityDistancesException (e.getMessage ());
        }
    }

    /**
     * Select event based on sql.
     * Making private to not allow operation from outer class
     *
     * @param sql
     * @return Vector of events.
     * @throws CityDistancesException
     */
    public Vector selectCityDistances (String sql) throws CityDistancesException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances", "selectCityDistances...sql" );
        try
        {
            Debug.info (LOGINFO + "Record selected." );
            return DBUtil.selectRecords (sql);
        }
        catch (DBException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new CityDistancesException (e.getMessage ());
        }
    }

    public String getKms(String fromCity, String toCity) throws CityDistancesException
    {
        String distance = "N/A";

        String keyToFrom = toCity.toLowerCase() + KEY_SEP + fromCity.toLowerCase();
        String keyFromTo = fromCity.toLowerCase() + KEY_SEP + toCity.toLowerCase();
        if (citiesDistance.containsKey(keyToFrom))
            distance = (String) citiesDistance.get(keyToFrom);
        else if (citiesDistance.containsKey(keyFromTo))
            distance = (String) citiesDistance.get(keyFromTo);

        return distance;
    }

    public String getDistance(String fromCity, String toCity) throws CityDistancesException
    {
        return getKms(fromCity, toCity);
    }

    public TreeSet getCityNames (String startsWith) throws CityDistancesException
    {
        TreeSet t = new TreeSet();
        Iterator itar = cities.iterator();
        while (itar.hasNext())
        {
            String city = (String) itar.next();
            if (city.toLowerCase().startsWith(startsWith.toLowerCase()))
                t.add(city);
        }
        return t;
    }

    private Vector getCities (String fromCity, String toCity) throws CityDistancesException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances", "getCities" );

        // comparing both fromCity and toCity in both the columns TO_CITY and FROM_CITY
        String sql = new StringBuilder ().append("select ")
            .append(DBConstants.CITY_DISTANCES_TAB_FROM_CITY)
            .append(COMMA)
            .append(DBConstants.CITY_DISTANCES_TAB_TO_CITY)
            .append(" from ")
            .append(DBConstants.CITY_DISTANCES_TABLE)
            .append(" Where (lower(")
            .append(DBConstants.CITY_DISTANCES_TAB_FROM_CITY)
            .append(") like '%")
            .append(fromCity.toLowerCase())
            .append("%' and lower(")
            .append(DBConstants.CITY_DISTANCES_TAB_TO_CITY)
            .append(") like '%")
            .append(toCity.toLowerCase())
            .append("%') ")
            .append(" OR (lower(")
            .append(DBConstants.CITY_DISTANCES_TAB_FROM_CITY)
            .append(") like '%")
            .append(toCity.toLowerCase())
            .append("%' and lower(")
            .append(DBConstants.CITY_DISTANCES_TAB_TO_CITY)
            .append(") like '%")
            .append(fromCity.toLowerCase())
            .append("%') ")
            .toString();

        Debug.info ( LOGINFO + "sql [" + sql + "]");

        return selectCityDistances(sql);
    }

    /**
     * start the reload thread
     *
     * @param reloadInterval
     */
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances", "startReloadThread" );
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
            final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances.ReloadThread", "constructor" );
            if (reloadInterval < 5)
            {
                Debug.info ( LOGINFO + "Parameter reloadInterval passed as [" + reloadInterval + "].");
                Debug.info ( LOGINFO + "As it is less than 5 minutes, setting reloadInterval to 5.");
                this.reloadInterval = MSEC_PER_MINUTE * 5;
            }
            else
                this.reloadInterval = MSEC_PER_MINUTE * reloadInterval;
        }

        /**
         * run method
         */
        public void run()
        {
            final String LOGINFO = LogInfo.getLogInfo ("ManageCityDistances.ReloadThread", "run" );
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
                    Debug.info (LOGINFO + "Reloading events properties...");
                    init ();
                    Debug.info (LOGINFO + "Reloaded successfully.");
                } while (true);
            }
            catch (Exception e)
            {
                String msg = LOGINFO + e.getMessage ();
                Debug.error (msg, e);
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            ManageCityDistances.getInstance().init ();
            //Vector rows = ManageCityDistances.getCities ("Mumbai", "Indore");
            Vector rows = ManageCityDistances.getInstance().getCities ("I", "M");

            for (int row = 0; row < rows.size(); row++)
            {
                Vector rec = (Vector) rows.get(row);
                String fromCity = (String) rec.get (0);
                String toCity = (String) rec.get (1);

                System.out.println("toCity = " + toCity + ", fromCity = " + fromCity);
                System.out.println("getDistance(toCity, fromCity) = " + ManageCityDistances.getInstance().getDistance(toCity, fromCity));
            }
        }
        catch (CityDistancesException e)
        {
            e.printStackTrace();  
        }
        catch (ProcessingException e)
        {
            e.printStackTrace();
        }
    }

}