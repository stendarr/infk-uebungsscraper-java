package uebungsscraper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

// "You know, we are all hackers, for us the source code is the documentation!" 
// - Gustavo Alonso, Vorlesung Parallel and Distributed Databases (Datum unbekannt)

public class Main {
	static String output;
	static boolean outputEnable = true;

	public static void main(String[] args) throws IOException {
		AtomicInteger linkCounter = new AtomicInteger();
		AtomicInteger downloadCounter = new AtomicInteger();

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
				if (downloadFromTag(tag, "AD/Programmieraufgaben/", adBuilderUrl)) {
					downloadCounter.incrementAndGet();
				}
				linkCounter.incrementAndGet();
			} else {
				if (!sTag.contains("graphentheorie.pdf") && !sTag.contains("skript.pdf") && (!sTag.contains("book"))) {
					if (downloadFromTag(tag, "AD/Uebungen/", adBuilderUrl)) {
						downloadCounter.incrementAndGet();
					}
					linkCounter.incrementAndGet();
				}
			}
		}
		// Notes
		String adnRelevantHtml = adHtml.split("<table id=\"vorlesung\">", 2)[1].split("<h3>Literatur zur", 2)[0];
		ArrayList<Element> adnRelevantTags = Jsoup.parse(adnRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : adnRelevantTags) {
			String sTag = tag.toString();
			if (!(sTag.contains("index.html") || sTag.contains("moodle") || sTag.contains("disclaimer.html")
					|| sTag.contains("copyright.html") || sTag.contains("skript.pdf") || sTag.contains("watch"))) {
				if (downloadFromTag(tag, "AD/Slides/", adBuilderUrl)) {
					downloadCounter.incrementAndGet();
				}
				linkCounter.incrementAndGet();
			}
		}
		// END AnD

		// START DiskMath
		String dmMasterUrl = "http://www.crypto.ethz.ch/teaching/lectures/DM18/";
		String dmHtml = "";
		try {
			dmHtml = readStringFromURL(dmMasterUrl);
			String dmRelevantHtml = dmHtml.split("tter</h3>", 2)[1].split("bungsgruppen</h3", 2)[0];
			ArrayList<Element> dmRelevantTags = Jsoup.parse(dmRelevantHtml, "UTF-8").select("a[href]");
			for (Element tag : dmRelevantTags) {
				if (downloadFromTag(tag, "DiskMat/Uebungen/", dmMasterUrl)) {
					downloadCounter.incrementAndGet();
				}
				linkCounter.incrementAndGet();
			}
		} catch (Exception e) {
			if (e.toString().contains("500")) {
				System.out.println("\n[Exception DiskMat] 500 They fucked up - Try again later\n");
			}
		}
		// END DiskMath

		// START EProg
		String epMasterUrl = "http://www.lst.inf.ethz.ch/education/einfuehrung-in-die-programmierung-i--252-0027-.html";
		String epRelevantHtml = "";
		int countTries = 0;
		while (true) {
			try {
				epRelevantHtml = readStringFromURL(epMasterUrl).split("e contains-textimage\">", 2)[1]
						.split("<a class=\"accordionAnchor\" href=\"#\"><span class=\"title\">Lit", 2)[0];
				break;
			} catch (FileNotFoundException e) {
				if (++countTries == 4) {
					System.out.println("\n===== [EProg failed badly - usually you can fix this by trying again] =====\n"
							+ e + "\n");
					break;
				}
			}
		}
		ArrayList<Element> epRelevantTags = Jsoup.parse(epRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : epRelevantTags) {
			if (tag.toString().contains("18E")) {
				if (downloadFromTag(tag, "EProg/Slides/", "")) {
					downloadCounter.incrementAndGet();
				}
				linkCounter.incrementAndGet();
			} else if (tag.attr("href").toString().contains("#")) {
			} else {
				if (downloadFromTag(tag, "EProg/Uebungen/", "")) {
					downloadCounter.incrementAndGet();
				}
				linkCounter.incrementAndGet();
			}
		}
		// END EProg

		// START LinAlg
		// Exercises
		String laMasterUrl = "http://igl.ethz.ch/teaching/linear-algebra/la2018/";
		String laHtml = readStringFromURL(laMasterUrl);
		String laRelevantHtml = laHtml.split("bungen</h3>", 2)[1].split("bungsgruppen</h3>", 2)[0];
		ArrayList<Element> laRelevantTags = Jsoup.parse(laRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : laRelevantTags) {
			if (downloadFromTag(tag, "LinAlg/Uebungen/", laMasterUrl)) {
				downloadCounter.incrementAndGet();
			}
			linkCounter.incrementAndGet();
		}
		String lanRelevantHtml = laHtml.split("bersicht der Vorlesung</h3>", 2)[1]
				.split("<p><a class=\"a-int\" href=\"https://www.vid", 2)[0];
		ArrayList<Element> lanRelevantTags = Jsoup.parse(lanRelevantHtml, "UTF-8").select("a[href]");
		for (Element tag : lanRelevantTags) {
			if (downloadFromTag(tag, "LinAlg/Notes/", laMasterUrl)) {
				downloadCounter.incrementAndGet();
			}
			linkCounter.incrementAndGet();
		}
		// END LinAlg

		// Print results - end of program
		if (outputEnable) {
			System.out.println(output);
		}
		String[] thankyous = { "Thank you for flying Emirates!",
				"Thank you for flying Delta Business Express. We hope you enjoyed giving us the business as much as we enjoyed taking you for a ride.",
				"Thank you for smoking!", "tank you come again",
				"I know where you live " + System.getProperty("user.name"), "All your exercises are belong to us!" };
		System.out.println();
		System.out.println(linkCounter.get() + " links discovered and " + downloadCounter.get() + " downloaded.");
		System.out.println(thankyous[new Random().nextInt(thankyous.length)]);
	}

	public static boolean downloadFromTag(Element tag, String directory, String builderUrl) throws IOException {
		String link = tag.attr("href");
		String[] tmp = link.split("/");
		String filename = directory + tmp[tmp.length - 1];
		URL url = new URL((builderUrl + link).replace(" ", "%20"));
		if (url.toString().replaceFirst("http", "").contains("http")) {
			return false;
		}
		File file = new File(filename);
		if (!new File(filename).isFile()) {
			try {
				FileUtils.copyURLToFile(url, file);
				System.out.println("[Downloaded " + directory.split("/")[0] + "] " + url + " into " + filename);
				return true;
			} catch (IOException e) {
				System.out.println("\n[Failed " + directory.split("/")[0] + "] " + url);
				if (e.toString().contains("403")) {
					System.out.println(
							"[Exception] 403 You fucked up - Please connect to the ETH Network or VPN and try again");
					return false;
				} else if (e.toString().contains("500")) {
					System.out.println("[Exception] 500 They fucked up - Try again later");
					return false;
				}
				System.out.println("[Exception]\n" + e + "\n");
				return false;
			}
		} else {
			output += "[Skipped " + directory.split("/")[0] + "] " + url + "\n";
			return false;
		}
	}

	public static String readStringFromURL(String requestURL) throws IOException {
		try (Scanner scanner = new Scanner(new URL(requestURL).openStream(), StandardCharsets.UTF_8.toString())) {
			scanner.useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
//TODO remove redundant function 
	public static boolean inEthNetwork() {

		return false;
	}
}
