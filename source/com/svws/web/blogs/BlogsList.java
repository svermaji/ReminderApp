package com.svws.web.blogs;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.BasicContextManager;
import com.svws.common.utils.ProcessingException;
import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ServletConstants;
import com.svws.common.utils.ReadSource;

import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 25, 2007
 * Time: 1:05:12 PM
 */
/**
 * This class returns the list of the files
 * in the source directory under blogs
 * package.
 *
 * This class flushes from BasicContextManager
 * to reload the file names dynamically
 * time to time.
 */
public class BlogsList
{
    // TreeSet to hold file names in sorted order
    //private static Thread reloadThread;
    private static TreeMap fileNames = null;
    private static BlogsList blogs = null;
    private static String blogsDir = null;

    /**
     * private constructor to avoid object initialization
     */
    private BlogsList ()
    {
    }

    /**
     * Create instance of BlogsList for first time and then
     * returns from cache for the next time.
     *
     * @return BlogsList object
     * @throws ProcessingException
     */
    public static BlogsList getInstance () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("BlogsList", "getInstance" );
        synchronized ( BlogsList.class )
        {
            if (blogs == null)
            {
                blogs = new BlogsList();
            }
                String dirName = (String) BasicContextManager.getServletContextParamVal (ServletConstants.BLOGS_SRC);
                blogsDir = dirName;
                Debug.info (LOGINFO + "Source obtained from context is [" + dirName + "]");

                //int reloadTime = BasicContextManager.getValueOfReloadProperty ();
                //int reloadTime = 30;
                //Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");

                //startReloadThread (reloadTime);
                //String dirName = "C:/Tomcat5.5/webapps/svws/resources/source/blogs";
            //}
            blogs.init (dirName);
        }
        return blogs;
    }

    /**
     * The file name that starts with "blogs-" will only be included
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
        final String LOGINFO = LogInfo.getLogInfo ("BlogsList", "init" );
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
            if (file.startsWith (ServletConstants.BLOGS_FILE_PREFIX) && !fileNames.containsKey (file))
            {
                Debug.info (LOGINFO + "adding file [" + file + "]");
                synchronized ( fileNames )
                {
                    fileNames.put(file, fileList [elem]);
                }
            }
            else
                Debug.info (LOGINFO + "skipping file [" + file + "] as either this entry is present or name does not starts with [" + ServletConstants.BLOGS_FILE_PREFIX + "] or insufficient length");
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
        final String LOGINFO = LogInfo.getLogInfo ("BlogsList", "flush" );
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
     * @return treeset
     */
    public TreeMap getBlogsListFiles ()
    {
        return getBlogsListFiles ("ALL");
    }

    /**
     * Returns the filenames arranged in ascending order
     * @param userName filter results for userName
     * @return treeset
     */
    public TreeMap getBlogsListFiles (String userName)
    {
        final String LOGINFO = LogInfo.getLogInfo ("BlogsList", "getBlogsListFiles" );
        if (!StringUtils.hasValue(userName) || (StringUtils.hasValue(userName) && userName.equals("ALL")))
        {
            Debug.info (LOGINFO + "returning all file names.");
            return fileNames;
        }

        Debug.info (LOGINFO + "User name as param is [" + userName + "].");
        TreeMap tempFNs = new TreeMap();
        Iterator itar = fileNames.keySet().iterator();
        while (itar.hasNext())
        {
            String key = (String) itar.next();
            File keyVal = (File) fileNames.get(key);
            if (key.toLowerCase().startsWith(ServletConstants.BLOGS_FILE_PREFIX + userName.toLowerCase()))
            {
                Debug.info (LOGINFO + "Including key [" + key + "].");
                tempFNs.put(key, keyVal);
            }
        }
        Debug.info (LOGINFO + "Returning total [" + tempFNs.size() + "] file names.");
        return tempFNs;
    }

    /**
     * test method
     *
     * @param args
     * @throws ProcessingException
     */
    public static void main(String[] args)  throws ProcessingException
    {
        BlogsList pp = BlogsList.getInstance ();
        System.out.println ("pp.getBlogsListFiles = " + pp.getBlogsListFiles ());
    }

    /**
     * start the reload thread
     *
     * @ param reloadInterval
     */
/*
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("BlogsList", "startReloadThread" );
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
*/

    public String getBlogsDir()
    {
        return blogsDir;
    }

    /**
     * Inner ReloadThread class
     */
/*
    private static class ReloadThread implements Runnable
    {
        private long reloadInterval = 5;
        private static final long MSEC_PER_MINUTE = 60 * 1000;

        */
/**
         * Constructor
         *
         * @param reloadInterval
         */
/*
        public ReloadThread(long reloadInterval) {
            final String LOGINFO = LogInfo.getLogInfo ("BlogsList.ReloadThread", "constructor" );
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

        */
/**
         * run method
         */
/*
        public void run()
        {
            final String LOGINFO = LogInfo.getLogInfo ("BlogsList.ReloadThread", "run" );
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
                    synchronized (blogs)
                    {
                        Debug.info ( LOGINFO + "In synchronized block...");
                        blogs.flush ();
                        Debug.info ( LOGINFO + "Flushing static object of a class...");
                        blogs = null;
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
*/
}