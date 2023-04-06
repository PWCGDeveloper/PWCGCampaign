package pwcg.campaign.squadmember;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.medals.Medal;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class Ace extends SquadronMember
{
    public Ace()
    {
        super();
        aiSkillLevel = AiSkillLevel.ACE;
    }
    public Ace copy()
    {
        Ace target = new Ace();

        target.name = this.name;
        target.rank = this.rank;
        target.picName = this.picName;
        target.pilotActiveStatus = this.pilotActiveStatus;
        target.country = this.country;
        target.missionFlown = this.missionFlown;
        target.aggressiveness = this.aggressiveness;
        target.aiSkillLevel = this.aiSkillLevel;
        target.commonSense = this.commonSense;
        target.serialNumber = this.serialNumber;

        target.skins.addAll(this.skins);
        target.victories.addAll(this.victories);
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

        Ace historicalAceNow = historicalAce.getAtDate(campaign.getDate());
        setSquadronId(historicalAceNow.getSquadronId());
        getSkins().addAll(historicalAceNow.getSkins());
        setCountry(historicalAce.getCountry());
        setRank(historicalAceNow.getRank());
        setName(historicalAceNow.getName());
        setPicName(historicalAceNow.getPicName());

        for (Victory historicalVictory : historicalAceNow.getSquadronMemberVictories().getAirToAirVictories())
        {
            boolean found = false;
            for (Victory victory : getSquadronMemberVictories().getAirToAirVictories())
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
        Collections.sort(getSquadronMemberVictories().getAirToAirVictories());

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

        if (historicalAce.getStatus(campaign.getDate()) == SquadronMemberStatus.STATUS_KIA)
        {
            setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
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
