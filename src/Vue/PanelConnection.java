package Vue;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelConnection extends JPanel{
    
    VueClient vue;

    public PanelConnection(VueClient v){
        this.vue = v;
        JButton boutonConnection = new JButton("Se connecter");
        boutonConnection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                vue.getEtat().seConnecter();
            }
        });
        this.add(boutonConnection);
    }
}
