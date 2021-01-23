package com.svws.utils;

import com.svws.common.utils.StringUtils;
import com.svws.common.utils.ServletConstants;
import com.svws.common.CommonPaths;
import com.svws.common.log.Debug;
import com.svws.xml.XMLParser;
import com.nightfire.framework.message.common.xml.XMLPlainGenerator;
import com.nightfire.framework.message.common.xml.XMLGenerator;
import com.nightfire.framework.message.MessageException;
import com.nightfire.framework.util.FileUtils;
import com.nightfire.framework.util.FrameworkException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * tag utility which provides support for retrieving and setting variables for tags.
 */
 
public class ServletUtils
{
    public static String COMMA = ",";
    public static String SPACE = " ";
    public static String COMMASP = COMMA + SPACE;
    /**
     * Returns the context path of the current webapp.
     * If the context path can not be obtained then an empty string is
     * returned.
     * @param sreq Servlet Reqest
     * @return Stromg
     */
    public static final String getWebAppContextPath(ServletRequest sreq)
    {
      HttpServletRequest req = (HttpServletRequest)sreq;

      String webApp = "";
      webApp = req.getContextPath();
      if (!StringUtils.hasValue(webApp) )
        webApp = getWebAppContextPath( req.getSession().getServletContext() );

      return webApp;
    }

