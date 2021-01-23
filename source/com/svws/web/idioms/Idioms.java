package com.svws.web.idioms;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.BasicContextManager;
import com.svws.common.utils.ProcessingException;
import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ServletConstants;
import com.svws.xml.XMLParser;

import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 25, 2007
 * Time: 1:05:12 PM
 */
/**
 * This class returns the list of the files
 * in the source idioms
 * package.
 *
 * This class flushes from BasicContextManager
 * to reload the file names dynamically
 * time to time.
 */
public class Idioms
{
    // TreeSet to hold file names in sorted order
    private static Thread reloadThread;
    TreeMap allIdioms;
    private static Idioms idioms;

    /**
     * private constructor to avoid object initialization
     */
    private Idioms ()
    {
    }

    /**
     * Create instance of Idioms for first time and then
     * returns from cache for the next time.
     *
     * @return Idioms object
     * @throws ProcessingException on err
     */
    public static Idioms getInstance () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("Idioms", "getInstance" );
        synchronized ( Idioms.class )
        {
            if (idioms == null)
            {
                idioms = new Idioms();
                //String fileName = "test";
                String fileName = (String) BasicContextManager.getServletContextParamVal (ServletConstants.IDIOMS_PROP_LOC);
                Debug.info (LOGINFO + "Source obtained from context is [" + fileName + "]");

                int reloadTime = BasicContextManager.getValueOfReloadProperty ();
                //int reloadTime = 30;
                Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");

                startReloadThread (reloadTime);
                //String dirName = "C:/Tomcat5.5/webapps/svws/resources/source/blogs";
                idioms.init (fileName);
            }
        }
        return idioms;
    }

    /**
     * The file name that starts with "dyk-fact-" will only be included
     *
     * This method on the basis of passed
     * fileName reads the directory contents
     * and populates the file names in ascending order.
     * This class reads the file name from parent directory
     * only and does not from sub-folders.
     *
     * @param fileName - directory name to read the file names from
     * @throws ProcessingException on error
     */
    private void init (String fileName) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("Idioms", "init" );
        if (!StringUtils.hasValue (fileName))
        {
            String msg = LOGINFO + "Unable to get file names as null passed as Directory name.";
            Debug.error ( msg );
            throw new ProcessingException (msg);
        }

        Debug.info (LOGINFO + "fileName passed as [" + fileName + "]");

        allIdioms = new TreeMap();
        XMLParser xmlp = new XMLParser();
        Document doc = xmlp.getDocument(fileName);
        NodeList list = xmlp.getDocElemList(doc, "Letter");

        for (int l = 0; l < list.getLength(); l++)
        {
            Idiom[] letterIdioms = new Idiom[0];
            Node letter = list.item(l);
            String alphabet = xmlp.getElementAttr(letter, "value");

            Node[] letterIdiomsList = xmlp.getChildren(letter, "LetterIdioms"); // Letter.LetterIdioms.childs

            for (int li = 0; li < letterIdiomsList.length; li++)
            {
                Node letterIdiom = letterIdiomsList [li];
                Node [] letterIdiomChilds = xmlp.getChildren(letterIdiom, null);
                letterIdioms = new Idiom[letterIdiomChilds.length];
                Debug.info (LOGINFO + "letterIdiomChilds.length [" + letterIdiomChilds.length + "]");
                for (int i = 0; i < letterIdiomChilds.length; i++)
                {
                    Node letterIdiomChild = letterIdiomChilds[i];
                    Idiom idiomObj = new Idiom();

                    Node [] idiomNodes = xmlp.getChildren(letterIdiomChild, "Idiom");
                    idiomObj.setIdiom(xmlp.getText(idiomNodes[0]));
                    Node [] descNodes = xmlp.getChildren(letterIdiomChild, "Description");
                    idiomObj.setDescription(xmlp.getText(descNodes[0]));

                    letterIdioms [i] = idiomObj;
                    Debug.info (LOGINFO + "Processing idiom [" + idiomObj + "]");
                }
            }
            allIdioms.put(alphabet, letterIdioms);
            Debug.info (LOGINFO + "Total [" + letterIdioms.length + "] Idioms loaded for alphabet [" + alphabet + "]");
        }
    }

    /**
     * Method to flush the file names.
     * So if new files are added then
     * they would loaded dynamically.
     *
     * @throws ProcessingException on error
     */
    private void flush () throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("Idioms", "flush" );
        try
        {
            Debug.info (LOGINFO + "Flushing files.");

            synchronized ( allIdioms )
            {
                if (allIdioms != null)
                {
                    allIdioms.clear();
                    Debug.info (LOGINFO + "FileNames cleared.");
                    allIdioms = null;
                }
                else
                    Debug.info (LOGINFO + "FileNames already null.");
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
     * Returns the filenames arranged in ascending order
     * @return TreeMap
     */
    public TreeMap getIdioms ()
    {
        return allIdioms;
    }

    /**
     * test method
     *
     * @param args argss
     * @throws ProcessingException on err
     */
    public static void main(String[] args)  throws ProcessingException
    {
        Idioms dd = Idioms.getInstance ();
        System.out.println ("dd.getIdiomsFiles = " + dd.getIdioms ());
    }

    /**
     * start the reload thread
     *
     * @param reloadInterval int
     */
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("Idioms", "startReloadThread" );
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
         * @param reloadInterval int
         */
        public ReloadThread(long reloadInterval) {
            final String LOGINFO = LogInfo.getLogInfo ("Idioms.ReloadThread", "constructor" );
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
            final String LOGINFO = LogInfo.getLogInfo ("Idioms.ReloadThread", "run" );
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
                    synchronized (idioms)
                    {
                        Debug.info ( LOGINFO + "In synchronized block...");
                        idioms.flush ();
                        Debug.info ( LOGINFO + "Flushing static object of a class...");
                        idioms = null;
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

    public class Idiom
    {
        String idiom, description;

        public String getIdiom()
        {
            return idiom;
        }

        public void setIdiom(String idiom)
        {
            this.idiom = idiom;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String toString ()
        {
            return "Idiom [" + idiom + "], description [" + description + "]";
        }
    }
}