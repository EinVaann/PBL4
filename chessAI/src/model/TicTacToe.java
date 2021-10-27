package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class TicTacToe extends JFrame implements MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new TicTacToe();
	}
	
	int n = 15;
	int s = 30;
	int os = 50;
	List<Point> history = new ArrayList<Point>();
	
	public TicTacToe() {
		this.setSize(n*s+2*os,n*s+2*os);
		this.setTitle("Caro");
		this.setDefaultCloseOperation(3);
		this.addMouseListener(this);
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		g.setColor(Color.black);
		for(int i=0;i<=n;i++) {
			g.drawLine(os, os+i*s, os+n*s, os+i*s);
			g.drawLine(os+i*s, os, os+i*s, os+n*s);
		}
		g.setFont(new Font("arial",Font.BOLD,s));
		for(int i=0;i<history.size();i++) {
			int ix = history.get(i).x;
			int iy = history.get(i).y;
			int x = os+ix*s+s/2-s/4;
			int y = os+iy*s+s/2+s/4;
			String st="o";
			if( i%2 != 0 ) st="x";
			g.drawString(st, x, y);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x=e.getX();
		int y=e.getY();
		if ( x<os || x>=os+n*s || y<os || y>=os+n*s) return;
		int ix = (x-os)/s;
		int iy = (y-os)/s;
		
		System.out.println(ix+","+iy);
		for(Point d: history) {
			if(ix==d.x && iy == d.y) return;
		}
		history.add(new Point(ix,iy));
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
