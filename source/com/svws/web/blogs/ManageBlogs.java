package com.svws.web.blogs;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;
import com.svws.common.BasicContextManager;
import com.svws.common.utils.*;

import java.util.*;
import java.io.*;

/**
 * Created by
 * User: sverma
 * Date: Mar 8, 2006
 * Time: 4:55:45 PM
 */
/**
 * This class contains the operation related to blogs
 */
public final class ManageBlogs
{

    private static Thread reloadThread;

    /**
     * Private constructor
     */
    private ManageBlogs ()
    {
    }

    /**
     * Static block to initiate the class at loading time
     * and start timer.
     */
    static {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "static block" );
        try {
            int defaultReloadTime = 30;
            Object prm = BasicContextManager.getServletContextParamVal (ServletConstants.RELOAD_TIME_IN_MIN, new Integer ( defaultReloadTime ));
            int reloadTime = Integer.parseInt((String) prm);
            //int reloadTime = 30;
            if (reloadTime > 40 && reloadTime < 10)
                reloadTime = 30;

            Debug.info (LOGINFO + "Reload time obtained as [" + reloadTime + "]");

            startReloadThread (reloadTime);
            init ();

        }
        catch(Exception e)
        {
            Debug.error (LOGINFO + e, e);
        }
    }

    /**
     * Initialize method
     */
    private static void init ()
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "init" );
        Debug.info (LOGINFO + "Initialization complete.");
    }

    /**
     * Add and edit the blog.
     * @param blog bean object
     * @throws BlogsException on error
     */
    public static void handleBlog (Blog blog, String origHeading) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "handleBlog" );
        try
        {
            Debug.info (LOGINFO + "Parameter obtained as Blog [" + blog + "] and origHeading as [" + origHeading + "]." );
            if (StringUtils.hasValue(blog.getBlogCreateDate(), true))
            {
                updateBlog (blog, origHeading);
                Debug.info (LOGINFO + "Blog updated." );
            }
            else
            {
                createBlog (blog);
                Debug.info (LOGINFO + "Blog created." );
            }
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
    }

    /**
     * Adds new blog
     *
     * @param blog of type Blog
     * @return boolean status
     * @throws BlogsException on error
     */
    private static boolean createBlog (Blog blog) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "insertBlog" );
        try
        {
            Debug.info (LOGINFO + "Record inserted." );
            return manageBlog (blog, false, "");
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
    }

    /**
     * Updates the blog.
     *
     * @param blog of type Blog
     * @return boolean as status
     * @throws BlogsException on error
     */
    private static boolean updateBlog (Blog blog, String origHeading) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "updateBlog" );
        try
        {
            Debug.info (LOGINFO + "Record updated." );
            return manageBlog (blog, true, origHeading);
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
    }

    private static boolean manageBlog(Blog blog, boolean blogExists, String origHeading) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "manageBlog" );
        blog.setBlogHeading(StringUtils.encodeForHtml(blog.getBlogHeading()));
        Debug.info (LOGINFO + "Blog to be processed [" + blog + "], blogExists [" + blogExists + "] and original Heading [" + origHeading + "]." );
        File file = null;
        BufferedWriter bw = null;
        try
        {
            if (blogExists)
            {
                file = getBlogFile (blog, origHeading);
            }
            else
            {
                blog.setBlogCreateDate(DateUtils.getFormattedDate(UtilConstants.DATE_FORMAT_DD_MMM_YYYY));
                String fileName = BlogsList.getInstance().getBlogsDir() + "/" + getBlogFileName (blog, origHeading) + ServletConstants.BLOGS_EXT;
                Debug.info (LOGINFO + "File name to be created is [" + fileName + "]." );
                file = ReadSource.getResource (fileName, true);
                Debug.info (LOGINFO + "File created." );
            }

            //file.setWritable(true);
            Debug.info (LOGINFO + "File obtained." );
            bw = new BufferedWriter(new FileWriter(file));
            Debug.info (LOGINFO + "Writer obtained." );
            bw.write(encodeBlog (blog.getBlogData()));
            Debug.info (LOGINFO + "Data [" + blog.getBlogData() + "] written." );
            if (blogExists && !blog.getBlogHeading().equals(origHeading))
            {
                // flushing and closing stream to rename the file
                try {
                    Debug.info (LOGINFO + "Flushing and closing stream to rename the file." );
                    bw.flush();
                    bw.close();
                    Debug.info (LOGINFO + "Stream closed." );
                } catch (Exception e) {}
                // sending intentionally blank string to rename the blog
                String path = file.getParent() + "/" + getBlogFileName (blog, "") + ServletConstants.BLOGS_EXT;
                Debug.info (LOGINFO + "Path for file to rename is [" + path + "]." );
                File tempFile = new File(path);
                boolean result = file.renameTo(tempFile);
                Debug.info (LOGINFO + "Operation of File renamed to new heading as [" + tempFile.getAbsolutePath() + "] is [" + result + "]." );
                if (!result)
                {
                    result = tempFile.createNewFile();
                    Debug.info (LOGINFO + "Operation of File [" + tempFile.getAbsolutePath() + "] creation is [" + result + "]." );
                    bw = new BufferedWriter(new FileWriter(tempFile));
                    Debug.info (LOGINFO + "Writer obtained." );
                    bw.write(encodeBlog (blog.getBlogData()));
                    Debug.info (LOGINFO + "Data [" + blog.getBlogData() + "] written." );
                    result = file.delete();
                    Debug.info (LOGINFO + "Operation of File [" + file.getAbsolutePath() + "] delete is [" + result + "]." );
                }
            }
            Debug.info (LOGINFO + "Completed." );
        }
        catch (FileNotFoundException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        finally
        {
            try {
                bw.flush();
                bw.close();
            } catch (Exception e) {}
        }

        return false;
    }

    private static String encodeBlog(String blogData)
    {
        return blogData.replaceAll("\n", "<br><br>");
    }

    private static String decodeBlog(String blogData)
    {
        return blogData.replaceAll("<br><br>", "\n");
    }

    private static File getBlogFile(Blog blog, String origHeading) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "getBlogFile" );
        try
        {
            String blogName = getBlogFileName (blog, origHeading);
            Debug.info ( LOGINFO + "Blog name obtained as [" + blogName + "].");

            return getFile (blogName);

        }
        catch (ProcessingException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
    }

    private static File getFile(String fileName) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "getFile" );
        try
        {
            Debug.info ( LOGINFO + "File name param obtained as [" + fileName + "].");
            if (!fileName.endsWith(ServletConstants.BLOGS_EXT))
            {
                fileName += ServletConstants.BLOGS_EXT;
                Debug.info ( LOGINFO + "File name after adding extension is [" + fileName + "].");
            }

            TreeMap blogList = BlogsList.getInstance().getBlogsListFiles();
            Debug.info ( LOGINFO + "blogList has files [" + blogList.size() + "].");
            Iterator itar = blogList.keySet().iterator();
            while (itar.hasNext())
            {
                String fName = (String) itar.next();
                File tempFile = (File) blogList.get(fName);
                Debug.info ( LOGINFO + "Comapring file name [" + fName + "].");
                if (fName.equals(fileName))
                {
                    Debug.info ( LOGINFO + "Found file object. Returning from cache.");
                    return tempFile;
                }
            }
        }
        catch (ProcessingException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        Debug.info ( LOGINFO + "No file object exists. Returning null.");
        return null;
    }

    private static String getBlogFileName (Blog blog, String origHeading) throws BlogsException
    {
        return ServletConstants.BLOGS_FILE_PREFIX + blog.getBlogOwner() + ServletConstants.DASH +
                blog.getBlogCreateDate() + ServletConstants.DASH + (StringUtils.hasValue(origHeading) ? origHeading : blog.getBlogHeading());
    }

    public static Blog getBlog (String filePath) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "getBlog" );
        Blog blog = new Blog();
        try
        {
            Debug.info ( LOGINFO + "file path received as [" + filePath + "].");
            String DASH = "-";
            String blogOwner = "";
            String blogHeading = "";
            String blogCreatedDate = "";
            filePath = StringUtils.decodeForHtml(filePath);
            Debug.info ( LOGINFO + "file path after decode is [" + filePath + "].");
            filePath = filePath.substring(filePath.lastIndexOf("/")+1);
            Debug.info ( LOGINFO + "file path after first formatting [" + filePath + "].");
            File file = getFile (filePath);
            filePath = filePath.substring(0, filePath.lastIndexOf(ServletConstants.BLOGS_EXT));
            Debug.info ( LOGINFO + "file path after formatting is [" + filePath + "].");
            // blog name has format "blog-<UserName>-<Day>-<Month>-<Year>-<BlogName>"
            // the actual blog-name/heading will be available after 5th DASH "-"
            int blogStrIdx = filePath.indexOf(ServletConstants.BLOGS_FILE_PREFIX);
            blogStrIdx += ServletConstants.BLOGS_FILE_PREFIX.length();
            Debug.info ( LOGINFO + "blogStrIdx [" + blogStrIdx + "].");
            blogOwner = filePath.substring(blogStrIdx, filePath.indexOf(DASH, blogStrIdx));
            Debug.info ( LOGINFO + "blogOwner [" + blogOwner + "].");
            String tempCD = filePath.substring(filePath.indexOf(blogOwner)+blogOwner.length()+1);
            Debug.info ( LOGINFO + "tempCD [" + tempCD + "].");
            int tempIdx = 0;
            for (int i = 0; i < 3; i++)
            {
                if (tempCD.indexOf("-") > -1)
                    tempIdx = tempCD.indexOf(DASH, tempIdx+1);
            }
            Debug.info ( LOGINFO + "tempIdx [" + tempIdx + "].");
            blogCreatedDate = tempCD.substring(0, tempIdx);
            Debug.info ( LOGINFO + "blogCreatedDate [" + blogCreatedDate + "].");
            blogHeading = filePath.substring(filePath.indexOf(blogCreatedDate)+blogCreatedDate.length()+1);
            Debug.info ( LOGINFO + "blogHeading [" + blogHeading + "].");
            blog.setBlogCreateDate(blogCreatedDate);
            blog.setBlogHeading(blogHeading);
            blog.setBlogOwner(blogOwner);
            blog.setBlogLastModifiedDate(DateUtils.getFormattedDate(UtilConstants.DATE_FORMAT_DD_MMM_YYYY , new Date(file.lastModified())));
            blog.setBlogData(decodeBlog(ReadSource.getFileContents(file)));
        }
        catch (BlogsException e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        return blog;
    }

    /**
     * deletes the blog matching the criteria.
     *
     * @param blogPath path of Blog
     * @return boolean as status
     * @throws BlogsException on error
     */
    public static boolean deleteBlogs (String blogPath) throws BlogsException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "deleteBlogs" );
        boolean result = false;
        try
        {
            Debug.info (LOGINFO + "Deleting blog [" + blogPath + "]." );
            File file = ReadSource.getResource (blogPath, false);
            if (file != null)
                result = file.delete();
            Debug.info (LOGINFO + "Blog deleted." );
        }
        catch (Exception e)
        {
            String msg = LOGINFO + e.getMessage ();
            Debug.error (msg, e);
            throw new BlogsException (e.getMessage ());
        }
        return result;
    }

    /**
     * start the reload thread
     *
     * @param reloadInterval long
     */
    private static synchronized void startReloadThread(long reloadInterval) {
        final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs", "startReloadThread" );
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
         * @param reloadInterval long
         */
        public ReloadThread(long reloadInterval) {
            final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs.ReloadThread", "constructor" );
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
            final String LOGINFO = LogInfo.getLogInfo ("ManageBlogs.ReloadThread", "run" );
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
                    Debug.info (LOGINFO + "Reloading blogs properties...");
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

    /**
     * test method.
     */
    public static void main ( String[] args ) throws BlogsException
    {
    }
}