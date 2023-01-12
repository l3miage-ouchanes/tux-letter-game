/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author soule
 */
public class Room {
    
    /* ------------------- Attributs ------------------- */
    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;


    /* ------------------- Constructeur ------------------- */
    public Room() {
        try{
        //Analyse le document xml et construit un arbre DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance () ;
        DocumentBuilder db = dbf.newDocumentBuilder() ;
        Document doc =db.parse( "src/data/xml/plateau.xml");
        //recuperation de l'element height
        Element heightElm=(Element) doc.getElementsByTagName("height").item(0);
        //recuperation du contenu de l'element height puis sa conversion en entier
        height=Integer.parseInt(heightElm.getTextContent());
        //recuperation de l'element width
        Element widthElm=(Element) doc.getElementsByTagName("width").item(0);
        //recuperation du contenu de l'element width puis sa conversion en entier
        width=Integer.parseInt(widthElm.getTextContent());
        //recuperation de l'element depth
        Element depthElm=(Element) doc.getElementsByTagName("depth").item(0);
        //recuperation du contenu de l'element depth puis sa conversion en entier
        depth=Integer.parseInt(depthElm.getTextContent());
        //recuperation de l'element textureBottom
        Element textureBottomElm=(Element) doc.getElementsByTagName("textureBottom").item(0);
        //recuperation du contenu de l'element textureBottom 
        textureBottom=textureBottomElm.getTextContent();
        //recuperation de l'element textureEast
        Element textureEastElm=(Element) doc.getElementsByTagName("textureEast").item(0);
        //recuperation du contenu de l'element textureEast 
        textureEast=textureEastElm.getTextContent();
        //recuperation de l'element textureWest
        Element textureWestElm=(Element) doc.getElementsByTagName("textureWest").item(0);
        //recuperation du contenu de l'element textureWestElm
        textureWest=textureWestElm.getTextContent();
        //recuperation de l'element textureNorth
        Element textureNorthElm=(Element) doc.getElementsByTagName("textureNorth").item(0);
        //recuperation du contenu de l'element textureNorthElm
        textureNorth=textureNorthElm.getTextContent();
        }catch (ParserConfigurationException | SAXException | IOException  ex) {
            Logger.getLogger(Dico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /* ------------------- Méthodes ------------------- */
    
    /* -------- Getters -------- */

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTextureBottom() {
        return textureBottom;
    }

    public String getTextureNorth() {
        return textureNorth;
    }

    public String getTextureEast() {
        return textureEast;
    }

    public String getTextureWest() {
        return textureWest;
    }

    public String getTextureTop() {
        return textureTop;
    }

    public String getTextureSouth() {
        return textureSouth;
    }
    
    /* -------- Setters -------- */

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }
    
}
