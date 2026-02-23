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

    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie() {
        this.vue.setEtat(new EtatEnAttentePartie(this.vue));
    }

    @Override
    public void demarrerPartie() {}

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {}
}
