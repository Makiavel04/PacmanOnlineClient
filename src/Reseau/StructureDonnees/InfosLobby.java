package Reseau.StructureDonnees;

import org.json.JSONObject;

public class InfosLobby {
    //Clé JSON
    public static final String ID_LOBBY = "idLobby";
    public static final String NB_JOUEUR = "nbJoueur";
    public static final String NB_MAX_JOUEUR = "nbMaxJoueur";

    //Attributs
    private int idLobby;
    private int nbJoueur;
    private int nbMaxJoueur;

    InfosLobby(int idLobby, int nbJoueur, int nbMaxJoueur) {
        this.idLobby = idLobby;
        this.nbJoueur = nbJoueur;
        this.nbMaxJoueur = nbMaxJoueur;
    }

    public int getIdLobby() {
        return idLobby;
    }

    public int getNbJoueur() {
        return nbJoueur;
    }

    public int getNbMaxJoueur() {
        return nbMaxJoueur;
    }

    public static InfosLobby fromJSON(JSONObject json){
        try{
            int idLobby = json.getInt(ID_LOBBY);
            int nbJoueur = json.getInt(NB_JOUEUR);
            int nbMaxJoueur = json.getInt(NB_MAX_JOUEUR);
            return new InfosLobby(idLobby, nbJoueur, nbMaxJoueur);
        }catch(Exception e){
            System.out.println("Error parsing JSON to InfosLobby: " + e.getMessage());
            return null;
        }
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put(ID_LOBBY, this.idLobby);
        json.put(NB_JOUEUR, this.nbJoueur);
        json.put(NB_MAX_JOUEUR, this.nbMaxJoueur);
        return json;
    }
    
}
