package Etat;

import Vue.PanelConnection;
import Vue.VueClient;

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

    @Override
    public void rejoindrePartie() {}

    @Override
    public void demarrerPartie() {}

    @Override
    public void majTour() {}

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {}
    
}
