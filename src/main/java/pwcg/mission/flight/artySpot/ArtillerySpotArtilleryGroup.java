package pwcg.mission.flight.artySpot;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PositionsManager;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.ww1.ground.vehicle.Artillery;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotArtilleryGroup extends GroundUnit
{
    public static final int MAX_ARTILLERY_RANGE = 10000;
    public static final int NUM_ARTILLERY = 4;
	
	protected ArrayList<Artillery> arty = new ArrayList<Artillery>();

	protected double heading = 90;
	protected McuTimer activateTimer = new McuTimer();
	protected McuActivate activate = new McuActivate();
	protected McuTimer deactivateTimer = new McuTimer();
	protected McuDeactivate deactivate = new McuDeactivate();
	protected Campaign campaign;

    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    
	public ArtillerySpotArtilleryGroup (Campaign campaign) 
	{
		super(TacticalTarget.TARGET_ARTILLERY);
		
		this.campaign = campaign;
	}

	public void initialize (
	                MissionBeginUnit missionBeginUnit,
	                Coordinate targetPosition, 
	                ICountry country) throws PWCGException 
	{
		PositionsManager positionsManager = new PositionsManager(campaign.getDate());
		
		String airfieldName = campaign.getAirfieldName();
        IAirfield field =  PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldName);

		Coordinate artilleryPosition = positionsManager.getClosestDefinitePosition(country.getSide(), 
																		  field.getPosition().copy());	

		heading = MathUtils.calcAngle(artilleryPosition, targetPosition);		

		// Make sure that the artillery is in range
		if (MathUtils.calcDist(targetPosition, artilleryPosition) > MAX_ARTILLERY_RANGE)
		{
			double angle = MathUtils.calcAngle(targetPosition, artilleryPosition);
			artilleryPosition = MathUtils.calcNextCoord(targetPosition, angle, MAX_ARTILLERY_RANGE);
		}
		
		String countryName = country.getNationality();
		String name = countryName + " Artillery Group";
		
		super.initialize (missionBeginUnit, name, artilleryPosition, targetPosition, country);
	}
	
	/**
	 *  Return all vehicles in the unit
	 */
	public List<IVehicle> getVehicles() 
	{
		List<IVehicle> vehicles = new ArrayList<IVehicle>();
		
		vehicles.addAll(arty);
		
		return vehicles;
	}

	   
    /**
     *  Return all vehicles in the unit
     */
    public int getLeadIndex() 
    {
        return arty.get(0).getEntity().getIndex();
    }

	/**
	 * Create a mission for this flight
	 * @throws PWCGException 
	 * 
	 * @
	 */
	@Override
	public void createUnitMission() throws PWCGException  
	{
		createArtillery();
		createActivation();
		createGroundTargetAssociations();
	}

    protected void createArtillery() throws PWCGException 
    {
        Artillery gunType = new Artillery(country);
                
        for (int i = 0; i < NUM_ARTILLERY; ++i)
        {
            Artillery gun = gunType.copy();
            gun.getEntity().setEnabled(1);

            Coordinate position = getRandomPosition();
            gun.setPosition(position);

            Orientation orient = new Orientation();
            orient.setyOri(heading);
            gun.setOrientation(orient);
            
            arty.add(gun);
            
            if (i > 0)
            {
                gun.getEntity().setTarget(arty.get(0).getEntity().getIndex());
            }
        }       
    }

	protected void createActivation()
	{
		activate.setName(name + ": Activate");		
		activate.setDesc("Activate for " + name);
		activate.setPosition(position.copy());
		
		activateTimer = new McuTimer();
		activateTimer.setName(name + ": Activate Timer");		
		activateTimer.setDesc("Activate Timer for " + name);
		activateTimer.setPosition(position.copy());

		deactivate.setName(name + ": Deactivate");		
		deactivate.setDesc("Deactivate for " + name);
		deactivate.setPosition(position.copy());
		
		deactivateTimer = new McuTimer();
		deactivateTimer.setName(name + ": Deactivate Timer");		
		deactivateTimer.setDesc("Deactivate Timer for " + name);
		deactivateTimer.setPosition(position.copy());
	}

    public void createSubtitles()
    {
        McuSubtitle artilleryReadySubtitle = new McuSubtitle();
        artilleryReadySubtitle.setName("artilleryReadySubtitle Subtitle");
        artilleryReadySubtitle.setText("Artillery Ready");
        artilleryReadySubtitle.setPosition(position.copy());
        activateTimer.setTarget(artilleryReadySubtitle.getIndex());
        subTitleList.add(artilleryReadySubtitle);
        
        McuSubtitle artilleryCancelledSubtitle = new McuSubtitle();
        artilleryCancelledSubtitle.setName("artilleryCancelledSubtitle Subtitle");
        artilleryCancelledSubtitle.setText("Artillery Cancelled");
        artilleryCancelledSubtitle.setPosition(position.copy());
        deactivateTimer.setTarget(artilleryCancelledSubtitle.getIndex());
        subTitleList.add(artilleryCancelledSubtitle);

        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(artilleryReadySubtitle.getLcText(), artilleryReadySubtitle.getText()); 
        subtitleHandler.registerMissionText(artilleryCancelledSubtitle.getLcText(), artilleryCancelledSubtitle.getText()); 
    }

	private Coordinate getRandomPosition() throws PWCGException 
	{
		Coordinate position = new Coordinate();
		
		int xoffset = RandomNumberGenerator.getRandom(1000);
		int zoffset = RandomNumberGenerator.getRandom(1000);
		
		position.setXPos(getPosition().getXPos() + xoffset - 500);
		position.setZPos(getPosition().getZPos() + zoffset - 500);
		
		return position;
	}

	@Override
	protected void createGroundTargetAssociations()
	{
	    missionBeginUnit.linkToMissionBegin(activateTimer.getIndex());
		activateTimer.setTarget(activate.getIndex());
		
		for (Artillery gun : arty)
		{
			activate.setObject(gun.getEntity().getIndex());
			deactivate.setObject(gun.getEntity().getIndex());
		}
		
		deactivateTimer.setTarget(deactivate.getIndex());
	}

	@Override
	final public void write(BufferedWriter writer) throws PWCGIOException 
	{	
	    try
	    {
	        missionBeginUnit.write(writer);
	        
    		activateTimer.write(writer);
    		activate.write(writer);
    		
    		deactivateTimer.write(writer);
    		deactivate.write(writer);
    		
    		for (Artillery gun: arty)
    		{
    			gun.write(writer);
    		}
    
    		for (int i = 0; i < subTitleList.size(); ++i)
            {
                McuSubtitle subtitle = subTitleList.get(i);
                subtitle.write(writer);
                writer.newLine();
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	public McuTimer getActivateTimer() 
	{
		return activateTimer;
	}

	public McuTimer getDeactivateTimer() 
	{
		return deactivateTimer;
	}

}	

