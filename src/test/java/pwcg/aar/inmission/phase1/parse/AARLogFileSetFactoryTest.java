package pwcg.aar.inmission.phase1.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

@RunWith(MockitoJUnitRunner.class)
public class AARLogFileSetFactoryTest
{
    @Mock
    private DirectoryReader directoryReader;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    

    @Mock private File file_2016_10_25_0;
    @Mock private File file_2016_10_25_1;
    @Mock private File file_2016_10_25_2;

    @Mock private File file_2016_11_22_0;
    @Mock private File file_2016_11_22_1;
    @Mock private File file_2016_11_22_2;
    @Mock private File file_2016_11_22_3;
    @Mock private File file_2016_11_22_4;

    @Mock private File file_2016_08_25_0;
    @Mock private File file_2016_08_25_1;

    @Test
    public void testMissionResultFileidentification() throws PWCGException
    {
        Mockito.when(file_2016_10_25_0.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[0].txt");
        Mockito.when(file_2016_10_25_1.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[1].txt");
        Mockito.when(file_2016_10_25_2.getName()).thenReturn("missionReport(2016-10-25-22-51-08)[2].txt");

        Mockito.when(file_2016_11_22_0.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[0].txt");
        Mockito.when(file_2016_11_22_1.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[1].txt");
        Mockito.when(file_2016_11_22_2.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[2].txt");
        Mockito.when(file_2016_11_22_3.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[3].txt");
        Mockito.when(file_2016_11_22_4.getName()).thenReturn("missionReport(2016-09-11-22-51-08)[4].txt");

        Mockito.when(file_2016_08_25_0.getName()).thenReturn("missionReport(2016-08-25-22-51-08)[0].txt");
        Mockito.when(file_2016_08_25_1.getName()).thenReturn("missionReport(2016-08-25-22-51-08)[1].txt");

        List<File> logFiles = new ArrayList<>();
        logFiles = new ArrayList<File>();
        logFiles.add(file_2016_10_25_0);
        logFiles.add(file_2016_10_25_1);
        logFiles.add(file_2016_10_25_2);

        logFiles.add(file_2016_11_22_0);
        logFiles.add(file_2016_11_22_1);
        logFiles.add(file_2016_11_22_2);
        logFiles.add(file_2016_11_22_3);
        logFiles.add(file_2016_11_22_4);

        logFiles.add(file_2016_08_25_0);
        logFiles.add(file_2016_08_25_1);

        Mockito.when(directoryReader.getFiles()).thenReturn(logFiles);

        AARLogFileSetFactory aarLogFileSetFactory = new AARLogFileSetFactory();
        aarLogFileSetFactory.setDirectoryReader(directoryReader);
        
        aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet("missionReport(2016-10-25-22-51-08)");
        List<File> filesInSet = aarLogFileSetFactory.getLogFileSets();
        assert(filesInSet.size() == 3);
        
        aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet("missionReport(2016-09-11-22-51-08)");
        filesInSet = aarLogFileSetFactory.getLogFileSets();
        assert(filesInSet.size() == 5);
        
        aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet("missionReport(2016-08-25-22-51-08)");
        filesInSet = aarLogFileSetFactory.getLogFileSets();
        assert(filesInSet.size() == 2);
        
        aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet("missionReport(2016-07-07-22-51-08)");
        filesInSet = aarLogFileSetFactory.getLogFileSets();
        assert(filesInSet.size() == 0);
    }
}
