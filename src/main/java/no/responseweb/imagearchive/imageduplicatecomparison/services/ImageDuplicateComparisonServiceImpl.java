package no.responseweb.imagearchive.imageduplicatecomparison.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.responseweb.imagearchive.model.ImageDuplicateComparisonDto;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageDuplicateComparisonServiceImpl implements ImageDuplicateComparisonService {

    private final FileStoreFetcherService fileStoreFetcherService;

    @Override
    public ImageDuplicateComparisonDto compareImages(UUID fileItemIdA, UUID fileItemIdB) throws IOException {
        log.info("Checking A:{} against B:{}", fileItemIdA, fileItemIdB);
        byte[] fileA = fileStoreFetcherService.fetchFile(fileItemIdA);
        byte[] fileB = fileStoreFetcherService.fetchFile(fileItemIdB);
        if (fileA==null || fileB==null) {
            log.error("fileA({}) or fileB({}) is null", fileA, fileB);
            return ImageDuplicateComparisonDto.builder().build();
        }
        log.info("Length A:{}, B:{}", fileA.length, fileB.length);
        BufferedImage imageA = ImageIO.read(new ByteArrayInputStream(fileA));
        BufferedImage imageB = ImageIO.read(new ByteArrayInputStream(fileB));
        log.info("BufferedImage A:{}, B:{}", imageA, imageB);
        if (imageA==null || imageB==null) {
            log.info("Not an Image! imageA({}) or imageB({}) is null.", imageA, imageB);
            return ImageDuplicateComparisonDto.builder().build();
        }
        Mat loadedImageA = bufferedImage2Mat(imageA);
        Mat loadedImageB = bufferedImage2Mat(imageB);
        log.info("Mat A:{}, B:{}", loadedImageA, loadedImageB);
        MatOfKeyPoint keypointsA = new MatOfKeyPoint();
        MatOfKeyPoint keypointsB = new MatOfKeyPoint();

        ORB detector = ORB.create();

        //Detect keypoints
        detector.detect(loadedImageA, keypointsA);
        log.info("Number of key points in image A {} : {}", fileItemIdA, keypointsA.size());
        detector.detect(loadedImageB, keypointsB);
        log.info("Number of key points in image B {} : {}", fileItemIdB, keypointsB.size());

        Mat descriptors1 = new Mat();
        Mat descriptors2 = new Mat();
        detector.detectAndCompute(loadedImageA, new Mat(), keypointsA, descriptors1);
        log.info("Descriptor A: {}", descriptors1);
        detector.detectAndCompute(loadedImageB, new Mat(), keypointsB, descriptors2);
        log.info("Descriptor B: {}", descriptors2);

        //Definition of descriptor matcher
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        List<MatOfDMatch> knnMatches = new ArrayList<>();
        matcher.knnMatch(descriptors1, descriptors2, knnMatches, 2);
        log.info("List of Possible Matches : {}", knnMatches.size());
        List<DMatch> listOfGoodMatches = new ArrayList<>();
        float ratioThresh = 0.7f;
        for (MatOfDMatch knnMatch : knnMatches) {
            if (knnMatch.rows() > 1) {
                DMatch[] matches = knnMatch.toArray();
                if (matches[0].distance < ratioThresh * matches[1].distance) {
                    listOfGoodMatches.add(matches[0]);
                }
            }
        }
        log.info("List of Good Matches : {}", listOfGoodMatches.size());
        return ImageDuplicateComparisonDto.builder()
                .possibleMatches(knnMatches.size())
                .goodMatches(listOfGoodMatches.size())
                .build();
    }

    private static Mat bufferedImage2Mat(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_UNCHANGED);
    }

}
