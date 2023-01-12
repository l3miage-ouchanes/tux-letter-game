<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : trie alphabetique.xsl
    Created on : 25 October 2022, 16:15
    Author     : soule
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:tux="http://myGame/tux"
                xmlns="http://www.w3.org/1999/xhtml">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    
    
    <!-- Template qui envoie le résultat final -->
    <!-- affiche la liste de mot par ordre alphabétique -->
    <xsl:template match="/">
        <html>
            <head>
                <title>trie alphabetique.xsl</title>
            </head>
            <body>
                <xsl:apply-templates select="//tux:mot">
                    <xsl:sort select="." order="ascending"/>
                </xsl:apply-templates>
            </body>
        </html>
    </xsl:template>
    
    
    <!-- Template qui récupère mes mots -->
    <xsl:template match="tux:mot">
        <xsl:value-of select="."/><br/>
    </xsl:template>
    

</xsl:stylesheet>