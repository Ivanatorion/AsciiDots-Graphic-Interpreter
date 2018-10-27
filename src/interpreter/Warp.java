package interpreter;

public class Warp {
	private char c;
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	Warp(char id, int posX1, int posY1, int posX2, int posY2){
		c = id;
		x1 = posX1;
		y1 = posY1;
		x2 = posX2;
		y2 = posY2;
	}
	
	public char getC() {
		return c;
	}
	public void setC(char c) {
		this.c = c;
	}
	public int getX1() {
		return x1;
	}
	public void setX1(int x) {
		this.x1 = x;
	}
	public int getY1() {
		return y1;
	}
	public void setY1(int y) {
		this.y1 = y;
	}
	
	public int getX2() {
		return x2;
	}
	public void setX2(int x) {
		this.x2 = x;
	}
	public int getY2() {
		return y2;
	}
	public void setY2(int y) {
		this.y2 = y;
	}
	
	
}
