package pwcg.mission.skin;

import java.util.Date;
import java.util.List;

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

        if (plane.getAiLevel() != AiSkillLevel.NOVICE)
        {
            List<Skin> squadronPersonalSkins = missionSkinSet.getSquadronPersonalSkins(plane.getType());
            squadronPersonalSkins = SkinFilter.skinFilterInUse(squadronPersonalSkins, flight.getMission().getSkinsInUse());
            Skin skin = MissionSkinGeneratorHelper.chooseSquadronPersonalSkin(flight, plane, squadronPersonalSkins);

            if (skin == null && (plane.getAiLevel() != AiSkillLevel.COMMON))
            {
                List<Skin> nonSquadronPersonalSkin = missionSkinSet.getNonSquadronPersonalSkin(plane.getType());
                nonSquadronPersonalSkin = SkinFilter.skinFilterInUse(nonSquadronPersonalSkin, flight.getMission().getSkinsInUse());
                skin = MissionSkinGeneratorHelper.chooseNonSquadronPersonalSkin(flight, plane, nonSquadronPersonalSkin);
            }
        }
    }
}
