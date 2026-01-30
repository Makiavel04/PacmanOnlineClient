package View;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelEnJeu extends JPanel{
    VueClient vue;

    public PanelEnJeu(VueClient v){
        this.vue = v;
        JButton finir = new JButton("fin partie vite vite vite");
        finir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.getEtat().finirPartie();
            }
        });
    }
    
}
