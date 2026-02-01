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
            default:
                System.out.println("Action inconnue reçue : " + action);
        }
    }

}
