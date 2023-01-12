/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author damessis
 */
public class Dico extends DefaultHandler {

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    //les attribut de la classe dico
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    String cheminFichierDico;
    private StringBuffer buffer;
    //des variables qui nous permettant de localiser la position du parseur
    enum State{
        START,
        NIVEAUX,
        NIVEAU,
        MOT,
        AUTRES,
    }
    State currentState;
    private int niv;

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/

 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    public Dico(String cheminFichierDico) {
        super();
        this.listeNiveau1 = new ArrayList<String>();
        this.listeNiveau2 = new ArrayList<String>();
        this.listeNiveau3 = new ArrayList<String>();
        this.listeNiveau4 = new ArrayList<String>();
        this.listeNiveau5 = new ArrayList<String>();
        this.cheminFichierDico = cheminFichierDico;
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/

 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    //methode getMotDepuisListeNiveau(int niveau) qui recupere aleatoirement un mot de la liste correspondant au niveau passé en parametre
    public String getMotDepuisListeNiveau(int niveau) {
        switch (verifieNiveau(niveau)) {
            case 1:
                return getMotDepuisListe(listeNiveau1);
            case 2:
                return getMotDepuisListe(listeNiveau2);
            case 3:
                return getMotDepuisListe(listeNiveau3);
            case 4:
                return getMotDepuisListe(listeNiveau4);
            case 5:
                return getMotDepuisListe(listeNiveau5);
            default:
                return "Erreur impossible";
        }
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/

 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    //methode getMotDepuisListe(ArrayList<String> list) qui recupere aleatoirement un mot de la liste passé en parametre
    private String getMotDepuisListe(ArrayList<String> list) {
        String motRenvoye;
        if (list.isEmpty()) {
            motRenvoye = "inexistant";
        } else {
            // generating the index using Math.random()
            int index = (int) (Math.random() * list.size());
            motRenvoye = list.get(index);
        }
        return motRenvoye;
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/

 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    //methode verifieNiveau qui verifie si le niveau passé en parametre est dans la limite autorisé, si ce n'est pas le cas elle change le niveau pour un niveau plus coherent
    private int verifieNiveau(int niveau) {
        switch (niveau) {
            case 5:
                return 5;
            case 4:
                return 4;
            case 3:
                return 3;
            case 2:
                return 2;
            case 1:
                return 1;
            default:
                return (int) (Math.random() * 5) + 1;

        }
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/

 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    //methode  ajouteMotADico(int niveau, String mot) verifie que le niveau en parametre est valide sinon le change puis ajoute ce mot dans la liste correspondant uaniveau indiqué
    public void ajouteMotADico(int niveau, String mot) {
        switch (verifieNiveau(niveau)) {
            case 1:
                listeNiveau1.add(mot);
                break;
            case 2:
                listeNiveau2.add(mot);
                break;
            case 3:
                listeNiveau3.add(mot);
                break;
            case 4:
                listeNiveau4.add(mot);
                break;
            case 5:
                listeNiveau5.add(mot);
                break;
            default:
                System.out.println("Impossible d'ajouter le mot, cet affichage indique que la fontion verifieNiveau ne fonctionne pas bien");
                break;
        }
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/
 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    // nous retourne l'attribut contenant le nom du fichier
    public String getCheminFichierDico() {
        return cheminFichierDico;
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/

 /*----------------------------------------------------------------------------------------------------------------------------------------------*/
    public void lireDictionnaireDom(String path, String filename) throws JAXBException {

        cheminFichierDico = path + "/" + filename;
        try {
            //Analyse le document xml et construit un arbre DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(cheminFichierDico);

            // Crée un objet "chemin XPath "
            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

            // Récupère la motsNiveau "i"  de niveau i appartient a [1,5]
            NodeList motsNiveau1 = (NodeList) xpath.evaluate("//niveau[@niv='1']/mot", doc, XPathConstants.NODESET);
            NodeList motsNiveau2 = (NodeList) xpath.evaluate("//niveau[@niv='2']/mot", doc, XPathConstants.NODESET);
            NodeList motsNiveau3 = (NodeList) xpath.evaluate("//niveau[@niv='3']/mot", doc, XPathConstants.NODESET);
            NodeList motsNiveau4 = (NodeList) xpath.evaluate("//niveau[@niv='4']/mot", doc, XPathConstants.NODESET);
            NodeList motsNiveau5 = (NodeList) xpath.evaluate("//niveau[@niv='5']/mot", doc, XPathConstants.NODESET);

            //on affecte les mot du niveau i a l'arraylist correspondant
            lireNiveauDictionnaireDom(motsNiveau1, 1);
            lireNiveauDictionnaireDom(motsNiveau2, 2);
            lireNiveauDictionnaireDom(motsNiveau3, 3);
            lireNiveauDictionnaireDom(motsNiveau4, 4);
            lireNiveauDictionnaireDom(motsNiveau5, 5);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(Dico.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //cette fonction lit tous les elements mot de la liste d'un niveau spécifiquepassé en parametre  et les range dans la liste passé parametre aussi
    public void lireNiveauDictionnaireDom(NodeList listMotsNiveau, int niveau) {
        String mot;
        for (int i = 0; i < listMotsNiveau.getLength(); i++) {
            //on recupere le contenu d'un element mot puis apres l'avoir converti en string on l'affecte a la variable mot
            mot = (String) listMotsNiveau.item(i).getTextContent();
            //on ajoute la variable mot initialisé precedement dans l'arraylist
            ajouteMotADico(niveau, mot);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------------------------------------*/
 /*-------------------------------------------------------------------Début---------------------------------------------------------------------------*/
    //lireDictionnaire qui parse le fichier XML avec un parser sax puis fait appel au differente 
    //fonction de DefaultHandler que nous redefinissons pour:
    public void lireDictionnaire() throws ParserConfigurationException, org.xml.sax.SAXException, IOException{
        XMLReader parser;
        parser=XMLReaderFactory.createXMLReader();
        parser.setContentHandler(this);
        parser.parse("src/data/xml/dico.xml");
    }
    //si l'element croisé lors de l'avancé du parseur est l'element debut niveau alors on essaie de recuperer
    //l'attribut niv dans le buffer si ce n'est pas faisable on leve une exception puis on passe 
    // la variable qui nous indique qu'on est dans un niveau a true
    //sinon si l'element est mot on ne fait rien et on passe la variable qui nous indique qu'on est dans un mot a true
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (currentState) {
            case START:
                if(localName.equals("niveaux")){
                    currentState=State.NIVEAUX;
                }
                break;
            case NIVEAUX:
                if(localName.equals("niveau")){
                    currentState=State.NIVEAU;
                    niv=Integer.parseInt(attributes.getValue("niv"));
                    
                }
                break;
            case NIVEAU:
                if(localName.equals("mot")){
                    currentState=State.MOT;
                }
                break;
            default:
                break;
        }
    }

    //quand on sort d'un niveau ou d'un mot on pense bien a le signaler en mettant 
    //les variable respective lié a la position du parseur a false
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (currentState) {
            case NIVEAUX:
                currentState=State.NIVEAU;
                break;
            case NIVEAU:
                currentState=State.NIVEAUX;
                break;
            case MOT:
                if(localName.equals("mot")){
                    currentState=State.NIVEAU;
                }
                
                break;
            default:
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String mot="";
        for(int i=start;i<start+length;i++){
            mot+=ch[i];
        }
        switch(currentState){
            case MOT:
                switch(niv){
                    case 1:
                        listeNiveau1.add(mot);
                    break;
                    case 2:
                        listeNiveau2.add(mot);
                    break;
                    case 3:
                        listeNiveau3.add(mot);
                    break;
                    case 4:
                        listeNiveau4.add(mot);
                    break;
                    case 5:
                        listeNiveau5.add(mot);
                    break;
                    default:
                    break;      
                }
            default:
            break;
        }
        
    }

    @Override
    public void startDocument() throws SAXException {
        currentState=State.START;
    }

    @Override
    public void endDocument() throws SAXException {
    }
    /*-------------------------------------------------------------------Fin-----------------------------------------------------------------------------*/

}
