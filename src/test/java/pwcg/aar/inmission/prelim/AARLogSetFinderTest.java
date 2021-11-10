package pwcg.aar.inmission.prelim;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogSetFinder;
import pwcg.core.utils.DirectoryReader;

@ExtendWith(MockitoExtension.class)
public class AARLogSetFinderTest
{
    @Mock
    private DirectoryReader directoryReader;
  
    @Test
    public void testGetLogSets () throws PWCGException
    {
        List<String> sortedLogSets = new ArrayList<String>();
        sortedLogSets.add("LogSet1");
        sortedLogSets.add("LogSet2");
        sortedLogSets.add("LogSet3");
        Mockito.when(directoryReader.getSortedFilesWithFilter("[0].txt")).thenReturn(sortedLogSets);

        LogSetFinder logSetFinderTest = new LogSetFinder(directoryReader);
        List<String> returnSortedLogSets = logSetFinderTest.getSortedLogFileSets();
        assert(returnSortedLogSets.size() == 3);
    }
}
