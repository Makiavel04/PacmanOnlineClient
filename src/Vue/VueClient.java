package Vue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Controller.ControleurClient;
import Etat.Etat;
import Etat.EtatInit;

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

    public void setEtat(Etat etat){
        this.etat = etat;
    }
    public Etat getEtat(){
        return this.etat;
    }

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

    public void demanderAuthentification(String nom, String motDePasse){
        this.controleur.demanderAuthentification(nom, motDePasse);
    }

    public void traiterAuthentification(boolean succes){
        if(succes){
            this.etat.seConnecter();
        } else {
            // Afficher un message d'erreur ou rester sur le même écran
            System.out.println("Authentification échouée. Veuillez réessayer.");
            this.afficherMessageErreur("Authentification échouée. Veuillez réessayer.");
        }
    }

    public void demanderPartie(){
        this.controleur.demanderPartie();
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

    public void afficherMessageErreur(String message){
        this.labelErreur.setText(message);
    }
    public void effacerMessageErreur(){
        this.labelErreur.setText("");
    }
}
