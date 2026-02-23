package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Reseau.ExpediteurClient;
import Reseau.RecepteurClient;
import Ressources.RequetesJSON;
import Ressources.EtatGame.EtatPacmanGame;
import Ressources.EtatLobby.DetailsLobby;
import Ressources.EtatLobby.ResumeLobby;
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
    int idLobby = -1;
    EtatPacmanGame etatPacmanGame = null;
    ArrayList<ResumeLobby> listeLobbies = new ArrayList<>();
    DetailsLobby detailsLobby = null;

    public ControleurClient(String adr, int port) {
        this.vue = new VueClient(this);
        this.recepteur = new RecepteurClient(this);
        this.expediteur = new ExpediteurClient(this);

        this.adresseServeur = adr;
        this.portServeur = port;   
    }

    public String getUsername(){return this.username;}
    public void setUsername(String username){this.username = username;}

    public int getIdClient() {return this.idClient;}
    public void setIdClient(int idClient) {this.idClient = idClient;}

    public int getIdLobby() {return this.idLobby;}
    public void setIdLobby(int idLobby) {this.idLobby = idLobby;}

    public ArrayList<ResumeLobby> getListeLobbies() {return this.listeLobbies;}
    public void setListeLobbies(ArrayList<ResumeLobby> listeLobbies) {this.listeLobbies = listeLobbies;}

    public DetailsLobby getDetailsLobby() {return this.detailsLobby;}
    public void setDetailsLobby(DetailsLobby detailsLobby) {this.detailsLobby = detailsLobby;}

    public EtatPacmanGame getEtatPacmanGame() {return this.etatPacmanGame;}
    public void setEtatPacmanGame(EtatPacmanGame etatPacmanGame) {this.etatPacmanGame = etatPacmanGame;}


    public void ouvrirConnexion(){
        try{
            this.socket = new Socket(this.adresseServeur, this.portServeur);
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
            default:
                System.out.println("Action inconnue reçue : " + action);
        }
    }

    public void demanderAuthentification(String nomJoueur, String mdp){
        JSONObject objRequete = new JSONObject();
        objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_AUTHENTIFICATION);
        objRequete.put(RequetesJSON.Attributs.Authentification.USERNAME, nomJoueur);
        objRequete.put(RequetesJSON.Attributs.Authentification.PASSWORD, mdp);
        this.expediteur.envoyerRequete(objRequete.toString());
        //this.retourAuthentification(objRequete);
    }

    public void retourAuthentification(JSONObject objReponse){
        boolean authResult = objReponse.getBoolean(RequetesJSON.Attributs.Authentification.RESULTAT);
        if(authResult){
            this.setUsername(objReponse.getString(RequetesJSON.Attributs.Authentification.USERNAME));
            this.setIdClient(objReponse.getInt(RequetesJSON.Attributs.Authentification.ID_CLIENT));
        }
        this.vue.traiterAuthentification(authResult);
    }

    public void demanderListeLobbies(){
        JSONObject objRequete = new JSONObject();
        objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_LISTE_LOBBIES);
        this.expediteur.envoyerRequete(objRequete.toString());
    } 

    public void traiterListeLobbies(JSONObject objReponse){
        // Traiter la liste de lobbies reçue du serveur et mettre à jour la vue
        System.out.println("Traitement de la liste des lobbies reçue.");
        JSONArray lobbiesJSON = objReponse.getJSONArray(RequetesJSON.Attributs.Lobby.LISTE_LOBBIES);
        ArrayList<ResumeLobby> infosLobbies = new ArrayList<>();
        for(int i = 0; i < lobbiesJSON.length(); i++) {
            JSONObject lobbyObj = lobbiesJSON.getJSONObject(i);
            infosLobbies.add(ResumeLobby.fromJSON(lobbyObj));
        }
        this.setListeLobbies(infosLobbies);
        this.vue.traiterListeLobbies();
    }

    public void demanderPartie(int idLobby){
        JSONObject objRequete = new JSONObject();
        objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_DEMANDE_PARTIE);
        objRequete.put(RequetesJSON.Attributs.Lobby.ID_LOBBY, idLobby);
        this.expediteur.envoyerRequete(objRequete.toString());
    }

    public void rejoindrePartie(JSONObject detailsPartie){
        DetailsLobby details =  DetailsLobby.fromJSON(detailsPartie);
        int idLobby = details.getIdLobby();

        if(idLobby == -1){
            System.out.println("Problème pour rejoindre une partie.");
            this.vue.rejoindrePartie(false);
        } else {
            System.out.println("Rejoint la partie : " + idLobby);
            this.setIdLobby(idLobby);
            this.setDetailsLobby(details);
            this.vue.rejoindrePartie(true);
            
        }
    }

    private void traiterMajLobby(JSONObject detailsPartie) {
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
            System.out.println("Demande de lancement de la partie : " + this.getIdLobby());
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
        this.vue.demarrerPartie();
    }

    public void envoyerAction(Object action){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "envoyerDeplacement");//À mettre en clés quand dev
        objRequete.put("details", action);
        this.expediteur.envoyerRequete(objRequete.toString());
    }
    
    public void recevoirMiseAJour(JSONObject miseAJourPartie){
        this.setEtatPacmanGame(EtatPacmanGame.fromJSON(miseAJourPartie));
        this.vue.majTour();
    }

    public void finirPartie(JSONObject resultatPartie){
        this.vue.finirPartie();
    }

    public void demanderAjoutBot(String type){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_AJOUT_BOT);
            objRequete.put(RequetesJSON.Attributs.Joueur.TYPE_AGENT, type);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

    public void demanderRetraitBot(String type){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_RETRAIT_BOT);
            objRequete.put(RequetesJSON.Attributs.Joueur.TYPE_AGENT, type);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

    public void demanderChangementCamp(){
        if(this.detailsLobby != null){
            JSONObject objRequete = new JSONObject();
            objRequete.put(RequetesJSON.Attributs.ACTION, RequetesJSON.ASK_CHANGEMENT_CAMP);
            this.expediteur.envoyerRequete(objRequete.toString());
        }
    }

}
