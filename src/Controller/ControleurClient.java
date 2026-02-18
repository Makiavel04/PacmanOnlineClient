package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import Reseau.RecepteurClient;
import Reseau.StructureDonnees.InfosLobby;
import Reseau.ExpediteurClient;
import Vue.VueClient;

public class ControleurClient {
    
    VueClient vue;
    RecepteurClient recepteur;
    ExpediteurClient expediteur;

    Socket socket;
    String adresseServeur;
    int portServeur;

    public ControleurClient(String adr, int port) {
        this.vue = new VueClient(this);
        this.recepteur = new RecepteurClient(this);
        this.expediteur = new ExpediteurClient(this);

        this.adresseServeur = adr;
        this.portServeur = port;   
    }

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
            case "reponseAuthentification":
                this.retourAuthentification(objReponse);
                break;
            //Reponse de liste de lobbies
            case "reponseListeLobbies":
                this.traiterListeLobbies(objReponse);
                break;
            //Reponse de demande de partie
            case "reponseDemandePartie":
                this.rejoindrePartie(objReponse);
                break;
            //Reponse de debut de partie
            case "debutPartie":
                debuterPartie(objReponse);
                break;
            //Reponse de mise a jour de partie
            case "miseAJourPartie":
                this.recevoirMiseAJour(objReponse);
                break;
            //Reponse de fin de partie
            case "finDePartie":
                this.finirPartie(objReponse);
                break;
            default:
                System.out.println("Action inconnue reçue : " + action);
        }
    }

    public void demanderAuthentification(String nomJoueur, String mdp){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "demanderAuthentification");
        objRequete.put("username", nomJoueur);
        objRequete.put("password", mdp);
        this.expediteur.envoyerRequete(objRequete.toString());
        //this.retourAuthentification(objRequete);
    }

    public void retourAuthentification(JSONObject objReponse){
        boolean authResult = objReponse.getBoolean("reponse");
        this.vue.traiterAuthentification(authResult);
    }

    public void demanderListeLobbies(){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "demanderListeLobbies");
        this.expediteur.envoyerRequete(objRequete.toString());
    } 

    public void traiterListeLobbies(JSONObject objReponse){
        // Traiter la liste de lobbies reçue du serveur et mettre à jour la vue
        System.out.println("Traitement de la liste des lobbies reçue.");
        JSONArray lobbiesJSON = objReponse.getJSONArray("lobbies");
        ArrayList<InfosLobby> infosLobbies = new ArrayList<>();
        for(int i = 0; i < lobbiesJSON.length(); i++) {
            JSONObject lobbyObj = lobbiesJSON.getJSONObject(i);
            infosLobbies.add(InfosLobby.fromJSON(lobbyObj));
        }
        this.vue.traiterListeLobbies(infosLobbies);
    }

    public void demanderPartie(int idLobby){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "demanderPartie");
        objRequete.put("idLobby", idLobby);
        this.expediteur.envoyerRequete(objRequete.toString());
    }

    public void rejoindrePartie(JSONObject detailsPartie){
        int idMatch = detailsPartie.getInt("idMatch");
        if(idMatch == -1){
            System.out.println("Problème pour rejoindre une partie.");
            this.vue.rejoindrePartie(false, idMatch);
        } else {
            System.out.println("Rejoint la partie : " + idMatch);
            this.vue.rejoindrePartie(true, idMatch);
        }
    }

    public void debuterPartie(JSONObject configPartie){
        this.vue.demarrerPartie();
    }

    public void envoyerAction(Object action){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "envoyerAction");
        objRequete.put("details", action);
        this.expediteur.envoyerRequete(objRequete.toString());
    }
    
    public void recevoirMiseAJour(JSONObject miseAJourPartie){
        int tour = miseAJourPartie.getInt("tour");
        this.vue.majTour(tour);
    }

    public void finirPartie(JSONObject resultatPartie){
        this.vue.finirPartie();
    }

}
