package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public interface Utils {

	public static interface Strings {

		public static String normalice(final Object obj) {
			String str = "";
			if(obj != null) {
				str = obj.toString();
			}
			return str.trim();
		}

	}

	public static interface Files {

		public static String readFileContent(final String filepath) throws IOException {
			return new String(java.nio.file.Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);
		}

		public static int[] getImgDimension(final String imgPath) throws IOException {
			File imgFile = new File(imgPath);

			int pos = imgFile.getName().lastIndexOf(".");
			if(pos == -1)
				throw new IOException("No extension for file: " + imgFile.getAbsolutePath());
			String suffix = imgFile.getName().substring(pos + 1);
			Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
			if(iter.hasNext()) {
				ImageReader reader = iter.next();
				try {
					ImageInputStream stream = new FileImageInputStream(imgFile);
					reader.setInput(stream);
					int width = reader.getWidth(reader.getMinIndex());
					int height = reader.getHeight(reader.getMinIndex());
					return new int[] {width, height};
				} catch (IOException e) {
					throw new RuntimeException("Error reading: " + imgFile.getAbsolutePath());
				} finally {
					reader.dispose();
				}
			}
			throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
		}

	}

}
