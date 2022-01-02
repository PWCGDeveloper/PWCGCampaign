package pwcg.campaign.medals;

import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.utils.DateUtils;

public class MedalText
{
    public static String getTextForMedal(CrewMember crewMember, Medal medal)
    {
        String medaltext = "On " + DateUtils.getDateStringPretty(medal.getMedalDate()) + "\n";
        medaltext += "The " + medal.getMedalName() + " was awarded to " + crewMember.getNameAndRank() + "\n";
        return medaltext;
    }
}
