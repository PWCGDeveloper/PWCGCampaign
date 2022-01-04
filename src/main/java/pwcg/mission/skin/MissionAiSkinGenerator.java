package pwcg.mission.skin;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.skin.SkinFilter;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
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

    private void setAISkin(Company company, PlaneMcu plane, Date date) throws PWCGException
    {
        MissionSkinInitializer.intitializeSkin(missionSkinSet, company, plane, date);
        if (shouldUsePersonalSkin(plane))
        {
            chooseNonSquadronPersonalSkin(plane);
            chooseSquadronPersonalSkin(plane);
        }
        else
        {
            chooseFactorySkinForLowRank(plane);
        }
    }

    private void chooseSquadronPersonalSkin(PlaneMcu plane)
    {
        List<Skin> squadronPersonalSkins = missionSkinSet.getSquadronPersonalSkins(plane.getType());
        squadronPersonalSkins = SkinFilter.skinFilterInUse(squadronPersonalSkins, flight.getMission().getSkinsInUse());
        MissionSkinGeneratorHelper.chooseSquadronPersonalSkin(flight, plane, squadronPersonalSkins);
    }

    private void chooseNonSquadronPersonalSkin(PlaneMcu plane)
    {
        List<Skin> nonSquadronPersonalSkin = missionSkinSet.getNonSquadronPersonalSkin(plane.getType());
        nonSquadronPersonalSkin = SkinFilter.skinFilterInUse(nonSquadronPersonalSkin, flight.getMission().getSkinsInUse());
        MissionSkinGeneratorHelper.chooseNonSquadronPersonalSkin(flight, plane, nonSquadronPersonalSkin);
    }

    private void chooseFactorySkinForLowRank(PlaneMcu plane) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            List<Skin>factorySkins = missionSkinSet.getFactorySkins(plane.getType());
            factorySkins = SkinFilter.skinFilterInUse(factorySkins, flight.getMission().getSkinsInUse());
            MissionSkinGeneratorHelper.chooseFactorySkin(plane, factorySkins);
        }
    }
    
    private boolean shouldUsePersonalSkin(PlaneMcu plane) throws PWCGException
    {
        plane.getCrewMember().getRank();
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(plane.getSquadronId());
        IRankHelper rankHelper = RankFactory.createRankHelper();
        int rankPos = rankHelper.getRankPosByService(plane.getCrewMember().getRank(), squadron.determineServiceForSquadron(flight.getCampaign().getDate()));
        if (rankPos < 3)
        {
            return true;
        }
        
        if (plane.getCrewMember().getAiSkillLevel().getAiSkillLevel() > AiSkillLevel.COMMON.getAiSkillLevel())
        {
            return true;
        }
        
        return false;
    }
}
