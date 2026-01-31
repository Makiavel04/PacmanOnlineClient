package Controller;

import Reseau.ClientReceiver;
import Reseau.ClientSender;
import Vue.VueClient;

public class ControleurClient {
    
    VueClient vue;
    ClientReceiver receiver;
    ClientSender sender;

    public ControleurClient() {
        this.receiver = new ClientReceiver();
        this.sender = new ClientSender();
        this.vue = new VueClient(this);
    }

    public void fermerConnexion() {
        // Logique pour fermer proprement la connexion, les sockets, threads, etc.
        System.out.println("Fermeture de la connexion client.");
        //STOP les threads reseau et ferme les sockets.
    }

}
