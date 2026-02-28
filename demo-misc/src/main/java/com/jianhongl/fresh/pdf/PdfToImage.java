package com.jianhongl.fresh.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PdfToImage {

    private static final String fileName = "xyks_ctb_1772096189434.pdf";

    public static void main(String[] args) {
        // 代码动态获取文件路径
        String pdfFilePath = Thread.currentThread().getContextClassLoader().getResource("pdf/" + fileName).getPath();
        System.out.println("PDF file path: " + pdfFilePath);


        // 输出图片文件夹路径: target 目录下的 pdf_images 文件夹
        String outputDirPath = "target/pdf_images";
        // 打印当前目录:
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        File pdfFile = new File(pdfFilePath);
        File outputDir = new File(outputDirPath);

        // 创建输出文件夹（如果不存在）
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // 加载 PDF 文件
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numberOfPages = document.getNumberOfPages();

            for (int pageNumber = 0; pageNumber < numberOfPages; ++pageNumber) {
                // 渲染当前页为 BufferedImage
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageNumber, 130, ImageType.RGB);

                // 设置输出图片文件路径和格式
                File outputFile = new File(outputDir, "page_" + (pageNumber + 1) + ".jpg");

                // 将 BufferedImage 写入文件
                ImageIO.write(bufferedImage, "jpeg", outputFile);
                System.out.println("Saved page " + (pageNumber + 1) + " as image.");
            }

        } catch (IOException e) {
            System.err.println("Exception while trying to create pdf document - " + e);
        }
    }
}

