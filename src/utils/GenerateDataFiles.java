package utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.poi.hwpf.model.Sttb;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GenerateDataFiles {


    public static void generateSegmentFiles(List<Path> segmentList, Path segmentFile) throws IOException {
        if (!segmentFile.getParent().toFile().exists()) {
            Files.createDirectories(segmentFile.getParent());
        }
        try (PrintWriter pw = new PrintWriter(segmentFile.toFile(), "UTF-8")) {
            Set<String> segmentSet = new LinkedHashSet<>();
            for (Path path : segmentList) {
                List<String> list = Files.readAllLines(path);
                segmentSet.addAll(list);
            }

            List<String> segmentSortedList = new ArrayList<>(segmentSet);
            Collections.sort(segmentSortedList);

            for (String line : segmentSortedList) {
                pw.println(line);
            }
        }
    }

    public static void generateTextFile(List<Path> textList, Path transcriptFile) throws IOException {
        if (!transcriptFile.getParent().toFile().exists()) {
            Files.createDirectories(transcriptFile.getParent());
        }
        try (PrintWriter pw = new PrintWriter(transcriptFile.toFile(), "UTF-8")) {
            Set<String> textSegment = new LinkedHashSet<>();
            for (Path path : textList) {
                List<String> list = Files.readAllLines(path);
                textSegment.addAll(list);
            }

            List<String> textSortedList = new ArrayList<>(textSegment);
            Collections.sort(textSortedList);

            for (String line : textSortedList) {
                pw.println(line);
            }
        }
    }

    public static void generateWavScpFile(List<Path> wavScpFiles, Path outWavScpFile) throws IOException {
        if (!outWavScpFile.getParent().toFile().exists()) {
            Files.createDirectories(outWavScpFile.getParent());
        }

        try (PrintWriter pw = new PrintWriter(outWavScpFile.toFile(), "UTF-8")) {
            Set<String> wavScpSet = new LinkedHashSet<>();
            for (Path wavScpFile : wavScpFiles) {
                List<String> list = Files.readAllLines(wavScpFile);
                wavScpSet.addAll(list);
            }
            List<String> sortedWavScpList = new ArrayList<>(wavScpSet);
            Collections.sort(sortedWavScpList);
            for (String line : sortedWavScpList) {
                pw.println(line);
            }
        }
    }

    public static void generateSpeakerFiles(Path segmentFile, Path outDir) throws IOException {
        if (!outDir.toFile().exists()) {
            Files.createDirectories(outDir);
        }

        List<String> segmentList = Files.readAllLines(segmentFile);
        List<String> uttList = new ArrayList<>();
        for (String line : segmentList) {
            List<String> list = Splitter.on(" ").trimResults().splitToList(line);
            uttList.add(list.get(0));
        }

        Collections.sort(uttList);

        try (PrintWriter pwSpk = new PrintWriter(outDir.resolve("spk2utt").toFile(), "UTF-8");
             PrintWriter pwUtt = new PrintWriter(outDir.resolve("utt2spk").toFile(), "UTF-8")) {
            for (String uttID : uttList) {
                pwSpk.println(uttID + " " + uttID);
                pwUtt.println(uttID + " " + uttID);
            }
        }
    }

    public static void generateCorpus(Path dataDir) throws IOException {
        List<String> textList = Files.readAllLines(dataDir.resolve("text"));
        int count = 0;
        try(PrintWriter pw = new PrintWriter(dataDir.resolve("corpus").toFile(), "UTF-8")){
            for (String line : textList) {
                String[] tokens = line.split("\\s+");
                if (tokens.length >= 2){
                    line = line.replace(tokens[0], "");
                    pw.println(line.trim());
                }else {
                    count++;
                }
            }
        }
        System.out.println("empty line : " + count);
    }
    public static void main(String[] args) throws IOException {
        Path sourceDir = Paths.get("/media/kdtl/depo/tez/main/data/test");
        List<Path> textList = Lists.newArrayList(
                sourceDir.resolve("segments"));

//        Path textFile = Paths.get("/media/kdtl/depo/tez/main/data/test/segments");
//        generateSegmentFiles(textList, textFile);

//        Path segmentFile = Paths.get("/media/kdtl/depo/tez/main/data/test/segments");
//        generateSpeakerFiles(segmentFile, segmentFile.getParent());

        generateCorpus(sourceDir);
    }

}

