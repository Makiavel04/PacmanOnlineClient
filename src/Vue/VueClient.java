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
import Vue.Panel.PanelAttentePartie;
import Vue.Panel.PanelEnJeu;
import Vue.Panel.PanelListeLobbies;

public class VueClient {
    
    JFrame frame;
    JPanel panel;
    ControleurClient controleur;
    JLabel labelErreur;

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

    //Affichage
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
    public void demanderAuthentification(String nom, String motDePasse){
        if(nom == null || nom.isEmpty() ){
            this.afficherMessageErreur("Veuillez entrer un nom d'utilisateur et un mot de passe.");
            return;
        }else{
            this.effacerMessageErreur();
            this.controleur.demanderAuthentification(nom, motDePasse);
        }
    }

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

    public void demanderListeLobbies(){
        this.controleur.demanderListeLobbies();
    }

    public void traiterListeLobbies(){
        java.awt.EventQueue.invokeLater(() -> {
            if(this.panel instanceof PanelListeLobbies){
                ((PanelListeLobbies)this.panel).actualiserLobbies();//Passer par action d'état pour eviter instanceof
            }
        });
    }

    public void demanderPartie(int idLobby){
        this.controleur.demanderPartie(idLobby);
    }

    public void rejoindrePartie(boolean succes){
        // Traiter les détails de la partie et mettre à jour l'affichage
        if(succes){
            this.etat.rejoindrePartie();
        } else {
            System.out.println("Impossible de rejoindre la partie.");
            this.afficherMessageErreur("Impossible de rejoindre la partie.");
        }
    }

    public void traiterMajLobby(){
        // Mettre à jour les détails du lobby et rafraîchir l'affichage si nécessaire
        java.awt.EventQueue.invokeLater(() -> {
            if(this.panel instanceof PanelAttentePartie){
                ((PanelAttentePartie)this.panel).majAttente();
            }
        });
    }

    public void demanderLancementPartie(){
        this.controleur.demanderLancementPartie();
    }

    public void demarrerPartie(){
        this.etat.demarrerPartie();
    }
    
    public void deplacement(int direction){
        this.controleur.envoyerDeplacement(direction);
    }

    public void majTour(){
        java.awt.EventQueue.invokeLater(() -> {
            if(this.panel instanceof PanelEnJeu){
                ((PanelEnJeu)this.panel).majTour();
            }
        });
    }

    public void finirPartie(){
        this.etat.finirPartie();
    }

    public void ajouterBot(String type){
        this.controleur.demanderAjoutBot(type);
    }

    public void retirerBot(String type){
        this.controleur.demanderRetraitBot(type);
    }

    public void changerStrategieBot(int numBot, String nouvelleStrat){
        this.controleur.demanderChangementStrategieBot(numBot, nouvelleStrat);
    }

    public void changerCamp(){
        this.controleur.demanderChangementCamp();
    }

    public void changerMap(String nouvelleMap){
        this.controleur.demanderChangementMap(nouvelleMap);
    }

    // --- Message d'erreur ---
    public void afficherMessageErreur(String message){
        this.labelErreur.setText(message);
    }
    public void effacerMessageErreur(){
        this.labelErreur.setText("");
    }

    // --- Afficher le compte connecté en bas de l'écran ---
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
