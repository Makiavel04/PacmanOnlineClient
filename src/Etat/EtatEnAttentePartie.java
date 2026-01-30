package Etat;

import View.VueClient;

public class EtatEnAttentePartie implements Etat{

    private VueClient vue;

    public EtatEnAttentePartie(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage();
    }

    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie() {}


    @Override
    public void demarrerPartie() {
        this.vue.setEtat(new EtatEnJeu(this.vue));
    }

    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {}
    
}
