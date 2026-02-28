package com.jianhongl.fresh.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;

public class PdfToImageWithDpi {

    private static final String fileName = "xyks_ctb_1772096189434.pdf";

    public static void main(String[] args) throws PDFSecurityException, PDFException {

        String pdfFilePath = Thread.currentThread().getContextClassLoader().getResource("pdf/" + fileName).getPath();
        // 输出图片文件夹路径: target 目录下的 pdf_images 文件夹
        String outputDirPath = "target/pdf_images_with_dpi";

        File pdfFile = new File(pdfFilePath);
        File outputDir = new File(outputDirPath);

        // Create the output directory if it does not exist
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Load the PDF document using IcePDF
        Document document = new Document();
        try {
            document.setFile(pdfFilePath);
            int numberOfPages = document.getNumberOfPages();

            // Iterate through each page and render it as an image with specified DPI
            for (int pageNumber = 0; pageNumber < numberOfPages; ++pageNumber) {
                // Control rendering using a scale factor to achieve desired DPI
                float dpi = 120; // Desired DPI
                float scaleFactor = dpi / 72f; // Default PDF DPI is 72

                BufferedImage image = (BufferedImage) document.getPageImage(pageNumber, Page.BOUNDARY_CROPBOX, 0,0, scaleFactor);
                File outputFile = new File(outputDir, "page_" + (pageNumber + 1) + ".jpg");

                // Save the image to a file
                ImageIO.write(image, "jpeg", outputFile);
                System.out.println("Saved page " + (pageNumber + 1) + " as image.");

                // Clean up resources
                image.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to load PDF file - " + e.getMessage());
        } finally {
            document.dispose();
        }
    }
}
