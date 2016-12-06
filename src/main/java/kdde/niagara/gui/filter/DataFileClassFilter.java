package kdde.niagara.gui.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class DataFileClassFilter extends FileFilter {
	String ext;

	/**
	 * <p>
	 * Title: Constructor
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param ext
	 */
	public DataFileClassFilter(String ext) {
		super();
		this.ext = ext;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		String fileName = file.getName();
		int index = fileName.lastIndexOf('.');
		if (index > 0 && index < fileName.length() - 1) {
			String extension = fileName.substring(index + 1).toLowerCase();
			if (extension.equals(ext)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {
		if (ext.equals("arff")) {
			return "Data File(*.arff)";
		}
		if (ext.equals("cvs")) {
			return "Data File(*.cvs)";
		}
		if (ext.equals("txt")) {
			return "Data File(*.txt)";
		}
		return null;
	}

}
