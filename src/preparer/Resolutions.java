package preparer;

public enum Resolutions {
	/** 1280 x 720 px */
	HD("HD", 1280, 720),
	/** 1920 x 1080 px */
	FHD("FHD", 1920, 1080),
	/** 2560 x 1440 px */
	_2K("2K", 2560, 1440),
	/** 3840 x 2160 px */
	_4K("4K", 3840, 2160);

	//---- Atributtes ---
	private final String name;
	private final Dimension dimensionPx;

	//---- constructor ----
	private Resolutions(final String name, final int widthPx, final int heightPx) {
		this.name = name;
		dimensionPx = new Dimension(widthPx, heightPx);
	}

	//---- Methods ----
	public String getName() {
		return name;
	}

	public Dimension getDimension() {
		return dimensionPx;
	}

}
