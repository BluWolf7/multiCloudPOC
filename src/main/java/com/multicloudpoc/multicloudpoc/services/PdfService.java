package com.multicloudpoc.multicloudpoc.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

public interface PdfService {

    void uploadPDF(MultipartFile file) throws IOException;

    byte[] downloadPDF(String filename);

    List<String> listPDFs();

    String generateSignedUrl(String filename);

    boolean deletePDF(String filename);
}

