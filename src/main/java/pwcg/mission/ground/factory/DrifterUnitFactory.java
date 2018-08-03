package pwcg.mission.ground.factory;

import pwcg.campaign.Campaign;
import pwcg.campaign.target.TargetDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;

public class DrifterUnitFactory
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;
    
    public DrifterUnitFactory (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition  = targetDefinition;
    }

    public GroundUnit createDrifterUnit () throws PWCGException 
    {
        MissionBeginUnit missionBeginUnit = new MissionBeginUnit();
        missionBeginUnit.initialize(targetDefinition.getTargetPosition().copy());
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, missionBeginUnit, targetDefinition);
        DrifterUnit drifterUnit = new DrifterUnit(groundUnitInformation);
        drifterUnit.createUnitMission();

        return drifterUnit;
    }
}
