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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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

    private JButton selecteurMap;
    

    public PanelAttentePartie(VueClient v) {
        this.vue = v;
        this.isHost = false;

        // BorderLayout est idéal pour structurer Centre (Liste) / Bas (Bouton)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(20, 50, 20, 50)); // Marges globales pour centrer le tout

        this.initComposantsVides();
        
        JButton boutonRetour = new JButton("Quitter");
        boutonRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                vue.quitterPartie();
            }
        });
        JPanel panelRetour = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRetour.add(boutonRetour);
        this.add(panelRetour);


        JPanel panelTitre = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitre.add(this.titre());
        this.add(panelTitre);

        
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(this.compteurs(), BorderLayout.NORTH);
        panelCentral.add(this.panelListeJoueurs(), BorderLayout.CENTER);
        panelCentral.add(this.composantBots(), BorderLayout.SOUTH);
        this.add(panelCentral);


        JPanel panelMap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel labelMap = new JLabel("Map : ");
        labelMap.setFont(new Font("Arial", Font.BOLD, 16));
        panelMap.add(labelMap);
        panelMap.add(this.selecteurMap());
        this.add(panelMap);

        this.add(this.composantStart());

        this.majAttente();
    }

    /** Initialise tous les composants à vides */
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

        this.selecteurMap = null;
        
        this.panelStart = null;
        this.boutonStart = null;
    }

    /**
     * Initialise le titre
     * @return le jlabel du titre, initialisé s'il ne l'était pas déjà
     */
    private JLabel titre(){
        if(this.labelTitre == null) {
            this.labelTitre = new JLabel("Lobby #--", JLabel.CENTER);
            this.labelTitre.setFont(new Font("Arial", Font.BOLD, 28));
            this.labelTitre.setBorder(new EmptyBorder(0, 0, 20, 0));
        }
        return this.labelTitre;
    }

    /**
     * Initialise les compteurs de joueurs
     * @return le panel contenant les compteurs, initialisé s'il ne l'était pas déjà
     */
    private JPanel compteurs(){
        if(this.labelNbPacman == null) this.labelNbPacman = new JLabel("0 / 0 Pacmans");
        if(this.labelNbFantome == null) this.labelNbFantome = new JLabel("0 / 0 Fantômes");

        JPanel panelCompteurs = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 5));
        panelCompteurs.add(this.labelNbPacman);
        panelCompteurs.add(this.labelNbFantome);
        return panelCompteurs;
    }

    /**
     * Initialise les listes pacmans
     * @return le panel contenant la liste, initialisé si elle ne l'était pas déjà
     */
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

    /**
     * Initialise les listes de fantomes
     * @return le panel contenant la liste , initialisé si elle ne l'était pas déjà
     */
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

    /**
     * Initialise le bouton de changement de camp1
     * @return le bouton de changement de camp, initialisé s'il ne l'était pas déjà
     */
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

    /**
     * Initialise les listes de joueurs et les boutons de changement de camp
     * @return le panel contenant les listes et les boutons, initialisé s'il ne l'était pas déjà
     */
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

    /**
     * Initialise les composants de gestion des bots (ajout/suppression)
     * @return le panel contenant les composants de gestion des bots, initialisé s'il ne l'était pas déjà
     */
    private JPanel composantBots(){
        if(this.panelBots == null) {
            this.panelBots = new JPanel(new GridBagLayout());
            this.panelBots.setBorder(BorderFactory.createTitledBorder("Configuration des Bots"));
            
            this.ajouterColonneGestionBots("Pacman Bot", TypeAgent.PACMAN, this.panelBots);
            this.ajouterColonneGestionBots("Fantôme Bot", TypeAgent.FANTOME, this.panelBots);
        }
        return this.panelBots;
    }

    /**
     * Ajoute une colonne de gestion des bots (ajout/suppression) pour un type d'agent donné
     * @param label
     * @param typeAgent
     * @param panel
     */
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
    
    /**
     * Initialise le bouton de démarrage de la partie
     * @return le panel contenant le bouton de démarrage, initialisé s'il ne l'était pas déjà
     */
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

    /**
     * Initialise le sélecteur de map
     * @return
     */
    private JButton selecteurMap(){
        if(this.selecteurMap == null){
            String mapActuelle  = this.vue.getDetailsLobby().getMap();
            List<String> mapsDisponibles = this.vue.getListeMaps();

            this.selecteurMap = new JButton(mapActuelle);
            JPopupMenu menu = new JPopupMenu();

            for (String s : mapsDisponibles) {
                JMenuItem item = new JMenuItem(s);
                item.addActionListener(e -> {
                    this.selecteurMap.setText(s); // On met à jour le texte du bouton
                    vue.changerMap(s);
                });
                menu.add(item);
            }

            this.selecteurMap.addActionListener(e -> {
                menu.show(this.selecteurMap, 0, this.selecteurMap.getHeight());
            });
        }

        return this.selecteurMap;
    }

    /**
     * Met à jour l'affichage du panel d'attente en fonction des nouvelles informations du lobby
     */
    public void majAttente() {
        DetailsLobby detailsLobby = this.vue.getDetailsLobby();
        if (detailsLobby != null) {
            this.isHost = detailsLobby.getIdHost() == this.vue.getIdClient();

            TypeAgent monCamp = null;

            this.labelTitre.setText("Lobby #" + detailsLobby.getIdLobby());

            this.labelNbPacman.setText(detailsLobby.getNbPacman() + " / " + detailsLobby.getNbMaxPacman() + " Pacmans");
            this.labelNbFantome.setText(detailsLobby.getNbFantome() + " / " + detailsLobby.getNbMaxFantome() + " Fantômes");

            // Nettoyage de la liste actuelle
            conteneurPacmans.removeAll();
            conteneurFantomes.removeAll();

            for (DetailsJoueur detailsJoueurs : detailsLobby.getJoueurs()) {
                //Récuperation du camp du client
                if (!detailsJoueurs.isBot() &&detailsJoueurs.getIdClient() == this.vue.getIdClient()) {
                    monCamp = detailsJoueurs.getTypeAgent();
                }

                Component joueur;
                if(!detailsJoueurs.isBot()) joueur = this.creerLigneJoueur(detailsJoueurs);
                else joueur = this.creerLigneBot(detailsJoueurs);
                

                if (detailsJoueurs.getTypeAgent()  == TypeAgent.PACMAN) conteneurPacmans.add(joueur);
                else if(detailsJoueurs.getTypeAgent() == TypeAgent.FANTOME) conteneurFantomes.add(joueur);
            }

            if(monCamp != null){
                if(detailsLobby.getNbFantome() < detailsLobby.getNbMaxFantome() && monCamp == TypeAgent.PACMAN){
                    this.boutonChangerCamp.setEnabled(true);
                } else if(detailsLobby.getNbPacman() < detailsLobby.getNbMaxPacman() && monCamp == TypeAgent.FANTOME){
                    this.boutonChangerCamp.setEnabled(true);
                } else {
                    this.boutonChangerCamp.setEnabled(false);
                }
            }

            // Mise à jour de l'état du bouton si on est l'hôte et que les conditions sont remplies
            this.selecteurMap.setText(detailsLobby.getMap());
            this.afficherPourGrade(isHost, detailsLobby);


            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Crée une ligne d'affichage pour un joueur humain
     * @param dj
     * @return
     */
    private Component creerLigneJoueur(DetailsJoueur dj) {
        String username = dj.getUsername();
        if (dj.getIdClient() == this.vue.getIdClient()) {
            username += " (Vous)";
        }
        if (dj.getIdClient() == this.vue.getDetailsLobby().getIdHost()) {
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
        if (dj.getIdClient() == this.vue.getIdClient()) {
            labelJoueur.setForeground(Color.BLUE); 
        }

        return labelJoueur;
    }

    /**
     * Crée une ligne d'affichage pour un bot, avec un sélecteur de stratégie si on est l'hôte
     * @param idBot id du bot représenté par la ligne
     * @param typeAgent type du bot (Pacman ou Fantome)
     * @param stratActuelle stratégie actuelle du bot, à afficher sur le bouton
     * @return
     */
    private JButton creerSelecteurStrat(int idBot, TypeAgent typeAgent, String stratActuelle) {
        List<String> stratsDispos = this.vue.getStrategiesDisponibles(typeAgent);

        //JComboBox inutilisable quand on la redimensionne
        // JComboBox<String> selecteur = new JComboBox<String>(stratsDispos.toArray(new String[0]));

        // selecteur.setSelectedItem(stratActuelle);
        // selecteur.setEnabled(this.isHost);

        // if(this.isHost){
        //     selecteur.addActionListener(e -> {
        //         String selection = (String) selecteur.getSelectedItem();
        //         if(selection!=null && !selection.isEmpty())vue.changerStrategieBot(idBot, selection); 
        //     });
        // }
        // return selecteur;
        
        JButton bouton = new JButton(stratActuelle);
        JPopupMenu menu = new JPopupMenu();


        for (String s : stratsDispos) {
            JMenuItem item = new JMenuItem(s);
            item.addActionListener(e -> {
                bouton.setText(s); // On met à jour le texte du bouton
                vue.changerStrategieBot(idBot, s);
            });
            menu.add(item);
        }

        bouton.addActionListener(e -> {
            menu.show(bouton, 0, bouton.getHeight());
        });


        return bouton;
    }

    /**
     * Crée une ligne d'affichage pour un bot, avec un sélecteur de stratégie si on est l'hôte
     * @param dj les détails du bot à afficher
     * @return la ligne d'affichage du bot, avec un sélecteur de stratégie si on est l'hôte
     */
    private Component creerLigneBot(DetailsJoueur dj){
        JLabel labelBot = new JLabel(dj.getUsername());
        labelBot.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelBot.setFont(new Font("Arial", Font.ITALIC, 16)); // Italique pour différencier des joueurs humains
        

        JButton selecteurStrat = this.creerSelecteurStrat(dj.getIdClient(), dj.getTypeAgent(), dj.getStrategie());
        selecteurStrat.setEnabled(this.isHost); // Seul l'hôte peut changer la stratégie des bots

        JPanel panelLigne = new JPanel();
        panelLigne.setLayout(new BoxLayout(panelLigne, BoxLayout.X_AXIS));
        panelLigne.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Ligne du bas
            BorderFactory.createEmptyBorder(0, 0, 0, 0)               // Marges internes
        ));
        panelLigne.add(labelBot);
        panelLigne.add(Box.createHorizontalGlue());
        panelLigne.add(selecteurStrat);


        return panelLigne;
    }

    /**
     * Affiche ou masque les composants de gestion des bots et de démarrage de la partie en fonction du grade du joueur
     * @param isHost indique si le joueur est l'hôte ou non
     * @param details les détails du lobby
     */
    public void afficherPourGrade(boolean isHost, DetailsLobby details){
        this.panelBots.setVisible(isHost);
        this.panelStart.setVisible(isHost);

        this.selecteurMap.setEnabled(isHost);
        
        if(isHost){
            this.boutonAjouterFantomeBot.setEnabled(details.getNbFantome() < details.getNbMaxFantome());
            this.boutonRetirerFantomeBot.setEnabled(details.getNbFantome() > 0);
            this.boutonAjouterPacmanBot.setEnabled(details.getNbPacman() < details.getNbMaxPacman());
            this.boutonRetirerPacmanBot.setEnabled(details.getNbPacman() > 0);

            this.boutonStart.setEnabled(details.getNbJoueur() >= details.getNbMaxJoueur() && details.getNbFantome()>=details.getNbMaxFantome() && details.getNbPacman()>=details.getNbMaxPacman());
        }
    }

}
