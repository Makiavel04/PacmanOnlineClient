package Etat;

import Vue.VueClient;
import Vue.Panel.PanelConnection;

public class EtatInit implements Etat{

    private VueClient vue;

    public EtatInit(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelConnection(vue));
    }

    @Override
    public void seConnecter() {
        this.vue.setEtat(new EtatListeLobbies(this.vue));
    }

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
    public void rejoindrePartie() {}

    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void demarrerPartie() {}

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
