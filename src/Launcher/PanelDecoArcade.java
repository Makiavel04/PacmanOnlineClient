package Launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class PanelDecoArcade extends JPanel {

    private double sizeMultiplier = 1.1; 
    private Color arcadeBlue = Color.decode("#2121FF");

    public PanelDecoArcade() {
        this.setPreferredSize(new Dimension(450, 65));
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int dx = getWidth();
        int dy = getHeight();

        // Structure en unités
        double wPacman = 1.0;
        double wSpace1 = 0.8; 
        double wGum    = 0.3; 
        double wSpace2 = 0.5; 
        double wSpace3 = 0.8; 
        double wGhost  = 1.0; 
        double ghostGap = 0.15; 

        double totalUnits = wPacman + wSpace1 + (2 * wGum) + wSpace2 + wSpace3 + (4 * wGhost) + (3 * ghostGap);

        double marginY = 12.0;
        double step = Math.min(dx / totalUnits, (dy - marginY) / sizeMultiplier);

        double totalDrawingWidth = totalUnits * step;
        double startX = (dx - totalDrawingWidth) / 2.0; 
        double startY = (dy - (step * sizeMultiplier)) / 2.0; 

        double currentX = startX;

        // A. Pacman (Regarde à DROITE vers les fantômes)
        drawPacman(g2d, currentX, startY, step, Color.YELLOW);
        currentX += (wPacman + wSpace1) * step;

        // B. Les 2 Pac-Gums
        g2d.setColor(Color.WHITE);
        double gumSize = step * 0.22;
        for (int i = 0; i < 2; i++) {
            double gx = currentX + (wGum * step) / 2.0 - gumSize / 2.0;
            double gy = dy / 2.0 - gumSize / 2.0;
            g2d.fillOval((int) gx, (int) gy, (int) gumSize, (int) gumSize);
            
            if (i == 0) currentX += (wGum + wSpace2) * step;
            else currentX += (wGum + wSpace3) * step;
        }

        // C. Les Fantômes (Regardent à GAUCHE vers Pacman)
        Color[] colors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
        for (int i = 0; i < colors.length; i++) {
            drawGhost(g2d, currentX, startY, step, colors[i]);
            currentX += (wGhost + ghostGap) * step;
        }

        // D. Ligne de sol
        g2d.setColor(arcadeBlue);
        g2d.fillRect(0, dy - 3, dx, 3);
    }

    private void drawPacman(Graphics2D g, double x, double y, double step, Color c) {
        double size = step * sizeMultiplier;
        double px = x + (step - size) / 2.0; 
        g.setColor(c);
        // Start à 35°, dessine 290° -> laisse une bouche de 70° centrée sur l'axe X (droite)
        g.fillArc((int) px, (int) y, (int) size, (int) size, 35, 290);
    }

    private void drawGhost(Graphics2D g, double x, double y, double step, Color c) {
        double size = step * sizeMultiplier;
        double px = x + (step - size) / 2.0;
        
        g.setColor(c);
        // Tête
        g.fillArc((int) px, (int) y, (int) size, (int) size, 0, 180);
        // Corps
        int bodyH = (int) (size * 0.45);
        g.fillRect((int) px, (int) (y + size / 2), (int) size, bodyH);

        // --- Yeux (Regard vers la gauche) ---
        g.setColor(Color.WHITE);
        int eyeW = (int) (size * 0.22);
        int eyeH = (int) (size * 0.28);
        double eyeY = y + size * 0.25;
        
        // On dessine le blanc des yeux
        double leftEyeX = px + size * 0.18;
        double rightEyeX = px + size * 0.58;
        g.fillOval((int) leftEyeX, (int) eyeY, eyeW, eyeH);
        g.fillOval((int) rightEyeX, (int) eyeY, eyeW, eyeH);
        
        // Pupilles (décalées vers la gauche de l'oeil pour regarder Pacman)
        g.setColor(Color.BLUE); // Bleu pour un look classique, ou Noir
        int pupilSize = (int) (eyeW * 0.55);
        // Offset de 1 ou 2 pixels vers la gauche dans le blanc de l'oeil
        g.fillOval((int) leftEyeX, (int) (eyeY + eyeH/4), pupilSize, pupilSize);
        g.fillOval((int) rightEyeX, (int) (eyeY + eyeH/4), pupilSize, pupilSize);
    }
}