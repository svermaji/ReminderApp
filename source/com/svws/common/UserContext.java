package com.svws.common;

import com.svws.common.log.Debug;
import com.svws.common.log.LogInfo;

/**
 * Created by
 * User: sverma
 * Date: Mar 8, 2006
 * Time: 4:55:45 PM
 */
/**
 * This class contains the vector of user object
 * that is currently logged in.
 */
public class UserContext
{
    private int userId = 0;
    private String userName, firstName, lastName, email;

    /**
     * private constructor.
     */
    private UserContext ()
    {
    }

    /**
     * Get the user context object associated with the current thread of execution.
     *
     * @return  The current customer context object.
     * @exception  Exception  Thrown on errors.
     */
    public static UserContext getInstance ( ) throws Exception
    {
        UserContext currentContext = (UserContext) userContexts.get( );

        if ( currentContext == null )
        {
            currentContext = new UserContext( );

            userContexts.set( currentContext );
        }

        return currentContext;
    }

    /**
    * This is used in accessing the thread-specific UserContext instance.
    */
    private static InheritableThreadLocal userContexts = new InheritableThreadLocal(){
        final String LOGINFO = LogInfo.getLogInfo ( "UserContext", "InheritableThreadLocal");

       /**
       * This makes a copy of the parent thread's UserContext instead
       * of referencing the parent thread's instance directly, which is the
       * default behavior.
       */
       protected Object childValue(Object parentValue){

           UserContext parentContext = (UserContext) parentValue;
           UserContext childContext = new UserContext();

           if(parentContext != null){

              try
              {
                 childContext.setUserId( parentContext.getUserId() );
                 childContext.setUserName( parentContext.getUserName() );
                 childContext.setFirstName( parentContext.getFirstName() );
                 childContext.setLastName( parentContext.getLastName() );
                 childContext.setEmail( parentContext.getEmail() );
              }
              catch(Exception e)
              {
                 Debug.error ( LOGINFO + "An error occured while trying to copy the parent user context into a new child thread: "+ e, e);
              }
           }

           return childContext;

       }

    };

    /**
     * get user name.
     * @return string
     */
    public String getUserName ()
    {
        return userName;
    }

    /**
     * sets user name.
     * @param userName string
     */
    public void setUserName ( String userName )
    {
        this.userName = userName;
    }

    /**
     * get user id.
     * @return string
     */
    public int getUserId ()
    {
        return userId;
    }

    /**
     * sets user id.
     * @param userId int
     */
    public void setUserId ( int userId )
    {
        this.userId = userId;
    }

    /**
     * get first name.
     * @return string
     */
    public String getFirstName ()
    {
        return firstName;
    }

    /**
     * sets first name.
     * @param firstName string
     */
    public void setFirstName ( String firstName )
    {
        this.firstName = firstName;
    }

    /**
     * get last name.
     * @return string
     */
    public String getLastName ()
    {
        return lastName;
    }

    /**
     * sets last name.
     * @param lastName string
     */
    public void setLastName ( String lastName )
    {
        this.lastName = lastName;
    }

    /**
     * get email.
     * @return string
     */
    public String getEmail ()
    {
        return email;
    }

    /**
     * sets email.
     * @param email address
     */
    public void setEmail ( String email )
    {
        this.email = email;
    }

    /**
     * Explains the variables of the class.
     */
    public void describe ()
    {
        final String LOGINFO = LogInfo.getLogInfo ("UserContext", "describe" );
        Debug.info (LOGINFO + "Values in User Context are: " +
                "userId [" + userId + "]" +
                ", userName [" + userName + "]" +
                ", firstName [" + firstName + "]" +
                ", lastName [" + lastName + "]" +
                " and email [" + email + "]");
    }

    /**
     * Cleans up the class variables.
     */
    public void destroy ()
    {
        final String LOGINFO = LogInfo.getLogInfo ("UserContext", "destroy" );
        Debug.info (LOGINFO + "Destroying following values of User Context: " +
                "userId [" + userId + "]" +
                ", userName [" + userName + "]" +
                ", firstName [" + firstName + "]" +
                ", lastName [" + lastName + "]" +
                " and email [" + email + "]");
        setUserId ( 0 );
        setUserName ( null );
        setFirstName ( null );
        setLastName ( null );
        setEmail ( null );
    }

    /**
     * Retrieves the context and sets the value.
     * @param userId
     * @param userName
     * @param firstName
     * @param lastName
     * @param email address
     */
    public void setAndDescribe(int userId, String userName, String firstName, String lastName, String email)
    {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        describe();
    }
}