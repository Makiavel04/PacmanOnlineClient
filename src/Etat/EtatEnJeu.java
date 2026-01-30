package Etat;

import View.VueClient;

public class EtatEnJeu implements Etat {

    private VueClient vue;

    public EtatEnJeu(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage();
    }

    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie() {}


    @Override
    public void demarrerPartie() {}

    @Override
    public void finirPartie() {
        this.vue.setEtat(new EtatJeuFini(this.vue));
    }

    @Override
    public void retourAccueil() {}
    
}
