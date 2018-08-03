package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;

public class ArtilleryUnitFactory
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;

    public ArtilleryUnitFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition = targetDefinition;
    }

    public GroundUnit createGroundArtilleryBattery () throws PWCGException
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(targetDefinition.getTargetPosition());
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, missionBeginUnit, targetDefinition);

        GroundArtilleryUnit artilleryUnit = new GroundArtilleryUnit(groundUnitInformation);
        artilleryUnit.createUnitMission();

        return artilleryUnit;
    }
}
