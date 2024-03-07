package com.microservicepdf.microservicepdf.web.controller;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;
import org.bouncycastle.util.encoders.Base64;
import org.jsoup.Jsoup;
import org.springframework.boot.autoconfigure.ssl.SslProperties.Bundles.Watch.File;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;

import com.microservicepdf.microservicepdf.domain.Base64RequestGenerate;
import com.microservicepdf.microservicepdf.web.IO.ApiResponse;
import com.openhtmltopdf.DOMBuilder;
import com.openhtmltopdf.css.parser.property.PageSize;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@RestController
@RequestMapping("/pdf")
public class GeneratorPdf {

    @CrossOrigin(origins = "http://php-legacy.avance.org.co:7080/")
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse> getPdf(@RequestBody Base64RequestGenerate request) {
        String base64 = request.getBase64();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            // builder.buildPdfRenderer(baos).createPDFWithoutClosing();
            Document documento = html5ParseString(base64, 1000);
            builder.withW3cDocument(documento, System.getProperty("java.io.tmpdir"));
            builder.toStream(baos);
            builder.run();
            byte[] pdfBytes = baos.toByteArray();
            String base64String = new String(Base64.encode(pdfBytes));
            ApiResponse response = new ApiResponse(true, "Se ha Generado el Pdf correctamente en el base64");
            response.addData("base64", base64String);
            return new ResponseEntity<ApiResponse>(response, HttpStatus.OK);

        } catch (Exception ex) {
            Map<String, String> errors = new HashMap<String, String>();
            errors.put("error", ex.getMessage());
            ApiResponse response = new ApiResponse(false, "Ha ocurrido un error", null, errors);
            return new ResponseEntity<ApiResponse>(response, HttpStatus.BAD_REQUEST);

        }

    }

    public static Document html5ParseString(String base64Html, int timeoutMs) {
        String html = new String(Base64.decode(base64Html));
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        return DOMBuilder.jsoup2DOM(doc);
    }
}
