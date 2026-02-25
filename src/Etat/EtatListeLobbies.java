package Etat;

import Vue.VueClient;
import Vue.Panel.PanelListeLobbies;

public class EtatListeLobbies implements Etat{

    private VueClient vue;

    public EtatListeLobbies(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelListeLobbies(vue));
        this.vue.demanderListeLobbies();
    }

    /**
     * Ne fait rien
     * {@inheritDoc}
     */
    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie() {
        this.vue.setEtat(new EtatEnAttentePartie(this.vue));
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
