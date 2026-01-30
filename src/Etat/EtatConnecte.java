package Etat;

import View.VueClient;

public class EtatConnecte implements Etat{

    private VueClient vue;

    public EtatConnecte(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage();
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
