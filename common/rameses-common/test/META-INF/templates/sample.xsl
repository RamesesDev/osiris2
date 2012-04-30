<?xml version="1.0" encoding="UTF-8" ?>

<!--
    Document   : sample.xsl
    Created on : April 21, 2012, 9:02 AM
    Author     : Elmo
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:strip-space elements="*"/>
    
    <xsl:template match="/">
        <html>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="header">
        This is header <xsl:value-of select="." disable-output-escaping="yes"/>
    </xsl:template>
</xsl:stylesheet>

