package uebungsscraper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class Main {

	public static void main(String[] args) throws IOException {
		int linkCounter = 0;
		int downloadCounter = 0;

		String userAgent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36";

		// START AnD
		// Exercises
		String adMasterUrl = "https://www.cadmo.ethz.ch/education/lectures/HS18/DA/index.html";
		String adBuilderUrl = "https://www.cadmo.ethz.ch/education/lectures/HS18/DA/";
		String adHtml = readStringFromURL(adMasterUrl);
		String adRelevantHtml = adHtml.split("4. September.</em></p>", 2)[1].split("<h3>Fragen und Kommentare</h3>",
				2)[0];
		ArrayList<Element> adRelevantTags = Jsoup.parse(adRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : adRelevantTags) {
			String sTag = tag.toString();
			if (sTag.contains("Programmieraufgaben") || sTag.contains("Vorlage") || sTag.contains("Anleitung")) {
				downloadCounter += downloadFromTag(tag, "AD/Programmieraufgaben/", adBuilderUrl) ? 1 : 0;
				linkCounter += 1;
			} else {
				if (!sTag.contains("skript.pdf")) {
					downloadCounter += downloadFromTag(tag, "AD/Uebungen/", adBuilderUrl) ? 1 : 0;
					linkCounter += 1;
				}
			}
		}
		// Notes
		String adnRelevantHtml = adHtml.split("<table id=\"vorlesung\">", 2)[1].split("<h3>Literatur zur", 2)[0];
		ArrayList<Element> adnRelevantTags = Jsoup.parse(adnRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : adnRelevantTags) {
			String sTag = tag.toString();
			if (!(sTag.contains("index.html") || sTag.contains("moodle") || sTag.contains("disclaimer.html")
					|| sTag.contains("copyright.html") || sTag.contains("skript.pdf"))) {
				downloadCounter += downloadFromTag(tag, "AD/Slides/", adBuilderUrl) ? 1 : 0;
				linkCounter += 1;
			}
		}
		// END AnD

		// START DiskMath
		String dmMasterUrl = "http://www.crypto.ethz.ch/teaching/lectures/DM18/";
		String dmRelevantHtml = readStringFromURL(dmMasterUrl).split("tter</h3>", 2)[1].split("bungsgruppen</h3", 2)[0];
		ArrayList<Element> dmRelevantTags = Jsoup.parse(dmRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : dmRelevantTags) {
			downloadFromTag(tag, "DiskMat/Uebungen/", dmMasterUrl);
		}
		// END DiskMath

		// START EProg
		String epMasterUrl = "http://www.lst.inf.ethz.ch/education/einfuehrung-in-die-programmierung-i--252-0027-.html";
		String epRelevantHtml = readStringFromURL(epMasterUrl).split("e contains-textimage\">", 2)[1]
				.split("<a class=\"accordionAnchor\" href=\"#\"><span class=\"title\">Lit", 2)[0];
		ArrayList<Element> epRelevantTags = Jsoup.parse(epRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : epRelevantTags) {
			if (tag.toString().contains("18E")) {
				downloadCounter += downloadFromTag(tag, "EProg/Slides/", "") ? 1 : 0;
				;
				linkCounter += 1;
			} else if (tag.attr("href").toString().contains("#")) {
			} else {
				downloadCounter += downloadFromTag(tag, "EProg/Uebungen/", "") ? 1 : 0;
				linkCounter += 1;
			}
		}
		// END EProg

		// START LinAlg
		// Exercises
		String laMasterUrl = "http://igl.ethz.ch/teaching/linear-algebra/la2018/";
		String laHtml = readStringFromURL(laMasterUrl);
		String laRelevantHtml = laHtml.split("bungen</h3>", 2)[1].split("<h3>Übungsgruppen</h3>", 2)[0];
		ArrayList<Element> laRelevantTags = Jsoup.parse(laRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : laRelevantTags) {
			downloadFromTag(tag, "LinAlg/Uebungen/", laMasterUrl);
		}
		String lanRelevantHtml = laHtml.split("bersicht der Vorlesung</h3>", 2)[1]
				.split("<p><a class=\"a-int\" href=\"https://www.vid", 2)[0];
		ArrayList<Element> lanRelevantTags = Jsoup.parse(lanRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : lanRelevantTags) {
			downloadFromTag(tag, "LinAlg/Notes/", laMasterUrl);
		}
		// END LinAlg

		System.out.println();
		System.out.println(linkCounter + " links discovered and " + downloadCounter + " downloaded.");
		System.out.println("Thank you for flying Emirates!");

	}

	public static boolean downloadFromTag(Element tag, String directory, String builderUrl) throws IOException {
		String link = tag.attr("href");
		String[] tmp = link.split("/");
		String filename = directory + tmp[tmp.length - 1];
		URL url = new URL((builderUrl + link).replace(" ", "%20"));
		File file = new File(filename);
		if (!new File(filename).isFile()) {
			FileUtils.copyURLToFile(url, file);
			System.out.println("[Downloaded " + directory.split("/")[0] + "] " + url + " into " + filename);
			return true;
		} else {
			System.out.println("[Skipped " + directory.split("/")[0] + "] " + url);
			return false;
		}
	}

	public static String readStringFromURL(String requestURL) throws IOException {
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}
	}

}
