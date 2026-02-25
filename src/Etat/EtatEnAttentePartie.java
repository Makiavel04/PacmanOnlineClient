package Etat;

import Vue.VueClient;
import Vue.Panel.PanelAttentePartie;

/**
 * Classe représentant l'état d'attente d'une partie, où le joueur attend que la partie commence.
 */
public class EtatEnAttentePartie implements Etat{

    private VueClient vue;

    public EtatEnAttentePartie(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelAttentePartie(vue));
    }

    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void seConnecter() {}

    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void rejoindrePartie() {}

    @Override
    public void quitterPartie(){
        this.vue.setEtat(new EtatListeLobbies(this.vue));
    }

    @Override
    public void demarrerPartie() {
        this.vue.setEtat(new EtatEnJeu(this.vue));
    }
    
    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void finirPartie() {}

    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void retourAccueil() {}
    
    @Override
    public void deconnectionServeur() {
        this.vue.setEtat(new EtatInit(this.vue)); 
    }
}
