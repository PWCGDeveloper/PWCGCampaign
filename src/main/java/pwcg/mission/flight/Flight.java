package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;
import pwcg.mission.IUnit;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.Unit;
import pwcg.mission.flight.escort.EscortForPlayerFlight;
import pwcg.mission.flight.escort.VirtualEscortFlight;
import pwcg.mission.flight.initialposition.FlightPositionSetter;
import pwcg.mission.flight.initialposition.TakeoffBuilder;
import pwcg.mission.flight.objective.MissionObjectiveFactory;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.ActualWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;

public abstract class Flight extends Unit
{
    protected int flightId = -1;
    protected FlightInformation flightInformation;

    protected List<PlaneMCU> planes = new ArrayList<PlaneMCU>();
    protected int numPlanesInFlight = 4;

    protected McuTimer formationTimer = null;
    protected McuFormation formationEntity = null;
    protected McuTimer activationTimer = null;
    protected McuActivate activationEntity = null;

    protected McuTakeoff takeoff = null;
    protected McuLanding landing = null;
    protected WaypointPackage waypointPackage = null;

    protected List<Integer> contactWithPlayer = new ArrayList<Integer>();
    protected double closestContactWithPlayerDistance = -1.0;
    protected boolean flightHasBeenIncluded = false;
    
    private MissionBeginUnit missionBeginUnit;

    protected VirtualEscortFlight virtualEscortFlight = null;

    protected List<Bridge> bridgeTargets = new ArrayList<Bridge>();
    protected List<Block> trainTargets = new ArrayList<Block>();
    protected List<IAirfield> airfieldTargets = new ArrayList<IAirfield>();
    
    abstract protected void createFlightSpecificTargetAssociations() throws PWCGException;
    abstract protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException;

