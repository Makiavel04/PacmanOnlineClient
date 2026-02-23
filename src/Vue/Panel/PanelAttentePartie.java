package Vue.Panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Ressources.TypeAgent;
import Ressources.EtatLobby.DetailsJoueur;
import Ressources.EtatLobby.DetailsLobby;
import Vue.VueClient;

public class PanelAttentePartie extends JPanel{
    private VueClient vue;
    private JLabel labelTitre;
    private JLabel labelNbPacman;
    private JLabel labelNbFantome;
    private JButton boutonChangerCamp;
    private JPanel conteneurPacmans;
    private JPanel conteneurFantomes;


    private boolean isHost;

    private JPanel panelBots;
    private JButton boutonAjouterPacmanBot;
    private JButton boutonRetirerPacmanBot;
    private JButton boutonAjouterFantomeBot;
    private JButton boutonRetirerFantomeBot;

    private JPanel panelStart;
    private JButton boutonStart;
    

    public PanelAttentePartie(VueClient v) {
        this.vue = v;
        this.isHost = false;

        // BorderLayout est idéal pour structurer Centre (Liste) / Bas (Bouton)
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(new EmptyBorder(20, 50, 20, 50)); // Marges globales pour centrer le tout

        this.initComposantsVides();

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(this.compteurs(), BorderLayout.NORTH);
        panelCentral.add(this.panelListeJoueurs(), BorderLayout.CENTER);
        panelCentral.add(this.composantBots(), BorderLayout.SOUTH);

        //Ajout des composants au panel principal
        this.add(this.titre(), BorderLayout.NORTH);
        this.add(panelCentral, BorderLayout.CENTER);
        this.add(this.composantStart(), BorderLayout.SOUTH);

        this.majAttente();
    }

    private void initComposantsVides() {
        this.labelTitre = null;
        
        this.labelNbPacman = null;
        this.labelNbFantome = null;

        this.boutonChangerCamp = null;
        this.conteneurPacmans = null;
        this.conteneurFantomes = null;

        this.panelBots = null;
        this.boutonAjouterPacmanBot = null;
        this.boutonRetirerPacmanBot = null;
        this.boutonAjouterFantomeBot = null;
        this.boutonRetirerFantomeBot = null;
        
        this.panelStart = null;
        this.boutonStart = null;
    }

    private JLabel titre(){
        if(this.labelTitre == null) {
            this.labelTitre = new JLabel("Lobby #--", JLabel.CENTER);
            this.labelTitre.setFont(new Font("Arial", Font.BOLD, 28));
            this.labelTitre.setBorder(new EmptyBorder(0, 0, 20, 0));
        }
        return this.labelTitre;
    }

    private JPanel compteurs(){
        if(this.labelNbPacman == null) this.labelNbPacman = new JLabel("0 / 0 Pacmans");
        if(this.labelNbFantome == null) this.labelNbFantome = new JLabel("0 / 0 Fantômes");

        JPanel panelCompteurs = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
        panelCompteurs.add(this.labelNbPacman);
        panelCompteurs.add(this.labelNbFantome);
        return panelCompteurs;
    }

    private JScrollPane scrollPaneListePacmans(){
        if(this.conteneurPacmans == null) {
            this.conteneurPacmans = new JPanel();
            this.conteneurPacmans.setLayout(new BoxLayout(conteneurPacmans, BoxLayout.Y_AXIS));
        }

        JScrollPane scrollPane = new JScrollPane(this.conteneurPacmans);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pacmans"));
        scrollPane.setPreferredSize(new Dimension(100, scrollPane.getPreferredSize().height));
        return scrollPane;
    }

