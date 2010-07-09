-----------------------------------------------------------------
author: jaycverg
description: osiris2web framework version 1.1
libraries:
    facelet 1.1.12      apache-bean-utils
    javaee              groovy1.5.5
-----------------------------------------------------------------

--- CONFIGURATIONS

1. web.xml configuration
    (a) register Osiris Resource Resolver

    (b) register Osiris2Startup Servlet


    (c)  Osiris Filter ( required mapping is /* )
        <filter>
            <filter-name>Osiris Filter</filter-name>
            <filter-class>com.rameses.osiris2.web.Osiris2WebFilter</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>Osiris Filter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>

    (d) Ajax For Jsf Filter configuration
        <filter>
            <display-name>Ajax4jsf Filter</display-name>
            <filter-name>ajax4jsf</filter-name>
            <filter-class>org.ajax4jsf.Filter</filter-class>
            <!-- parse ajax request only -->
            <init-param>
                <param-name>forceparser</param-name>
                <param-value>false</param-value>
            </init-param>
        </filter>
        <filter-mapping>
            <filter-name>ajax4jsf</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>

    (e) jboss config
        <listener>
            <listener-class>org.apache.myfaces.webapp.StartupServletContextListener</listener-class>
        </listener>

2. client.conf
    app.title=<platform title>
    app.url=http://localhost/updates.xml

3. 



