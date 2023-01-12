/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author damessis
 */
public class JeuDevineLeMotOrdre extends Jeu {
    /* ------------------- Attributs ------------------- */
    private int nbLettresRestantes;
    private Chronometre chrono;
    
    
    /* ------------------- Constructeur ------------------- */
    public JeuDevineLeMotOrdre() throws ParserConfigurationException, SAXException, IOException {
        super();
        
    }
    
    
    /* ------------------- Méthodes ------------------- */
    protected void démarrePartie(Partie partie) {
        System.out.println(partie.getNiveau());
        if(partie.getNiveau()==1){
            chrono=new Chronometre(60);
        }else if(partie.getNiveau()==2){
            chrono=new Chronometre(120);
        }else if(partie.getNiveau()==3){
            chrono=new Chronometre(180);
        }else if(partie.getNiveau()==4){
            chrono=new Chronometre(240);
        }else{
            chrono=new Chronometre(300);
        }
        chrono.start();
        nbLettresRestantes=super.getLetters().size();
    }
    
    protected void appliqueRegles(Partie partie) {
        boolean tr=false;
        int tempsTr=0;
        int timeSpent = (int) ((chrono.getCurrent() - chrono.getBegin())/1000.0);
        menuText.addText("Temps restant : "+(chrono.getLimite()-timeSpent), "remaningtime", 150, 40);
        menuText.getText("remaningtime").display();
        if((chrono.getLimite()-timeSpent)<=chrono.getLimite()-5){
            menuText.getText("intro_mot").clean();
            menuText.getText("_mot").clean();
        }
        if((chrono.getLimite()-timeSpent)<=1){
           menuText.getText("remaningtime").clean(); 
           menuText.getText("tr").clean();
        }
        menuText.addText("Vous avez trouvé une bonne lettre " , "tr", 150, 250);
        tr=tuxTrouveLettre();
        if(tr==true){
            menuText.getText("tr").display();
            tempsTr=chrono.getLimite()-timeSpent;
        }
        
        if(tempsTr<chrono.getLimite()-timeSpent-5){
            menuText.getText("tr").clean();
        }
        if(nbLettresRestantes==0||!chrono.remainsTime()){
            tuxTrouveLettre();
            menuText.getText("tr").clean();
            terminePartie(partie);
            
        }
    }
    
    protected void terminePartie(Partie partie) {
        chrono.stop();
        partie.setTrouve(nbLettresRestantes);
        partie.setTemps(chrono.getSeconds());
        
    }
    
    private boolean tuxTrouveLettre() {
        boolean trouve=false;
        if(!letters.isEmpty()){
            if(super.collision(letters.get(0))){ 
                trouve=true;
                env.removeObject(letters.get(letters.size() - nbLettresRestantes));
                System.out.println("on a trouvé une lettre");
                letters.remove(letters.get(0));
                nbLettresRestantes=nbLettresRestantes-1;
                System.out.println("nbLettresRestantes "+nbLettresRestantes);
            }
        }
        return trouve;
    }
    
    private int getNblettresRestantes() {
        return nbLettresRestantes;
    }
    
    private int getTemps() {
        return 0;
    }
}
