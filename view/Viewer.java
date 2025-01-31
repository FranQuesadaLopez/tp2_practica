package simulator.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver{
	
	private static final long serialVersionUID = 1L;
	private static final int _WIDTH = 1000;
	private static final int _HEIGHT = 1000;
	
	private static final Color _RED = new Color(255, 0, 0);
	private static final Color _GREEN = new Color(0, 255, 0);
	private static final Color _BLUE = new Color(0, 0, 255);
	private static final Color _BLACK = new Color(0, 0, 0);
	
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	private boolean _showVectors;
	private String help1;
	private String help2;
	
	Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI() {
		this.setBorder(new TitledBorder("Viewer"));
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		_showVectors = true;
		help1 = "h: toggle help, v: toggle vectors, +: zoom-in, -: zoom-out, =: fit";
		help2 = "Scaling ratio: " + _scale;
		//this.setSize(_WIDTH, _HEIGHT);
		
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
					case '-':
						_scale = _scale * 1.1;
						repaint();
						break;
					case '+':
						_scale = Math.max(1000.0, _scale / 1.1);
						repaint();
						break;
					case '=':
						autoScale();
						repaint();
						break;
					case 'h':
						_showHelp = !_showHelp;
						repaint();
						break;
					case 'v':
						_showVectors = !_showVectors;
						repaint();
						break;
					default:
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		
		addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		repaint();
	}
	
	
	
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		// use ’gr’ to draw not ’g’ --- it gives nicer results
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		
		help2 = "Scaling ratio: " + _scale;
		
		drawCrossAtCenter(gr,_RED, 10, 10);
		drawBody(gr, 10, 10 , _BLUE, _GREEN, _RED);
		
		if(_showHelp) {
			drawHelp(gr, _RED, help1, help2);
		}
		// TODO draw help if _showHelp is true
		
	}
	
	private void drawHelp(Graphics g, Color helpColor, String help1, String help2) {
		g.setColor(helpColor);
		g.drawString(help1, 15, 28);
		g.drawString(help2, 15, 45);
	}
	
	// other private/protected methods
	
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector2D p = b.getPosition();
			max = Math.max(max, Math.abs(p.getX()));
			max = Math.max(max, Math.abs(p.getY()));
		}
		double size = Math.max(1.0, Math.min(getWidth(), getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	private void drawCrossAtCenter(Graphics g, Color crossColor, int height, int width) {
		g.setColor(crossColor);
		
		//Vertical
		g.drawLine(_centerX, _centerY -width/2, _centerX, _centerY +width/2);
		//Horizontal
		g.drawLine(_centerX -height/2, _centerY, _centerX +height/2, _centerY);
	}
	
	private void drawBody(
			Graphics g, int bHeight, int bWidth, 
			Color bodiesColor, Color forceArrowColor, 
			Color velocityArrowColor
			) {
		
		for(Body b : _bodies) {
			int bodyX = _centerX + (int) (b.getPosition().getX()/_scale);
			int bodyY = _centerY - (int) (b.getPosition().getY()/_scale);
			g.setColor(bodiesColor);			
			g.fillOval(bodyX, bodyY, bHeight, bWidth);
			g.setColor(_BLACK);
			g.drawString(b.getId(), bodyX, bodyY);
		}

		if(_showVectors) {
			for(Body b : _bodies) {
				int bodyX = _centerX + (int) (b.getPosition().getX()/_scale);
				int bodyY = _centerY - (int) (b.getPosition().getY()/_scale);
				drawLineWithArrow(g, bodyX, bodyY, (int) (bodyX + (b.getForce().direction().getX()) * 25), (int) (bodyY + (b.getForce().direction().getY()) * 25), 5 ,5, forceArrowColor);
				drawLineWithArrow(g, bodyX, bodyY, (int) (bodyX + (b.getVelocity().direction().getX()) * 25), (int) (bodyY + (b.getVelocity().direction().getY()) * 25), 5 ,5, velocityArrowColor);
			}
		}

	}
	
	private void drawLineWithArrow(//
		Graphics g, //
		int x1, int y1, //
		int x2, int y2, //
		int w, int h, //
		Color arrowColor
	) {
		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;
		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;
		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;
		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };
		g.setColor(arrowColor);
		g.drawLine(x1, y1, x2, y2);
		g.fillPolygon(xpoints, ypoints, 3);
	}
	
	
	// SimulatorObserver methods

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_bodies = bodies;
		autoScale();
		repaint();
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		_bodies = bodies;
		autoScale();
		repaint();
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		_bodies = bodies;
		autoScale();
		repaint();
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		_bodies = bodies;
		repaint();
	}

	@Override
	public void onDeltaTimeChanged(double dt) {}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {}

}
