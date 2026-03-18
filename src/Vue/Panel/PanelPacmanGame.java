package Vue.Panel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import pacman.online.commun.moteur.EtatAgent;
import pacman.online.commun.moteur.Maze;


/**
 * Affichage d'un labyrinthe avec ses agents et la nourriture.
 */
public class PanelPacmanGame extends JPanel {

	private static final long serialVersionUID = 1L;

	private Color wallColor = Color.BLUE;

	private double sizePacman = 1.1;
	private Color pacmansColor = Color.yellow;

	private Color ghostsColor = Color.white;

	private Color ghostScarredColor = Color.BLUE;

	private double sizeFood = 0.3;
	private Color colorFood = Color.white;

	private double sizeCapsule = 0.7;
	private Color colorCapsule = Color.red;

	/** Labyrinthe représenté */
	private Maze m;

	/** Positions courantes des pacmans */
	private ArrayList<EtatAgent> pacmans_etat;
	/** Positions courantes des fantômes */
	private ArrayList<EtatAgent> ghosts_etat;
	/** Cache de couleurs des agents */
	private HashMap<Integer, Color> colors_buffer;

	private HashMap<Integer, Integer> last_directions;

	private boolean ghostsScarred;
	

	/**
	 * Crée le panneau d'affichage du labyrinthe
	 * @param maze
	 */
	public PanelPacmanGame(Maze maze) {
		this.m = maze;
		if(maze != null){
			pacmans_etat = this.m.getEtat_pacmans();
			ghosts_etat = this.m.getEtat_ghosts();

			colors_buffer = new HashMap<Integer, Color>();
			last_directions = new HashMap<Integer, Integer>();

			for(EtatAgent etat : pacmans_etat){
				if(etat.getCouleur() != null && !etat.getCouleur().isEmpty()){
					colors_buffer.put(etat.getIdAgent(), Color.decode(etat.getCouleur()));
					System.out.println("Couleur pacman " + etat.getIdAgent() + " : " + etat.getCouleur() + " -> " + colors_buffer.get(etat.getIdAgent()));
				}
				if(etat.getPosition() != null){
					last_directions.put(etat.getIdAgent(), etat.getPosition().getDir());
				}
			}

			for(EtatAgent etat : ghosts_etat){
				if(etat.getCouleur() != null && !etat.getCouleur().isEmpty()){
					colors_buffer.put(etat.getIdAgent(), Color.decode(etat.getCouleur()));
					System.out.println("Couleur fantôme " + etat.getIdAgent() + " : " + etat.getCouleur() + " -> " + colors_buffer.get(etat.getIdAgent()));
				}
				if(etat.getPosition() != null){
					last_directions.put(etat.getIdAgent(), etat.getPosition().getDir());
				}
			}

		}else{
			pacmans_etat = new ArrayList<EtatAgent>();
			ghosts_etat = new ArrayList<EtatAgent>();
			colors_buffer = new HashMap<Integer, Color>();
		}
		ghostsScarred = false;
	}

