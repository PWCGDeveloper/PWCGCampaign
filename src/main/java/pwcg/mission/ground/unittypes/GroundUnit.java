package pwcg.mission.ground.unittypes;

import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.location.Coordinate;
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
    protected TacticalTarget targetType = TacticalTarget.TARGET_NONE;
    protected Coordinate destinationCoords = null;    
    protected boolean isFiring = false;

    abstract protected void createGroundTargetAssociations();

    public GroundUnit(TacticalTarget targetType) 
	{
        super();
        this.targetType = targetType;
	}

    public void initialize (
                    MissionBeginUnit missionBeginUnit, 
                    String name, 
                    Coordinate position, 
                    Coordinate destinationCoords, 
                    ICountry country) 
    {
        this.destinationCoords = destinationCoords;
        super.initialize(missionBeginUnit, position, name, country);
    }

    public TacticalTarget getTargetType() 
    {
        return targetType;
    }   
    
    public boolean isFiring()
    {
        return this.isFiring;
    }

    public void setFiring(boolean isFiring)
    {
        this.isFiring = isFiring;
    }

    public boolean isCombatUnit()
    {
        if (targetType == TacticalTarget.TARGET_ASSAULT ||
            targetType == TacticalTarget.TARGET_DEFENSE ||
            targetType == TacticalTarget.TARGET_INFANTRY)
        {
            return true;
        }

        return false;
    }

    abstract public List<IVehicle> getVehicles() ;

}	

