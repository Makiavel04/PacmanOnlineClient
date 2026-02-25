package Vue.Panel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Vue.VueClient;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel de connexion pour le client PacmanOnline.
 * Permet à l'utilisateur de saisir son nom et mot de passe pour se connecter au serveur.
 */
public class PanelConnection extends JPanel{
    
    VueClient vue;
    JTextField nom;
    JPasswordField motDePasse;
    private JLabel labelErreur;


    public PanelConnection(VueClient v){
        this.vue = v;
        
        // Configuration du layout principal
        this.setLayout(new GridBagLayout());
        this.setBorder(new EmptyBorder(20, 20, 20, 20)); // Marges autour du panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espacement entre les composants
        gbc.fill = GridBagConstraints.HORIZONTAL;

        this.labelErreur = new JLabel(" "); // Un espace vide pour garder la place
        this.labelErreur.setForeground(Color.RED);
        this.labelErreur.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0; // Ligne 0
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 10, 0);
        this.add(labelErreur, gbc);


        // Initialisation des champs
        this.nom = new JTextField(15);
        this.motDePasse = new JPasswordField(15);
        JButton boutonConnection = new JButton("Se connecter");
        gbc.gridwidth = 1; // Réinitialiser pour les composants suivants

        // --- Ligne 1 : Nom ---
        gbc.gridx = 0; gbc.gridy = 1;
        this.add(new JLabel("Nom :"), gbc);
        
        gbc.gridx = 1;
        this.add(this.nom, gbc);

        // --- Ligne 2 : Mot de passe ---
        gbc.gridx = 0; gbc.gridy = 2;
        this.add(new JLabel("Mot de passe :"), gbc);
        
        gbc.gridx = 1;
        this.add(this.motDePasse, gbc);

        // --- Ligne 3 : Bouton (Centré et réduit) ---
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2; // Prend les deux colonnes
        gbc.fill = GridBagConstraints.NONE; // Ne s'étire pas horizontalement
        gbc.anchor = GridBagConstraints.CENTER; // Centre le bouton
        
        boutonConnection.setMargin(new Insets(5, 20, 5, 20));
        this.add(boutonConnection, gbc);

        // Action
        boutonConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Conversion du tableau de char en String pour le mot de passe
                String password = new String(motDePasse.getPassword());
                vue.demanderAuthentification(nom.getText(), password);
            }
        });

    }
    
    public void afficherMessageErreur(String message) {
        this.labelErreur.setText(message);
    }

}
