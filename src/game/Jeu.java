/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.Env;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.lwjgl.input.Keyboard;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 *
 * @author soule
 */
public abstract class Jeu {
    
    enum MENU_VAL {
        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

   /* ------------------- Attributs ------------------- */
    final Env env;
    Tux tux;
    private final Room mainRoom;
    private final Room menuRoom;
    private Profil profil;
    ArrayList<Letter> letters=new ArrayList<Letter>();
    EditeurDico edition;

    public void setLetters(ArrayList<Letter> letters) {
        this.letters = letters;
    }

    public ArrayList<Letter> getLetters() {
        return letters;
    }
    
    private final Dico dico;
    protected EnvTextMap menuText;
    int niveau;
    
    private boolean[][] grilleCoord;
    
    /* ------------------- Constructeur ------------------- */
    public Jeu() throws ParserConfigurationException, SAXException, IOException {
        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        mainRoom = new Room();
        
        grilleCoord = null;

        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();
        
        // Dictionnaire
        dico = new Dico("src/data/xml/dico.xml");
        //aprsing sax du dictonnaire
        dico.lireDictionnaire();
        
        
        // éditeur de dico
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        edition = new EditeurDico(doc);
        edition.lireDOM("src/data/xml/dico.xml");

        // instancie le menuText
        menuText = new EnvTextMap(env);
        
        // Textes affichés à l'écran
        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("Vous etes un nouveau joueur, vous n'avez pas de partie existante", "pasDePartie", 250, 280);
        menuText.addText("Choisir un niveau[1-5] :", "niveau", 150, 280);
        menuText.addText("2. Charger une partie existante ?", "Jeu2", 250, 260);
        menuText.addText("3. Sortir de ce jeu ?", "Jeu3", 250, 240);
        menuText.addText("4. Quitter le jeu ?", "Jeu4", 250, 220);
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("Entrez une date de naissance : ", "DateNaissanceJoueur", 200, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("3. Sortir du jeu ?", "Principal3", 250, 240);
        menuText.addText("4. Editer le dictionnaire", "ajoutMot", 250, 220);
        menuText.addText("Mot a trouver : ", "intro_mot", 350, 40);
        
        
    }


    /* ------------------- Méthodes ------------------- */
    public void execute() throws JAXBException, IOException, InterruptedException, SAXException, TransformerException {
        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);
        this.env.setDisplayStr("Au revoir !", 300, 30);
    }
    
    // fourni
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return nomJoueur;
    }
    