    public Flight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super();
        flightId = IndexGenerator.getInstance().getNextIndex();
        this.flightInformation = flightInformation;
        this.missionBeginUnit = missionBeginUnit;
        this.planes = flightInformation.getPlanes();
    }

    public void createUnitMission() throws PWCGException 
    {
        createWaypointPackage();
        createWaypoints();
        FlightPositionSetter.setFlightInitialPosition(this);
        WaypointGeneratorUtils.setWaypointsNonFighterPriority(this);
        createTakeoff();
        createLanding();
        createActivation();
        createFormation();
        setFlightPayload();
    }
    
    protected void createWaypointPackage() throws PWCGException
    {
        if (flightInformation.isPlayerFlight())
        {
            waypointPackage = new ActualWaypointPackage(this);
        }
        else if (flightInformation.isVirtual())
        {
            waypointPackage = new VirtualWaypointPackage(this);
        }
        else
        {
            waypointPackage = new ActualWaypointPackage(this);
        }
    }

    protected void createWaypoints() throws PWCGException
    {
        Coordinate startPosition = flightInformation.getDepartureAirfield().getTakeoffLocation().getPosition().copy();
        List<McuWaypoint> waypointList = createWaypoints(flightInformation.getMission(), startPosition);
        waypointPackage.setWaypoints(waypointList);
    }

    protected void createTakeoff() throws PWCGException
    {
        takeoff = TakeoffBuilder.createTakeoff(this);
    }

    protected void createLanding() throws PWCGException, PWCGException
    {
        LandingBuilder landingBuilder = new LandingBuilder(flightInformation.getCampaign());
        landing = landingBuilder.createLanding(flightInformation.getDepartureAirfield());
    }
    
    protected void createActivation() throws PWCGException, PWCGException 
    {
        if (flightInformation.isPlayerFlight())
        {
            if (!flightInformation.isAirStart())
            {
                missionBeginUnit.linkToMissionBegin(takeoff.getIndex());

                ConfigManagerCampaign configManager = flightInformation.getCampaign().getCampaignConfigManager();
                                
                int takeoffTime = configManager.getIntConfigParam(ConfigItemKeys.TakeoffTimeKey);
                // Sea plane missions have to take off faster to avoid drifting
                // off course
                if (takeoffTime > 30)
                {
                    takeoffTime = 30;
                }

                missionBeginUnit.setStartTime(takeoffTime);
            }
            else
            {
                List<McuWaypoint> waypoints = waypointPackage.getWaypointsForLeadPlane();
                if (waypoints.size() > 0)
                {
                    McuWaypoint firstWP = waypoints.get(0);
                    missionBeginUnit.linkToMissionBegin(firstWP.getIndex());
                    missionBeginUnit.setStartTime(1);
                }
            }
        }
        else
        {
            activationEntity = new McuActivate();
            activationEntity.setName(getName() + ": Activate");
            activationEntity.setDesc("Activate entity for " + getName());
            activationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());

            activationTimer = new McuTimer();
            activationTimer.setName(getName() + ": Activation Timer");
            activationTimer.setDesc("Activation Timer for " + getName());
            activationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
            activationTimer.setTarget(activationEntity.getIndex());
        }
    }


    protected void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }

    public boolean isBombingFlight()
    {
        if (flightInformation.getFlightType() == FlightTypes.BOMB ||
            flightInformation.getFlightType() == FlightTypes.LOW_ALT_BOMB ||
            flightInformation.getFlightType() == FlightTypes.GROUND_ATTACK ||
            flightInformation.getFlightType() == FlightTypes.DIVE_BOMB ||
            flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_BOMB || 
            flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_ATTACK || 
            flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING_DIVE_BOMB ||
            flightInformation.getFlightType() == FlightTypes.STRATEGIC_BOMB)
        {
            return true;
        }
        
        return false;
    }

    public ICountry getCountry() throws PWCGException
    {
        return flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate());
    }

    public Coordinate getPosition() throws PWCGException
    {
        return flightInformation.getSquadron().determineCurrentPosition(flightInformation.getCampaign().getDate());
    }

    public String getName() throws PWCGException
    {
        return flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate());
    }

    public void createTargetAssociationsForFlight() throws PWCGException
    {
        createFlightSpecificTargetAssociations();
        createLandingAssociation();
    }

    public void createSimpleTargetAssociations() throws PWCGException
    {
        if (flightInformation.isVirtual())
        {
            waypointPackage.duplicateWaypointsForFlight(this);
            for (PlaneMCU plane : getPlanes())
            {
                List<McuWaypoint>waypointsToLink = waypointPackage.getWaypointsForPlane(plane);
                createWaypointTargetAssociationsSimple(plane, waypointsToLink);
            }
        }
        else
        {
            List<McuWaypoint>waypointsToLink = waypointPackage.getWaypointsForLeadPlane();
            createWaypointTargetAssociationsSimple(getLeadPlane(), waypointsToLink);
        }
    }

    protected void createFormation() throws PWCGException
    {

        formationEntity = new McuFormation();
        formationEntity.setPosition(flightInformation.getDepartureAirfield().getPosition());

        formationTimer = new McuTimer();
        formationTimer.setName(flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()) + ": Formation Timer");
        formationTimer.setDesc("Formation timer entity for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        formationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
        formationTimer.setTarget(formationEntity.getIndex());
        formationTimer.setTimer(2);
    }

    private void createWaypointTargetAssociationsSimple(PlaneMCU plane, List<McuWaypoint>waypointsToLink)
    {
        linkWPToPlane(plane, waypointsToLink);
        
        if (waypointsToLink.size() > 0)
        {
            McuWaypoint prevWP = null;

            // TL each waypoint to the next one
            for (McuWaypoint waypoint: waypointsToLink)
            {
                if (prevWP != null)
                {
                    prevWP.setTarget(waypoint.getIndex());
                }
                
                prevWP = waypoint;
            }                        
        }
    }

    private void createLandingAssociation()
    {
        if (landing != null)
        {
            McuWaypoint approachWP = waypointPackage.getWaypointsForLeadPlane().get(waypointPackage.getWaypointsForLeadPlane().size() - 1);
            approachWP.setTarget(getLanding().getIndex());
        }
    }

    public void finalizeFlight() throws PWCGException 
    {
        FlightFinalizer finalizer = new FlightFinalizer(this);
        finalizer.finalizeFlight();
    }
    
    protected void enableNonVirtualFlight()
    {
        if (!flightInformation.isVirtual())
        {
            for (PlaneMCU plane : planes)
            {
                plane.getEntity().setEnabled(1);
            }
        }
    }

    protected void setWaypointOrientation() throws PWCGException 
    {
        // TL each waypoint to the next one
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : waypointPackage.getWaypointsForLeadPlane())
        {
            if (prevWP != null)
            {
                Coordinate prevPos = prevWP.getPosition().copy();
                Coordinate nextPos = nextWP.getPosition().copy();
    
                Orientation orient = new Orientation();
                orient.setyOri(MathUtils.calcAngle(prevPos, nextPos));
    
                prevWP.setOrientation(orient);
            }
            
            prevWP = nextWP;
        }
    }

    protected void linkWPToPlane(PlaneMCU plane, List<McuWaypoint>waypointsToLink)
    {
        // Get the flight leader entity
        for (BaseFlightMcu mcu : waypointsToLink)
        {
            mcu.clearObjects();
            mcu.setObject(plane.getLinkTrId());
        }
    }

    public List<McuWaypoint> getAllFlightWaypoints()
    {
        List<McuWaypoint> returnWaypoints = waypointPackage.getWaypointsForLeadPlane();
        if (waypointPackage.getWaypointsForLeadPlane().size() == 0)
        {
            for (IUnit linkedUnit : this.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    Flight linkedFlight = (Flight) linkedUnit;

                    returnWaypoints = linkedFlight.getWaypointsNoSearch().getWaypointsForLeadPlane();
                    if (returnWaypoints != null && returnWaypoints.size() > 0)
                    {
                        return returnWaypoints;
                    }
                }
                else
                {
                    continue;
                }
            }
        }

        return returnWaypoints;
    }

    protected List<McuWaypoint> getAllWaypointsForPlane(PlaneMCU plane)
    {
        List<McuWaypoint> returnWaypoints = waypointPackage.getWaypointsForPlane(plane);
        if (waypointPackage.getWaypointsForLeadPlane().size() == 0)
        {
            for (IUnit linkedUnit : this.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    Flight linkedFlight = (Flight) linkedUnit;

                    returnWaypoints = linkedFlight.getWaypointsNoSearch().getWaypointsForPlane(plane);
                    if (returnWaypoints != null && returnWaypoints.size() > 0)
                    {
                        return returnWaypoints;
                    }
                }
                else
                {
                    continue;
                }
            }
        }

        return returnWaypoints;
    }

    public WaypointPackage getLinkedWaypoints()
    {
        for (IUnit linkedUnit : linkedUnits)
        {
            if (linkedUnit instanceof Flight)
            {
                Flight linkedFlight = (Flight) linkedUnit;

                return linkedFlight.getWaypointsNoSearch();
            }
        }

        return null;
    }

    /**
     * @return
     */
    public WaypointPackage getWaypoints()
    {
        return waypointPackage;
    }

    public double calcFlightDistance() 
    {
        double flightWPDistance = 0.0;
        int i = 0;
        McuWaypoint prevWP = null;
        for (McuWaypoint wp : getAllFlightWaypoints())
        {
            if (i == 0)
            {
                prevWP = wp;
                ++i;
                continue;
            }

            flightWPDistance += MathUtils.calcDist(prevWP.getPosition(), wp.getPosition());
            prevWP = wp;
            ++i;
        }

        return flightWPDistance;
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int i = 0; i < planes.size(); ++i)
        {
            PlaneMCU plane = planes.get(i);
            plane.write(writer);
        }

        waypointPackage.write(writer);

        if (takeoff != null)
        {
            takeoff.write(writer);
        }

        if (landing != null)
        {
            landing.write(writer);
        }
                
        if (activationEntity != null)
        {
            activationTimer.write(writer);
            activationEntity.write(writer);
        }

        if (formationEntity != null)
        {
            formationTimer.write(writer);
            formationEntity.write(writer);
        }

        missionBeginUnit.write(writer);
    }

    public int getTotalDistance()
    {
        int totalDistance = 0;

        McuWaypoint prevWP = null;
        
        // This does get called before waypoints are created.  In this case just return zero
        List<McuWaypoint> originalWPList =  waypointPackage.getWaypointsForLeadPlane();
        if (originalWPList != null)
        {
            for (McuWaypoint wp : waypointPackage.getWaypointsForLeadPlane())
            {
                int distanceAsInt = 0;
                if (prevWP != null)
                {
                    double distanceExact = MathUtils.calcDist(prevWP.getPosition(), wp.getPosition());
                    distanceAsInt = new Double(distanceExact).intValue();
                    totalDistance += distanceAsInt;
                }
    
                prevWP = wp;
            }
        }

        return totalDistance;
    }

    public List<Coordinate> getAllMissionCoordinates()
    {
        List<Coordinate> allMissionPointsForPlane = new ArrayList<>();
        allMissionPointsForPlane.add(getLeadPlane().getPosition());
        
        List<McuWaypoint> allWaypoints = this.getAllWaypointsForPlane(getLeadPlane());
        for (McuWaypoint waypoint : allWaypoints)
        {
            allMissionPointsForPlane.add(waypoint.getPosition());
        }
        
        return allMissionPointsForPlane;
    }

    public List<BaseFlightMcu> getAllMissionPoints()
    {
        return getAllMissionPointsForPlane(getLeadPlane());
    }

    public List<BaseFlightMcu> getAllMissionPointsForPlane(PlaneMCU plane)
    {
        List<BaseFlightMcu> allMissionPointsForPlane = new ArrayList<>();        
        List<McuWaypoint> missionWP = waypointPackage.getWaypointsForPlane(plane);
        if (missionWP != null)
        {
            for (McuWaypoint waypoint : missionWP)
            {
                allMissionPointsForPlane.add(waypoint);
            }
        }

        return allMissionPointsForPlane;
    }

    public void setFuel(double myFuel) 
    {
        for (PlaneMCU plane : getPlanes())
        {
            plane.setFuel(myFuel);
        }
    }

    public List<PlaneMCU> getAiPlanes() throws PWCGException 
    {
        List<PlaneMCU> aiPlanes = new ArrayList<>();
        for (PlaneMCU plane : planes)
        {
            if (!plane.getPilot().isPlayer())
            {
                aiPlanes.add(plane);
            }
        }

        return aiPlanes;
    }

    public List<PlaneMCU> getPlayerPlanes() throws PWCGException 
    {
        List<PlaneMCU> playerPlanes = new ArrayList<>();
        for (PlaneMCU plane : planes)
        {
            if (plane.getPilot().isPlayer())
            {
                playerPlanes.add(plane);
            }
        }

        return playerPlanes;
    }

    public PlaneMCU getPlaneForPilot(Integer pilotSerialNumber)
    {
        PlaneMCU pilotPlane = null;
        for (PlaneMCU plane : planes)
        {
            if (plane.getPilot().getSerialNumber() == pilotSerialNumber)
            {
                pilotPlane = plane;
                break;
            }
        }

        return pilotPlane;
    }

    public void updateWaypoints(List<McuWaypoint> modifiedWaypoints) 
    {
        waypointPackage.setWaypoints(modifiedWaypoints);
    }

    public WaypointPackage getWaypointsNoSearch()
    {
        return waypointPackage;
    }

    public int getFlightId()
    {
        return this.flightId;
    }

    public McuTakeoff getTakeoff()
    {
        return takeoff;
    }

    public void setTakeoff(McuTakeoff takeoff)
    {
        this.takeoff = takeoff;
    }

    public McuFormation getFormation()
    {
        return formationEntity;
    }

    public int getNumPlanes()
    {
        return numPlanesInFlight;
    }

    public PlaneMCU getFlightLeader()
    {
        return planes.get(0);
    }

    public List<PlaneMCU> getPlanes()
    {
        return planes;
    }

    public int getFirstContactWithPlayer()
    {
        int firstContactWithPlayer = -1;
        
        if (contactWithPlayer.size() > 0)
        {
            firstContactWithPlayer = contactWithPlayer.get(0);
        }
        
        return firstContactWithPlayer;
    }

    public int getLastContactWithPlayer()
    {
        int lastContactWithPlayer = -1;
        
        if (contactWithPlayer.size() > 0)
        {
            lastContactWithPlayer = contactWithPlayer.get(contactWithPlayer.size() - 1);
        }
        
        return lastContactWithPlayer;
    }

    public void setContactWithPlayer(int contact)
    {
        contactWithPlayer.add(contact);
    }

    public boolean isFighterMission()
    {
        boolean isFighterMission = false;
        if (this.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            isFighterMission = true;
        }

        return isFighterMission;
    }

    public void dump() throws PWCGException
    {
        Logger.log(LogLevel.DEBUG, "Flight Type: " + flightInformation.getFlightType() + "       Squad: " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
    }

    public void dumpVerpose() throws PWCGException
    {
        Logger.log(LogLevel.DEBUG, "Flight id   : " + flightId);
        Logger.log(LogLevel.DEBUG, "       Squad: " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        Logger.log(LogLevel.DEBUG, "        Type: " + flightInformation.getFlightType());
        Logger.log(LogLevel.DEBUG, "        Type: " + planes.get(0).getDisplayName());
        Logger.log(LogLevel.DEBUG, "        Size: " + planes.size());
    }

    public WaypointPackage getWaypointPackage()
    {
        return this.waypointPackage;
    }

    public double getClosestContactWithPlayerDistance()
    {
        return this.closestContactWithPlayerDistance;
    }

    public void setClosestContactWithPlayerDistance(double newClosestDistance)
    {
        if (newClosestDistance > 0.0)
        {
            if (this.closestContactWithPlayerDistance <= 0.0 || newClosestDistance < this.closestContactWithPlayerDistance)
            {
                this.closestContactWithPlayerDistance = newClosestDistance;
            }
        }
    }

    public void addFlightTarget(Flight targetFlight)
    {
        for (PlaneMCU plane : planes)
        {
            if (isAggressivePlane(plane))
            {
                for (PlaneMCU targetPlane : targetFlight.getPlanes())
                {
                    plane.addPlaneTarget(targetPlane.getEntity().getIndex());
                }
            }
        }
    }

    public void addGroundUnitTarget(IGroundUnit targetGroundUnit)
    {
        for (PlaneMCU plane : planes)
        {
            if (isAggressivePlane(plane))
            {
                plane.addPlaneTarget(targetGroundUnit.getVehicle().getEntity().getIndex());
            }
        }
    }

    private boolean isAggressivePlane(PlaneType plane)
    {
        if (plane.isPrimaryRole(Role.ROLE_FIGHTER))
        {
            return true;
        }

        if (flightInformation.getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            return true;
        }

        return false;
    }

    public McuWaypoint findFirstStartWaypoint()
    {
        List<McuWaypoint> waypoints = getWaypointPackage().getWaypointsForLeadPlane();
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_INGRESS) ||
                waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEZVOUS) ||
                waypoint.getWpAction().equals(WaypointAction.WP_ACTION_START) ||
                waypoint.isTargetWaypoint())
            {
                return waypoint;
            }
        }

        return waypoints.get(0);
     }

    public int getFlightCruisingSpeed()
    {
        int cruisingSpeed = planes.get(0).getCruisingSpeed();
        for (PlaneMCU plane : planes)
        {
            if (plane.getCruisingSpeed() < cruisingSpeed)
            {
                cruisingSpeed = plane.getCruisingSpeed();
            }
        }
        
        return cruisingSpeed;
    }

    public int getFlightAltitude()
    {
        return flightInformation.getAltitude();
    }

    public boolean isLowAltFlightType()
    {
        if (flightInformation.getFlightType() == FlightTypes.ARTILLERY_SPOT    ||
            flightInformation.getFlightType() == FlightTypes.CONTACT_PATROL    ||
            flightInformation.getFlightType() == FlightTypes.GROUND_ATTACK     ||
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE          ||
            flightInformation.getFlightType() == FlightTypes.SEA_PATROL        ||
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE          ||
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE          ||
            flightInformation.getFlightType() == FlightTypes.SPY_EXTRACT)
        {
            return true;
        }
        
        return false;
    }

    public EscortForPlayerFlight getEscortForPlayer()
    {
        EscortForPlayerFlight escortForPlayerFlight = null;
        for (IUnit unit : getLinkedUnits())
        {
            if (unit instanceof EscortForPlayerFlight)
            {
                escortForPlayerFlight = (EscortForPlayerFlight)unit;
            }
        }
        return escortForPlayerFlight;
    }
    
    public String getMissionObjective() throws PWCGException
    {
        return MissionObjectiveFactory.formMissionObjective(this);
    }

    public boolean isFlightHasFighterPlanes()
    {
        return getPlanes().get(0).isPrimaryRole(Role.ROLE_FIGHTER);
    }

    public void linkGroundUnitsToFlight(IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        addLinkedUnit(groundUnitCollection);              
    }
    
    public List<Bridge> getBridgeTargets()
    {
        return bridgeTargets;
    }

    public void addBridgeTarget(Bridge bridge)
    {
        this.bridgeTargets.add(bridge);
    }

    public List<Block> getTrainTargets()
    {
        return trainTargets;
    }

    public void addTrainTargets(Block trainStation)
    {
        this.trainTargets.add(trainStation);
    }

    public List<IAirfield> getAirfieldTargets()
    {
        return airfieldTargets;
    }

    public void addAirfieldTargets(IAirfield airfield)
    {
        this.airfieldTargets.add(airfield);
    }
    
    public PlaneMCU getLeadPlane()
    {
        return planes.get(0);
    }

    public McuLanding getLanding()
    {
        return landing;
    }

    public void setLanding(McuLanding landing)
    {
        this.landing = landing;
    }

    public McuTimer getFormationTimer()
    {
        return formationTimer;
    }

    public void setFormationTimer(McuTimer formationTimer)
    {
        this.formationTimer = formationTimer;
    }

    public McuFormation getFormationEntity()
    {
        return formationEntity;
    }

    public void setFormationEntity(McuFormation formationEntity)
    {
        this.formationEntity = formationEntity;
    }

    public McuTimer getActivationTimer()
    {
        return activationTimer;
    }

    public void setActivationTimer(McuTimer activationTimer)
    {
        this.activationTimer = activationTimer;
    }

    public McuActivate getActivationEntity()
    {
        return activationEntity;
    }

    public void setActivationEntity(McuActivate activationEntity)
    {
        this.activationEntity = activationEntity;
    }

    public List<Integer> getContactWithPlayer()
    {
        return contactWithPlayer;
    }

    public void setContactWithPlayer(List<Integer> contactWithPlayer)
    {
        this.contactWithPlayer = contactWithPlayer;
    }

    public VirtualEscortFlight getVirtualEscortFlight()
    {
        return virtualEscortFlight;
    }

    public void setVirtualEscortFlight(VirtualEscortFlight virtualEscortFlight)
    {
        this.virtualEscortFlight = virtualEscortFlight;
    }

    public void setFlightId(int flightId)
    {
        this.flightId = flightId;
    }

    public void setPlanes(List<PlaneMCU> planes) throws PWCGException
    {
        this.planes = planes;        
    }

    public TargetCategory getTargetCategory()
    {
        return flightInformation.getTargetDefinition().getTargetCategory();
    }

    public TargetDefinition getTargetDefinition()
    {
        return flightInformation.getTargetDefinition();
    }

    public MissionBeginUnit getMissionBeginUnit()
    {
        return missionBeginUnit;
    }

    public FlightInformation getFlightInformation()
    {
        return flightInformation;
    }

    public FlightTypes getFlightType()
    {
        return flightInformation.getFlightType();
    }

    public Squadron getSquadron()
    {
        return flightInformation.getSquadron();
    }

    public Campaign getCampaign()
    {
        return flightInformation.getCampaign();
    }

    public Mission getMission()
    {
        return flightInformation.getMission();
    }

    public IAirfield getAirfield() throws PWCGException
    {
        return flightInformation.getDepartureAirfield();
    }

    public Coordinate getTargetPosition() throws PWCGException
    {
        return flightInformation.getTargetPosition().copy();
    }

    public boolean isPlayerFlight()
    {
        return flightInformation.isPlayerFlight();
    }

    public boolean isVirtual()
    {
        return flightInformation.isVirtual();
    }
    
    public boolean isAirStart() throws PWCGException
    {
        return flightInformation.isAirStart();
    } 

    public boolean isParkedStart() throws PWCGException
    {
        return flightInformation.isParkedStart();
    }
    
    public boolean isFlightHasBeenIncluded()
    {
        return flightHasBeenIncluded;
    }
    
    public void setFlightHasBeenIncluded(boolean flightHasBeenIncluded)
    {
        this.flightHasBeenIncluded = flightHasBeenIncluded;
    }

}
