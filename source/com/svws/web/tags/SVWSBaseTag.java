package com.svws.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Created by
 * User: sverma
 * Date: Dec 8, 2005
 * Time: 8:29:08 PM
 */
public class SVWSBaseTag extends BodyTagSupport
{
    /**
     * Starts processing of the tag.
     * @return EVAL_BODY_BUFFERED is returned so that the body is NOT evaluated.
     */
    public int doStartTag() throws JspException
    {
      return EVAL_BODY_BUFFERED;
    }


    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }

    /**
    * Free resources after the tag evaluation is complete.
    * Child classes must call super.release();
    */
   public void release()
   {
       super.release();
   }

}