    // pour obtenir la date de naissance
    private String getNiveau_Mot() throws JAXBException, InterruptedException, SAXException, IOException {
        niveau = 0;
        //demande a l'utilsateur de saisir un niveau
        menuText.getText("niveau").display();
        while (!(niveau == Keyboard.KEY_1||niveau == Keyboard.KEY_NUMPAD1|| niveau == Keyboard.KEY_2 ||niveau == Keyboard.KEY_NUMPAD2|| niveau == Keyboard.KEY_3||niveau == Keyboard.KEY_NUMPAD3|| niveau == Keyboard.KEY_4||niveau == Keyboard.KEY_NUMPAD4|| niveau == Keyboard.KEY_5||niveau == Keyboard.KEY_NUMPAD5)) {
                niveau = env.getKey();
                env.advanceOneFrame();
        }
        menuText.getText("niveau").clean();
        switch (niveau) {
            case Keyboard.KEY_1:
            case Keyboard.KEY_NUMPAD1:
                niveau=1;
                break;
            case Keyboard.KEY_2:
            case Keyboard.KEY_NUMPAD2:
                niveau=2;
                break;
            case Keyboard.KEY_3:
            case Keyboard.KEY_NUMPAD3:
                niveau=3;
                break;
            case Keyboard.KEY_4:
            case Keyboard.KEY_NUMPAD4:
                niveau=4;
                break;
            case Keyboard.KEY_5:
            case Keyboard.KEY_NUMPAD5:
                niveau=5;
                break;
            default:
                break;
        }
        String mot;
        mot=dico.getMotDepuisListeNiveau(niveau);      
        menuText.getText("intro_mot").display();
        menuText.addText(mot.toUpperCase(), "_mot",475, 40);
        menuText.getText("_mot").display();
        System.out.println("niveau dans apres "+niveau);
        return mot;
    }
    
    
    private MENU_VAL menuJeu() throws JAXBException, InterruptedException, IOException, SAXException {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // restaure la room du menu
            env.setRoom(menuRoom);
            // affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();
            
            // vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1||touche == Keyboard.KEY_NUMPAD1|| touche == Keyboard.KEY_2 ||touche == Keyboard.KEY_NUMPAD2|| touche == Keyboard.KEY_3||touche == Keyboard.KEY_NUMPAD3)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();
            
            //
            
            // restaure la room du jeu
            env.setRoom(mainRoom);
            
            // et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // ----------------------------------------- 
                case Keyboard.KEY_NUMPAD1:
                case Keyboard.KEY_1: // choisi un niveau et charge un mot depuis le dico
                    // .......... dico.******
                    //demande a l'utilisateur de selectionner un niveau
                    String mot=getNiveau_Mot();
                    System.out.println("niveau juste apres "+niveau);
                    // crée un nouvelle partie
                    
                    LocalDate dateObj = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String date = dateObj.format(formatter);
                    partie = new Partie(date, mot, niveau);   
                    /*while(chronoTemp.remainsTime()){
                        menuText.getText("intro_mot").display();
                        menuText.addText(mot, "_mot",425, 40);
                        menuText.getText("_mot").display();
                        System.out.println("on a du temps");
                    }*/
                    
                    //chronoTemp.stop();
                    joue(partie);
                    //menuText.getText("_mot").display();
                    // enregistre la partie dans le profil --> enregistre le profil
                    profil.ajouterPartie(partie);
                    // .......... profil.******
                    profil.sauvegarder("./src/data/xml/"+profil.getNom()+".xml");
                    playTheGame = MENU_VAL.MENU_JOUE;
                    break;

                // -----------------------------------------
                // Touche 2 : Charger une partie existante
                // -----------------------------------------  
                case Keyboard.KEY_NUMPAD2:
                case Keyboard.KEY_2: // charge une partie existante
                    if(!profil.getParties().isEmpty()){
                        //partie = new Partie("2018-09-7", "test", 1); //XXXXXXXXX
                        partie =profil.getUnePartieInacheve();
                        menuText.addText(partie.getMot().toUpperCase(), "_mot",475, 40);
                        menuText.getText("_mot").display();
                        // Recupère le mot de la partie existante
                        // ..........
                        // joue
                        joue(partie);
                        // enregistre la partie dans le profil --> enregistre le profil
                        profil.ajouterPartie(partie);
                        
                        // .......... profil.******
                        if (profil.charge(profil.getNom())) {
                            profil.sauvegarder(profil.getNom()+".xml");
                        }
                    }else{
                        playTheGame = MENU_VAL.MENU_JOUE;
                    }
                    break;

                // -----------------------------------------
                // Touche 3 : Sortie de ce jeu
                // ----------------------------------------- 
                case Keyboard.KEY_NUMPAD3:
                case Keyboard.KEY_3:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // ----------------------------------------- 
                case Keyboard.KEY_NUMPAD4:
                case Keyboard.KEY_4:
                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    private MENU_VAL menuPrincipal() throws JAXBException, InterruptedException, IOException, SAXException, TransformerException {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;

        // restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
        menuText.getText("ajoutMot").display();
               
        // vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1||touche == Keyboard.KEY_NUMPAD1|| touche == Keyboard.KEY_2 ||touche == Keyboard.KEY_NUMPAD2|| touche == Keyboard.KEY_3||touche == Keyboard.KEY_NUMPAD3 ||touche == Keyboard.KEY_4||touche == Keyboard.KEY_NUMPAD4)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();
        menuText.getText("ajoutMot").clean();

        // et décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_NUMPAD1:
            case Keyboard.KEY_1:
                // demande le nom du joueur existant
                nomJoueur = getNomJoueur();
                // charge le profil de ce joueur si possible
                
                //System.out.println(nomFichier);
                if (profil.charge(nomJoueur)) {
                    profil=new Profil("./src/data/xml/"+nomJoueur+".xml");
                    choix = menuJeu();
                } else {
                    System.out.println("fichier non trouve");
                    choix = MENU_VAL.MENU_CONTINUE;
                    //choix = MENU_VAL.MENU_SORTIE;//CONTINUE;
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_NUMPAD2:
            case Keyboard.KEY_2:
                // demande le nom du nouveau joueur
                nomJoueur = getNomJoueur();
                // crée un profil avec le nom d'un nouveau joueur
                profil = new Profil(nomJoueur,"22/09/1971");
                profil.sauvegarder(nomJoueur+".xml");
                choix = menuJeu();
                break;
            // -------------------------------------
            // Touche 4 : Editer le dictionnaire
            // -------------------------------------
            case Keyboard.KEY_NUMPAD4:
            case Keyboard.KEY_4:
                int niveauMot = 0;
                String motAAjouter = "";
                 menuText.addText("Niveau du nouveau mot (de 1 à 5) : ", "newWordLevel", 50, 300);
        menuText.addText("Le nouveau mot  : ", "newWord", 25, 300);
                do {
                    menuText.getText("newWordLevel").display();
                    try{
                        niveauMot = Integer.parseInt(menuText.getText("newWordLevel").lire(true));
                        menuText.getText("newWordLevel").clean();
                    }catch(NumberFormatException e){
                        e.toString();
                    }
                } while (niveauMot < 1 && niveauMot > 5);

                menuText.getText("newWord").display();
                motAAjouter = menuText.getText("newWord").lire(true);
                menuText.getText("newWord").clean();
                edition.ajouterMot(motAAjouter, niveauMot);
                dico.ajouteMotADico(niveauMot, motAAjouter);
                edition.ecrireDOM("src/data/xml/dico.xml");
                break;
            // -------------------------------------
            // Touche 3 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_NUMPAD3:    
            case Keyboard.KEY_3:
                choix = MENU_VAL.MENU_SORTIE;
        }
        return choix;
    }
    
    
    public void joue(Partie partie) throws JAXBException {

        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        //generation d'un niveau aléatoire 
        //int niv=(int)(Math.random() * 5)+1;
        
        // Création de la liste de lettres
        String mot=partie.getMot();
        for (int i = 0; i < mot.length(); i++) {
            Letter letter;
            letter = new Letter(mot.charAt(i),  i * 20 + 10, mainRoom.getDepth() / 2.);
            letters.add(letter);
            env.addObject(letter);
        }
        
        creerGrilleCoord(); // création de la grille
        
        placerLettre();// placer les lettres sur le plateau
        
        
         
        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        
        démarrePartie(partie);
        // Boucle de jeu
        Boolean finished;
        finished = false;
        while (!finished) {
            
            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1|| env.getKey()==Keyboard.KEY_NUMPAD1) {
                finished = true;
            }

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.déplace();

            // Ici, on applique les regles
            appliqueRegles(partie);
            

            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
            if(partie.getTemps()!=0 ||partie.getTrouve()!=0 ){
                finished = true;
            }
        }

        // Ici on peut calculer des valeurs lorsque la partie est terminée
        //terminePartie(partie);

    }

