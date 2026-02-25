package Etat;

import Vue.VueClient;
import Vue.Panel.PanelAttentePartie;

public class EtatEnAttentePartie implements Etat{

    private VueClient vue;

    public EtatEnAttentePartie(VueClient vue){
        this.vue = vue;
        this.vue.changerAffichage(new PanelAttentePartie(vue));
    }

    @Override
    public void seConnecter() {}

    @Override
    public void rejoindrePartie() {}

    @Override
    public void quitterPartie(){
        this.vue.setEtat(new EtatListeLobbies(this.vue));
    }

    @Override
    public void demarrerPartie() {
        this.vue.setEtat(new EtatEnJeu(this.vue));
    }
    
    @Override
    public void finirPartie() {}

    @Override
    public void retourAccueil() {}
    
    @Override
    public void deconnectionServeur() {
        this.vue.setEtat(new EtatInit(this.vue)); 
    }
}
