/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package game;

import env3d.advanced.EnvNode;

/**
 *
 * @author soule
 */
public class Letter extends EnvNode {
    /* ------------------- Attributs ------------------- */
    protected double SCALE = 5;
    private double DIAMETRE_BASE = 2;
    
    private char letter;
    private double diametre;
    
    /* ------------------- Constructeur ------------------- */
    public Letter(char l, double x, double y) {
        letter = l;
        diametre = SCALE * DIAMETRE_BASE;
        setScale(SCALE); //diametre d'une lettre est de 2 pour un scale = 1
        setX(x);// positionnement au milieu de la largeur de la room
        setY(SCALE * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(y); // positionnement au milieu de la profondeur de la room
        if (letter == ' ') {
            setTexture("models/letter/cube.png");
        } else {
            setTexture("models/letter/" + letter + ".png");
        }
        setModel("models/letter/cube.obj");
    }
    
    
    /* ------------------- Méthodes ------------------- */
    public double getDiametre() {
        return diametre;
    }

    public void setPositionX(double x) {
        setX(x);
    }
    
    public void setPositionY(double Y) {
        setZ(Y);
    }
    
}
