package pwcg.campaign.squadmember;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.crewmember.CrewMemberNames;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class CrewMemberNamesTest 
{
	@Test
	public void noAsciiInNamesTest() throws PWCGException 
	{
		CrewMemberNames.getInstance().validateAscii();
	}
}
