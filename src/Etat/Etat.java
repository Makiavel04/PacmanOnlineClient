package Etat;

public interface Etat {
    public void seConnecter();//Se connecter au client (Authentification)
    public void rejoindrePartie(int idMatch);//Entrer dans le salon (Rejoindre le salon trouvé ou crée)
    public void demarrerPartie();//Démarrer une partie (Afficher le jeu)
    public void majTour();//Mise à jour du tour (Afficher les changements dans le jeu)
    public void finirPartie();//Fin de partie (Jeu fini => ecran de fin)
    public void retourAccueil();//Retourner à l'acceuil (Retour au choix creer ou chercher)
}
