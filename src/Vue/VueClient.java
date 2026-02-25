package Vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.ControleurClient;
import Etat.Etat;
import Etat.EtatInit;
import Ressources.TypeAgent;
import Ressources.EtatGame.EtatPacmanGame;
import Ressources.EtatLobby.DetailsLobby;
import Ressources.EtatLobby.ResumeLobby;
import Ressources.EtatLobby.ScoreFinPartie;
import Vue.Panel.PanelAttentePartie;
import Vue.Panel.PanelEnJeu;
import Vue.Panel.PanelListeLobbies;

/** Classe pour l'affichage du client */
public class VueClient {
    
    JFrame frame;
    /** Panel qui va changer selon l'état du client */
    JPanel panel;
    ControleurClient controleur;
    /**Label pour afficher les erreures */
    JLabel labelErreur;

    /**État du client */
    Etat etat;
    

    public VueClient(ControleurClient controleur){
        this.controleur = controleur;
        this.frame = new JFrame("Client Pacman");
        this.frame.setSize(800,600);
        //this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Fermer l'application quand on clique sur la croix
        this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Informe le controller que la fenêtre ferme
                controleur.fermerConnexion();  // ferme proprement le socket, threads, etc.

                frame.dispose();// remplace le EXIT_ON_CLOSE 
                
            }
        });

        BorderLayout layout = new BorderLayout();
        this.frame.setLayout(layout);

        // Création du label d'erreur fixe
        this.labelErreur = new JLabel(" "); // Espace pour garder la hauteur
        this.labelErreur.setForeground(Color.RED);
        this.labelErreur.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.frame.add(labelErreur, java.awt.BorderLayout.NORTH);

        this.etat = new EtatInit(this);

        this.frame.setVisible(true);
    }

    //Getters et setters
    public void setEtat(Etat etat){
        this.etat = etat;
    }
    public Etat getEtat(){
        return this.etat;
    }

    public int getIdClient(){return this.controleur.getIdClient();}
    public int getIdLobby(){return this.controleur.getIdLobby();}
    public ArrayList<ResumeLobby> getInfosLobbies(){return this.controleur.getListeLobbies();}
    public DetailsLobby getDetailsLobby(){return this.controleur.getDetailsLobby();}
    public EtatPacmanGame getEtatPacmanGame(){return this.controleur.getEtatPacmanGame();}
    public ControleurClient getControleur(){return this.controleur;}
    public List<String> getStrategiesDisponibles(TypeAgent typeAgent){return this.controleur.getStrategiesDisponible(typeAgent);}
    public List<String> getListeMaps(){return this.controleur.getListeMaps();}
    public ScoreFinPartie getScoreFinPartie(){return this.controleur.getScoreFinPartie();}

    //Affichage
    /**
     * Change le panel affiché dans la fenêtre principale.
     * @param panel nouveau panel à afficher
     */
    public void changerAffichage(JPanel panel){
        if(this.panel != null){
            this.frame.remove(this.panel);
        }
        // On remet le message à vide à chaque changement d'écran
        this.effacerMessageErreur();
        
        this.panel = panel;
        this.frame.add(this.panel, java.awt.BorderLayout.CENTER);
        this.frame.revalidate();
        this.frame.repaint();
    }
    
    //Actions et échanges avec le controller
    /**
     * Demande au controller d'authentifier l'utilisateur avec les informations fournies.
     * @param nom nom d'utilisateur
     * @param motDePasse mot de passe de l'utilisateur
     */
    public void demanderAuthentification(String nom, String motDePasse){
        if(nom == null || nom.isEmpty() ){
            this.afficherMessageErreur("Veuillez entrer un nom d'utilisateur et un mot de passe.");
            return;
        }else{
            this.effacerMessageErreur();
            this.controleur.demanderAuthentification(nom, motDePasse);
        }
    }

    /**
     * Traite la réponse du controller concernant l'authentification de l'utilisateur.
     * @param succes indique si l'authentification a réussi ou échoué
     */
    public void traiterAuthentification(boolean succes){
        if(succes){
            this.etat.seConnecter();
            this.afficherCompte();
        } else {
            // Afficher un message d'erreur ou rester sur le même écran
            System.out.println("Authentification échouée. Veuillez réessayer.");
            this.afficherMessageErreur("Authentification échouée. Veuillez réessayer.");
        }
    }

    /**
     * Demande au controller la liste des lobbies disponibles sur le serveur. Le controller traitera la réponse et mettra à jour l'affichage en conséquence.
     */
    public void demanderListeLobbies(){
        this.controleur.demanderListeLobbies();
    }

    /**
     * Mets à jour l'affichage de liste des lobbies
     */
    public void traiterListeLobbies(){
        java.awt.EventQueue.invokeLater(() -> {
            if(this.panel instanceof PanelListeLobbies){
                ((PanelListeLobbies)this.panel).actualiserLobbies();//Passer par action d'état pour eviter instanceof
            }
        });
    }

    /**
     * Demande au controleur de rejoindre une partie
     * @param idLobby id de la partie à rejoindre
     */
    public void demanderPartie(int idLobby){
        this.controleur.demanderPartie(idLobby);
    }

    /**
     * Traite la réponse du controller concernant la demande de rejoindre une partie.
     * @param succes indique si la demande de rejoindre la partie a réussi ou échoué
     */
    public void rejoindrePartie(boolean succes){
        // Traiter les détails de la partie et mettre à jour l'affichage
        if(succes){
            this.etat.rejoindrePartie();
        } else {
            System.out.println("Impossible de rejoindre la partie.");
            this.afficherMessageErreur("Impossible de rejoindre la partie.");
        }
    }

    /**
     * Traite la mise à jour des détails du lobby. 
     */
    public void traiterMajLobby(){
        // Mettre à jour les détails du lobby et rafraîchir l'affichage si nécessaire
        java.awt.EventQueue.invokeLater(() -> {
            if(this.panel instanceof PanelAttentePartie){
                ((PanelAttentePartie)this.panel).majAttente();
            }
        });
    }

    /** Demande au controleur de quitter la partie en cours */
    public void quitterPartie(){
        this.controleur.quitterLobby();
        this.etat.quitterPartie();
    }

    /** Demande au controleur de lancer la partie */
    public void demanderLancementPartie(){
        this.controleur.demanderLancementPartie();
    }

    /** Lance la partie */
    public void demarrerPartie(){
        this.etat.demarrerPartie();
    }
    
    /** Envoie au controleur le déplacement demandé par l'utilisateur */
    public void deplacement(int direction){
        this.controleur.envoyerDeplacement(direction);
    }

    /** Met à jour l'affichage du tour en cours */
    public void majTour(){
        java.awt.EventQueue.invokeLater(() -> {
            if(this.panel instanceof PanelEnJeu){
                ((PanelEnJeu)this.panel).majTour();
            }
        });
    }

    /** Fini la partie en cours */
    public void finirPartie(){
        this.etat.finirPartie();
    }

    /**
     * Demande au controller d'ajouter un bot de type spécifié dans la partie en cours.
     * @param type type du bot à ajouter
     */
    public void ajouterBot(String type){
        this.controleur.demanderAjoutBot(type);
    }

    /**
     * Demande au controller de retirer un bot de type spécifié dans la partie en cours.
     * @param type type du bot à retirer
     */
    public void retirerBot(String type){
        this.controleur.demanderRetraitBot(type);
    }

    /**
     * Demande au controller de changer la stratégie d'un bot spécifique dans la partie en cours.
     * @param numBot numéro du bot dont la stratégie doit être changée
     * @param nouvelleStrat nouvelle stratégie à appliquer au bot 
     */
    public void changerStrategieBot(int numBot, String nouvelleStrat){
        this.controleur.demanderChangementStrategieBot(numBot, nouvelleStrat);
    }

    /**
     * Demande au controller de changer de camp
     */
    public void changerCamp(){
        this.controleur.demanderChangementCamp();
    }

    /**Demande au controleur de changer la map */
    public void changerMap(String nouvelleMap){
        this.controleur.demanderChangementMap(nouvelleMap);
    }

    /** Gère le cas ou le serveur se déconnecte */
    public void deconnectionServeur(){
        this.etat.deconnectionServeur();
        this.afficherMessageErreur("Déconnecté du serveur.");
    }

    // --- Message d'erreur ---
    public void afficherMessageErreur(String message){
        this.labelErreur.setText(message);
    }
    public void effacerMessageErreur(){
        this.labelErreur.setText("");
    }

    // --- Afficher le compte connecté en bas de l'écran ---
    /** Affiche l'username en bas de l'écran */
    public void afficherCompte(){
        String username = this.controleur.getUsername();
        JLabel labelCompte = new JLabel("Connecté en tant que : " + username);
        labelCompte.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labelCompte.setHorizontalAlignment(SwingConstants.CENTER);


        this.frame.add(labelCompte, java.awt.BorderLayout.SOUTH);
        this.frame.revalidate();
        this.frame.repaint();
    }
}
