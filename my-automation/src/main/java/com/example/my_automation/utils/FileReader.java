package com.example.my_automation.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

@Component
public class FileReader {
	
    public Optional<String> read(String filePath) throws IOException {
        InputStream inputStream = new ClassPathResource(filePath).getInputStream();

        return read(inputStream);
    }

    public Optional<String> read(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");

        return scanner.hasNext() ? Optional.of(scanner.next()) : Optional.empty();
    }

    public static InputStream getFileInputStream(String fileName) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(fileName);
        return is;
    }

    public static String getExtention(String filename) {
        String extension = "";

        int i = filename.lastIndexOf('.');
        if (i > 0) {
            extension = filename.substring(i+1);
        }
        return extension;
    }


}
