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
    
    
    <!-- Template qui renvoie le résultat final -->
    <!-- affiche la liste de mot par niveau par ordre alphabétique -->
    <xsl:template match="/">
        <html>
            <head>
                <title>trie alphabetique par niveau.xsl</title>
            </head>
            <body>
                <xsl:apply-templates select="//tux:niveau"/>
            </body>
        </html>
    </xsl:template>
    
    <!-- Template qui trie par niveau -->
    <xsl:template match="tux:niveau"><b>Niveau
        <xsl:value-of select="@niv"/> :</b><br/>
        <xsl:apply-templates select="tux:mot">
            <xsl:sort select="."/>
        </xsl:apply-templates>
            <br/>
    </xsl:template>
    
    <!-- Template qui récupère les mots -->
    <xsl:template match="tux:mot">
        <xsl:value-of select="."/><br/>
    </xsl:template>

</xsl:stylesheet>