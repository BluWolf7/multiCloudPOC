package com.multicloudpoc.multicloudpoc.controller;

import com.multicloudpoc.multicloudpoc.services.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class PdfController {

    @Autowired
    private PdfService pdfService; // PdfService is a common interface implemented by GcsService and S3Service

    // Upload PDF file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPDF(@RequestParam("file") MultipartFile file) {
        try {
            pdfService.uploadPDF(file);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Download PDF file
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPDF(@RequestParam("filename") String filename) {
        byte[] fileContent = pdfService.downloadPDF(filename);
        if (fileContent != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + filename)
                    .body(fileContent);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // List PDF files
    @GetMapping("/list")
    public ResponseEntity<List<String>> listPDFs() {
        List<String> pdfFiles = pdfService.listPDFs();
        return new ResponseEntity<>(pdfFiles, HttpStatus.OK);
    }

    // Get Signed or Presigned URL
    @GetMapping("/signed-url")
    public ResponseEntity<String> getSignedUrl(@RequestParam String filename) {
        String signedUrl = pdfService.generateSignedUrl(filename);
        if (signedUrl != null) {
            return ResponseEntity.ok(signedUrl);
        } else {
            return new ResponseEntity<>("Failed to generate signed URL", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete PDF file
    @DeleteMapping("/{filename}")
    public ResponseEntity<String> deleteFile(@RequestParam String filename) {
        if (pdfService.deletePDF(filename)) {
            return ResponseEntity.ok("File " + filename + " deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File " + filename + " not found");
        }
    }
}
