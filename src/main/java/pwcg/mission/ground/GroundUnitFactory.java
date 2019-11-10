package pwcg.mission.ground;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.SpotLightGroup;
import pwcg.mission.mcu.CoalitionFactory;

public class GroundUnitFactory
{    
    private Campaign campaign;
    private Coordinate location;
    private ICountry country;
    
    public GroundUnitFactory (Campaign campaign, Coordinate location, ICountry country)
    {
        this.campaign  = campaign;
        this.location  = location.copy();
        this.country  = country;
    }

    public SpotLightGroup createSpotLightGroup() throws PWCGException 
    {
        MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone(location.copy(), 12000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalitions(CoalitionFactory.getAllCoalitions());

        String nationality = country.getNationality();
        String name = nationality + " Spotlight Battery";
        
        boolean isPlayerTarget = true;
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, missionBeginUnit, country, name, TacticalTarget.TARGET_ARTILLERY, location, location, null, isPlayerTarget);

        SpotLightGroup spotLightGroup = new SpotLightGroup(groundUnitInformation, 8);
        spotLightGroup.createUnitMission();
        
        return spotLightGroup;
    }
}