	/**
	 * Dessine le labyrinthe avec tous ses éléments.
	 * 
	 * @param g contexte graphique sur lequel dessiner
	 */
	@Override
	public void paint(Graphics g) {

		int dx = getSize().width;
		int dy = getSize().height;
		g.setColor(Color.black);
		g.fillRect(0, 0, dx, dy);

		int sx = m.getSizeX();
		int sy = m.getSizeY();
		double stepx = dx / (double) sx;
		double stepy = dy / (double) sy;
		double posx = 0;

		for (int x = 0; x < sx; x++) {
			double posy = 0;
			for (int y = 0; y < sy; y++) {
				if (m.isWall(x, y)) {
					drawWall(g, x, y, wallColor);
					// g.setColor(wallColor2);
					// g.fillRect((int) posx, (int) posy, (int) (stepx + 1),
					// 		(int) (stepy + 1));
					// g.setColor(wallColor);
					// double nsx = stepx * 0.5;
					// double nsy = stepy * 0.5;
					// double npx = (stepx - nsx) / 2.0;
					// double npy = (stepy - nsy) / 2.0;
					// g.fillRect((int) (npx + posx), (int) (npy + posy),
					// 		(int) (nsx), (int) nsy);
				}
				if (m.isFood(x, y)) {
					g.setColor(colorFood);
					double nsx = stepx * sizeFood;
					double nsy = stepy * sizeFood;
					double npx = (stepx - nsx) / 2.0;
					double npy = (stepy - nsy) / 2.0;
					g.fillOval((int) (npx + posx), (int) (npy + posy),
							(int) (nsx), (int) nsy);
				}
				if (m.isCapsule(x, y)) {
					g.setColor(colorCapsule);
					double nsx = stepx * sizeCapsule;
					double nsy = stepy * sizeCapsule;
					double npx = (stepx - nsx) / 2.0;
					double npy = (stepy - nsy) / 2.0;
					g.fillOval((int) (npx + posx), (int) (npy + posy),
							(int) (nsx), (int) nsy);
				}
				posy += stepy;
			}
			posx += stepx;
		}

		for (EtatAgent etat : pacmans_etat) {
			//Si on a une direction qui est STOP, on dessine dans le sens de la précédente.
			Integer dir = (etat.getPosition()!=null && etat.getPosition().getDir() != Maze.STOP) ? etat.getPosition().getDir() : last_directions.getOrDefault(etat.getIdAgent(), Maze.EAST);
			if(etat.getPosition() != null){
				drawPacmans(g, etat.getPosition().getX(), etat.getPosition().getY(), dir, this.colors_buffer.getOrDefault(etat.getIdAgent(), this.pacmansColor));
			}
		}

		for (EtatAgent etat : ghosts_etat) {
			if(etat.getPosition() != null){
				if (ghostsScarred) {
					drawGhosts(g, etat.getPosition().getX(), etat.getPosition().getY(), this.ghostScarredColor);
				} else {
					drawGhosts(g, etat.getPosition().getX(), etat.getPosition().getY(), this.colors_buffer.getOrDefault(etat.getIdAgent(), this.ghostsColor));
					
				}
			}
		}
	}

	/**
	 * Dessiner un mur
	 * 
	 * @param g contexte graphique sur lequel dessiner
	 * @param x coordonnée x de la case
	 * @param y coordonnée y de la case
	 * @param wallColor couleur des murs
	 */
	private void drawWall(Graphics g, int x, int y, Color wallColor) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		int gridWidth = m.getSizeX();
		int gridHeight = m.getSizeY();

		double stepx = (double) panelWidth / gridWidth;
		double stepy = (double) panelHeight / gridHeight;
		double posx = x * stepx;
		double posy = y * stepy;

		// Couleur extérieure
		g.setColor(wallColor);
		g.fillRect((int) posx, (int) posy, (int) stepx + 1, (int) stepy + 1);

		// Couleur intérieure (centre noir)
		g.setColor(Color.BLACK);
		double nsx = stepx * 0.5;
		double nsy = stepy * 0.5;
		double npx = posx + (stepx - nsx) / 2.0;
		double npy = posy + (stepy - nsy) / 2.0;
		g.fillRect((int) npx, (int) npy, (int) nsx, (int) nsy);

		// Connexions aux murs voisins (même épaisseur que le mur extérieur)
		boolean wallUp    = (y > 0) && m.isWall(x, y - 1);
		boolean wallDown  = (y < gridHeight - 1) && m.isWall(x, y + 1);
		boolean wallLeft  = (x > 0) && m.isWall(x - 1, y);
		boolean wallRight = (x < gridWidth - 1) && m.isWall(x + 1, y);

		int connWidth  = (int)(stepx * 0.5);
		int connHeight = (int)(stepy * 0.5);

