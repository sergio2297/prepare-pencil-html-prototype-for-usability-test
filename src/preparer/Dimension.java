package preparer;

public class Dimension {

	//---- Atributtes ----
	private int height, width;

	//---- Constructor ----
	public Dimension(final int width, final int height) {
		setWidth(width);
		setHeight(height);
	}

	//---- Methods ----
	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		if(height < 0) {
			throw new IllegalArgumentException("Error. Height can't be less than 0");
		}
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		if(width < 0) {
			throw new IllegalArgumentException("Error. Width can't be less than 0");
		}
		this.width = width;
	}

}
