package preparer;

public class Configuration {

	//---- Atributtes ----
	private final boolean centerHorizontally;
	private final boolean centerVertically;
	private final boolean fullscreenModeWillBeUsed;
	private final boolean removeStyles;

	//---- Constructor ----
	private Configuration(final boolean centerH, final boolean centerV, final boolean fsMode, final boolean rmStyles) {
		this.centerHorizontally = centerH;
		this.centerVertically = centerV;
		this.fullscreenModeWillBeUsed = fsMode;
		this.removeStyles = rmStyles;
	}

	//---- Methods ----
	public boolean centerHorizontally() {
		return centerHorizontally;
	}

	public boolean centerVertically() {
		return centerVertically;
	}

	public boolean fullscreenModeWillBeUsed() {
		return fullscreenModeWillBeUsed;
	}

	public boolean removeStyles() {
		return removeStyles;
	}

	/* **********************************************
	 * 					BUILDER
	 * **********************************************/
	public static class Builder {

		//---- Atributtes ----
		private boolean centerHorizontally = true,
				centerVertically = true,
				fullScreenModeWillBeUsed = true,
				removeStyles = true;

		//---- Constructor ----
		public Builder() {}

		//---- Methods ----
		public Configuration build() {
			return new Configuration(centerHorizontally, centerVertically, fullScreenModeWillBeUsed, removeStyles);
		}

		public Builder noCenterHorizontally() {
			centerHorizontally = false;
			return this;
		}

		public Builder noCenterVertically() {
			centerVertically = false;
			return this;
		}

		public Builder noFullScreenMode() {
			fullScreenModeWillBeUsed = false;
			return this;
		}

		public Builder noRemoveStyles() {
			removeStyles = false;
			return this;
		}

	}
}
