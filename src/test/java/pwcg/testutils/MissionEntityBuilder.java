package pwcg.testutils;

import java.util.Date;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.exception.PWCGException;

public class MissionEntityBuilder
{
    public static TankAce makeDeadAceWithVictories(String aceName, int aceSerialNumber, int numVictories, Date date) throws PWCGException
    {
        TankAce aceKilledInMission = new TankAce();
        aceKilledInMission.setSerialNumber(aceSerialNumber);
        aceKilledInMission.setName(aceName);
        aceKilledInMission.setCompanyId(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        aceKilledInMission.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, date, null);
        aceKilledInMission.setInactiveDate(date);
        for (int i = 0; i < numVictories; ++i)
        {
            Victory victory = new Victory();
            aceKilledInMission.addVictory(victory);
        }
        return aceKilledInMission;
    }


    public static CrewMember makeCrewMemberWithStatus(String crewMemberName, int serialNumber, int status, Date statusDate, Date returnDate) throws PWCGException
    {
        CrewMember crewMember = new TankAce();
        crewMember.setSerialNumber(serialNumber);
        crewMember.setName(crewMemberName);
        crewMember.setCompanyId(SquadronTestProfile.ESC_103_PROFILE.getCompanyId());
        crewMember.setCrewMemberActiveStatus(status, statusDate, returnDate);
        return crewMember;
    }

}
