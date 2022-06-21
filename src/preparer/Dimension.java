package preparer;

public class Dimension {

	//---- Atributtes ----
	private final int width, height;

	//---- Constructor ----
	public Dimension(final int width, final int height) {
		if(width <= 0 || height <= 0) {
			throw new IllegalArgumentException("Error. Dimensions must be greather than 0. width: " + width + ", height:" + height + ".");
		}

		this.width = width;
		this.height = height;
	}

	//---- Methods ----
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
