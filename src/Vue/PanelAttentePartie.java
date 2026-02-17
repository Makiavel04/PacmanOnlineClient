package Vue;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridLayout;

public class PanelAttentePartie extends JPanel{
    VueClient vue;

    public PanelAttentePartie(VueClient v){
        this.vue = v;
        GridLayout layout = new GridLayout(2,1);
        this.setLayout(layout);

        JLabel attenteLabel = new JLabel("Match " + this.vue.getIdMatch() + " En attente d'autres joueurs...", JLabel.CENTER);

        this.add(attenteLabel);
    }
}
