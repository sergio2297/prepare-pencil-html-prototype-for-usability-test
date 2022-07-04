package preparer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.Utils;

public class Preparer {

	//---- Constants and Definitions ----
	private final String OUTPUT_FILE_PARAM = ":file";
	private final String OUTPUT_FILE_NAME = OUTPUT_FILE_PARAM + "_prepared.html";

	private final Set<String> excludedTags = new HashSet<>(Arrays.asList(new String[] {
			"title", "link", "h1", "h2", "p"
	}));

	private final String PAGE_ELEMENT_CLASS_NAME = "Page";
	private final String IMAGE_CONTAINER_CLASS_NAME = "ImageContainer";

	private final int BROWSER_HEADER_HEIGHT_PX = 100;
	private final int OS_BOTTOM_TOOLBAR_HEIGHT_PX = 43;

	//---- Atributtes ----
	private final Dimension screenResolution;
	private final Configuration configuration;

	private File inputFile;

	//---- Constructor ----
	public Preparer(final Resolutions resolution, final Configuration configuration) {
		this.screenResolution = resolution.getDimension();
		this.configuration = configuration;
	}

	public Preparer(final int screenWidth, final int screenHeight, final Configuration configuration) {
		this.screenResolution = new Dimension(screenWidth, screenHeight);
		this.configuration = configuration;
	}

	//---- Methods ----
	public String prepare(final String pathHtmlFile) {
		this.inputFile = loadInputFile(pathHtmlFile);

		String outputPath = constructOutputFilePath();
		String outputContent = constructOutputFileContent();

		writePreparedHtmlFile(outputPath, outputContent);

		return outputPath;
	}

	private File loadInputFile(final String pathHtmlFile) {
		if(pathHtmlFile == null)
			throw new IllegalArgumentException("Error. The html file's path can't be null");

		File inputFile = new File(pathHtmlFile);

		if(!inputFile.exists())
			throw new PreparerException("Error. File '" + pathHtmlFile + "' not found");

		Optional<String> fileExtension = getFileExtension(inputFile.getName());
		if(!inputFile.isFile() || !fileExtension.isPresent() || !fileExtension.get().equals("html"))
			throw new PreparerException("Error. The path '" + pathHtmlFile + "' isn't correspond to a html file");

		return inputFile;
	}

	public Optional<String> getFileExtension(final String filename) {
	    return Optional.ofNullable(filename)
	      .filter(f -> f.contains("."))
	      .map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

	private String constructOutputFilePath() {
		String fileInName = inputFile.getName();
		String fileInNameWithoutExtension = fileInName.substring(0, fileInName.lastIndexOf('.'));

		String path = inputFile.getAbsolutePath().replace(fileInName, "");
		String outputFileName = OUTPUT_FILE_NAME.replace(OUTPUT_FILE_PARAM, fileInNameWithoutExtension);

		return path + outputFileName;
	}

	private String constructOutputFileContent() {
		Document docHtml = transformInputToHtmlDocument();

		deleteUnusedTags(docHtml);
		centerPages(docHtml);

		return docHtml.toString();
	}

	private Document transformInputToHtmlDocument() {
		try {
			return Jsoup.parse(inputFile, StandardCharsets.UTF_8.name());
		} catch(IOException ex) {
			throw new PreparerException("Error trying to transform the input file (" + inputFile.getAbsolutePath() + ") as a Jsoup Document.");
		}
	}

	private void deleteUnusedTags(final Document docHtml) {
		if(!configuration.removeStyles()) {
			excludedTags.remove("link");
		}

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
		Dimension imgDimensions = getImgDimension(imgContainer);

		if(configuration.centerVertically()) {
			centerPageVertically(page, imgDimensions);
		}

		if(configuration.centerHorizontally()) {
			centerPageHorizontally(page, imgDimensions);
		}
	}

	private Dimension getImgDimension(final Element imgContainer) {
		String imgRelativePath = imgContainer.getElementsByTag("img").first().attr("src");
		String parentDirectory = Utils.Strings.normalice(inputFile.getParent());
		String imgAbsolutePath = (parentDirectory.isEmpty() ? "" : parentDirectory + File.separator) + imgRelativePath;

		try {
			int[] dimensions = Utils.Files.getImgDimension(imgAbsolutePath);
			return new Dimension(dimensions[0], dimensions[1]);
		} catch (Exception ex) {
			throw new PreparerException("Error when trying to get dimensions from image (" + imgAbsolutePath + ").");
		}
	}

	private void centerPageVertically(final Element page, final Dimension imgDimensions) {
		int padding = calculateSpacing(screenResolution.getHeight(), imgDimensions.getHeight());
		int paddingTop = padding, paddingBottom = padding;

		if(!configuration.fullscreenModeWillBeUsed()) {
			paddingTop -= BROWSER_HEADER_HEIGHT_PX;
			paddingBottom += OS_BOTTOM_TOOLBAR_HEIGHT_PX;
		}

		addStyleToElement(page, "padding-top: " + paddingTop + "; padding-bottom: " + paddingBottom);
	}

	private void centerPageHorizontally(final Element page, final Dimension imgDimensions) {
		int margin = calculateSpacing(screenResolution.getWidth(), imgDimensions.getWidth());
		addStyleToElement(page, "margin-left: " + margin + "; margin-right: " + margin);
	}

	private int calculateSpacing(final int screenPx, final int imgPx) {
		return screenPx <= imgPx ? 0 : (screenPx - imgPx) / 2;
	}

	private void addStyleToElement(final Element element, final String style) {
		String currentStyle = element.attr("style");
		currentStyle = (currentStyle.isEmpty() || currentStyle.trim().endsWith(";")) ? currentStyle : currentStyle.concat(";");
		element.attributes().put("style", currentStyle + style);
	}

	private void writePreparedHtmlFile(final String path, final String content) {
		try {
			Files.write(Paths.get(path), content.getBytes());
		} catch (IOException ex) {
			throw new PreparerException("Error when trying to write the preparer html file");
		}
	}

}
