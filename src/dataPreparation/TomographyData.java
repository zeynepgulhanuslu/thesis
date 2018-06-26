package dataPreparation;

import com.google.common.base.Splitter;
import utils.TurkishNormalizer;
import zemberek.core.turkish.Turkish;
import zemberek.morphology.analysis.tr.TurkishNumbers;
import zemberek.tokenization.TurkishSentenceExtractor;
import zemberek.tokenization.TurkishTokenizer;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TomographyData {

    public static void main(String[] args) throws IOException {
        Path inFile = Paths.get("/media/kdtl/depo/tez/tomography-data/tomografi-rapor.txt");
        Path outFile = Paths.get("/media/kdtl/depo/tez/tomography-data/tomografi-rapor/transcript");
        splitter();
    }

    public static void sentenceBoundaryDetection(Path inFile, Path outFile) throws IOException {
        if (!outFile.getParent().toFile().exists()) {
            Files.createDirectories(outFile.getParent());
        }
        TurkishSentenceExtractor extractor = TurkishSentenceExtractor.DEFAULT;
        List<String> textList = Files.readAllLines(inFile);
        String text = String.join(" ", textList);
        List<String> list = extractor.fromDocument(text);
        try (PrintWriter pw = new PrintWriter(outFile.toFile(), "UTF-8")) {
            for (String line : list) {
                List<String> outList = new ArrayList<>();
                List<String> wordList = Splitter.on(" ").splitToList(line);
                for (String word : wordList) {
                    TurkishNumbers.convertOrdinalNumberString(word);
                    TurkishNumbers.convertNumberToString(word);
                    Turkish.inferPronunciation(word);
                    outList.add(word);
                }
                pw.println(String.join(" ", outList));
            }
        }
    }

    public static void splitter() {
        String s = "T2, T1, FİESTA ağırlıklı Axial ve Koronal planlarda MR görüntüleri elde olunmuştur. " +
                "Sol böbrek orta kesimde 15 mm çapında kortikal kist mevcuttur. " +
                "Posterior fossa da 4. ventrikül normal form ve lokalizasyondadır. " +
                "C3-4 seviyesinde solda unkovertebral osteofit oluşumu izlenmiş olup sol foramina hafif daralmıştır. " +
                "L4-5 diffüz diskal taşma vardır. Bilateral nöral foramen girimi daralmıştır. ";
        TurkishSentenceExtractor extractor = TurkishSentenceExtractor.DEFAULT;
        List<String> list = extractor.fromParagraph(s);
        for (String s1 : list) {
            List<String> wordsList = Splitter.on(" ").splitToList(s1);
            for (String word : wordsList) {

                String x = TurkishNumbers.convertOrdinalNumberString(word);
                System.out.println(x);
                String y = Turkish.inferPronunciation(x);
                System.out.println(y);
                String z = TurkishNumbers.convertNumberToString(y);
                System.out.println(z);
            }
        }



    }

    private static void test() throws IOException {
        Path transcriptDir = Paths.get("/home/kdtl/Documents/tomography_test");
        List<String> transcriptList = Files.readAllLines(transcriptDir);

        Set<String> numberSet = new LinkedHashSet<>();
        for (String line : transcriptList) {

            List<String> words = Splitter.on(" ").splitToList(line);

            for (String word : words) {

                StringBuilder sb = new StringBuilder();
                if (word.matches(".*\\d+.*")) {
                    if (word.endsWith(".") || word.contains(".")) {
                        char[] chars = word.replace(".", "").toCharArray();
                        for (char ch : chars) {
                            if (!Character.isDigit(ch)) {
                                break;
                            }
                            if (Character.isDigit(ch)) {
                                sb.append(ch);
                            }
                        }
                    }

                }
                numberSet.add(sb.toString());

            }
        }

        for (String number : numberSet) {
            System.out.println(number);
        }
    }
}
