package Vue.Panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Ressources.EtatLobby.ScoreFinPartie;
import Vue.VueClient;

public class PanelFinPartie extends JPanel{
    VueClient vue;

    public PanelFinPartie(VueClient v){
        this.vue = v;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(50, 20, 50, 20));
        this.setBackground(Color.WHITE);

        ScoreFinPartie score = v.getScoreFinPartie();

        // --- Titre ---
        JLabel labelVainqueur = new JLabel(score.getVainqueur() + " a gagné !");
        labelVainqueur.setFont(new Font("Arial", Font.BOLD, 26));
        labelVainqueur.setAlignmentX(CENTER_ALIGNMENT);

        // --- Scores ---
        JLabel sPacman = new JLabel("Score Pacman : " + score.getScorePacman());
        JLabel sFantomes = new JLabel("Score Fantômes : " + score.getScoreFantome());
        sPacman.setAlignmentX(CENTER_ALIGNMENT);
        sFantomes.setAlignmentX(CENTER_ALIGNMENT);

        // --- Bouton ---
        JButton btn = new JButton("Retour au lobby");
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.addActionListener(e -> vue.getEtat().retourAccueil());

        // --- Assemblage avec ressorts (Glues) ---
        this.add(Box.createVerticalGlue()); // Ressort haut
        this.add(labelVainqueur);
        this.add(Box.createVerticalStrut(20)); // Espace fixe
        this.add(sPacman);
        this.add(sFantomes);
        this.add(Box.createVerticalStrut(40)); // Espace fixe
        this.add(btn);
        this.add(Box.createVerticalGlue()); // Ressort bas
    }
    
    
}
