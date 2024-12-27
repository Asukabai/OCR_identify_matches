package com.ss.price.controller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFConverter {

    public static List<String> convertPdfToImages(String pdfPath, String outputFolder) throws IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        List<String> images = new ArrayList<>();

        for (int page = 0; page < document.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300); // 300 DPI
            String fileName = String.format("%s/page-%d.png", outputFolder, page + 1);
            ImageIO.write(bim, "png", new File(fileName));
            images.add(fileName);
        }

        document.close();
        return images;
    }
}
