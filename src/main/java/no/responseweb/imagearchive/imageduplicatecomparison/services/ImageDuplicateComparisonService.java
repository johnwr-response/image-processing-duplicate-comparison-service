package no.responseweb.imagearchive.imageduplicatecomparison.services;

import no.responseweb.imagearchive.model.ImageDuplicateComparisonDto;

import java.io.IOException;
import java.util.UUID;

public interface ImageDuplicateComparisonService {
    ImageDuplicateComparisonDto compareImages(UUID fileItemIdA, UUID fileItemIdB) throws IOException;
}
