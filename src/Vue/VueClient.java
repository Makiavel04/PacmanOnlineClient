package Vue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import Controller.ControleurClient;
import Etat.Etat;
import Etat.EtatInit;

public class VueClient {
    
    JFrame frame;
    JPanel panel;
    ControleurClient controleur;

    Etat etat;

    public VueClient(ControleurClient controleur){
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
        this.panel = panel;
        this.frame.add(this.panel);
        this.frame.revalidate();
        this.frame.repaint();
    }
}
