package pwcg.campaign.medals;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.utils.DateUtils;

public class MedalText
{
    public static String getTextForMedal(SquadronMember pilot, Medal medal)
    {
        String medaltext = "On " + DateUtils.getDateStringPretty(medal.getMedalDate()) + "\n";
        medaltext += "The " + medal.getMedalName() + " was awarded to " + pilot.getNameAndRank() + "\n";
        return medaltext;
    }
}
