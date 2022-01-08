package pwcg.campaign.crewmember;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.medals.Medal;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class TankAce extends CrewMember
{
    public TankAce()
    {
        super();
        aiSkillLevel = AiSkillLevel.ACE;
    }
    public TankAce copy()
    {
        TankAce target = new TankAce();

        target.name = this.name;
        target.rank = this.rank;
        target.picName = this.picName;
        target.activeStatus = this.activeStatus;
        target.country = this.country;
        target.battlesFought = this.battlesFought;
        target.aiSkillLevel = this.aiSkillLevel;
        target.serialNumber = this.serialNumber;

        target.skins.addAll(this.skins);
        target.airVictories.addAll(this.airVictories);
        target.medals.addAll(this.medals);

        return target;
    }

    public void mergeWithHistorical(Campaign campaign) throws PWCGException
    {
        HistoricalAce historicalAce = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(getSerialNumber());

        if (historicalAce == null)
        {
            PWCGLogger.log(LogLevel.ERROR, "Ace from campaign file not found: " + getSerialNumber());
            return;
        }

        TankAce historicalAceNow = historicalAce.getAtDate(campaign.getDate());
        setSquadronId(historicalAceNow.getCompanyId());
        getSkins().addAll(historicalAceNow.getSkins());
        setCountry(historicalAce.getCountry());
        setRank(historicalAceNow.getRank());

        for (Victory historicalVictory : historicalAceNow.getCrewMemberVictories().getAirToAirVictories())
        {
            boolean found = false;
            for (Victory victory : getCrewMemberVictories().getAirToAirVictories())
            {
                if (victory.getDate().equals(historicalVictory.getDate())
                                && victory.getVictim().getType().equals(historicalVictory.getVictim().getType()))
                {
                    found = true;
                }
            }

            if (!found)
            {
                this.addVictory(historicalVictory);
            }
        }
        Collections.sort(getCrewMemberVictories().getAirToAirVictories());

        for (Medal historicalMedal : historicalAceNow.getMedals())
        {
            boolean found = false;
            for (Medal medal : getMedals())
            {
                if (medal.getMedalName().equals(historicalMedal.getMedalName()))
                {
                    found = true;
                }
            }

            if (!found)
            {
                this.addMedal(historicalMedal);
            }
        }
        Collections.sort(getMedals());

        setSkins(historicalAce.getSkins());

        if (historicalAce.getStatus(campaign.getDate()) == CrewMemberStatus.STATUS_KIA)
        {
            setCrewMemberActiveStatus(CrewMemberStatus.STATUS_KIA, campaign.getDate(), null);
        }
    }

   public void addGroundVictory(Victory victory)
    {
        super.addGroundVictory(victory);
    }

    public void setGroundVictories(List<Victory> victories)
    {
        if (victories.size() > 0)
        {
            super.setGroundVictories(victories);
        }
    }
}
