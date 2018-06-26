package test;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
    public void readWord() {
        try {
            String path = "/media/kdtl/depo/tez/tomography-data/rapor-ornegi/Klasor 3/16 haziran turgutlu/16 HZİRAN MEHMET ERKEK/FUATCAN ÖZTÜRK.docx";
            FileInputStream fis = new FileInputStream(path);
            XWPFDocument xdoc=new XWPFDocument(OPCPackage.open(fis));
            List<XWPFParagraph> paragraphList =  xdoc.getParagraphs();
            for (XWPFParagraph paragraph: paragraphList){
                System.out.println(paragraph.getText());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void tomographyDataTest() throws IOException {
        Path input = Paths.get("/media/kdtl/depo/tez/tomography-data/rapor-ornegi/Klasor 3/16 haziran turgutlu/16 HZİRAN MEHMET ERKEK");
        Path outPut = Paths.get("/media/kdtl/depo/tez/tomography-data/tomografi-rapor/formated-data/mehmet-erkek/transcript");

        List<Path> transcriptList = Files.walk(input, 1).
                filter(s -> s.toFile().getName().endsWith("docx")).collect(Collectors.toList());

        Files.createDirectories(outPut);
        for (Path path : transcriptList) {
            String fileName = path.toFile().getName().replace("docx", "txt");
            try (PrintWriter pw = new PrintWriter(outPut.resolve(fileName).toFile(), "UTF-8")) {
                try {
                    FileInputStream fis = new FileInputStream(path.toString());
                    XWPFDocument xdoc= new XWPFDocument(OPCPackage.open(fis));
                    List<XWPFParagraph> paragraphList =  xdoc.getParagraphs();
                    for (XWPFParagraph paragraph: paragraphList){
                        if (!paragraph.getText().isEmpty()){
                            pw.println(paragraph.getText().trim());
                        }
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Test
    public  void dataFilesTest() throws IOException {
        Path sourceDir = Paths.get("/media/kdtl/depo/tez/test-data/out");
        Path outDir = Paths.get("/media/kdtl/depo/tez/main/data/test");
//
//        Files.copy(sourceDir.resolve("segments"), outDir.resolve("segments"), REPLACE_EXISTING);
        Files.copy(sourceDir.resolve("paths"), outDir.resolve("wav.scp"), REPLACE_EXISTING);

        List<String> segmentList = Files.readAllLines(outDir.resolve("segments"));
        List<String> uttList = new ArrayList<>();
        for (String line : segmentList) {
            String [] tokens = line.split("\\s+");
            uttList.add(tokens[0]);
        }

        Collections.sort(uttList);

        try(PrintWriter pwSpk = new PrintWriter(outDir.resolve("spk2utt").toFile(),"UTF-8");
            PrintWriter pwUtt = new PrintWriter(outDir.resolve("utt2spk").toFile(),"UTF-8")){
            for (String utt : uttList) {
                pwSpk.println(utt + " " + utt);
                pwUtt.println(utt + " " + utt);
            }
        }
    }
}
