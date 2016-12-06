package kdde.niagara.mining;

import java.io.File;

public class Param {
	private File file1;
	private File file2;
	private double alpha = 0.0;
	private double betal = 0.0;
	public Param(File file1, File file2) {
		super();
		this.file1 = file1;
		this.file2 = file2;
	}
	public Param() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Param(File file1, File file2, double alpha, double betal) {
		super();
		this.file1 = file1;
		this.file2 = file2;
		this.alpha = alpha;
		this.betal = betal;
	}
	public File getFile1() {
		return file1;
	}
	public void setFile1(File file1) {
		this.file1 = file1;
	}
	public File getFile2() {
		return file2;
	}
	public void setFile2(File file2) {
		this.file2 = file2;
	}
	public double getAlpha() {
		return alpha;
	}
	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
	public double getBetal() {
		return betal;
	}
	public void setBetal(double betal) {
		this.betal = betal;
	}
	
}
