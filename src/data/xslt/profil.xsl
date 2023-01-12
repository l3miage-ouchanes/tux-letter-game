<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : profil.xsl
    Created on : 26 October 2022, 01:02
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
    
    
    <!-- Temple qui envoie le résultat final -->
    <!-- affiche un profil -->
    <xsl:template match="/">
        <html>
            <head>
                <title> Profil de <xsl:value-of select="tux:profil/tux:nom"/> </title>
                <link href="../css/profil.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <div class="interface">
                    <br/>
                    <h1>Profil</h1>
                    <br/>
                    <br/>
                    <br/>
                    <div class="infos">
                        
                        <div class="nom">
                            <xsl:value-of select="tux:profil/tux:nom"/>
                            <br/>
                        </div>
                        
                
                        <!-- récupère l'image de l'avatar dans la variable image -->
                        <xsl:variable name="image" select="tux:profil/tux:avatar"/>
                
                        <img class="avatar" src="../images/{$image}"/>
                        <br/>
                        Date d'anniversaire : <xsl:value-of select="tux:profil/tux:anniversaire/text()"/>
                    </div>
                
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                    <br/>
                
                    <table>
                        <tr>
                            <th>Date</th>
                            <th>Niveau</th>
                            <th>Mot</th>
                            <th>Temps</th>
                            <th>Finis à</th>
                        </tr>
                        <xsl:apply-templates select="//tux:partie"/>
                    </table>
                </div>
            </body>
        </html>
    </xsl:template>
    
    
    <!-- Template qui affiche les infos pour chaque partie dans un tableau -->
    <xsl:template match="tux:partie">
        <tr>
            <td>
                <xsl:value-of select="@date"/>
            </td>
            <td>
                <xsl:value-of select="tux:mot/@niveau"/>
            </td>
            <td>
                <xsl:value-of select="tux:mot"/>
            </td>
            <td>
                <xsl:value-of select="tux:temps"/>
            </td>
            <td>
                <xsl:value-of select="@trouvé"/>
            </td>
        </tr>
    </xsl:template>

</xsl:stylesheet>