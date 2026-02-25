package Etat;

import Vue.VueClient;
import Vue.Panel.PanelFinPartie;

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
    public void quitterPartie(){}

    @Override
    public void demarrerPartie() {
        this.vue.setEtat(new EtatEnJeu(this.vue));//Si relance alors que toujours sur score de fin de partie, on est forcé dans le jeu
    }

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {
        this.vue.setEtat(new EtatEnAttentePartie(this.vue));
    }
    
    @Override
    public void deconnectionServeur() {
        this.vue.setEtat(new EtatInit(this.vue)); 
    }
}
