package no.responseweb.imagearchive.imageduplicatecomparison.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.responseweb.imagearchive.imageduplicatecomparison.services.ImageDuplicateComparisonService;
import no.responseweb.imagearchive.model.ImageDuplicateComparisonDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ImageDuplicateComparisonController {
    private final ImageDuplicateComparisonService imageDuplicateComparisonService;

    @GetMapping("api/v1/compare/{compareFileItemId}/{compareWithFileItemId}")
    public ImageDuplicateComparisonDto getComparison(@PathVariable UUID compareFileItemId, @PathVariable UUID compareWithFileItemId) throws IOException {
        return imageDuplicateComparisonService.compareImages(compareFileItemId, compareWithFileItemId);
    }
}
