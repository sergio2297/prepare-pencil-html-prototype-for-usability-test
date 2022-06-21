package preparer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DimensionTest {

	@Test
	@DisplayName("It's not possible to instantiate a Dimension with zero width")
	public void cannotInstantiateDimensionWithZeroWidth() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(0, 10));
	}

	@Test
	@DisplayName("It's not possible to instantiate a Dimension with zero height")
	public void cannotInstantiateDimensionWithZeroHeight() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, 0));
	}

	@Test
	@DisplayName("It's not possible to instantiate a Dimension with negative width")
	public void cannotInstantiateDimensionWithNegativeWidth() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(-1, 10));
	}

	@Test
	@DisplayName("It's not possible to instantiate a Dimension with negative height")
	public void cannotInstantiateDimensionWithNegativeHeight() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Dimension(10, -1));
	}

	@Test
	@DisplayName("Width should be the same as when the Dimension was created")
	public void widthShouldBeTheSameASWhenDimensionWasCreated() {
		Assertions.assertEquals(10, new Dimension(10, 2).getWidth());
	}

	@Test
	@DisplayName("Height should be the same as when the Dimension was created")
	public void heightShouldBeTheSameASWhenDimensionWasCreated() {
		Assertions.assertEquals(10, new Dimension(2, 10).getHeight());
	}

}
