package com.sumologic;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public abstract class BaseSumoReportGeneratorTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    protected String tempFolderPath;

    @Before
    public void setUp() {
        tempFolderPath = tempFolder.getRoot().getAbsolutePath();
        MockitoAnnotations.initMocks(this);
    }

    protected void copyResourceToFolder(String resourcePath, String destinationFilePath) throws IOException {
        URL testURL = this.getClass().getResource(resourcePath);
        File dest = new File(destinationFilePath);
        FileUtils.copyURLToFile(testURL, dest);
    }

}
