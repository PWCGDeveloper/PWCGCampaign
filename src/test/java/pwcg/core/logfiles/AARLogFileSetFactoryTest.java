package pwcg.core.logfiles;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DirectoryReader;

@ExtendWith(MockitoExtension.class)
public class AARLogFileSetFactoryTest
{
    @Mock
    private DirectoryReader directoryReader;

    public AARLogFileSetFactoryTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void testMissionResultFileidentification() throws PWCGException
    {
        List<String> filenames = new ArrayList<String>();
        filenames.add("missionReport(2016-10-25-22-51-08)[0].txt");
        filenames.add("missionReport(2016-10-25-22-51-08)[1].txt");
        filenames.add("missionReport(2016-10-25-22-51-08)[2].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[0].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[1].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[2].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[3].txt");
        filenames.add("missionReport(2016-09-11-22-51-08)[4].txt");
        filenames.add("missionReport(2016-08-25-22-51-08)[0].txt");
        filenames.add("missionReport(2016-08-25-22-51-08)[1].txt");
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        LogFileSetFactory aarLogFileSetFactory = new LogFileSetFactory();
        aarLogFileSetFactory.setDirectoryReader(directoryReader);
        
        aarLogFileSetFactory.determineMissionResultsFileForRequestedFileSet("missionReport(2016-10-25-22-51-08)");
        List<String> filesInSet = aarLogFileSetFactory.getLogFileSets();
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