    private JScrollPane scrollPaneListeFantomes(){
        if(this.conteneurFantomes == null) {
            this.conteneurFantomes = new JPanel();
            this.conteneurFantomes.setLayout(new BoxLayout(conteneurFantomes, BoxLayout.Y_AXIS));
        }
        
        JScrollPane scrollPane = new JScrollPane(this.conteneurFantomes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Fantômes"));
        scrollPane.setPreferredSize(new Dimension(100, scrollPane.getPreferredSize().height));
        return scrollPane;
    }

    private JButton boutonChangementCamp(){
        if(this.boutonChangerCamp == null) {
            this.boutonChangerCamp = new JButton("\u21C4"); // Flèche de changement
            this.boutonChangerCamp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    vue.changerCamp();
                }
            });
        }
        return this.boutonChangerCamp;
    }

    private JPanel panelListeJoueurs(){
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // --- Colonne des Pacmans ---
            gbc.gridx = 0;
            gbc.weightx = 0.5;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH; // Remplissage
            panel.add(this.scrollPaneListePacmans(), gbc);

            // --- Bouton switch ---
            gbc.gridx = 1;
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.fill = GridBagConstraints.NONE;
            panel.add(this.boutonChangementCamp(), gbc);

            // --- Colonne des Fantômes ---
            gbc.gridx = 2;
            gbc.weightx = 0.5;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH; // On repasse en mode remplissage
            panel.add(this.scrollPaneListeFantomes(), gbc);

        return panel;
    }

    private JPanel composantBots(){
        if(this.panelBots == null) {
            this.panelBots = new JPanel(new GridBagLayout());
            this.panelBots.setBorder(BorderFactory.createTitledBorder("Configuration des Bots"));
            
            this.ajouterColonneGestionBots("Pacman Bot", TypeAgent.PACMAN, this.panelBots);
            this.ajouterColonneGestionBots("Fantôme Bot", TypeAgent.FANTOME, this.panelBots);
        }
        return this.panelBots;
    }

    private void ajouterColonneGestionBots(String label, TypeAgent typeAgent, JPanel panel){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(5, 10, 5, 10); // Marges entre les composants

        // +
        gbc.gridy = 0;
        JButton boutonAjouter = new JButton("+");
        boutonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                vue.ajouterBot(typeAgent.name());  
            }
        });
        panel.add(boutonAjouter, gbc);

        // Label
        gbc.gridy = 1;
        JLabel labelType = new JLabel(label, SwingConstants.CENTER);
        panel.add(labelType, gbc);

        // -
        gbc.gridy = 2;
        JButton boutonRetirer = new JButton("-");
        boutonRetirer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                vue.retirerBot(typeAgent.name());
            }
        });
        panel.add(boutonRetirer, gbc);

        //Stock les boutons au bon endroit
        if (typeAgent == TypeAgent.PACMAN) {
            if(this.boutonAjouterPacmanBot == null) this.boutonAjouterPacmanBot = boutonAjouter;
            if(this.boutonRetirerPacmanBot == null) this.boutonRetirerPacmanBot = boutonRetirer;
        } else {
            if(this.boutonAjouterFantomeBot == null) this.boutonAjouterFantomeBot = boutonAjouter;
            if(this.boutonRetirerFantomeBot == null) this.boutonRetirerFantomeBot = boutonRetirer;
        }
    }
    
    private JPanel composantStart(){
        if(this.boutonStart == null) {
            this.boutonStart = new JButton("Démarrer la partie");
            this.boutonStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    vue.demanderLancementPartie();
                }
            });
            this.boutonStart.setFont(new Font("Arial", Font.BOLD, 16));
            this.boutonStart.setPreferredSize(new Dimension(200, 50)); // Bouton un peu plus grand
        }

        // Panel intermédiaire pour que le bouton ne prenne pas toute la largeur
        if(this.panelStart == null) this.panelStart = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.panelStart.add(this.boutonStart);

        return this.panelStart;
    }

    public void majAttente() {
        DetailsLobby details = this.vue.getDetailsLobby();
        if (details != null) {
            this.isHost = details.getIdHost() == this.vue.getIdClient();

            TypeAgent monCamp = null;

            this.labelTitre.setText("Lobby #" + details.getIdLobby());

            this.labelNbPacman.setText(details.getNbPacman() + " / " + details.getNbMaxPacman() + " Pacmans");
            this.labelNbFantome.setText(details.getNbFantome() + " / " + details.getNbMaxFantome() + " Fantômes");

            // Nettoyage de la liste actuelle
            conteneurPacmans.removeAll();
            conteneurFantomes.removeAll();

            for (DetailsJoueur detailsClient : details.getJoueurs()) {
                String username = detailsClient.getUsername();

                // Ajout des mentions Hôte

                if (detailsClient.getIdClient() == this.vue.getIdClient()) {
                    username += " (Vous)";
                    monCamp = detailsClient.getTypeAgent();
                }
                if (detailsClient.getIdClient() == this.vue.getDetailsLobby().getIdHost()) {
                    username += " (Hôte)";
                }

                JLabel labelJoueur = new JLabel(username);
                labelJoueur.setAlignmentX(Component.CENTER_ALIGNMENT);
                labelJoueur.setFont(new Font("Arial", Font.PLAIN, 18)); // Pseudo plus gros
                labelJoueur.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Ligne du bas
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)               // Marges internes
                ));

                // Mise en bleu si c'est moi 
                if (detailsClient.getIdClient() == this.vue.getIdClient()) {
                    labelJoueur.setForeground(Color.BLUE); 
                }

                if (detailsClient.getTypeAgent()  == TypeAgent.PACMAN) conteneurPacmans.add(labelJoueur);
                else if(detailsClient.getTypeAgent() == TypeAgent.FANTOME) conteneurFantomes.add(labelJoueur);
            }

            if(monCamp != null){
                if(details.getNbFantome() < details.getNbMaxFantome() && monCamp == TypeAgent.PACMAN){
                    this.boutonChangerCamp.setEnabled(true);
                } else if(details.getNbPacman() < details.getNbMaxPacman() && monCamp == TypeAgent.FANTOME){
                    this.boutonChangerCamp.setEnabled(true);
                } else {
                    this.boutonChangerCamp.setEnabled(false);
                }
            }

            // Mise à jour de l'état du bouton si on est l'hôte et que les conditions sont remplies
            this.afficherPourGrade(isHost, details);


            this.revalidate();
            this.repaint();
        }
    }



    public static final String ASK_CHANGEMENT_TYPE_AGENT = "demanderChangementTypeAgent";    public void afficherPourGrade(boolean isHost, DetailsLobby details){
        this.panelBots.setVisible(isHost);
        this.panelStart.setVisible(isHost);
        
        if(isHost){
            this.boutonAjouterFantomeBot.setEnabled(details.getNbFantome() < details.getNbMaxFantome());
            this.boutonRetirerFantomeBot.setEnabled(details.getNbFantome() > 0);
            this.boutonAjouterPacmanBot.setEnabled(details.getNbPacman() < details.getNbMaxPacman());
            this.boutonRetirerPacmanBot.setEnabled(details.getNbPacman() > 0);

            this.boutonStart.setEnabled(details.getNbJoueur() >= details.getNbMaxJoueur() && details.getNbFantome()>=details.getNbMaxFantome() && details.getNbPacman()>=details.getNbMaxPacman());
        }
    }
}
