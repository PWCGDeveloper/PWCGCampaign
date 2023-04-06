package pwcg.campaign.personnel;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.PilotNames;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

import java.util.Objects;

public class SquadronMemberNationalityConverter {

    public static SquadronMember convertIfNeeded(Campaign campaign, Squadron squadron, SquadronMember squadronMember) throws PWCGException {
        if (squadronMember.isPlayer()) // let the player be whoever they want
            return squadronMember;
        if (squadron.getNationalityOverride() == null) // don't care about pilot's nationality if the squadron does not care either
            return squadronMember;
        if (Objects.equals(squadron.getNationalityOverride(), squadronMember.getNationality())) // pilot already has a matching nationality
            return squadronMember;

        // note that pilot picture could also be changed here
        squadronMember.setNationality(squadron.getNationalityOverride());
        squadronMember.setName(PilotNames.getInstance().getNameForNationality(squadron.getNationalityOverride(), squadron.getNamesInUse(campaign)));
        return squadronMember;
    }
}
