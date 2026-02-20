package Etat;

import Vue.PanelFinPartie;
import Vue.VueClient;

public class EtatJeuFini implements Etat {

    private VueClient vue;

    public EtatJeuFini(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelFinPartie(vue));
    }

    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie() {}

    @Override
    public void demarrerPartie() {}

    @Override
    public void majTour() {}

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {
        this.vue.setEtat(new EtatListeLobbies(this.vue));
    }
}
