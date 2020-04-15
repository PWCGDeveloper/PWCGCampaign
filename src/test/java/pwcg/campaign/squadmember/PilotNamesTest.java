package pwcg.campaign.squadmember;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class PilotNamesTest 
{
	@Test
	public void noAsciiInNamesTest() throws PWCGException 
	{
		PilotNames.getInstance().validateAscii();
	}
}
