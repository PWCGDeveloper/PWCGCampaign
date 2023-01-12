package pwcg.mission.finalize;

import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.group.airfield.RunwayBlockageDetector;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitElement;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.mcu.McuSpawn;

public class RunwayCleaner
{
    private Mission mission;
    
    public RunwayCleaner(Mission mission)
    {
        this.mission = mission;
    }
    
    public void cleanMissionRunways() throws PWCGException
    {
        for (Airfield airfield : mission.getMissionAirfields())
        {
            cleanAirfield(airfield);
        }
    }

    private void cleanAirfield(Airfield airfield) throws PWCGException
    {
        for (GroundUnitCollection groundUnitCollection : mission.getGroundUnitBuilder().getAllMissionGroundUnits())
        {
            for (IGroundUnit groundUnit : groundUnitCollection.getGroundUnits())
            {
                removeObstructingGroundUnitElements(airfield, groundUnit);
            }
        }
    }

    private void removeObstructingGroundUnitElements(Airfield airfield, IGroundUnit groundUnit) throws PWCGException
    {
        for (GroundUnitElement groundElement : groundUnit.getGroundElements())
        {
            if (RunwayBlockageDetector.isRunwayBlocked(mission.getCampaignMap(), airfield, groundElement.getVehicleStartLocation()))
            {
                groundUnit.removeGroundUnitElement(groundElement.getIndex());                
            }
        }
    }
}
