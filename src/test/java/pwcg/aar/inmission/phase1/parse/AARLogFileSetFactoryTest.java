package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
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
        PWCGContextManager.setRoF(false);
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
        filenames.add("foo");
        filenames.add("missionReport(2016-08-25-22-51-08)[0].txt");
        filenames.add("missionReport(2016-08-25-22-51-08)[1].txt");
        Mockito.when(directoryReader.getFiles()).thenReturn(filenames);

        AARLogFileSetFactory aarLogFileSetFactory = new AARLogFileSetFactory();
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
