package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Reseau.ExpediteurClient;
import Reseau.RecepteurClient;
import Ressources.RequetesJSON;
import Ressources.TypeAgent;
import Ressources.EtatGame.EtatPacmanGame;
import Ressources.EtatLobby.DetailsLobby;
import Ressources.EtatLobby.ResumeLobby;
import Ressources.EtatLobby.ScoreFinPartie;
import Vue.VueClient;

public class ControleurClient {
    
    VueClient vue;
    RecepteurClient recepteur;
    ExpediteurClient expediteur;

    Socket socket;
    String adresseServeur;
    int portServeur;

    String username = null;
    int idClient = -1;
    EtatPacmanGame etatPacmanGame = null;
    ArrayList<ResumeLobby> listeLobbies = new ArrayList<>();
    DetailsLobby detailsLobby = null;
    List<String> stratsPacman = new ArrayList<>();
    List<String> stratsFantome = new ArrayList<>();
    List<String> listeMaps = new ArrayList<>();
    ScoreFinPartie scoreFinPartie = null;

    public ControleurClient(String adr, int port) {
        this.vue = new VueClient(this);
        this.recepteur = null;
        this.expediteur = null;

        this.adresseServeur = adr;
        this.portServeur = port;   
    }

    public String getUsername(){return this.username;}
    public void setUsername(String username){this.username = username;}

    public int getIdClient() {return this.idClient;}
    public void setIdClient(int idClient) {this.idClient = idClient;}

    public int getIdLobby() {return this.detailsLobby.getIdLobby();}

    public ArrayList<ResumeLobby> getListeLobbies() {return this.listeLobbies;}
    public void setListeLobbies(ArrayList<ResumeLobby> listeLobbies) {this.listeLobbies = listeLobbies;}

    public DetailsLobby getDetailsLobby() {return this.detailsLobby;}
    public void setDetailsLobby(DetailsLobby detailsLobby) {this.detailsLobby = detailsLobby;}

    public EtatPacmanGame getEtatPacmanGame() {return this.etatPacmanGame;}
    public void setEtatPacmanGame(EtatPacmanGame etatPacmanGame) {this.etatPacmanGame = etatPacmanGame;}

    public List<String> getStrategiesDisponible(TypeAgent typeAgent){
        if(typeAgent == TypeAgent.PACMAN) return this.stratsPacman;
        else if(typeAgent == TypeAgent.FANTOME) return this.stratsFantome;
        else return new ArrayList<>();
    }

    public List<String> getListeMaps() {return this.listeMaps;}

    public ScoreFinPartie getScoreFinPartie() {return this.scoreFinPartie;}

    public boolean estConnecte() {
        return this.socket != null && this.socket.isConnected() && !this.socket.isClosed();
    }

    public void ouvrirConnexion(){
        try{
            this.socket = new Socket(this.adresseServeur, this.portServeur);
            this.socket.setTcpNoDelay(true);
            this.recepteur = new RecepteurClient(this);
            this.expediteur = new ExpediteurClient(this);
            PrintWriter sortie = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entree = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.recepteur.setSocket(socket);
            this.recepteur.setEntreeReseau(entree);
            this.recepteur.start();

            this.expediteur.setSocket(socket);
            this.expediteur.setSortieReseau(sortie);

        } catch (Exception e){
            System.out.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }

    public void fermerConnexion() {
        try{
        // Logique pour fermer proprement la connexion, les sockets, threads, etc.
            this.recepteur.interrupt();
            this.socket.close();
            System.out.println("Fermeture de la connexion client.");

        } catch (Exception e){
            System.out.println("Erreur lors de la deconnexion : " + e.getMessage());
        }
        this.vue.deconnectionServeur();
    }

    public void gestionReception(String action, JSONObject objReponse){
        switch(action){
            case "":
                // Traiter l'action spécifique
                break;
            //Reponse d'authentification
            case RequetesJSON.RES_AUTHENTIFICATION:
                this.retourAuthentification(objReponse);
                break;
            //Reponse de liste de lobbies
            case RequetesJSON.RES_LISTE_LOBBIES:
                this.traiterListeLobbies(objReponse);
                break;
            //Reponse de demande de partie
            case RequetesJSON.RES_DEMANDE_PARTIE:
                this.rejoindrePartie(objReponse);
                break;
            case RequetesJSON.MAJ_LOBBY:
                this.traiterMajLobby(objReponse);
                break;
            //Reponse de debut de partie
            case RequetesJSON.DEBUT_PARTIE:
                debuterPartie(objReponse);
                break;
            //Reponse de mise a jour de partie
            case RequetesJSON.MAJ_PARTIE:
                this.recevoirMiseAJour(objReponse);
                break;
            //Reponse de fin de partie
            case RequetesJSON.FIN_PARTIE:
                this.finirPartie(objReponse);
                break;
            case RequetesJSON.RES_CHANGEMENT_MAP:
                this.traiterAutorisationChangerMap(objReponse);
                break;
            default:
                System.out.println("Action inconnue reçue : " + action);
        }
    }

    public void demanderAuthentification(String nomJoueur, String mdp){
        if(!this.estConnecte()){
            System.out.println("Connexion au serveur...");
            this.ouvrirConnexion();
        }
        if(this.estConnecte()){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_AUTHENTIFICATION);
            objRequete.put(RequetesJSON.Attributs.Authentification.USERNAME, nomJoueur);
            objRequete.put(RequetesJSON.Attributs.Authentification.PASSWORD, mdp);
            this.expediteur.envoyerRequete(objRequete.toString());
            //this.retourAuthentification(objRequete);
        }else{
            this.vue.afficherMessageErreur("Impossible de se connecter au serveur.");
        }
    }