    public static final String createSCScript(HttpServletRequest sreq)
    {
      String ctxPath = CommonPaths.getCurrentContext(sreq);

      StringBuffer script = new StringBuffer ();
      script.append("<SCRIPT>var isomorphicDir=\"").append(ctxPath).append("/isomorphic/\";</SCRIPT>")
            .append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/system/modules/ISC_Core.js\"></SCRIPT>")
            .append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/system/modules/ISC_Foundation.js\"></SCRIPT>")
            .append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/system/modules/ISC_Containers.js\"></SCRIPT>")
            .append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/system/modules/ISC_Grids.js\"></SCRIPT>")
            .append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/system/modules/ISC_Forms.js\"></SCRIPT>")
            .append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/system/modules/ISC_DataBinding.js\"></SCRIPT>");

      return script.toString();
    }

    public static final String createSkinScript(HttpServletRequest sreq, String skinName)
    {
        String ctxPath = CommonPaths.getCurrentContext(sreq);
        //setting default skin as "TreeFrog" if not set 
        if (!StringUtils.hasValue(skinName))
            skinName = "TreeFrog";


        StringBuffer script = new StringBuffer ();
        script.append("<SCRIPT SRC=\"").append(ctxPath).append("/isomorphic/skins/").append(skinName).append("/load_skin.js\"></SCRIPT> ");

        return script.toString();
    }

    public static final String createTabScript(HttpServletRequest sreq, String filePath) 
    {
        String ctxPath = CommonPaths.getCurrentContext(sreq);

        StringBuffer script = new StringBuffer ();
        script.append("isc.TabSet.create");
        script.append("({");

        XMLParser xml = new XMLParser();
        XMLPlainGenerator xmlpg = new XMLPlainGenerator(xml.getDocument(filePath));

        Node[] childs = xmlpg.getChildren(xmlpg.getDocument().getFirstChild());
        //System.out.println("childs.length = " + childs.length);
        for (int num = 0; num < childs.length; num++)
        {
            Node node = childs[num];
            //System.out.println("node.getNodeName() = " + node.getNodeName());
            //System.out.println("xmlpg.getText(node) = " + xmlpg.getText(node));
            if (!node.getNodeName().equals("tabs"))
            {
                script.append(node.getNodeName()).append(": ");
                script.append(getQuotes(xmlpg, node)).append(getConvertedValue(xmlpg.getText(node), sreq)).append(getQuotes(xmlpg, node));

                if (num != childs.length-1)
                    script.append(COMMASP);
            }
            else
            {
                Node[] tabs = xmlpg.getChildren(node);
                //System.out.println("tabs.length = " + tabs.length);
                script.append(node.getNodeName()).append(":");
                script.append("[");
                for (int tabnum = 0; tabnum < tabs.length; tabnum++)
                {
                    // tab node here
                    Node[] tab = xmlpg.getChildren(tabs[tabnum]);
                    script.append("{");
                    for (int tabcnt = 0; tabcnt < tab.length; tabcnt++)
                    {
                        script.append(tab[tabcnt].getNodeName()).append(": ");
                        script.append(getQuotes(xmlpg, tab[tabcnt])).append(getConvertedValue(xmlpg.getText(tab[tabcnt]), sreq)).append(getQuotes(xmlpg, tab[tabcnt]));
                        if (tabcnt != tab.length-1)
                            script.append(COMMASP);
                    }
                    script.append("}");
                    if (tabnum != tabs.length-1)
                        script.append(COMMASP);
                }
                script.append("]");
            }

        }
        script.append("});");

        Debug.info("script = " + script);
        return script.toString();
    }

    public static final String createFields(HttpServletRequest sreq, String filePath, String tabId)
    {
        String ctxPath = CommonPaths.getCurrentContext(sreq);

        StringBuffer script = new StringBuffer ();

        XMLGenerator xmlpg = null;
        try
        {
            xmlpg = new XMLPlainGenerator(FileUtils.readFile(filePath));
        } catch (FrameworkException e)
        {
            Debug.error ("", e);
        }
        Document doc = xmlpg.getDocument();
        Node root = doc.getFirstChild();

        Node formsNode = null;
        try
        {
            formsNode = xmlpg.getNode(root, "forms");
        } catch (MessageException e)
        {
            Debug.error ("", e);
        }
        Node[] allForms = xmlpg.getChildren(formsNode);
        //System.out.println("allForms.length = " + allForms.length);
        for (int num = 0; num < allForms.length; num++)
        {
            Node node = allForms[num];
            Node[] frmChilds = xmlpg.getChildren(node);
            //System.out.println("frmChilds.length = " + frmChilds.length);
            for (int i = 0; i < frmChilds.length; i++)
            {
                Node frmChild = frmChilds[i];
                if (frmChild.getNodeName().equals("Id") && xmlpg.getText(frmChild).equals(tabId))
                {
                    //System.out.println("Found id with value [" + xmlpg.getText(frmChild) + "]");
                    //System.out.println("xmlpg.getNode() = " + xmlpg.getNode(node, "Fields").getNodeName());
                    Node fieldsNode = null;
                    try
                    {
                        fieldsNode = xmlpg.getNode(node, "Fields");
                    } catch (MessageException e)
                    {
                        Debug.error ("", e);
                    }
                    Node[] fields = xmlpg.getChildren(fieldsNode);

                    script.append("isc.DynamicForm.create({");
                    StringBuffer scriptFlds = new StringBuffer();

                    //System.out.println("fields.length = " + fields.length);
                    for (int j = 0; j < fields.length; j++)
                    {
                        // tab node here
                        Node nd = fields[j];
                        if (nd.getNodeName().equals("Field"))
                        {
                            Node[] fldProps = xmlpg.getChildren(nd);
                            if (!StringUtils.hasValue(scriptFlds.toString()))
                                scriptFlds.append("fields: [");
                            scriptFlds.append("{");
                            //System.out.println("fldProps.length = " + fldProps.length);
                            for (int cnt = 0; cnt < fldProps.length; cnt++)
                            {
                                scriptFlds.append(fldProps[cnt].getNodeName()).append(": ");
                                scriptFlds.append(getQuotes(xmlpg, fldProps[cnt])).append(getConvertedValue(xmlpg.getText(fldProps[cnt]), sreq)).append(getQuotes(xmlpg, fldProps[cnt]));
                                if (cnt != fldProps.length-1)
                                    scriptFlds.append(COMMASP);
                            }
                            scriptFlds.append("}").append(COMMASP);
                        }
                        else // properties of fields
                        {
                            script.append(nd.getNodeName()).append(": ");
                            script.append(getQuotes(xmlpg, nd)).append(getConvertedValue(xmlpg.getText(nd), sreq)).append(getQuotes(xmlpg, nd));
                            script.append(COMMASP);
                        }
                    }

                    //System.out.println("scriptFlds = " + scriptFlds);
                    //System.out.println("script = " + script);
                    
                    // here we need comma to separate script and scriptFlds
                    if (scriptFlds.toString().endsWith(COMMASP))
                        scriptFlds = new StringBuffer(scriptFlds.substring(0, scriptFlds.length()-COMMASP.length()));

                    scriptFlds.append("]"); // end of fields
                    script.append(scriptFlds);

                    // in case if their are no fields then remove extra comma
                    if (script.toString().endsWith(COMMASP))
                        script = new StringBuffer(script.substring(0, script.length()-COMMASP.length()));

                    script.append("})"); // end of dynamic form
                    break;
                }
/*
                else
                {
                    System.out.println("Found node " + frmChild.getNodeName() + " with value [" + xmlpg.getText(frmChild).trim() + "]");
                }
*/
            }
        }

        //System.out.println("script = " + script);
        //System.out.println("-----------------------------------");
        Debug.info("script = " + script);
        return script.toString();
    }

    /**
     * Returns the context path of the current webapp.
     * If the context path can not be obtained then an empty string is
     * returned.
     */
  public static final String getWebAppContextPath(ServletContext context)
  {

    String webappName = (String)context.getAttribute(com.nightfire.webgui.core.ServletConstants.WEB_APP_NAME);
    if ( StringUtils.hasValue(webappName) )
      return webappName;


    try {
      String url = context.getResource("/").toString();
      if ( url.endsWith("/") )
        url = url.substring(0, url.length() - 1);

      webappName = url.substring(url.lastIndexOf("/") + 1 );

    }
    catch (Exception e) {
    }



    return "/" + webappName;

  }

  public static final String getConvertedValue (String val, HttpServletRequest req)
  {
      if (val.indexOf("&lt;ctx&gt;") > -1)
      {
          val = StringUtils.replaceSubstrings(val, "&lt;ctx&gt;", CommonPaths.getCurrentContext(req));
      }
      if (val.indexOf("<ctx>") > -1)
      {
          val = StringUtils.replaceSubstrings(val, "<ctx>", CommonPaths.getCurrentContext(req));
      }
      return val;
  }

    public static void main(String[] args) throws Exception
    {
        /*StringBuffer script = new StringBuffer ();
        script.append("isc.TabSet.create");
        script.append("({");

        XMLParser xml = new XMLParser();
        Document doc = xml.getDocument("C:\\Tomcat5.5.26\\webapps\\sc\\meta\\tabs.xml");
        XMLPlainGenerator xmlpg = new XMLPlainGenerator(doc);

        Node[] childs = xmlpg.getChildren(doc.getFirstChild());
        for (int num = 0; num < childs.length; num++)
        {
            Node node = childs[num];
            System.out.println("node.getNodeName() = " + node.getNodeName());
            System.out.println("xmlpg.getText(node) = " + xmlpg.getText(node));
            if (!node.getNodeName().equals("tabs"))
            {
                script.append(node.getNodeName()).append(": ");
                script.append("\"").append(xmlpg.getText(node)).append("\"");
                if (num != childs.length-1)
                    script.append(COMMASP);
            }
            else
            {
                Node[] tabs = xmlpg.getChildren("tabs");
                System.out.println("tabs.getLength() = " + tabs.length);
                script.append("tabs:");
                script.append("[");
                for (int tabnum = 0; tabnum < tabs.length; tabnum++)
                {
                    // tab node here
                    Node[] tab = xmlpg.getChildren(tabs[tabnum]);
                    System.out.println("tab.getLength() = " + tab.length);
                    script.append("{");
                    for (int tabcnt = 0; tabcnt < tab.length; tabcnt++)
                    {
                        script.append(tab[tabcnt].getNodeName()).append(": ");
                        script.append("\"").append(xmlpg.getText(tab[tabcnt])).append("\"");
                        if (tabcnt != tab.length-1)
                            script.append(COMMASP);
                    }
                    script.append("}");
                    if (tabnum != tabs.length-1)
                        script.append(COMMASP);
                }
                script.append("]");
            }

        }
        script.append("});");

        System.out.println("script = " + script);
        */
        XMLGenerator xmlpg = new XMLPlainGenerator(FileUtils.readFile("C:\\Tomcat5.5.26\\webapps\\sc\\meta\\search.xml"));
        Document doc = xmlpg.getDocument();
        Node root = doc.getFirstChild();
        System.out.println("doc.getFirstChild().getNodeName() = " + doc.getFirstChild().getNodeName());
        System.out.println("o/p = " + xmlpg.getNode(root, "forms").getNodeName());
        //System.out.println("childs.getLength() = " + childs.getNodeName());
    }

    public static String getQuotes (XMLGenerator xmlpg, Node node)
    {
        String quotes = "\"";
        try
        {
            String applyQuotes = xmlpg.getAttribute(node, "quotes");
            if (!StringUtils.getBoolean(applyQuotes, true))
                quotes = "";
        }
        catch (Exception e)
        {
            quotes = "\"";
        }
        
        return quotes;
    }

}