/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author soule
 */
public class Profil {
    
    
    
  /*-------------------------------------------------------------Debut---------------------------------------------------------------------------------*/
    /* ------------------- Les attributs ------------------- */
    private String nom;

    public String getNom() {
        return nom;
    }
    private String dateNaissance;
    private String avatar;
    private  ArrayList<Partie> parties;

    public ArrayList<Partie> getParties() {
        return parties;
    }

    public Partie getUnePartieInacheve() {
        int i = 0;
        while(i < parties.size() && parties.get(i).getTrouve() == 100){
            i++;
        }
        if(i < parties.size()){
            supprimerPartie(parties.get(i).getMot());
            return parties.get(i);
            
        }else{
            return null;
        }
    }
    
    private void supprimerPartie(String mot) {
        Element partiesElm = (Element)doc.getElementsByTagName("parties").item(0);
        NodeList nParties = doc.getElementsByTagName("partie");
        for(int j = 0; j < nParties.getLength(); j++){
            //System.out.println(nParties.item(j).getTextContent());
        }
        for(int i = 0; i < nParties.getLength(); i++){
            Element ePartie = (Element) nParties.item(i);
            if(mot.equals(ePartie.getElementsByTagName("mot").item(0).getTextContent())){
                partiesElm.removeChild(ePartie);
            }
        }
        // On met à jour le fichier après avoir supprimer les parties qu'il faut
        toXML("src/data/xml/profil-"+this.getNom()+".xml");
    }
    /* ------------------- variable ------------------- */
    public Document doc;
    
  /*-------------------------------------------------------------Fin---------------------------------------------------------------------------------*/
    
    
    
  /*-------------------------------------------------------------Debut---------------------------------------------------------------------------------*/
    /* ------------------- Constructeur ------------------- */
    //ce premier constructeur initialise un profil avec les informations qui lui sont passé en parametre
    public Profil(String nom,String dateNaissance) {
        this.nom=nom;
        this.dateNaissance=dateNaissance;
        this.avatar=nom+".png";
        this.parties=new ArrayList<Partie>();
    }

