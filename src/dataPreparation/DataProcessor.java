package dataPreparation;

import com.google.common.base.Splitter;
import utils.TurkishNormalizer;
import utils.WavTool;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessor {
    Path outDataDir;
    List<TranscriptData> transcriptDataList;

    public DataProcessor(Path outDataDir, List<TranscriptData> transcriptDataList) {
        this.outDataDir = outDataDir;
        this.transcriptDataList = transcriptDataList;
    }

    public static class TranscriptData {
        public String uttID, callID, speakerID, startTime, endTime, sentence, wavPath;

        public TranscriptData(String uttID, String callID, String speakerID, String startTime,
                              String endTime, String sentence, String wavPath) {
            this.uttID = uttID;
            this.callID = callID;
            this.speakerID = speakerID;
            this.startTime = startTime;
            this.endTime = endTime;
            this.sentence = sentence;
            this.wavPath = wavPath;
        }

        @Override
        public String toString() {
            return "TranscriptData{" +
                    "uttID='" + uttID + '\'' +
                    ", callID='" + callID + '\'' +
                    ", speakerID='" + speakerID + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", sentence='" + sentence + '\'' +
                    ", wavPath='" + wavPath + '\'' +
                    '}';
        }

        public String getSpeakerID() {
            return speakerID;
        }

        public String getUttID() {
            return uttID;
        }
    }

    public static List<TranscriptData> loadData(List<Path> sourceDataDir, Path outDir) throws Exception {

        if (!outDir.toFile().exists()) {
            Files.createDirectories(outDir);
        }
        List<Path> transcriptPaths = new ArrayList<>();
        List<Path> wavPaths = new ArrayList<>();
        for (Path path : sourceDataDir) {
            transcriptPaths.addAll(Files.walk(path, Integer.MAX_VALUE).filter(s -> s.toFile().isFile()
                    && s.toFile().getName().equals("transcription")).collect(Collectors.toList()));
            wavPaths.addAll(Files.walk(path, Integer.MAX_VALUE).filter(s -> s.toFile().isFile()
                    && s.toFile().getName().endsWith(".wav")).collect(Collectors.toList()));
        }

        Map<String, Path> wavMap = new TreeMap<>();

        for (Path path : wavPaths) {
            String name = path.toFile().getName().replace(".wav", "");
            wavMap.put(name, path);
        }
        List<TranscriptData> transcriptDataList = new ArrayList<>();
        for (Path transcriptPath : transcriptPaths) {
            int id = 1;
            System.out.println(transcriptPath + " file is loading");
            List<String> transcriptList = Files.readAllLines(transcriptPath);
            List<TranscriptData> tr = new ArrayList<>();
            for (String line : transcriptList) {
                if (line.isEmpty()) {
                    continue;
                }
                line = line.trim().replace("<s>", "");
                line = line.trim().replace("</s>", "");

                List<String> list = Splitter.on(" ").splitToList(line.trim());

                String callID = list.get(list.size() - 1).
                        replace("(", "").replace(")", "");

                String startTime = "0.0";
                if (!wavMap.keySet().contains(callID)) {
                    System.out.println(callID + " wav file not exist");
                    continue;
                }

                String endTime = String.format("%.4f", WavTool.getDurationInSeconds(wavMap.get(callID).toFile()));
                List<String> elements = list.subList(0, list.size() - 1);
                String sentence = TurkishNormalizer.cleanPunctuations(String.join(" ", elements));
                String uttID = callID + "-" + String.format("%04d", id);
                TranscriptData transcriptData = new TranscriptData(uttID, callID, uttID, startTime,
                        endTime, sentence, wavMap.get(callID).toString());

                if (!transcriptData.sentence.isEmpty()) {
                    tr.add(transcriptData);
                }
                id++;
            }
            transcriptDataList.addAll(tr);
        }
        transcriptDataList.sort(Comparator.comparing(TranscriptData::getUttID));
        return transcriptDataList;
    }

    public static List<TranscriptData> loadData(Path sourceDataDir, Path outDir, Path testListFile, boolean isTrain) throws Exception {

        if (!outDir.toFile().exists()) {
            Files.createDirectories(outDir);
        }

        List<Path> transcriptPaths = Files.walk(sourceDataDir, Integer.MAX_VALUE).filter(s -> s.toFile().isFile()
                && s.toFile().getName().equals("transcription")).collect(Collectors.toList());
        List<Path> wavPaths = Files.walk(sourceDataDir, Integer.MAX_VALUE).filter(s -> s.toFile().isFile()
                && s.toFile().getName().endsWith(".wav")).collect(Collectors.toList());


        Map<String, Path> wavMap = new TreeMap<>();

        for (Path path : wavPaths) {
            String name = path.toFile().getName().replace(".wav", "");
            wavMap.put(name, path);
        }

        Set<String> testSet = new LinkedHashSet<>(Files.readAllLines(testListFile));
        List<TranscriptData> transcriptDataList = new ArrayList<>();
        int countTest = 0;
        for (Path transcriptPath : transcriptPaths) {
            int id = 1;
            System.out.println(transcriptPath + " file is loading");
            List<String> transcriptList = Files.readAllLines(transcriptPath);
            List<TranscriptData> tr = new ArrayList<>();
            for (String line : transcriptList) {
                if (line.isEmpty()) {
                    continue;
                }
                line = line.trim().replace("<s>", "");
                line = line.trim().replace("</s>", "");

                List<String> list = Splitter.on(" ").splitToList(line.trim());

                String callID = list.get(list.size() - 1).
                        replace("(", "").replace(")", "");

                if (isTrain){
                    if (testSet.contains(callID)){
                        System.out.println(callID + " file is in test set so skipping");
                        countTest++;
                        continue;
                    }
                }else {
                    if (!testSet.contains(callID)){
                        continue;
                    }
                }
                String startTime = "0.0";

                if (!wavMap.keySet().contains(callID)) {
                    System.out.println(callID + " wav file not exist");
                    continue;
                }

                String endTime = String.format("%.4f", WavTool.getDurationInSeconds(wavMap.get(callID).toFile()));

                List<String> elements = list.subList(0, list.size() - 1);
                String sentence = TurkishNormalizer.cleanPunctuations(String.join(" ", elements));
                String uttID = callID + "-" + String.format("%04d", id);
                float end = Float.parseFloat(endTime);
                float start = Float.parseFloat(startTime);
                if (end <= start) {
                    System.out.printf("%s utterance end time %f is equal or less than %f start time.Skipping",
                            uttID, end, start);
                    continue;
                }

                float duration = end - start;
                if (duration < 0.2) {
                    System.out.printf("%s utterance has small duration %f. Skipping", callID, duration);
                    continue;
                }
                if (duration > end) {
                    System.out.printf("%s segment duration %f bigger then %f end time.Skipping", callID, duration, end);
                    continue;
                }
                TranscriptData transcriptData = new TranscriptData(uttID, callID, uttID, startTime,
                        endTime, sentence, wavMap.get(callID).toString());

                if (!transcriptData.sentence.isEmpty()) {
                    tr.add(transcriptData);
                }
                id++;
            }
            transcriptDataList.addAll(tr);
        }
        System.out.println(countTest);
        transcriptDataList.sort(Comparator.comparing(TranscriptData::getUttID));
        return transcriptDataList;
    }

    private void generateWavScpFile() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter pw = new PrintWriter(outDataDir.resolve("wav.scp").toFile(), "UTF-8")) {
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.callID + " " + transcriptData.wavPath);
            }
        }
    }

    private void generateCorpusFile() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter pw = new PrintWriter(outDataDir.resolve("corpus").toFile(), "UTF-8")) {
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.sentence);
            }
        }
    }

    private void generateSpeakerFiles() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter pwUtt = new PrintWriter(outDataDir.resolve("utt2spk").toFile(), "UTF-8");
             PrintWriter pwSpk = new PrintWriter(outDataDir.resolve("spk2utt").toFile(), "UTF-8")) {

            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pwUtt.println(transcriptData.uttID + " " + transcriptData.speakerID);
            }

            Map<String, List<DataProcessor.TranscriptData>> bySpeaker = transcriptDataList.stream().
                    collect(Collectors.groupingBy(DataProcessor.TranscriptData::getSpeakerID));

            bySpeaker = bySpeaker.entrySet().stream().sorted(Map.Entry.comparingByKey()).
                    collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            for (String key : bySpeaker.keySet()) {
                List<DataProcessor.TranscriptData> list = bySpeaker.get(key);
                List<String> uttList = list.stream().map(s -> s.uttID).collect(Collectors.toList());
                String utt = String.join(" ", uttList);
                pwSpk.println(key + " " + utt);
            }
        }
    }

    private void generateSegmentFile() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter pw = new PrintWriter(outDataDir.resolve("segments").toFile(), "UTF-8")) {
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.uttID + " " + transcriptData.callID + " " +
                        transcriptData.startTime + " " + transcriptData.endTime);
            }
        }
    }

    private void generateTextFile() throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter pw = new PrintWriter(outDataDir.resolve("text").toFile(), "UTF-8")) {
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.uttID + " " + transcriptData.sentence);
            }
        }
    }

    private static void generateDataFiles(Path outDataDir, List<DataProcessor.TranscriptData> transcriptDataList)
            throws FileNotFoundException, UnsupportedEncodingException {
        DataProcessor dp = new DataProcessor(outDataDir, transcriptDataList);
        dp.generateCorpusFile();
        dp.generateSegmentFile();
        dp.generateSpeakerFiles();
        dp.generateTextFile();
        dp.generateWavScpFile();

    }

    public static void main(String[] args) throws Exception {

        Path dataDir = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/");
        Path outDir = Paths.get("/media/kdtl/depo/tez/main/data/train");
        Path testListFile = Paths.get("/media/kdtl/depo/tez/main/test-calls");

        List<TranscriptData> transcriptDataList = loadData(dataDir, outDir, testListFile, true);
        generateDataFiles(outDir, transcriptDataList);
    }
}
