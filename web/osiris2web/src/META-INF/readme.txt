-----------------------------------------------------------------
author       : jaycverg
description  : osiris2web framework version 1.1 settings
               for a new web platform

libraries
    facelet 1.1.12      apache-bean-utils
    javaee              groovy1.5.5
-----------------------------------------------------------------

--- CONFIGURATIONS

1. web.xml configuration
    (a) register Osiris Resource Resolver
        <context-param>
            <param-name>facelets.RESOURCE_RESOLVER</param-name>
            <param-value>com.rameses.osiris2.web.Osiris2ResourceResolver</param-value>
        </context-param>

    (b) register Osiris2Startup Servlet
        <servlet>
            <servlet-name>Faces Servlet</servlet-name>
            <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
            <load-on-startup>1</load-on-startup>
        </servlet>   

        <servlet-mapping>
            <servlet-name>Faces Servlet</servlet-name>
            <url-pattern>*.jsf</url-pattern>
        </servlet-mapping>

        <servlet>
            <servlet-name>osiris2-startup</servlet-name>
            <servlet-class>com.rameses.osiris2.web.Osiris2Startup</servlet-class>
            <load-on-startup>1</load-on-startup>
        </servlet>

        <servlet-mapping>
            <servlet-name>osiris2-startup</servlet-name>
            <url-pattern>/reload</url-pattern>
        </servlet-mapping>

    (c)  Osiris Filter ( required mapping is /* )
        <filter>
            <filter-name>Osiris Filter</filter-name>
            <filter-class>com.rameses.osiris2.web.Osiris2WebFilter</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>Osiris Filter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>

    (d) Rameses Common Web Filter
        <filter>
            <filter-name>WebFilter</filter-name>
            <filter-class>com.rameses.web.common.Filter</filter-class>
            <init-param>
                <param-name>forceparser</param-name>
                <param-value>false</param-value>
            </init-param>
        </filter>
        <filter-mapping>
            <filter-name>WebFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>   

    (e) jboss config
        <listener>
            <listener-class>org.apache.myfaces.webapp.StartupServletContextListener</listener-class>
        </listener>

2. client.conf
    app.title=<platform title>
    app.url=http://localhost/updates.xml