    protected abstract void démarrePartie(Partie partie);

    protected abstract void appliqueRegles(Partie partie);

    protected abstract void terminePartie(Partie partie);
    
    /**
     * Renvoie la grille de coordonnées de notre room selon la taille d'une lettre
     * @return 
     */
    private void creerGrilleCoord() {
        int x; // abscisse de la grille
        int y; // ordonnée de la grille
        
        x = (int) (mainRoom.getWidth() / letters.get(0).getDiametre());
        y = (int) (mainRoom.getDepth() / letters.get(0).getDiametre());
        
        grilleCoord = new boolean[x][y]; // création de la grille
    }
    
    
    /**
     * Place les lettres sur le terrain
     */
    private void placerLettre() {
        int x;
        int y;
        double positionX;
        double positionY;
        double diametreLettre;
        boolean placé;
        
        diametreLettre = letters.get(0).getDiametre();
                
        for (int i = 0; i < letters.size(); i++) {
            placé = false;
            while (placé == false) {
                x = (int) (Math.random() * diametreLettre);
                y = (int) (Math.random() * diametreLettre);

                if (grilleCoord[x][y] == false) {
                    grilleCoord[x][y] = true;
                    positionX =  x * diametreLettre + diametreLettre/2;
                    positionY = y * diametreLettre + diametreLettre/2;
                    letters.get(i).setPositionX(positionX);
                    letters.get(i).setPositionY(positionY);
                    placé = true;
                }
            }
        }
    }
    
    
    /**
     * Renvoie la distance du personnage tux à la lettre
     * @param letter
     * @return 
     */
    protected double distance(Letter letter) {
        double distance;
        double x=Math.pow((tux.getX()-letter.getX()),2);
        double y=Math.pow((tux.getY()-letter.getY()),2);
        double z=Math.pow((tux.getZ()-letter.getZ()),2);
        distance=Math.sqrt(x+z);
        return distance;
    }
    
    protected boolean collision(Letter letter) {
        boolean colli=false;
        if(distance(letter)< letter.getScale() + tux.getScale()){
           colli=true;
        }
        return colli;
    }
}