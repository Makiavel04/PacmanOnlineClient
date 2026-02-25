package Etat;

import Vue.VueClient;
import Vue.Panel.PanelEnJeu;

public class EtatEnJeu implements Etat {

    private VueClient vue;

    public EtatEnJeu(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelEnJeu(vue));
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

    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void quitterPartie(){}


    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void demarrerPartie() {}

    @Override
    public void finirPartie() {
        this.vue.setEtat(new EtatJeuFini(this.vue));
    }

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
