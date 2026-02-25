package Vue.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Ressources.EtatGame.EtatPacmanGame;
import Vue.VueClient;



public class PanelEnJeu extends JPanel{
    VueClient vue;

    private JLabel labelLobby;
    private JLabel labelTour;
    private PanelPacmanGame zonePlateau; // Ou un composant custom pour le labyrinthe
    private JLabel labelScorePacmans;
    private JLabel labelScoreFantomes;
    private JLabel labelVies;

    public PanelEnJeu(VueClient v) {
        this.vue = v;
        this.setLayout(new BorderLayout(10, 10)); // Marges de 10px

        // --- HAUT : Informations Lobby et Tour ---
        JPanel panelHaut = new JPanel(new GridLayout(1, 2));
        this.labelLobby = new JLabel("Lobby : #" + vue.getIdLobby(), SwingConstants.LEFT);
        this.labelTour = new JLabel("Tour : 0", SwingConstants.RIGHT);
        
        panelHaut.add(labelLobby);
        panelHaut.add(labelTour);
        panelHaut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // --- CENTRE : Le Plateau ---
        if(this.vue.getEtatPacmanGame() != null) this.zonePlateau = new PanelPacmanGame(this.vue.getEtatPacmanGame().getPlateau());
        else this.zonePlateau = new PanelPacmanGame(null);
        this.zonePlateau.setBorder(BorderFactory.createTitledBorder("Labyrinthe"));


        // --- BAS : Stats (3 colonnes) ---
        JPanel panelBas = new JPanel(new GridLayout(1, 3));
        this.labelScorePacmans = new JLabel("Score Pacmans : 0", SwingConstants.CENTER);
        this.labelScoreFantomes = new JLabel("Score Fantômes : 0", SwingConstants.CENTER);
        this.labelVies = new JLabel("Vies : 0", SwingConstants.CENTER);

        panelBas.add(labelScorePacmans);
        panelBas.add(labelScoreFantomes);
        panelBas.add(labelVies);
        panelBas.setPreferredSize(new Dimension(0, 40));
        panelBas.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        // Ajout des sections au BorderLayout
        this.add(panelHaut, BorderLayout.NORTH);
        this.add(this.zonePlateau, BorderLayout.CENTER);
        this.add(panelBas, BorderLayout.SOUTH);

        this.mapperTouchesClavier();


        this.majTour();
    }

    public void mapperTouchesClavier(){
        int[] codeTouches = {
            java.awt.event.KeyEvent.VK_UP,
            java.awt.event.KeyEvent.VK_DOWN,
            java.awt.event.KeyEvent.VK_LEFT,
            java.awt.event.KeyEvent.VK_RIGHT
        };

        String[] nomTouches = {"Haut", "Bas", "Gauche", "Droite"};

        InputMap inputMap = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);//Touches captées uniquement si element ou sa fênetre on le focus
        ActionMap actionMap = this.getActionMap();

        for(int i=0; i<codeTouches.length; i++){
            int code = codeTouches[i];
            String nom = nomTouches[i];
            
            //Associer la touche à un nom d'action
            inputMap.put(javax.swing.KeyStroke.getKeyStroke(code, 0), nom);

            //Associer le nom d'action à l'appel de fonction
            actionMap.put(nom, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e){
                    vue.deplacement(code);
                }
            });
        }
    }

    /**
     * Met à jour l'affichage avec les données du dernier état reçu
     */
    public void majTour() {
        EtatPacmanGame etat = this.vue.getEtatPacmanGame();

        if (etat != null) {
            // Mise à jour des textes
            this.labelTour.setText("Tour : " + etat.getTour());
            this.labelScorePacmans.setText("Score Pacmans : " + etat.getScorePacmans());
            this.labelScoreFantomes.setText("Score Fantômes : " + etat.getScoreFantomes());
            this.labelVies.setText("Vies : " + etat.getViesPacmans());
            
            // Mise à jour du plateau
            this.zonePlateau.setMaze(etat.getPlateau());

            // Changement visuel si capsule active (optionnel)
            if (etat.isCapsuleActive()) {
                this.zonePlateau.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            } else {
                this.zonePlateau.setBorder(null);
            }
        }

        this.revalidate();
        this.repaint();
    }
}
