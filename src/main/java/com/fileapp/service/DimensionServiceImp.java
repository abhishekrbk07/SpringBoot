package com.fileapp.service;

import com.fileapp.constants.DimensionNameConstant;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DimensionServiceImp implements DimensionService {


    public List<String> generate(Integer dimensionId){
        return getFileDetails(dimensionId);
}
    public List<String>getCricketerNames(){
        List<String> names= new ArrayList<>();
        names.add("Abhishek");
        names.add("Virat");
        names.add("Dhoni");
        names.add("Abd");
        return names;
    }

    public List<String>getActorsNames(){
        List<String> names= new ArrayList<>();
        names.add("Srk");
        names.add("Karthik");
        return names;
    }

    public ResponseEntity<?>
    download(Integer dimensionId) {
          List<String> data = getFileDetails(dimensionId);
        try {
            // Create a temporary file to store the CSV data
            File tempFile = File.createTempFile("cricketer", ".csv");

            // Write the data to the CSV file using the CSVWriter library
            FileWriter writer = new FileWriter(tempFile);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
            for (String row : data) {
                csvPrinter.printRecord(row);
            }
            csvPrinter.close();

            // Create a resource object for the temporary file
            FileSystemResource resource = new FileSystemResource(tempFile);

            // Return the resource as a response with the appropriate content type and content disposition headers
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tempFile.getName() + "\"")
                    .body(resource);

        } catch (IOException e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    public List<String>getFileDetails(Integer dimensionId){
        if(dimensionId == DimensionNameConstant.CRICKETER.getDimensionId()){
            return getCricketerNames();
        }
        else if(dimensionId == DimensionNameConstant.ACTOR.getDimensionId() ){
            return getActorsNames();
        }
        return null;
    }
}
