package Vue.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Ressources.EtatLobby.ResumeLobby;
import Vue.VueClient;

public class PanelListeLobbies extends JPanel{
    
    VueClient vue;
    JPanel panelLobbies;
    JScrollPane scrollPane;
    

    public PanelListeLobbies(VueClient v){
        this.vue = v;
        this.setLayout(new BorderLayout());

        //Bouton haut pour actualiser la liste des lobbies et en créer un nouveau
        JPanel panelHaut = new JPanel();
        panelHaut.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton boutonActualiser = new JButton("Actualiser");
        boutonActualiser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.demanderListeLobbies();
            }
        });
        panelHaut.add(boutonActualiser);

        JButton boutonCreer = new JButton("Créer une partie");
        boutonCreer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.demanderPartie(-1); // -1 pour indiquer qu'on veut créer une partie
            }
        });
        panelHaut.add(boutonCreer);
        
        this.add(panelHaut, BorderLayout.NORTH);


        //Panel bas pour afficher la liste des lobbies
        this.panelLobbies = new JPanel();
        BoxLayout layoutPanelLobbies = new BoxLayout(panelLobbies, BoxLayout.Y_AXIS);
        panelLobbies.setLayout(layoutPanelLobbies);
        
        JScrollPane scrollPane = new JScrollPane(panelLobbies);
        this.add(scrollPane, BorderLayout.CENTER);

        this.actualiserLobbies();
    }

    public void actualiserLobbies(){
        ArrayList<ResumeLobby> infosLobbies = this.vue.getInfosLobbies();

        this.panelLobbies.removeAll();//Vide le panel des lobbies

        //Met à jour le panel des lobbies avec les nouvelles infos
        for(ResumeLobby infos : infosLobbies) {
            JPanel panelLobby = new JPanel();
            panelLobby.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Ligne du bas
                BorderFactory.createEmptyBorder(10, 10, 10, 10)               // Marges internes
            ));//Bordure du bas pour separer les lobies dans la liste

            panelLobby.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelLobby.getPreferredSize().height));//Taille max du panel
            panelLobby.setLayout(new GridLayout(1,4));
            
            JLabel labelId = new JLabel("Lobby " + infos.getIdLobby());
            JLabel labelNbJoueurs = new JLabel("Joueurs connectés: " + infos.getNbJoueur());
            JLabel labelNbJoueursMax = new JLabel("Max: " + infos.getNbMaxJoueur() + " joueurs");
        
            JButton boutonRejoindre = new JButton("Rejoindre");
            boutonRejoindre.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    vue.demanderPartie(infos.getIdLobby());   
                }
            });
            
            panelLobby.add(labelId);
            panelLobby.add(labelNbJoueurs);
            panelLobby.add(labelNbJoueursMax);
            panelLobby.add(boutonRejoindre);
            
            this.panelLobbies.add(panelLobby);
        }
    
        this.panelLobbies.add(Box.createVerticalGlue());

        // Refresh le panel pour afficher les changements
        this.panelLobbies.revalidate();
        this.panelLobbies.repaint();
    }
}