    //ce deuxieme constructeur prend le nom d'un fichier xml en parametre il le parse sous forme d'arbre DOM
    //puis recupere les informations necessaire pour initialiser un profil
    /*cette fonction marche*/
    public Profil(String filename){
        doc = fromXML(filename);
        this.parties=new ArrayList<Partie>();
        try {
            // recuperation de l'element nom
            Element nomElt=(Element)doc.getElementsByTagName("nom").item(0);
            // recuperation du contenu l'element nom
            this.nom=nomElt.getTextContent();
            // recuperation de l'element nom
            Element dateNaissanceElt=(Element)doc.getElementsByTagName("anniversaire").item(0);
            // recuperation du contenu l'element dateNaissance au format xmlDate
            String dateNaissanceXML=dateNaissanceElt.getTextContent();
            if(dateNaissanceXML.length()>=10){
                //conversion de la date au format du profile
                this.dateNaissance=xmlDateToProfileDate(dateNaissanceXML);
            }
            //conversion de la date au format du profile
            this.dateNaissance=xmlDateToProfileDate(dateNaissanceXML);
            // recuperation de l'element avatar
            Element avatarElt=(Element)doc.getElementsByTagName("avatar").item(0);
            // recuperation du contenu l'element avatar
            this.avatar=avatarElt.getTextContent();
            //recuperation de la liste des parties
            NodeList lesParties=doc.getElementsByTagName("partie");
            System.out.println(lesParties);
            //on parcourt la liste des parties recuperé jusqu'a la fin et on recupere chaque partie 
            //pour l'affecter a l'arraylist contenant les parties
            for(int i=0;i<lesParties.getLength();i++){
                //recuperation d'une partie de la liste des parties
                Element partieElt=(Element)lesParties.item(i);
                //creation d'une partie qu'on instancie avec le constructeur qui prend un element en parametre
                Partie partie=new Partie(partieElt);
                partie.toString();
                parties.add(partie);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Profil() {
    }
    
  /*-------------------------------------------------------------Fin---------------------------------------------------------------------------------*/

    
    
    
  /*-------------------------------------------------------------Debut---------------------------------------------------------------------------------*/
    /* ------------------- Les methodes de recuperation et d'envoie vers le fichier xml ------------------- */
    // Cree un DOM à partir d'un fichier XML
    private Document fromXML(String filename) {
        try {
            return XMLUtil.DocumentFactory.fromFile(filename);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    // Sauvegarde un DOM en XML
    private void toXML(String filename) {
        try {
            XMLUtil.DocumentTransform.writeDoc(doc, filename);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /// prend une date au format XML (i.e. ????-??-??) et retourne une date
    /// dans profile au format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// prend une date dans profile au format: dd/mm/yyyy et retourne une date
    /// au format XML  (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }
    /*-------------------------------------------------------------Fin---------------------------------------------------------------------------------*/
    
    
    
    /*-------------------------------------------------------------Debut---------------------------------------------------------------------------------*/
      /* ------------------- autres methodes ------------------- */
    //La méthode ajoutePartie(p : Partie):void permet de rajouter à la liste des parties une Partie instanciée passé en parametre
    public void ajouterPartie(Partie p){
       parties.add(p); 
    }
    
    public int getDernierNiveau(){
        return parties.get(parties.size()-1).getNiveau();
    }
    
    public void sauvegarder(String filename) throws IOException, SAXException{
        if(charge(this.nom)){
             doc = fromXML(filename);
            Element partiesElm = (Element) doc.getElementsByTagName("parties").item(0);
            Partie p = this.parties.get(this.parties.size()-1);
            Element partieElm = p.getPartie(doc);
            partiesElm.appendChild(partieElm);
            toXML(filename);
        }else{
            try{
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
                //creation du noeud racine profil
                Element rootProfil = doc.createElement("profil");
                doc.setXmlVersion("1.0");
                 rootProfil.setAttribute("xmlns", "http://myGame/tux");
                 rootProfil.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
                 rootProfil.setAttribute("xsi:schemaLocation", "http://myGame/tux ../xsd/profil.xsd");

                //affectation du noeud racine dans le document 
                //doc.appendChild(profil);
                //creation du noeud nom
                Element nomElm = doc.createElement("nom");
                nomElm.setTextContent(this.nom);
                //on attache le noeud nom a la racine
                rootProfil.appendChild(nomElm);
                //creation du noeud avatar
                Element avatarElm = doc.createElement("avatar");
                avatarElm.setTextContent(this.avatar);
                //on attache le noeud avatar a la racine
                rootProfil.appendChild(avatarElm);
                //creation du noeud anniversaire
                Element anniversaireElm = doc.createElement("anniversaire");
                if(this.dateNaissance.length()>=10){
                    anniversaireElm.setTextContent(profileDateToXmlDate(this.dateNaissance));
                }
                //on attache le noeud anniversaire a la racine
                rootProfil.appendChild(anniversaireElm);
                //creation du noeud parties
                Element partiesElm = doc.createElement("parties");
                for (Partie p : parties) {
                    Element partieElm = p.getPartie(doc);
                    partiesElm.appendChild(partieElm);
                }
                //on attache les parties a la  racine
                rootProfil.appendChild(partiesElm);
                //File f1=new File(filename);
                //f1.createNewFile();
                doc.appendChild(rootProfil);
                toXML(filename);
            }catch(ParserConfigurationException | DOMException ex){
                    
            }
                
        }
        
        
    }
    
    @Override
    public String toString() {
        return "Profil{" + "nom=" + nom + ", dateNaissance=" + dateNaissance + ", avatar=" + avatar + ", parties=" + parties + ", doc=" + doc + '}';
    }
    /*-------------------------------------------------------------Fin---------------------------------------------------------------------------------*/

    boolean charge(String nomJoueur) {
        boolean chargementReussi=false;
        Path p = Paths.get("src/data/xml/"+nomJoueur+".xml");
        if(Files.exists(p) && Files.isRegularFile(p)){
            chargementReussi=true;
        }
        return chargementReussi; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
