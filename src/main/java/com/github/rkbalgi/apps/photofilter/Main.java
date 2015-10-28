package com.github.rkbalgi.apps.photofilter;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        String basePath = Paths.get(".").toString();
        System.out.println(basePath);

        File file = Paths.get(basePath, "filter.txt").toFile();
        if (!file.exists()) {
            System.err.println("file filter.txt doesn't exist");
            return;
        }


        File dir = new File(basePath);
        String[] files = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".JPG") || name.endsWith(".jpeg");


            }
        });

        List<String> filesList = Arrays.asList(files);
        SimpleDateFormat sdf = new SimpleDateFormat("MMddYYYY_HHmmss");
        Path filterDirectory = Files.createDirectory(Paths.get(basePath, sdf.format(new Date())));


        List<String> lines = Files.readAllLines(file.toPath());
        int lineno = 1;
        for (String line : lines) {

            if (line.matches("([0-9]{6})")) {

                filesList.stream().filter((f) -> {

                    if (f.contains("_" + line + ".")) {
                        return true;

                    }
                    return false;


                }).forEach((f) -> {

                    //copy the file to the dir
                    try {
                        System.out.println("copying file -" + f);


                        Files.copy(Paths.get(basePath, f), Paths.get(filterDirectory.toString(), f), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            } else {
                System.err.println("discarding line -" + line + " line number: " + lineno);
            }

            lineno++;

        }


    }
}
