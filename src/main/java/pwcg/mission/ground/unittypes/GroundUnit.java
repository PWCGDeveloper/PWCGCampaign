package pwcg.mission.ground.unittypes;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.Unit;
import pwcg.mission.ground.vehicle.IVehicle;

/**
 * 
 * @author Patrick Wilson
 *
 * Ground Unit
 *      GroundMovingUnit
 *          InfantryAssaultUnit
 *              - Infantry
 *              - Tanks
 *              - Machine Guns
 *              - AAA MG Unit
 *              
 *          Train
 *              - Locomotive and cars
 *              
 *          Truck Convoy
 *              - Trucks
 *              - Wagons
 *              
 *          Ship Convoy
 *              - Ships
 *              
 *      GroundStationaryUnit
 *          InfantryDefenseUnit
 *              - Infantry
 *              - Machine Guns
 *              - AAA Machine Guns
 *              - AAA MG Unit
 *              - Artillery (Direct Fire)
 *          
 *      GroundSpawningUnit
 *              
 *      GroundRepeatSpawningUnit
 *          - AAABattery
 *              
 *          ArtilleryBattery
 *              - Artillery (Fires at zone)
 *              
 *          AAABattery
 *              - AAA Artillery
 *              - AAA MG
 *              
 *          InfantryUnit
 *              - Infantry
 *              - Machine Guns
 *              - Tanks
 *              - Wagons
 *              
 *      GroundStaticUnit - Target for AI missions
 *          - StaticInfantry
 *              - Static Trucks
 *              - Static Artillery
 *              
 *          - AmmoDump
 *              - Tents
 *              - Ammo Objects
 *              - Static Trucks
 *
 */
public abstract class GroundUnit extends Unit
{
    protected GroundUnitInformation pwcgGroundUnitInformation;

    abstract public void createUnitMission() throws PWCGException ;
    abstract public List<IVehicle> getVehicles() ;
    abstract protected void createGroundTargetAssociations();

    public GroundUnit(GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super();
        this.pwcgGroundUnitInformation = pwcgGroundUnitInformation;
	}

    public boolean isCombatUnit()
    {
        if (pwcgGroundUnitInformation.getTargetType() == TacticalTarget.TARGET_ASSAULT ||
            pwcgGroundUnitInformation.getTargetType() == TacticalTarget.TARGET_DEFENSE ||
            pwcgGroundUnitInformation.getTargetType() == TacticalTarget.TARGET_INFANTRY)
        {
            return true;
        }

        return false;
    }
    
    public GroundUnitInformation getPwcgGroundUnitInformation()
    {
        return pwcgGroundUnitInformation;
    }
    
    public ICountry getCountry() throws PWCGException
    {
        return pwcgGroundUnitInformation.getCountry();
    }
    
    public Coordinate getPosition() throws PWCGException
    {
        return pwcgGroundUnitInformation.getPosition();
    }
    
    public String getName() throws PWCGException
    {
        return pwcgGroundUnitInformation.getName();
    }
    
    public MissionBeginUnit getMissionBeginUnit()
    {
        return pwcgGroundUnitInformation.getMissionBeginUnit();
    }
}	

