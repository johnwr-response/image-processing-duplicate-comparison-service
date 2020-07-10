package no.responseweb.imagearchive.imageduplicatecomparison.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.responseweb.imagearchive.model.ImageDuplicateDetectionJobDto;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageDuplicateDetectionJobListener {

    private final FileStoreFetcherService fileStoreFetcherService;

    public void listen(ImageDuplicateDetectionJobDto imageDuplicateDetectionJobDto) throws IOException {
        log.info("Called with: {}", imageDuplicateDetectionJobDto);
        if (imageDuplicateDetectionJobDto.getFileItemDto()!=null) {
            byte[] downloadedBytesQuery = fileStoreFetcherService.fetchFile(imageDuplicateDetectionJobDto.getFileItemDto().getId());
            BufferedImage imageQuery = ImageIO.read(new ByteArrayInputStream(downloadedBytesQuery));
            if (imageQuery!=null) {
            }

        }
    }
}
