package preparer;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class ConfigurationTest {

	@Test
	public void buildDefaultConfiguartionTest() {
		// Given
		Configuration.Builder builder = new Configuration.Builder();

		// When
		Configuration configuration = builder.build();

		// Then
		assertTrue(configuration.centerHorizontally());
		assertTrue(configuration.centerVertically());
		assertTrue(configuration.fullscreenModeWillBeUsed());
		assertTrue(configuration.removeStyles());
	}

	@Test
	public void buildConfigurationWithoutCenterHorizontallyTest() {
		// Given
		Configuration.Builder builder = new Configuration.Builder();

		// When
		Configuration configuration = builder
				.noCenterHorizontally()
				.build();

		// Then
		assertFalse(configuration.centerHorizontally());
		assertTrue(configuration.centerVertically());
		assertTrue(configuration.fullscreenModeWillBeUsed());
		assertTrue(configuration.removeStyles());
	}

	@Test
	public void buildConfigurationWithoutCenterVerticallyTest() {
		// Given
		Configuration.Builder builder = new Configuration.Builder();

		// When
		Configuration configuration = builder
				.noCenterVertically()
				.build();

		// Then
		assertTrue(configuration.centerHorizontally());
		assertFalse(configuration.centerVertically());
		assertTrue(configuration.fullscreenModeWillBeUsed());
		assertTrue(configuration.removeStyles());
	}

	@Test
	public void buildConfigurationWithoutFullScreenModeTest() {
		// Given
		Configuration.Builder builder = new Configuration.Builder();

		// When
		Configuration configuration = builder
				.noFullScreenMode()
				.build();

		// Then
		assertTrue(configuration.centerHorizontally());
		assertTrue(configuration.centerVertically());
		assertFalse(configuration.fullscreenModeWillBeUsed());
		assertTrue(configuration.removeStyles());
	}

	@Test
	public void buildConfigurationWithoutRemoveStylesTest() {
		// Given
		Configuration.Builder builder = new Configuration.Builder();

		// When
		Configuration configuration = builder
				.noRemoveStyles()
				.build();

		// Then
		assertTrue(configuration.centerHorizontally());
		assertTrue(configuration.centerVertically());
		assertTrue(configuration.fullscreenModeWillBeUsed());
		assertFalse(configuration.removeStyles());
	}

	@Test
	public void buildConfigurationWithAnyOptionTest() {
		// Given
		Configuration.Builder builder = new Configuration.Builder();

		// When
		Configuration configuration = builder.noCenterHorizontally()
				.noCenterVertically()
				.noFullScreenMode()
				.noRemoveStyles()
				.build();

		// Then
		assertFalse(configuration.centerHorizontally());
		assertFalse(configuration.centerVertically());
		assertFalse(configuration.fullscreenModeWillBeUsed());
		assertFalse(configuration.removeStyles());
	}

}
