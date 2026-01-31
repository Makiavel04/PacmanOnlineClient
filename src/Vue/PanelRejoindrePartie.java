package Vue;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelRejoindrePartie extends JPanel{
    
    VueClient vue;

    public PanelRejoindrePartie(VueClient v){
        this.vue = v;
        JButton boutonRejoindre = new JButton("Rejoindre une partie");
        boutonRejoindre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.getEtat().rejoindrePartie();
            }
        });
        this.add(boutonRejoindre);
    }
}
