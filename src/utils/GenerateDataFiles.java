package utils;

import dataPreparation.DataProcessor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenerateDataFiles {

    Path outDataDir;
    List<DataProcessor.TranscriptData> transcriptDataList;

    public GenerateDataFiles(Path outDataDir, List<DataProcessor.TranscriptData> transcriptDataList) {
        this.outDataDir = outDataDir;
        this.transcriptDataList = transcriptDataList;
    }

    public void generateWavScpFile() throws FileNotFoundException, UnsupportedEncodingException {
        try(PrintWriter pw = new PrintWriter(outDataDir.resolve("wav.scp").toFile(), "UTF-8")){
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.callID + " " + transcriptData.wavPath);
            }
        }
    }

    public void generateCorpusFile() throws FileNotFoundException, UnsupportedEncodingException {
        try(PrintWriter pw = new PrintWriter(outDataDir.resolve("corpus").toFile(), "UTF-8")){
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.sentence);
            }
        }
    }

    public void generateSpeakerFiles() throws FileNotFoundException, UnsupportedEncodingException {
        try(PrintWriter pwUtt = new PrintWriter(outDataDir.resolve("utt2spk").toFile(), "UTF-8");
            PrintWriter pwSpk = new PrintWriter(outDataDir.resolve("spk2utt").toFile(), "UTF-8")){

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

    public void generateSegmentFile() throws FileNotFoundException, UnsupportedEncodingException {
        try(PrintWriter pw = new PrintWriter(outDataDir.resolve("segments").toFile(), "UTF-8")){
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.uttID + " " + transcriptData.callID + " " +
                        transcriptData.startTime + " " + transcriptData.endTime);
            }
        }
    }
    public void generateTextFile() throws FileNotFoundException, UnsupportedEncodingException {
        try(PrintWriter pw = new PrintWriter(outDataDir.resolve("text").toFile(), "UTF-8")){
            for (DataProcessor.TranscriptData transcriptData : transcriptDataList) {
                pw.println(transcriptData.uttID + " " + transcriptData.sentence);
            }
        }
    }

    public static void generateDataFiles(Path outDataDir, List<DataProcessor.TranscriptData> transcriptDataList)
            throws FileNotFoundException, UnsupportedEncodingException {
        GenerateDataFiles generateDataFiles = new GenerateDataFiles(outDataDir, transcriptDataList);
        generateDataFiles.generateCorpusFile();
        generateDataFiles.generateTextFile();
        generateDataFiles.generateSegmentFile();
        generateDataFiles.generateSpeakerFiles();
        generateDataFiles.generateWavScpFile();
    }
}

