package Vue;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelAttentePartie extends JPanel{
    VueClient vue;

    public PanelAttentePartie(VueClient v){
        this.vue = v;
        JButton passer = new JButton("Passer Attente");
        passer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.getEtat().demarrerPartie();
            }
        });

        this.add(passer);
    }
}
