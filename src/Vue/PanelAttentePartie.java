package Vue;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import Ressources.DetailsClient;
import Ressources.DetailsLobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class PanelAttentePartie extends JPanel{
    private VueClient vue;
    private JPanel conteneurPseudos;
    private JScrollPane scrollPane;
    private JButton boutonStart;
    private JLabel labelTitre;

    public PanelAttentePartie(VueClient v) {
        this.vue = v;
        // BorderLayout est idéal pour structurer Centre (Liste) / Bas (Bouton)
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(new EmptyBorder(20, 50, 20, 50)); // Marges globales pour centrer le tout

        // Titre du lobby (Haut)
        this.labelTitre = new JLabel("Lobby #--", JLabel.CENTER);
        this.labelTitre.setFont(new Font("Arial", Font.BOLD, 28));
        this.labelTitre.setBorder(new EmptyBorder(0, 0, 20, 0));
        this.add(labelTitre, BorderLayout.NORTH);


        // Liste des joueurs (Centre)
        this.conteneurPseudos = new JPanel();
        this.conteneurPseudos.setLayout(new BoxLayout(conteneurPseudos, BoxLayout.Y_AXIS));
        
        this.scrollPane = new JScrollPane(conteneurPseudos);
        this.scrollPane.setBorder(BorderFactory.createTitledBorder("Joueurs présents"));
    
        // Zone du bouton (Bas)


        this.boutonStart = new JButton("Démarrer la partie");
        this.boutonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                vue.demanderLancementPartie();
            }
        });
        this.boutonStart.setFont(new Font("Arial", Font.BOLD, 16));
        this.boutonStart.setPreferredSize(new Dimension(200, 50)); // Bouton un peu plus grand
        
        // Panel intermédiaire pour que le bouton ne prenne pas toute la largeur
        JPanel panelBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBouton.add(boutonStart);


        //Ajout des composants au panel principal
        this.add(labelTitre, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(panelBouton, BorderLayout.SOUTH);

        this.majAttente();
    }

    public void majAttente() {
        DetailsLobby details = this.vue.getDetailsLobby();
        if (details != null) {
            labelTitre.setText("Lobby #" + details.getIdLobby());

            // Nettoyage de la liste actuelle
            conteneurPseudos.removeAll();

            for (DetailsClient detailsClient : details.getJoueurs()) {
                String username = detailsClient.getUsername();

                // Ajout des mentions Hôte

                if (detailsClient.getIdClient() == this.vue.getIdClient()) {
                    username += " (Vous)";
                }
                if (detailsClient.getIdClient() == this.vue.getDetailsLobby().getIdHost()) {
                    username += " (Hôte)";
                }

                JLabel labelJoueur = new JLabel(username);
                labelJoueur.setAlignmentX(Component.CENTER_ALIGNMENT);
                labelJoueur.setFont(new Font("Arial", Font.PLAIN, 18)); // Pseudo plus gros
                labelJoueur.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Ligne du bas
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)               // Marges internes
                ));

                // Mise en bleu si c'est moi 
                if (detailsClient.getIdClient() == this.vue.getIdClient()) {
                    labelJoueur.setForeground(Color.BLUE); 
                }

                conteneurPseudos.add(labelJoueur);
            }

            // Mise à jour de l'état du bouton
            this.boutonStart.setEnabled(details.getIdHost() == this.vue.getIdClient() && details.getNbJoueur() >= details.getNbMaxJoueur());

            this.revalidate();
            this.repaint();
        }
    }
}
