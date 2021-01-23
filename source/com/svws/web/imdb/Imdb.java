package com.svws.web.imdb;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.BasicContextManager;
import com.svws.common.utils.ProcessingException;
import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ServletConstants;
import com.svws.common.utils.ReadSource;

import java.util.TreeSet;
import java.util.TreeMap;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 25, 2007
 * Time: 1:05:12 PM
 */
/**
 * This class returns the list of the files
 * in the source directory under imdb
 * package.
 *
 * This class flushes from BasicContextManager
 * to reload the file names dynamically
 * time to time.
 */
public class Imdb
{
    // TreeMap to hold file names in sorted order
    private static Thread reloadThread;
    private static TreeMap fileNames = null;
    private static Imdb imdb = null;

    /**
     * private constructor to avoid object initialization
     */
    private Imdb ()
    {
    }

    /**
     * Create instance of Imdb for first time and then
     * returns from cache for the next time.
     *
     * @return Imdb object
     * @throws ProcessingException
     */
    public static Imdb getInstance () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("Imdb", "getInstance" );
        synchronized ( Imdb.class )
        {
            if (imdb == null)
            {
                imdb = new Imdb();
                String dirName = (String) BasicContextManager.getServletContextParamVal (ServletConstants.IMDB_SRC);
                Debug.info (LOGINFO + "Source obtained from context is [" + dirName + "]");

                int reloadTime = BasicContextManager.getValueOfReloadProperty ();
                Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");

                startReloadThread (reloadTime);
                imdb.init (dirName);
            }
        }
        return imdb;
    }

    /**
     * The file name that starts with "imdb-" will only be included
     *
     * This method on the basis of passed
     * dirName reads the directory contents
     * and populates the file names in ascending order.
     * This class reads the file name from parent directory
     * only and does not from sub-folders.
     *
     * @param dirName - directory name to read the file names from
     * @throws ProcessingException
     */
    private void init (String dirName) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("Imdb", "init" );
        if (!StringUtils.hasValue ( dirName ))
        {
            String msg = LOGINFO + "Unable to get file names as null passed as Directory name.";
            Debug.error ( msg );
            throw new ProcessingException (msg);
        }
        Debug.info (LOGINFO + "dirName passed as [" + dirName + "]");
        File[] fileList = ReadSource.getFileList(dirName);
        fileNames = new TreeMap ();
        int fileListSize = fileList.length;
        for (int elem = 0; elem < fileListSize; elem++) {
            String file = fileList[elem].getName();
            if (file.startsWith (ServletConstants.IMDB_FILE_PREFIX) && !fileNames.containsKey (file))
            {
                Debug.info (LOGINFO + "adding file [" + file + "]");
                synchronized ( fileNames )
                {
                    fileNames.put(file, fileList [elem]);
                }
            }
            else
                Debug.info (LOGINFO + "skipping file [" + file + "] as either this entry is present or name does not starts with [" + ServletConstants.IMDB_FILE_PREFIX + "] or insufficient length");
        }
        Debug.info (LOGINFO + "Total [" + fileNames.size() + "] files added.");
    }

    /**
     * Method to flush the file names.
     * So if new files are added then
     * they would loaded dynamically.
     *
     * @throws ProcessingException
     */
    private synchronized void flush () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("Imdb", "flush" );
        try
        {
            Debug.info (LOGINFO + "Flushing files.");

            synchronized ( fileNames )
            {
                if (fileNames != null)
                {
                    fileNames.clear();
                    Debug.info (LOGINFO + "FileNames cleared.");
                    fileNames = null;
                }
                else
                    Debug.info (LOGINFO + "FileNames already null.");
            }

            Debug.info (LOGINFO + "Flushed successfully.");
        }
        catch (Exception e)
        {
            String msg = LOGINFO + "Error while flushing files." + e.getMessage ();
            Debug.error ( msg, e);
            throw new ProcessingException (msg);
        }
    }

    /**
     * Returns the filenames arranged in ascending order
     * @return TreeMap
     */
    public TreeMap getImdbFiles ()
    {
        return fileNames;
    }

    /**
     * test method
     *
     * @param args
     * @throws ProcessingException
     */
    public static void main(String[] args)  throws ProcessingException
    {
        Imdb pp = Imdb.getInstance ();
        System.out.println ("pp.getImdbFiles = " + pp.getImdbFiles ());
    }

    /**
     * start the reload thread
     *
     * @param reloadInterval
     */
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("Imdb", "startReloadThread" );
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
            final String LOGINFO = LogInfo.getLogInfo ("Imdb.ReloadThread", "constructor" );
            if (reloadInterval < 5)
            {
                Debug.info ( LOGINFO + "Parameter reloadInterval passed as [" + reloadInterval + "].");
                Debug.info ( LOGINFO + "As it is less than 5 minutes, setting reloadInterval to 5.");
                this.reloadInterval = MSEC_PER_MINUTE * 5;
            }
            else
                this.reloadInterval = MSEC_PER_MINUTE * reloadInterval;

            //this.reloadInterval = MSEC_PER_MINUTE * 2;
        }

        /**
         * run method
         */
        public void run()
        {
            final String LOGINFO = LogInfo.getLogInfo ("Imdb.ReloadThread", "run" );
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
                    synchronized (imdb)
                    {
                        Debug.info ( LOGINFO + "In synchronized block...");
                        imdb.flush ();
                        Debug.info ( LOGINFO + "Flushing static object of a class...");
                        imdb = null;
                        getInstance ();
                    }
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
}