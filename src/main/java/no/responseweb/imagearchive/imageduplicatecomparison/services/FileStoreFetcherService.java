package no.responseweb.imagearchive.imageduplicatecomparison.services;

import java.util.UUID;

public interface FileStoreFetcherService {
    byte[] fetchFile(UUID fileItemId);
}
