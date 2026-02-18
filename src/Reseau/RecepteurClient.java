package Reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import org.json.JSONObject;

import Controller.ControleurClient;

public class RecepteurClient extends Thread{
    ControleurClient controleur;
    Socket socket;
    BufferedReader entreeReseau;

    public RecepteurClient(ControleurClient c) {
        // Initialisation du ClientReceiver
        super();
        this.controleur = c;
        this.socket = null;
        this.entreeReseau = null;
    }

    public void setSocket(Socket s){
        this.socket = s;
    }

    public void setEntreeReseau(BufferedReader in){
        this.entreeReseau = in;
    }

    public void run(){
        try{
            while(true){
                String ligne = entreeReseau.readLine(); // on lit ce qui arrive
                if(ligne == null) break;
                System.out.println("Donnée reçue du serveur.");
                System.out.println(ligne);
                JSONObject objReponse = new JSONObject(ligne);
                String action = objReponse.getString("action");

                this.controleur.gestionReception(action, objReponse);
            }
            controleur.fermerConnexion();
            throw new IOException("Connexion récépteur coupée");
        } catch (IOException e) {
            System.err.println("IOException :" + e.getMessage());
        } catch (Exception e){
            System.err.println("Exception :" + e.getMessage());
        } 
    }
}
