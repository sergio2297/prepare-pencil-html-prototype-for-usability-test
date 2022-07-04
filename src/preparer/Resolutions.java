package preparer;

import java.util.HashSet;
import java.util.Set;

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

	public static Set<String> names() {
		Set<String> names = new HashSet<>();
		for(int i = 0; i < values().length; ++i) {
			names.add(values()[i].name);
		}
		return names;
	}

	public static Resolutions getResolutionByName(final String name) {
		int i = 0;

		while(i < values().length && !values()[i].getName().equals(name)) {
			++i;
		}

		if(i < values().length) {
			return values()[i];
		} else {
			return null;
		}
	}
}
