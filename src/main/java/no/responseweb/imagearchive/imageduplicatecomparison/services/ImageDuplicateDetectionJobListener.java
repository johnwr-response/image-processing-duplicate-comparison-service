package no.responseweb.imagearchive.imageduplicatecomparison.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.AutoColorCorrelogram;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.FCTH;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import net.semanticmetadata.lire.utils.LuceneUtils;
import no.responseweb.imagearchive.imageduplicatecomparison.config.JmsConfig;
import no.responseweb.imagearchive.model.ImageDuplicateDetectionJobDto;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.IndexableFieldType;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.DelegationTokenResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageDuplicateDetectionJobListener {

    private final FileStoreFetcherService fileStoreFetcherService;

    public void listen(ImageDuplicateDetectionJobDto imageDuplicateDetectionJobDto) throws IOException, SolrServerException {
        log.info("Called with: {}", imageDuplicateDetectionJobDto);
        if (imageDuplicateDetectionJobDto.getFileItemDto()!=null) {
            byte[] downloadedBytesQuery = fileStoreFetcherService.fetchFile(imageDuplicateDetectionJobDto.getFileItemDto().getId());
            BufferedImage imageQuery = ImageIO.read(new ByteArrayInputStream(downloadedBytesQuery));
            if (imageQuery!=null) {
            }

        }
    }
}
