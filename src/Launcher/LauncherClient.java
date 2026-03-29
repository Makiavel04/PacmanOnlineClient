package Launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Controller.ControleurClient;

/** Laucher pour choisir son serveur avant de lancer le client */
public class LauncherClient {
    private JFrame frame;
    private JTextField adresseField;
    private JTextField portField;
    private ControleurClient controleur;

    public LauncherClient(){
        // Configuration de la fenêtre
        this.frame = new JFrame("Pacman Online - Launcher");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(600, 350);
        this.frame.setLocationRelativeTo(null); 
        this.frame.getContentPane().setBackground(Color.BLACK);
        this.frame.setLayout(new BorderLayout());
        this.frame.setResizable(false);


        JLabel titre = new JLabel();
        this.styliserGrosTitre(titre);
        this.frame.add(titre, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        this.adresseField = new JTextField("localhost");
        this.portField = new JTextField("50000");
        setupPanelPrincipal(mainPanel, portField, adresseField);
        this.frame.add(mainPanel, BorderLayout.CENTER);

        PanelDecoArcade decoPanel = new PanelDecoArcade();
        //decoPanel.setPreferredSize(new Dimension(450, 60));
        this.frame.add(decoPanel, BorderLayout.SOUTH);

        this.frame.setVisible(true);
    }

    

    private void lancerClient() {
        try {

            this.frame.setVisible(false);

            // On passe "this" (le launcher actuel) au contrôleur
            this.controleur = new ControleurClient(this, this.getAdresse(), this.getPort());
            this.controleur.ouvrirConnexion();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this.frame, "Le port doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getAdresse(){
        return this.adresseField.getText().trim();
    }

    public int getPort(){
        return Integer.parseInt(this.portField.getText().trim());
    }

    public void reafficherLauncher(){
        this.frame.setVisible(true);
        this.controleur = null;
    }

    /**
     * Applique le style Arcade au titre. 
     * @param lblTitre Le JLabel contenant le titre
     */
    private void styliserGrosTitre(JLabel lblTitre) {
        // Couleurs Retro
        Color arcadeBlue = Color.decode("#2121FF");

        // Texte en HTML pour gérer la taille et le centrage proprement
        lblTitre.setText("<html><body style='text-align:center; font-family:Monospaced; font-weight:bold; color:#FFEE00; font-size:28px;'>&nbsp;PACMAN ONLINE</html>");
        lblTitre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        // Bordure bleue épaisse en haut (Style Arcade)
        lblTitre.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(4, 0, 0, 0, arcadeBlue),
            javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
    }

    public void setupPanelPrincipal(JPanel mainPanel, JTextField fieldAdr, JTextField fieldPort) {
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5); // Espacement entre les éléments
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Ligne 1 : Adresse ---
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0; // Le label ne prend que la place nécessaire
        JLabel lblAdr = new JLabel("Adresse : ");
        lblAdr.setForeground(Color.WHITE);
        mainPanel.add(lblAdr, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; // Le champ de texte s'étire pour remplir la ligne
        mainPanel.add(fieldAdr, gbc);

        // --- Ligne 2 : Port ---
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblPort = new JLabel("Port API : ");
        lblPort.setForeground(Color.WHITE);
        mainPanel.add(lblPort, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(fieldPort, gbc);

        // --- Ligne 3 : Bouton Lancer ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // Le bouton prend les deux colonnes
        gbc.weightx = 0;
        gbc.insets = new Insets(25, 5, 10, 5); // Plus de marge au dessus du bouton

        JButton btnLancer = new JButton("Lancer le Client");
        btnLancer.setPreferredSize(new Dimension(0, 45)); // Hauteur fixe plus généreuse
        btnLancer.setBackground(Color.decode("#2121FF"));
        btnLancer.setForeground(Color.WHITE);
        btnLancer.setFocusPainted(false);
        btnLancer.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 14));
        
        btnLancer.addActionListener(e -> this.lancerClient());
        mainPanel.add(btnLancer, gbc);
    }
}