    public void retourAuthentification(JSONObject objReponse){
        boolean authResult = objReponse.getBoolean(RequetesJSON.Attributs.Authentification.RESULTAT);
        if(authResult){
            this.setUsername(objReponse.getString(RequetesJSON.Attributs.Authentification.USERNAME));
            this.setIdClient(objReponse.getInt(RequetesJSON.Attributs.Authentification.ID_CLIENT));
        }
        this.vue.traiterAuthentification(authResult);
        System.out.println("Résultat de l'authentification : " + authResult);
    }

    public void demanderListeLobbies(){
        JSONObject objRequete = new JSONObject();
        objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_LISTE_LOBBIES);
        this.expediteur.envoyerRequete(objRequete.toString());
        System.out.println("Demande la liste des lobbies.");
    } 

    public void traiterListeLobbies(JSONObject objReponse){
        // Traiter la liste de lobbies reçue du serveur et mettre à jour la vue
        System.out.println("Traitement de la liste des lobbies reçus.");
        JSONArray lobbiesJSON = objReponse.getJSONArray(RequetesJSON.Attributs.Lobby.LISTE_LOBBIES);
        this.listeLobbies.clear();
        for(int i = 0; i < lobbiesJSON.length(); i++) {
            JSONObject lobbyObj = lobbiesJSON.getJSONObject(i);
            this.listeLobbies.add(ResumeLobby.fromJSON(lobbyObj));
        }
        this.vue.traiterListeLobbies();
    }

    public void demanderPartie(int idLobby){
        JSONObject objRequete = new JSONObject();
        objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_DEMANDE_PARTIE);
        objRequete.put(RequetesJSON.Attributs.Lobby.ID_LOBBY, idLobby);
        this.expediteur.envoyerRequete(objRequete.toString());
        System.out.println("Demande à rejoindre le lobby#" + idLobby);
    }

    public void rejoindrePartie(JSONObject detailsPartie){
        DetailsLobby details =  DetailsLobby.fromJSON(detailsPartie);
        int idLobby = details.getIdLobby();

        //Récupérer les stratégies disponibles pour les bots
        this.stratsPacman.clear();
        this.stratsFantome.clear();
        this.listeMaps.clear();

        detailsPartie.getJSONArray(RequetesJSON.Attributs.Lobby.STRATS_PACMAN).forEach(item -> this.stratsPacman.add(item.toString()));
        detailsPartie.getJSONArray(RequetesJSON.Attributs.Lobby.STRATS_FANTOME).forEach(item -> this.stratsFantome.add(item.toString()));
        detailsPartie.getJSONArray(RequetesJSON.Attributs.Lobby.LISTE_MAPS_DISPONIBLES).forEach(item -> this.listeMaps.add(item.toString()));

        System.out.println("Lobby#" + idLobby + " rejoint.");

        if(idLobby == -1){
            System.out.println("Problème pour rejoindre une partie.");
            this.vue.rejoindrePartie(false);
        } else {
            System.out.println("Rejoint la partie : " + idLobby);
            this.setDetailsLobby(details);
            this.vue.rejoindrePartie(true);
            
        }
    }

    public void quitterLobby(){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.QUITTER_LOBBY);
            objRequete.put(RequetesJSON.Attributs.Lobby.ID_LOBBY, this.getIdLobby());
            this.expediteur.envoyerRequete(objRequete.toString());
            System.out.println("Demande de quitter le lobby#" + this.getIdLobby());
            this.detailsLobby = null;
            this.etatPacmanGame = null;
            this.stratsPacman.clear();
            this.stratsFantome.clear();
            this.listeMaps.clear();
        }
    }

    public void traiterMajLobby(JSONObject detailsPartie) {
        System.out.println("Mise à jour des détails du lobby reçue.");
        DetailsLobby details =  DetailsLobby.fromJSON(detailsPartie);
        int idLobby = details.getIdLobby();
        if(idLobby == -1 || details.getIdLobby()!=this.getIdLobby()){
            System.out.println("Problème pour mettre à jour les détails du lobby.");
        } else {
            System.out.println("Mise à jour des détails du lobby : " + idLobby);
            this.setDetailsLobby(details);
        }
        this.vue.traiterMajLobby();
    }

    public void demanderLancementPartie(){
        if(this.detailsLobby !=null && this.detailsLobby.getIdHost() == this.getIdClient() && this.detailsLobby.getNbJoueur() >= this.detailsLobby.getNbMaxJoueur()){
            System.out.println("Demande de lancement de la partie : Lobby#" + this.getIdLobby());
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_LANCEMENT_PARTIE);
            objRequete.put(RequetesJSON.Attributs.Lobby.ID_LOBBY, this.getIdLobby());
            this.expediteur.envoyerRequete(objRequete.toString());
        } else {
            if( this.detailsLobby == null) System.out.println("Aucun lobby trouvé pour le lancement de la partie.");
            else if(this.detailsLobby.getIdHost() != this.getIdClient()) System.out.println("Seul l'hôte peut lancer la partie.");
            else if(this.detailsLobby.getNbJoueur() < this.detailsLobby.getNbMaxJoueur()) System.out.println("La partie ne peut pas être lancée. Nombre de joueurs insuffisant.");
            else System.out.println("Conditions de lancement de la partie non remplies.");
        }
    }

    public void debuterPartie(JSONObject configPartie){
        this.setEtatPacmanGame(EtatPacmanGame.fromJSON(configPartie));
        System.out.println("Partie débutée : Lobby#" + this.getIdLobby());
        this.scoreFinPartie = null;
        this.vue.demarrerPartie();
    }

    public void envoyerDeplacement(int keyCode){
        JSONObject objRequete = new JSONObject();
        objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.SEND_DEPLACEMENT);    
        objRequete.put(RequetesJSON.Attributs.Partie.SENS_MOUVEMENT, keyCode);
        System.out.println("Envoi du déplacement : keyCode = " + keyCode);
        this.expediteur.envoyerRequete(objRequete.toString());
    }
    
    public void recevoirMiseAJour(JSONObject miseAJourPartie){
        this.setEtatPacmanGame(EtatPacmanGame.fromJSON(miseAJourPartie));
        System.out.println("Mise à jour de la partie reçue : Lobby#" + this.getIdLobby() +" - Tour " + this.getEtatPacmanGame().getTour());
        this.vue.majTour();
    }

    public void finirPartie(JSONObject resultatPartie){
        this.scoreFinPartie = ScoreFinPartie.fromJSON(resultatPartie);

        System.out.println("Fin de la partie reçue : Lobby#" + this.getIdLobby());
        this.vue.finirPartie();

        this.etatPacmanGame = null;
    }

    public void demanderAjoutBot(String type){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_AJOUT_BOT);
            objRequete.put(RequetesJSON.Attributs.Joueur.TYPE_AGENT, type);
            System.out.println("Demande d'ajout d'un bot : type = " + type);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

    public void demanderRetraitBot(String type){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_RETRAIT_BOT);
            objRequete.put(RequetesJSON.Attributs.Joueur.TYPE_AGENT, type);
            System.out.println("Demande de retrait d'un bot : type = " + type);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

    public void demanderChangementStrategieBot(int numBot, String nouvelleStrat){
        if(this.detailsLobby != null){//Si on a un lobby
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_CHANGER_STRATEGIE_BOT);
            objRequete.put(RequetesJSON.Attributs.Lobby.NUM_BOT, numBot);
            objRequete.put(RequetesJSON.Attributs.Lobby.TYPE_STRATEGIE, nouvelleStrat);
            System.out.println("Demande de changement de stratégie pour le bot#" + numBot + " : nouvelle strat = " + nouvelleStrat);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }
    
    public void demanderChangementCamp(){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_CHANGEMENT_CAMP);
            System.out.println("Demande de changement de camp.");
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

    public void demanderChangementMap(String nouvelleMap){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_CHANGEMENT_MAP);
            objRequete.put(RequetesJSON.Attributs.Lobby.MAP, nouvelleMap);
            System.out.println("Demande de changement de map : nouvelle map = " + nouvelleMap);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

    public void traiterAutorisationChangerMap(JSONObject objReponse){
        boolean autorise = objReponse.getBoolean(RequetesJSON.Attributs.Lobby.AUTORISE_CHANGEMENT);
        int nbPacmanMax = objReponse.getInt(RequetesJSON.Attributs.Lobby.NB_MAX_PACMAN);
        int nbFantomeMax = objReponse.getInt(RequetesJSON.Attributs.Lobby.NB_MAX_FANTOME);
        if(!autorise){
            System.out.println("Changement de map refusé.");
            this.vue.afficherMessageErreur("Map non séléctionnable, prévue pour " + nbPacmanMax + " pacman(s) et " + nbFantomeMax + "fantôme(s).");
            this.vue.traiterMajLobby();
        } else {//Ne sera normalement pas utilisé car doit recevoir une mise a jour du lobby
            System.out.println("Changement de map autorisé.");
            this.vue.effacerMessageErreur();
        }
    }


}
