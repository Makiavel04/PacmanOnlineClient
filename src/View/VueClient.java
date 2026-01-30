package View;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Etat.Etat;
import Etat.EtatInit;

public class VueClient {
    
    JFrame frame;
    JPanel panel;

    Etat etat;

    public VueClient(){
        this.etat = new EtatInit(this);
    }

    public void setEtat(Etat etat){
        this.etat = etat;
    }
    public Etat getEtat(){
        return this.etat;
    }

    public void changerAffichage(){}//Placeholder pour les etat pour l'instant
}
