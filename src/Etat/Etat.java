package Etat;
/**
 * Interface représentant l'état d'une partie à un moment donné.
 * Chaque état doit implémenter les méthodes de transition vers les autres états.
 */
public interface Etat {//l'état ne gère que la transition entre les écrans et pas les MàJ d'affichage, le controleur s'occupe de dire à la de le faire.
    /**Se connecter au client (Authentification) */
    public void seConnecter();
    /**Entrer dans le salon (Rejoindre le salon trouvé ou crée) */
    public void rejoindrePartie();
    /**Quitter le salon (Retour à l'écran de choix de salon) */
    public void quitterPartie();
    /**Démarrer une partie (Afficher le jeu) */
    public void demarrerPartie();
    /**Fin de partie (Jeu fini => ecran de fin) */
    public void finirPartie();
    /**Retourner à l'acceuil (Retour au choix creer ou chercher) */
    public void retourAccueil();
    /**Gérer la déconnexion du serveur (Afficher un message d'erreur et revenir à l'écran de connexion) */
    public void deconnectionServeur();
}
