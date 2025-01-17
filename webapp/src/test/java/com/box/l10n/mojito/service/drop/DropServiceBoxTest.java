package com.box.l10n.mojito.service.drop;

import com.box.l10n.mojito.boxsdk.BoxSDKService;
import com.box.l10n.mojito.boxsdk.BoxSDKServiceConfigFromProperties;
import com.box.l10n.mojito.boxsdk.BoxSDKServiceException;
import com.box.l10n.mojito.entity.Drop;
import com.box.l10n.mojito.service.drop.exporter.BoxDropExporter;
import com.box.l10n.mojito.service.drop.exporter.BoxDropExporterConfig;
import com.box.l10n.mojito.service.drop.exporter.DropExporterException;
import com.box.l10n.mojito.test.category.BoxSDKTest;
import com.box.l10n.mojito.test.category.SlowTest;
import com.box.sdk.BoxFile;
import org.apache.commons.lang.StringUtils;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Ignore;
import org.springframework.boot.test.WebIntegrationTest;

import static com.box.l10n.mojito.service.drop.exporter.DropExporterDirectories.*;
import static org.junit.Assume.assumeTrue;

/**
 * @author jaurambault
 */
@Ignore("won't work without @DirtiesContext which makes the build to slow")
@WebIntegrationTest(randomPort = true, value = {"l10n.dropexporter.type=BOX"})
@Category({BoxSDKTest.class, SlowTest.class})
public class DropServiceBoxTest extends DropServiceTest {

    static Logger logger = LoggerFactory.getLogger(DropServiceBoxTest.class);

    @Autowired
    BoxSDKServiceConfigFromProperties boxSDKServiceConfigFromProperties;

    @Autowired
    BoxSDKService boxSDKService;

    @Before
    public void checkBoxConfig() {
        assumeTrue(StringUtils.isNotEmpty(boxSDKServiceConfigFromProperties.getRootFolderId()));
    }

    @Override
    public List<DropFile> getDropFiles(Drop drop, String dropFolder) throws DropExporterException, BoxSDKServiceException {
        return boxSDKService.listFiles(getDropFolderIdForName(drop, dropFolder)).stream()
                .map(boxFile ->
                        new BoxDropFile(boxFile, boxSDKService))
                .collect(Collectors.toList());
    }

    @Override
    public void writeDropFile(Drop drop, String dropFolder, String fileName, String content)
            throws BoxSDKServiceException, DropExporterException {
        boxSDKService.uploadFile(getDropFolderIdForName(drop, dropFolder), fileName, content);
    }

    public String getDropFolderIdForName(Drop drop, String dropFolderName)
            throws DropExporterException {
        BoxDropExporter boxDropExporter =
                (BoxDropExporter) dropExporterService.recreateDropExporter(drop);
        BoxDropExporterConfig boxDropExporterConfig = boxDropExporter.getBoxDropExporterConfig();

        switch (dropFolderName) {
            case DROP_FOLDER_SOURCE_FILES_NAME:
                return boxDropExporterConfig.getSourceFolderId();
            case DROP_FOLDER_LOCALIZED_FILES_NAME:
                return boxDropExporterConfig.getLocalizedFolderId();
            case DROP_FOLDER_IMPORTED_FILES_NAME:
                return boxDropExporterConfig.getImportedFolderId();
            case DROP_FOLDER_QUERIES_NAME:
                return boxDropExporterConfig.getQueriesFolderId();
            case DROP_FOLDER_QUOTES_NAME:
                return boxDropExporterConfig.getQuotesFolderId();
            default:
                return null;
        }
    }
}

class BoxDropFile implements DropFile {
    private final BoxFile boxFile;
    private final BoxSDKService boxSDKService;

    BoxDropFile(BoxFile boxFile, BoxSDKService boxSDKService) {
        this.boxFile = boxFile;
        this.boxSDKService = boxSDKService;
    }

    public String getName() {
        return boxFile.getInfo().getName();
    }

    public String getContent() throws BoxSDKServiceException {
        return boxSDKService.getFileContent(boxFile).getContent();
    }
}