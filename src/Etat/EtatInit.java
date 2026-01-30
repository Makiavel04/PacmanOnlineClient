package Etat;

import View.VueClient;

public class EtatInit implements Etat{

    private VueClient vue;

    public EtatInit(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage();
    }

    @Override
    public void seConnecter() {
        this.vue.setEtat(new EtatConnecte(this.vue));
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
