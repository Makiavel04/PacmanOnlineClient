package Etat;

public interface Etat {//l'état ne gère que la transition entre les écrans et pas les MàJ d'affichage, le controleur s'occupe de dire à la de le faire.
    public void seConnecter();//Se connecter au client (Authentification)
    public void rejoindrePartie();//Entrer dans le salon (Rejoindre le salon trouvé ou crée)
    public void quitterPartie();//Quitter le salon (Retour à l'écran de choix de salon)
    public void demarrerPartie();//Démarrer une partie (Afficher le jeu)
    public void finirPartie();//Fin de partie (Jeu fini => ecran de fin)
    public void retourAccueil();//Retourner à l'acceuil (Retour au choix creer ou chercher)
    public void deconnectionServeur();//Gérer la déconnexion du serveur (Afficher un message d'erreur et revenir à l'écran de connexion)
}