		if (wallUp)    g.fillRect((int) npx, (int) posy, (int) nsx, connHeight);
		if (wallDown)  g.fillRect((int) npx, (int) (npy + nsy), (int) nsx, connHeight);
		if (wallLeft)  g.fillRect((int) posx, (int) npy, connWidth, (int) nsy);
		if (wallRight) g.fillRect((int) (npx + nsx), (int) npy, connWidth, (int) nsy);
	}

	/**
	 * 
	 * @param g contexte graphique sur lequel dessiner
	 * @param px coordonnée x de la case
	 * @param py coordonnée y de la case
	 * @param pacmanDirection direction du pacman
	 * @param color couleur du pacman
	 */
	void drawPacmans(Graphics g, int px, int py, int pacmanDirection,Color color) {

		// si pacman est en vie
		if((px != -1) || (py != -1)){
		
			int dx = getSize().width;
			int dy = getSize().height;
	
			int sx = m.getSizeX();
			int sy = m.getSizeY();
			double stepx = dx / (double) sx;
			double stepy = dy / (double) sy;
	
			double posx = px * stepx;
			double posy = py * stepy;
	
			g.setColor(color);
			double nsx = stepx * sizePacman;
			double nsy = stepy * sizePacman;
			double npx = (stepx - nsx) / 2.0;
			double npy = (stepy - nsy) / 2.0;
			
			
			int sa = 0;
			int fa = 0;
			if (pacmanDirection == Maze.NORTH) {
				sa = 70;
				fa = -320;
			}
			if (pacmanDirection == Maze.SOUTH) {
				sa = 250;
				fa = -320;
			}
			if (pacmanDirection == Maze.EAST ) {
				sa = 340;
				fa = -320;
			}
			if (pacmanDirection == Maze.WEST) {
				sa = 160;
				fa = -320;
			}
		
	
			g.fillArc((int) (npx + posx), (int) (npy + posy), (int) (nsx),
					(int) nsy, sa, fa);
		}

	}

	/**
	 * Dessine un fantôme
	 * 
	 * @param g contexte graphique sur lequel dessiner
	 * @param px coordonnée x de la case
	 * @param py coordonnée y de la case
	 * @param color couleur du fantôme
	 */
	void drawGhosts(Graphics g, int px, int py, Color color) {

		if((px != -1) || (py != -1)){
			int dx = getSize().width;
			int dy = getSize().height;
	
			int sx = m.getSizeX();
			int sy = m.getSizeY();
			double stepx = dx / (double) sx;
			double stepy = dy / (double) sy;
	
			double posx = px * stepx;
			double posy = py * stepy;
	
			g.setColor(color);
	
			double nsx = stepx * sizePacman;
			double nsy = stepy * sizePacman;
			double npx = (stepx - nsx) / 2.0;
			double npy = (stepy - nsy) / 2.0;
	
			g.fillArc((int) (posx + npx), (int) (npy + posy), (int) (nsx),
					(int) (nsy), 0, 180);
			g.fillRect((int) (posx + npx), (int) (npy + posy + nsy / 2.0 - 1),
					(int) (nsx), (int) (nsy * 0.666));
			g.setColor(Color.BLACK);
			g.fillOval((int) (posx + npx + nsx / 5.0),
					(int) (npy + posy + nsy / 3.0), 4, 4);
			g.fillOval((int) (posx + npx + 3 * nsx / 5.0),
					(int) (npy + posy + nsy / 3.0), 4, 4);
	
			g.setColor(Color.black);
		}

	}
	
	public Maze getMaze(){
		return m;
	}
	
	/**
	 * Mettre un nouveau labyrinthe pour l'affichage et récupérer ses informations 
	 * 
	 * @param maze nouveau labyrinthe
	 */
	public void majMaze(Maze maze){
		this.m = maze;
		pacmans_etat = this.m.getEtat_pacmans();
		ghosts_etat = this.m.getEtat_ghosts();

		for(EtatAgent etat : pacmans_etat){
			if(etat.getPosition() != null && etat.getPosition().getDir() != Maze.STOP){
				last_directions.put(etat.getIdAgent(), etat.getPosition().getDir());
			}
		}
		for(EtatAgent etat : ghosts_etat){
			if(etat.getPosition() != null && etat.getPosition().getDir() != Maze.STOP){
				last_directions.put(etat.getIdAgent(), etat.getPosition().getDir());
			}
		}

		ghostsScarred = false;
	}
	
	public void setGhostsScarred(boolean ghostsScarred) {
		this.ghostsScarred = ghostsScarred;
	}

	public ArrayList<EtatAgent> getPacmans_etat() {
		return pacmans_etat;
	}

	public void setPacmans_etat(ArrayList<EtatAgent> pacmans) {
		this.pacmans_etat = pacmans;				
	}

	public ArrayList<EtatAgent> getGhosts_etat() {
		return ghosts_etat;
	}

	public void setGhosts_etat(ArrayList<EtatAgent> ghosts) {
		this.ghosts_etat = ghosts;
	}

	
}
