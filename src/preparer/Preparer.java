package preparer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Preparer {

	//---- Constants and Definitions ----
	private final String OUTPUT_FILE_PARAM = ":file";
	private final String OUTPUT_FILE_NAME = OUTPUT_FILE_PARAM + "_prepared.html";

	private final Set<String> excludedTags = new HashSet<>(Arrays.asList(new String[] {
			"title", "h1", "h2", "p"
	}));

	private final String PAGE_ELEMENT_CLASS_NAME = "Page";
	private final String IMAGE_CONTAINER_CLASS_NAME = "ImageContainer";

	//---- Atributtes ----
	private final boolean centerHorizontally;
	private final int screenHeight, screenWidth;

	private File inputFile;

	//---- Constructor ----
	public Preparer(final int screenHeight) {
		this(-1, screenHeight, false);
	}

	public Preparer(final int screenWidth, final int screenHeight) {
		this(screenWidth, screenHeight, screenWidth >= 0);
	}

	private Preparer(final int screenWidth, final int screenHeight, final boolean centerHorizontally) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.centerHorizontally = centerHorizontally;
	}

	//---- Methods ----
	public void prepare(final String pathHtmlFile) throws IOException {
		this.inputFile = new File(pathHtmlFile);

		String outputPath = constructOutputFilePath();
		String outputContent = constructOutputFileContent();

		writePreparedHtmlFile(outputPath, outputContent);
	}

	private String constructOutputFilePath() {
		String fileInName = inputFile.getName();
		String fileInNameWithoutExtension = fileInName.substring(0, fileInName.lastIndexOf('.'));

		String path = inputFile.getAbsolutePath().replace(fileInName, "");
		String outputFileName = OUTPUT_FILE_NAME.replace(OUTPUT_FILE_PARAM, fileInNameWithoutExtension);

		return path + outputFileName;
	}

	private String constructOutputFileContent() throws IOException {
		Document docHtml = Jsoup.parse(inputFile, StandardCharsets.UTF_8.name());

		deleteUnusedTags(docHtml);
		centerPages(docHtml);

		return docHtml.toString();
	}
	private void deleteUnusedTags(final Document docHtml) {
		for(String tag : excludedTags) {
			deleteTagFromDocument(docHtml, tag);
		}
	}

	private void deleteTagFromDocument(final Document docHtml, final String tag) {
		Elements elements = docHtml.getElementsByTag(tag);
		for(Element element : elements) {
			element.remove();
		}
	}

	private void centerPages(final Document docHtml) {
		Elements pages = docHtml.getElementsByClass(PAGE_ELEMENT_CLASS_NAME);
		for(Element page : pages) {
			centerPage(page);
		}
	}

	private void centerPage(final Element page) {
		Element imgContainer = page.getElementsByClass(IMAGE_CONTAINER_CLASS_NAME).first();
		Dimension imgDimensions;
		try {
			imgDimensions = getImgDimension(imgContainer);
			centerPageVertically(page, imgDimensions);
			centerPageHorizontally(page, imgDimensions);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Dimension getImgDimension(final Element imgContainer) throws IOException {
		String imgPath = imgContainer.getElementsByTag("img").first().attr("src");
		File imgFile = new File(inputFile.getParent() + "/" + imgPath);

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
				return new Dimension(width, height);
		  } catch (IOException e) {
			  System.err.println("Error reading: " + imgFile.getAbsolutePath());
		  } finally {
			  reader.dispose();
		  }
		}
		throw new IOException("Not a known image file: " + imgFile.getAbsolutePath());
	}

	private void centerPageVertically(final Element page, final Dimension imgDimensions) {
		int padding = calculateSpacing(screenHeight, imgDimensions.getHeight());
		addStyleToElement(page, "padding-top: " + padding + "; padding-bottom: " + padding);
	}

	private void centerPageHorizontally(final Element page, final Dimension imgDimensions) {
		if(centerHorizontally) {
			int margin = calculateSpacing(screenWidth, imgDimensions.getWidth());
			addStyleToElement(page, "margin-left: " + margin + "; margin-right: " + margin);
		}
	}

	private int calculateSpacing(final int screenPx, final int imgPx) {
		return screenPx <= imgPx ? 0 : (screenPx - imgPx) / 2;
	}

	private void addStyleToElement(final Element element, final String style) {
		String currentStyle = element.attr("style");
		currentStyle = (currentStyle.isEmpty() || currentStyle.trim().endsWith(";")) ? currentStyle : currentStyle.concat(";");
		element.attributes().put("style", currentStyle + style);
	}

	private void writePreparedHtmlFile(final String path, final String content) throws IOException {
		Files.write(Paths.get(path), content.getBytes());
	}

}
