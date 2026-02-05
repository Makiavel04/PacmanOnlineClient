package Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

import org.json.JSONObject;

import Reseau.RecepteurClient;
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
        System.out.println("Donnée reçue du serveur.");
        switch(action){
            case "":
                // Traiter l'action spécifique
                break;
            //Reponse d'authentification
            case "reponseAuthentification":
                this.retourAuthentification(objReponse);
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
                this.recevoirFinDePartie(objReponse);
                break;
            default:
                System.out.println("Action inconnue reçue : " + action);
        }
    }

    public void demanderAuthentification(String nomJoueur, String mdp){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "demanderAuthentification");
        objRequete.put("nomJoueur", nomJoueur);
        objRequete.put("mdp", mdp);
        //this.expediteur.envoyerRequete(objRequete.toString());
        this.retourAuthentification(objRequete);
    }

    public void retourAuthentification(JSONObject objReponse){
        this.vue.traiterAuthentification(true);

    }

    public void demanderPartie(){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "demanderPartie");
        //this.expediteur.envoyerRequete(objRequete.toString());
        this.rejoindrePartie(objRequete);
    }

    public void rejoindrePartie(JSONObject detailsPartie){
        try{
            for(int i=0; i<3; i++){
                Thread.sleep(1000);
                System.out.println("Rejoindre la partie dans " + (5-i) + " secondes...");
            }
        } catch (InterruptedException e){}
        this.vue.rejoindrePartie(true);
    }

    public void envoyerAction(Object action){
        JSONObject objRequete = new JSONObject();
        objRequete.put("action", "envoyerAction");
        objRequete.put("details", action);
        this.expediteur.envoyerRequete(objRequete.toString());
    }

    public void debuterPartie(JSONObject configPartie){}
    
    public void recevoirMiseAJour(JSONObject miseAJourPartie){}

    public void recevoirFinDePartie(JSONObject resultatPartie){}

}
