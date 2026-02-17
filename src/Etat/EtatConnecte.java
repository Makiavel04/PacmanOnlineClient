package Etat;

import Vue.PanelRejoindrePartie;
import Vue.VueClient;

public class EtatConnecte implements Etat{

    private VueClient vue;

    public EtatConnecte(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelRejoindrePartie(vue));
    }

    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie(int idMatch) {
        this.vue.setIdMatch(idMatch);
        this.vue.setEtat(new EtatEnAttentePartie(this.vue));
    }


    @Override
    public void demarrerPartie() {}

    @Override
    public void majTour() {}

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {}
}
