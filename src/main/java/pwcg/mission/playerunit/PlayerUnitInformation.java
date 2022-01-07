package pwcg.mission.playerunit;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.mission.Mission;

public class PlayerUnitInformation
{

    public ICountry getCountry()
    {
        return null;
    }

    public Mission getMission()
    {
        return null;
    }

    public Company getCompany()
    {
        return null;
    }

    public String getBase()
    {
        return null;
    }

    public Campaign getCampaign()
    {
        return null;
    }

    public List<CrewMember> getParticipatingPlayersForCompany()
    {
        return null;
    }
    
    public UnitMissionType getUnitMissionType()
    {
        return UnitMissionType.ASSAULT;
    }

}
