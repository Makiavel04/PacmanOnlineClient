package Vue;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelFinPartie extends JPanel{
    VueClient vue;

    public PanelFinPartie(VueClient v){
        this.vue = v;
        JButton retourAccueil = new JButton("Retour Accueil");
        retourAccueil.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.getEtat().retourAccueil();
            }
        });
        this.add(retourAccueil);
    }
    
    
}
