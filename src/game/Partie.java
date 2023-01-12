/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author damessis
 */
public class Partie {
/*----------------------------------------------------------------Début------------------------------------------------------------------------------*/    
    /* ------------------- Attributs ------------------- */
    private String date; 
    private String mot; 
    private int niveau;
    private int trouve;//le pourcentage (arrondi) de lettres trouvées

    public int getTrouve() {
        return trouve;
    }

    public int getTemps() {
        return temps;
    }
    private int temps; 
 /*----------------------------------------------------------------Fin------------------------------------------------------------------------------*/ 



 /*----------------------------------------------------------------Début------------------------------------------------------------------------------*/
    /* ------------------- Constructeur 1 ------------------- */
    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
        this.trouve = 0 ;
        this.temps = 0;
    }

    public String getMot() {
        return mot;
    }
    /* ------------------------------------------------------ */
    /* ------------------- Constructeur 2 ------------------- */
    /*cette fonction marche*/
    public Partie(Element elt){
        this.date=elt.getAttribute("date");
        if(elt.hasAttribute("trouvé")){
            //recuperation de la valeur de l'attribut trouvé 
            String strTrouve=elt.getAttribute("trouvé");
            // on retire le caractère % de la chaine de trouvé avant de le caster en entier
            String strTrouveSansP = strTrouve.replace("%", "");
            this.trouve=Integer.parseInt(strTrouveSansP);
            //si trouvé existe alors il y a aussi l'element temps donc on recupère le texte contenu dans l'element temps 
            //avant de le caster en entier
            //recuperation de l'element temps
            Element tempsElt=(Element)elt.getElementsByTagName("temps").item(0);
            if(tempsElt!=null){
            //recuperation du contenu de l'element temps
            String tempsStr=tempsElt.getTextContent();
            //conversion du contenu recupéré sous forme de string en entier
            this.temps=Integer.parseInt(tempsStr);
            }else{
               this.temps = 0;  
            }
            
        }else{
            this.trouve = 0 ;
            this.temps = 0; 
        }
        //recuperation de l'element mot
        Element motElt=(Element)elt.getElementsByTagName("mot").item(0);
        //recuperation du contenu de l'element mot
        this.mot=motElt.getTextContent();
        //recuperation de la valeur de l'attribut niveau sous forme de string
        String niveauStr=motElt.getAttribute("niveau");
        //conversion de la valeur recupéré sous forme de string en entier
        this.niveau=Integer.parseInt(niveauStr);
    }
    /* ------------------------------------------------------ */
 /*----------------------------------------------------------------Fin------------------------------------------------------------------------------*/ 

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


 /*----------------------------------------------------------------Début------------------------------------------------------------------------------*/
    public Element getPartie(Document doc){
        //creation d'un element partie
        Element partieELm=doc.createElement("partie");
        //creation d'un element mot qu'on mettra dans partie
        Element motELm=doc.createElement("mot");
        //creation d'un element temps qu'on mettra dans partie
        Element tempsELm=doc.createElement("temps");
        //affectation de l'attribut date a l'element partie
        partieELm.setAttribute("date", this.date);
        //si l'attribut trouvé contient
        //si l'attribut trouvé contient une valeur convenable c'est a dire supérieur a 0 
        //donc tux a trouvé des lettres mais inferieur a 100 donc il ne les pas toutes trouvé 
        
        //affectation de l'attribut niveau a l'element mot
        motELm.setAttribute("niveau", Integer.toString(this.niveau));
        //affectation de la valeur textuelle qui se trouve dans l'element temps
        tempsELm.appendChild(doc.createTextNode(Integer.toString(this.temps)));
        //affectation de la valeur textuelle qui se trouve dans l'element mot
        motELm.appendChild(doc.createTextNode(this.mot));
        //affectation des element temps et mot en tant que fils de l'élement partie
        //System.out.println("motlength "+mot.length());
        //System.out.println("trouve "+trouve);
        if(trouve!=0){
            String trouve_pct=this.trouve+"%";
            partieELm.setAttribute("trouvé", trouve_pct);
        }
        partieELm.appendChild(tempsELm);
        partieELm.appendChild(motELm);
        //recuperation de parties qui se trouve dans document 
        //pour lui affecter la nouvelle partie qu'on vient de creer
        //Element parties=(Element)doc.getElementsByTagName("parties").item(0);
        //parties.appendChild(partieELm);
        return partieELm;
    }
/*----------------------------------------------------------------Fin------------------------------------------------------------------------------*/ 



/*----------------------------------------------------------------Début------------------------------------------------------------------------------*/
 /* ------------------- getteurs et setteurs ------------------- */

   /*getNiveau retourne le niveau de la partie courante */
    public int getNiveau() {
        return niveau;
    }
    /*setTrouve(nbrLettreRestantes) renvoie un pourcentage en fonction du nombre de lettres trouvées. */
    public void setTrouve(int nbLettresRestantes) {
        double tmp=(double)(mot.length()-nbLettresRestantes)/(double)mot.length()*100.0;
        this.trouve=(int)tmp;
    }
    //setTemps(temps) permet d'affecter a la partie courante le temps passer en parametre
    public void setTemps(int temps) {
        this.temps = temps;
    }
    //toString()
    @Override
    public String toString() {
        return "Partie{" + "date=" + date + ", mot=" + mot + ", niveau=" + niveau + ", trouv\u00e9=" + trouve + ", temps=" + temps + '}';
    }
/*----------------------------------------------------------------Fin------------------------------------------------------------------------------*/ 
}