package Vue;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridLayout;


public class PanelEnJeu extends JPanel{
    VueClient vue;

    public PanelEnJeu(VueClient v){
        this.vue = v;
        GridLayout  layout = new GridLayout(2, 1);
        this.setLayout(layout);
        JLabel label = new JLabel("Partie " + vue.getIdLobby() + " en cours...");
        JLabel label2 = new JLabel("Tour " + vue.getTour());
        label.setHorizontalAlignment(JLabel.CENTER);
        label2.setHorizontalAlignment(JLabel.CENTER);
        this.add(label2);
        this.add(label);
    }
    
}
