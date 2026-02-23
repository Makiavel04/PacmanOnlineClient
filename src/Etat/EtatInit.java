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

    @Override
    public void rejoindrePartie() {}

    @Override
    public void demarrerPartie() {}

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {}
    
}
