package Etat;

import Vue.PanelListeLobbies;
import Vue.VueClient;

public class EtatListeLobbies implements Etat{

    private VueClient vue;

    public EtatListeLobbies(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelListeLobbies(vue));
        this.vue.demanderListeLobbies();
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
