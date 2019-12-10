package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryBattery;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;

public class ArtilleryUnitBuilder
{
    private Campaign campaign;
    private TargetDefinition targetDefinition;

    public ArtilleryUnitBuilder (Campaign campaign, TargetDefinition targetDefinition)
    {
        this.campaign = campaign;
        this.targetDefinition = targetDefinition;
    }

    public IGroundUnitCollection createArtilleryBattery () throws PWCGException
    {
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(campaign, targetDefinition);
        IGroundUnit artilleryUnit = new GroundArtilleryBattery(groundUnitInformation);
        artilleryUnit.createGroundUnit();
        
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(
                GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION, 
                "Artillery Battery", 
                TacticalTarget.TARGET_ARTILLERY,
                Coalition.getCoalitionsForSide(groundUnitInformation.getCountry().getSide().getOppositeSide()));

        IGroundUnitCollection groundUnitCollection = new GroundUnitCollection (groundUnitCollectionData);
        groundUnitCollection.addGroundUnit(artilleryUnit);
        groundUnitCollection.finishGroundUnitCollection();

        return groundUnitCollection;
    }   
}
