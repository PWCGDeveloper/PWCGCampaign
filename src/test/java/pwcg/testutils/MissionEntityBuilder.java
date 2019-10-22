package pwcg.testutils;

import java.util.Date;

import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.exception.PWCGException;

public class MissionEntityBuilder
{
    public static Ace makeDeadAceWithVictories(String aceName, int aceSerialNumber, int numVictories, Date date) throws PWCGException
    {
        Ace aceKilledInMission = new Ace();
        aceKilledInMission.setSerialNumber(aceSerialNumber);
        aceKilledInMission.setName(aceName);
        aceKilledInMission.setSquadronId(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        aceKilledInMission.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, date, null);
        aceKilledInMission.setInactiveDate(date);
        for (int i = 0; i < numVictories; ++i)
        {
            Victory victory = new Victory();
            aceKilledInMission.addVictory(victory);
        }
        return aceKilledInMission;
    }


    public static SquadronMember makeSquadronMemberWithStatus(String pilotName, int serialNumber, int status, Date statusDate, Date returnDate) throws PWCGException
    {
        SquadronMember squadronMember = new Ace();
        squadronMember.setSerialNumber(serialNumber);
        squadronMember.setName(pilotName);
        squadronMember.setSquadronId(SquadronTestProfile.ESC_103_PROFILE.getSquadronId());
        squadronMember.setPilotActiveStatus(status, statusDate, returnDate);
        return squadronMember;
    }

}
