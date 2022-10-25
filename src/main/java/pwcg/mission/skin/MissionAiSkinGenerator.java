package pwcg.mission.skin;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinFilter;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

public class MissionAiSkinGenerator
{
    private IFlight flight;
    private MissionSkinSet missionSkinSet;

    public MissionAiSkinGenerator(IFlight flight, MissionSkinSet missionSkinSet)
    {
        this.flight = flight;
        this.missionSkinSet = missionSkinSet;
    }

    public void applyAiSkin() throws PWCGException
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            setAISkin(flight.getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
        }
    }

    private void setAISkin(Squadron squad, PlaneMcu plane, Date date) throws PWCGException
    {
        MissionSkinInitializer.intitializeSkin(missionSkinSet, squad, plane, date);
        if (shouldUsePersonalSkin(plane))
        {
            // chooseSquadronPersonalSkin(plane);
        }
    }

    private void chooseSquadronPersonalSkin(PlaneMcu plane)
    {
        List<Skin> squadronPersonalSkins = missionSkinSet.getSquadronPersonalSkins(plane.getType());
        squadronPersonalSkins = SkinFilter.skinFilterInUse(squadronPersonalSkins, flight.getMission().getSkinsInUse());
        MissionSkinGeneratorHelper.chooseSquadronPersonalSkin(flight, plane, squadronPersonalSkins);
    }
    
    private boolean shouldUsePersonalSkin(PlaneMcu plane) throws PWCGException
    {
        plane.getPilot().getRank();
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(plane.getSquadronId());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        int rankPos = rankHelper.getRankPosByService(plane.getPilot().getRank(), squadron.determineServiceForSquadron(flight.getCampaign().getDate()));
        if (rankPos < 3)
        {
            return true;
        }
        
        if (plane.getPilot().getAiSkillLevel().getAiSkillLevel() > AiSkillLevel.COMMON.getAiSkillLevel())
        {
            return true;
        }
        
        return false;
    }
}
