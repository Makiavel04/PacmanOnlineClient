package Reseau;

import java.io.PrintWriter;
import java.net.Socket;

import Controller.ControleurClient;

public class ExpediteurClient{ //Pas de thread car c'est lui qui s'enclenche
    ControleurClient controleur;
    Socket socket;
    PrintWriter sortieReseau;

    public ExpediteurClient(ControleurClient c) {
        // Initialisation du ClientSender
        super();
        this.controleur = c;
        this.socket = null;
        this.sortieReseau = null;
    }

    public void setSocket(Socket s){
        this.socket = s;
    }
    public void setSortieReseau(PrintWriter out){
        this.sortieReseau = out;
    }

    public void envoyerRequete(String requete){
        this.sortieReseau.println(requete);
    }
}
