package com.svws.common.utils;

import com.svws.common.log.LogInfo;
import com.svws.common.log.Debug;
import com.svws.common.BasicContextManager;

import java.io.*;
import java.util.Properties;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 25, 2007
 * Time: 1:05:12 PM
 */
public final class ReadSource
{
    private ReadSource ()
    {
    }

    public static String[] getFileNames (String dirName) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getFileNames" );
        File dir = getDirectory (dirName);
        Debug.info (LOGINFO + "Directory obtained.");
        return dir.list ();
    }

    public static File[] getFileList (String dirName) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getFileList" );
        File dir = getDirectory (dirName);
        Debug.info (LOGINFO + "Directory obtained.");
        return dir.listFiles ();
    }

    public static File getDirectory (String dirName) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getDirectory" );
        if (!StringUtils.hasValue ( dirName ))
        {
            String msg = LOGINFO + "Unable to get file names as null passed as Directory name.";
            Debug.error ( msg );
            throw new ProcessingException (msg);
        }
        Debug.info (LOGINFO + "dirName passed as [" + dirName + "]");
        String dirRealPath = BasicContextManager.getRealPath ( dirName );
        Debug.info (LOGINFO + "dirRealPath obtained as [" + dirRealPath + "]");
        File dir = new File (dirRealPath);
        if (!dir.exists () || dir.isFile())
        {
            String msg = LOGINFO + "Either path does not exists or it is file.";
            Debug.error ( msg );
            throw new ProcessingException (msg);
        }
        return dir;
    }

    public static String getParameter (String param, boolean readFromServletContext) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getParameter" );

        if (!StringUtils.hasValue ( param ))
            throw new ProcessingException (LOGINFO + "Null parameter passed.");

        String parameter = param;

        if (readFromServletContext)
        {
            Object prm = BasicContextManager.getServletContextParamVal (parameter);
            if (prm !=null && StringUtils.hasValue((String) prm) )
            {
                parameter = (String) prm;
            }
            else
                throw new ProcessingException (LOGINFO + "Unable to obtain value of parameter [" + parameter + "] from Servlet Context.");
        }

        return parameter;
    }

    public static Properties getResourceProperties (String param, boolean readFromServletContext) throws ProcessingException
    {
        return getResourceProperties ( getParameter (param, readFromServletContext) );
    }

    public static Properties getResourceProperties (String param) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getResourceProperties" );

        if (!StringUtils.hasValue ( param ))
            throw new ProcessingException (LOGINFO + "Null parameter passed.");

        Properties prop = new Properties ( );
        try
        {
            prop.load (BasicContextManager.getResourceAsStream ( param ));
            Debug.info (LOGINFO + "Total [" + prop.size () + "] properties loaded. Following are the properties..." );
            Iterator itar = prop.keySet ().iterator ();
            while (itar.hasNext ())
            {
                String key = (String) itar.next ();
                String val= prop.getProperty ( key );
                Debug.info (LOGINFO + "key [" + key + "] and value [" + val + "]." );
            }
        }
        catch (IOException e)
        {
            Debug.error ( LOGINFO + "Failed to load properties for param [" + param +"], " + e.getMessage (), e );
            throw new ProcessingException (e);
        }
        return prop;
    }

    private static void closeStream ( InputStream is )
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "closeStream" );
        try
        {
            if ( is != null)
                is.close();
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + "Failed to close stream: " + e.getMessage (), e );
        }
    }

    public static File getResource (String fileName, boolean createIfNotExists) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getResource" );
        final String filePrefix = "file:/";
        if (!StringUtils.hasValue ( fileName ))
        {
            String msg = LOGINFO + "Unable to read file as null passed as file name.";
            Debug.error ( msg );
            throw new ProcessingException (msg);
        }
        Debug.info (LOGINFO + "fileName passed as [" + fileName + "]");
        String actualFilePath = "";

        // if real path is passed
        if (fileName.startsWith(filePrefix))
        {
            fileName = fileName.substring(fileName.indexOf(filePrefix) + filePrefix.length());
            actualFilePath = fileName;
            Debug.info (LOGINFO + "fileName after formatting is [" + fileName + "]");
        }
        else
            actualFilePath = BasicContextManager.getRealPath ( fileName );
        
        Debug.info (LOGINFO + "file path obtained for file [" + fileName + "] is [" + actualFilePath + "]");

        File file = null;

        try
        {
            file = new File (actualFilePath);
            if (createIfNotExists)
            {
                if (file.createNewFile())
                    Debug.info (LOGINFO + "New file created.");
                else
                    Debug.info (LOGINFO + "Unable to create new file.");
            }
            else if (!file.exists () || !file.isFile())
            {
                String msg = LOGINFO + "Either path does not exists or it is not a file.";
                Debug.error ( msg );
                throw new ProcessingException (msg);
            }

            Debug.info (LOGINFO + "File processed...");

        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e );
        }
        return file;
    }
    
    public static String getFileContents (File file) throws ProcessingException, FileNotFoundException
    {
        return getFileContents(new FileInputStream (file));
    }

    public static String getFileContents (InputStream fileStream) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getFileContents:InputStream" );

        if (fileStream == null)
            throw new ProcessingException("The file input stream is found to be null.");

        Debug.info (LOGINFO + "Creating buffered input stream object...");
        BufferedInputStream bis = new BufferedInputStream ( fileStream  );
        Debug.info (LOGINFO + "reading file...");

        StringBuffer fileContents = new StringBuffer();
        int ch=0;
        try
        {
            while((ch=bis.read()) > -1)
            {
                fileContents.append((char)ch);
            }
        }
        catch (IOException e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e );
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e );
        }
        finally
        {
            closeStream ( bis );
            Debug.info (LOGINFO + "Closed buffered input stream object" );
        }

        Debug.info (LOGINFO + "Returning fileContents as [" + fileContents + "]" );
        return fileContents.toString ();

    }

    public static String getFileContents (String fileName) throws ProcessingException
    {
        final String LOGINFO = LogInfo.getLogInfo ("ReadSource", "getFileContents" );

        StringBuffer strB = new StringBuffer ( );
        InputStream fileStream = null;

        try
        {
            Debug.info (LOGINFO + "Creating file stream object...");
            fileStream = BasicContextManager.getResourceAsStream (fileName);
            Debug.info (LOGINFO + "Reading file stream object...");

            return getFileContents (fileStream);
        }
        catch (Exception e)
        {
            Debug.error ( LOGINFO + e.getMessage (), e );
        }
        finally
        {
            closeStream (fileStream);
        }

        return strB.toString ();
    }
}
