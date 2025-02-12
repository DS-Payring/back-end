package com.backend.payring.service.ocr;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleCloudVisionService {

    public String extractText(MultipartFile image) throws IOException {
        // 서비스 계정 JSON 키 파일 로드
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/google-vision-key.json"))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(
                ImageAnnotatorSettings.newBuilder()
                        .setCredentialsProvider(() -> credentials)
                        .build())) {

            // 이미지 변환
            ByteString imgBytes = ByteString.copyFrom(image.getBytes());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            // OCR 요청 설정
            Feature feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feature)
                    .setImage(img)
                    .build();

            // Vision API 요청
            List<AnnotateImageResponse> responses = vision.batchAnnotateImages(List.of(request)).getResponsesList();

            // 결과 텍스트 추출
            return responses.stream()
                    .map(AnnotateImageResponse::getTextAnnotationsList)
                    .flatMap(List::stream)
                    .map(EntityAnnotation::getDescription)
                    .collect(Collectors.joining("\n"));
        }
    }
}
