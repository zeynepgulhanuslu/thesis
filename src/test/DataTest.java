package test;

import com.google.common.base.Splitter;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataTest {

    @Test
    public void sabanciTest() throws IOException {

        Path dataDirWoman = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/bayan/sabanci");
        Path dataDirMan = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/erkek/sabanci");
        List<Path> folders = Files.walk(dataDirMan, 1).filter(s -> s.toFile().isDirectory()
                && !s.toFile().getName().equals(dataDirMan.toFile().getName()))
                .collect(Collectors.toList());

        for (Path folder : folders) {
            if (!folder.resolve("transcription").toFile().exists()) {
                System.out.println(folder + " not contain transcription file");
            }
        }

//        try (PrintWriter pw = new PrintWriter(dataDirWoman.resolve("f157/transcription").toFile(), "UTF-8")) {
//            List<Path> transcriptFiles = Files.walk(dataDirWoman.resolve("f157"), 1).filter(s -> s.toFile().isFile()
//                    && s.toFile().getName().endsWith(".txt")).collect(Collectors.toList());
//            for (Path transcriptFile : transcriptFiles) {
//                List<String> list = Files.readAllLines(transcriptFile);
//                for (String s : list) {
//                    pw.println(s + " " + transcriptFile.toFile().getName().replace(".txt", ""));
//                }
//            }
//        }

    }

    @Test
    public void kdtmTest() throws IOException {
        Path dataDirMan = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/erkek/kdtm");
        Path dataDirWoman = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/bayan/kdtm");
        List<Path> folders = Files.walk(dataDirMan, 1).filter(s -> s.toFile().isDirectory()
                && !s.toFile().getName().equals(dataDirMan.toFile().getName()))
                .collect(Collectors.toList());

        for (Path folder : folders) {
            if (!folder.resolve("transcription").toFile().exists()) {
                System.out.println(folder + " not contain transcription file");
            }
        }


    }

    @Test
    public void metuTest() throws IOException {
        Path dataDirMan = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/erkek/metu");
        Path dataDirWoman = Paths.get("/media/kdtl/depo/tez/tomography-data/mic/bayan/metu");
        List<Path> folders = Files.walk(dataDirMan, 1).filter(s -> s.toFile().isDirectory()
                && !s.toFile().getName().equals(dataDirMan.toFile().getName()))
                .collect(Collectors.toList());

        for (Path folder : folders) {
            if (!folder.resolve("transcription").toFile().exists()) {
                System.out.println(folder + " not contain transcription file");
            }
        }
    }

    @Test
    public void tomographyDataTest() throws IOException {
        Path dataDir = Paths.get("/media/kdtl/depo/tez/tomography-data/tomografi-rapor.txt");

        List<String> transcriptList = Files.readAllLines(dataDir);

        Set<String> ordinalSet = new HashSet<>();
        for (String line : transcriptList) {
            if (line.contains("1101357Accs.")) {
                System.out.println(line);
            }
//            if (line.matches(".*\\d+.*")){
//                List<String> list = Splitter.on(" ").trimResults().splitToList(line);
//                for (String word : list) {
//                    if (word.matches(".*\\d+.*") && word.endsWith(".")){
//                        ordinalSet.add(word);
//                    }
//                }
//            }
        }
//        for (String s : ordinalSet) {
//            System.out.println(s);
//        }


    }
}
