package utils;

import java.util.HashMap;
import java.util.Map;

public class TurkishNormalizer {

    private static final Map<Character, String> mapMorfNumber;

    static {
        mapMorfNumber = new HashMap<>();
        mapMorfNumber.put('0', "sıfır");
        mapMorfNumber.put('1', "bir");
        mapMorfNumber.put('2', "iki");
        mapMorfNumber.put('3', "üç");
        mapMorfNumber.put('4', "dört");
        mapMorfNumber.put('5', "beş");
        mapMorfNumber.put('6', "altı");
        mapMorfNumber.put('7', "yedi");
        mapMorfNumber.put('8', "sekiz");
        mapMorfNumber.put('9', "dokuz");
    }
    private static final Map<String, String> ordinalMaps;
    static{
        ordinalMaps = new HashMap<>();
        ordinalMaps.put("1.", "birinci");
        ordinalMaps.put("2.", "ikinci");
        ordinalMaps.put("3.", "üçüncü");
        ordinalMaps.put("4.", "dördüncü");
        ordinalMaps.put("5.", "beşinci");
        ordinalMaps.put("6.", "altıncı");
        ordinalMaps.put("7.", "yedinci");
        ordinalMaps.put("8.", "sekizinci");
        ordinalMaps.put("9.", "dokuzuncu");
        ordinalMaps.put("10.", "onuncu");
        ordinalMaps.put("11.", "on birinci");
        ordinalMaps.put("12.", "on ikinci");
        ordinalMaps.put("13.", "on üçüncü");
        ordinalMaps.put("14.", "on dördüncü");
        ordinalMaps.put("15.", "on beşinci");
        ordinalMaps.put("28.", "yirmi sekizinci");
        ordinalMaps.put("30.", "otuzuncu");
        ordinalMaps.put("89.", "seksen dokuzuncu");
    }
    public static void normalizeNumbers(){

    }

    public static String cleanPunctuations(String word){
        word = word.replaceAll("[_,.?!:;()\"]", " ");
        word = word.replaceAll("['$~\\-*^]", "");
        return word.replaceAll("\\s+", " ").trim();
    }
}
