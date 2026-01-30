package Etat;

import View.VueClient;

public class EtatJeuFini implements Etat {

    private VueClient vue;

    public EtatJeuFini(VueClient vue){
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
    public void finirPartie() {}

    @Override
    public void retourAccueil() {
        this.vue.setEtat(new EtatConnecte(this.vue));
    }
    
}
