package com.svws.web.blogs;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 23, 2007
 * Time: 3:53:43 PM
 */

public class Blog
{
    private String blogCreateDate;
    private String blogLastModifiedDate;
    private String blogOwner;
    private String blogHeading;
    private String blogData;


    public String getBlogCreateDate()
    {
        return blogCreateDate;
    }

    public void setBlogCreateDate(String blog_create_date)
    {
        this.blogCreateDate = blog_create_date;
    }

    public String getBlogLastModifiedDate()
    {
        return blogLastModifiedDate;
    }

    public void setBlogLastModifiedDate(String blog_last_modified_date)
    {
        this.blogLastModifiedDate = blog_last_modified_date;
    }

    public String getBlogOwner()
    {
        return blogOwner;
    }

    public void setBlogOwner(String blog_owner)
    {
        this.blogOwner = blog_owner;
    }

    public String getBlogHeading()
    {
        return blogHeading;
    }

    public void setBlogHeading(String blog_heading)
    {
        this.blogHeading = blog_heading;
    }

    public String getBlogData()
    {
        return blogData;
    }

    public void setBlogData(String blog_data)
    {
        this.blogData = blog_data;
    }

    public String toString ()
    {
        return new StringBuffer
                ("Create date [").append(blogCreateDate).append("], last modified date [").append(blogLastModifiedDate)
                    .append("], owner [").append(blogOwner)
                    .append("], heading [").append(blogHeading)
                    .append("], data [").append(blogData).append("]")
                .toString();
    }            
}
