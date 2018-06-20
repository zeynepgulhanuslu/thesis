package dataPreparation;

import com.google.common.base.Splitter;
import zemberek.core.turkish.Turkish;
import zemberek.morphology.analysis.tr.TurkishNumbers;
import zemberek.tokenization.TurkishSentenceExtractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TomographyData {

    public static void main(String[] args) throws IOException {
        splitter();
    }

    public static void splitter(){
        String s = "Ali gel. Okul açıldı.";
        TurkishSentenceExtractor extractor = TurkishSentenceExtractor.DEFAULT;
        List<String > list = extractor.fromParagraph(s);
        for (String s1 : list) {
            System.out.println(s1);
        }
        System.out.println(TurkishNumbers.convertNumberToString("-123"));
        System.out.println(Turkish.inferPronunciation("tdk"));
        System.out.println(TurkishNumbers.convertOrdinalNumberString("12."));
        System.out.println(TurkishNumbers.convertOrdinalNumberString("-12."));


    }
    private static void test() throws IOException {
        Path transcriptDir = Paths.get("/home/kdtl/Documents/tomography_test");
        List<String> transcriptList = Files.readAllLines(transcriptDir);

        Set<String> numberSet = new LinkedHashSet<>();
        for (String line : transcriptList) {

            List<String> words = Splitter.on(" ").splitToList(line);

            for (String word : words) {

                StringBuilder sb = new StringBuilder();
                if (word.matches(".*\\d+.*")){
                    if (word.endsWith(".") || word.contains(".")) {
                        char[] chars = word.replace(".", "").toCharArray();
                        for (char ch : chars) {
                            if (!Character.isDigit(ch)){
                                break;
                            }
                            if (Character.isDigit(ch)){
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
