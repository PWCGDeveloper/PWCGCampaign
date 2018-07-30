package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.infantry.GroundTroopConcentration;
import pwcg.mission.mcu.Coalition;

public class TroopConcentrationFactory
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public TroopConcentrationFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public GroundTroopConcentration createTroopConcentration () throws PWCGException
    {
        MissionBeginUnitCheckZone missionBeginUnit = buildMissionBegin();
        GroundUnitInformation groundUnitInformation = createGroundUnitInformationForUnit(missionBeginUnit);
        GroundTroopConcentration troopConcentration = new GroundTroopConcentration(campaign, groundUnitInformation);
        troopConcentration.createUnitMission();
        return troopConcentration;
    }

    private MissionBeginUnitCheckZone buildMissionBegin() throws PWCGException
    {
        Coalition playerCoalition = Coalition.getFriendlyCoalition(campaign.determineCountry());
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
        missionBeginUnit.initialize(targetDefinition.getTargetPosition(), 10000, playerCoalition);
        return missionBeginUnit;
    }

    private GroundUnitInformation createGroundUnitInformationForUnit(MissionBeginUnitCheckZone missionBeginUnit) throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnit, targetDefinition);
        groundUnitInformation.setDestination(targetDefinition.getTargetPosition());
        return groundUnitInformation;
    }
}
