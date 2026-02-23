package Vue.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import Ressources.EtatGame.EtatPacmanGame;
import Vue.VueClient;



public class PanelEnJeu extends JPanel{
    VueClient vue;

    private JLabel labelLobby;
    private JLabel labelTour;
    private JTextArea zonePlateau; // Ou un composant custom pour le labyrinthe
    private JLabel labelScorePacmans;
    private JLabel labelScoreFantomes;
    private JLabel labelVies;

    public PanelEnJeu(VueClient v) {
        this.vue = v;
        this.setLayout(new BorderLayout(10, 10)); // Marges de 10px

        // --- HAUT : Informations Lobby et Tour ---
        JPanel panelHaut = new JPanel(new GridLayout(1, 2));
        labelLobby = new JLabel("Lobby : #" + vue.getIdLobby(), SwingConstants.LEFT);
        labelTour = new JLabel("Tour : 0", SwingConstants.RIGHT);
        
        // Style optionnel
        panelHaut.add(labelLobby);
        panelHaut.add(labelTour);
        panelHaut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // --- CENTRE : Le Plateau ---
        // Utilisation d'une JTextArea avec police Monospaced pour que les caractères s'alignent
        zonePlateau = new JTextArea();
        zonePlateau.setEditable(false);
        zonePlateau.setFont(new Font("Monospaced", Font.PLAIN, 12));
        zonePlateau.setBorder(BorderFactory.createTitledBorder("Labyrinthe"));

        JPanel panelCentre = new JPanel(new BorderLayout());
        panelCentre.add(new JScrollPane(zonePlateau), BorderLayout.CENTER);

        // --- BAS : Statistiques (3 colonnes) ---
        JPanel panelBas = new JPanel(new GridLayout(1, 3));
        labelScorePacmans = new JLabel("Score Pacmans : 0", SwingConstants.CENTER);
        labelScoreFantomes = new JLabel("Score Fantômes : 0", SwingConstants.CENTER);
        labelVies = new JLabel("Vies : 0", SwingConstants.CENTER);

        panelBas.add(labelScorePacmans);
        panelBas.add(labelScoreFantomes);
        panelBas.add(labelVies);
        panelBas.setPreferredSize(new Dimension(0, 40));
        panelBas.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        // Ajout des sections au BorderLayout
        this.add(panelHaut, BorderLayout.NORTH);
        this.add(panelCentre, BorderLayout.CENTER);
        this.add(panelBas, BorderLayout.SOUTH);
    }

    /**
     * Met à jour l'affichage avec les données du dernier état reçu
     */
    public void majTour() {
        EtatPacmanGame etat = this.vue.getEtatPacmanGame();

        if (etat != null) {
            // Mise à jour des textes
            labelTour.setText("Tour : " + etat.getTour());
            labelScorePacmans.setText("Score Pacmans : " + etat.getScorePacmans());
            labelScoreFantomes.setText("Score Fantômes : " + etat.getScoreFantomes());
            labelVies.setText("Vies : " + etat.getViesPacmans());
            
            // Mise à jour du plateau
            zonePlateau.setText(etat.getPlateau());

            // Changement visuel si capsule active (optionnel)
            if (etat.isCapsuleActive()) {
                zonePlateau.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            } else {
                zonePlateau.setBorder(null);
            }
        }

        this.revalidate();
        this.repaint();
    }
}
