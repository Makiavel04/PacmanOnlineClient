import Controller.ControleurClient;

public class ClientPacman {
    public static void main(String[] args) throws Exception {
        ControleurClient controleur = new ControleurClient("localhost", 8080);
        controleur.ouvrirConnexion();
    }
}
