set INSTALLROOT=C:/Tomcat5.5.26
set ENDORSEDPATH=%INSTALLROOT%/common/endorsed
set SHAREDLIBPATH=%INSTALLROOT%/shared/lib
set SVWSCLASSESPATH=%INSTALLROOT%/webapps/svws/WEB-INF/classes

set CLASSPATH=%CLASSPATH%;%SHAREDLIBPATH%/jaxrpc.jar;%SHAREDLIBPATH%/axis.jar;%SHAREDLIBPATH%/commons-discovery-0.2.jar;%SHAREDLIBPATH%/commons-logging-1.0.4.jar;%SHAREDLIBPATH%/wsdl4j-1.5.1.jar;%SHAREDLIBPATH%/saaj.jar;%SHAREDLIBPATH%/activation.jar;%SHAREDLIBPATH%/mail.jar;%SHAREDLIBPATH%/jaxp-api.jar;%SHAREDLIBPATH%/sax.jar;%SHAREDLIBPATH%/serializer.jar;%SHAREDLIBPATH%/xalan.jar;%SHAREDLIBPATH%/xercesImpl.jar;%SHAREDLIBPATH%/xml-apis.jar;%SHAREDLIBPATH%/xsltc.jar;


rem set CLASSPATH=%CLASSPATH%;%ENDORSEDPATH%/jaxp-api.jar;%ENDORSEDPATH%/sax.jar;%ENDORSEDPATH%/serializer.jar;%ENDORSEDPATH%/xalan.jar;%ENDORSEDPATH%/xercesImpl.jar;%ENDORSEDPATH%/xml-apis.jar;%ENDORSEDPATH%/xsltc.jar;

set CLASSPATH=%CLASSPATH%;%SVWSCLASSESPATH%;