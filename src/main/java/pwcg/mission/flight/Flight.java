package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.TargetCategory;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.Unit;
import pwcg.mission.flight.escort.VirtualEscortFlight;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.plane.PlaneMCUFactory;
import pwcg.mission.flight.waypoint.ActualWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointPackage;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.ground.GroundUnitCollection;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.staticunits.AirfieldStaticGroup;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuLanding;
import pwcg.mission.mcu.McuTakeoff;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public abstract class Flight extends Unit
{
    protected int flightId = -1;
    protected FlightInformation flightInformation;

    protected TargetDefinition targetDefinition = new TargetDefinition();

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
    protected int firstContactWithEnemy = -1;
    protected Flight firstContactWithEnemyFlight = null;

    protected int missionStartTimeAdjustment = 0;

    protected boolean nightFlight = false;
    
    private MissionBeginUnit missionBeginUnit;

    protected VirtualEscortFlight virtualEscortFlight = null;

    protected List<Bridge> bridgeTargets = new ArrayList<Bridge>();
    protected List<Block> trainTargets = new ArrayList<Block>();
    protected List<IAirfield> airfieldTargets = new ArrayList<IAirfield>();
    
    abstract protected void createFlightSpecificTargetAssociations() throws PWCGException;
    abstract protected int calcNumPlanes() throws PWCGException;
    abstract public String getMissionObjective() throws PWCGException ;
    abstract protected List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException;

    public Flight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit)
    {
        super();
        flightId = IndexGenerator.getInstance().getNextIndex();
        this.flightInformation = flightInformation;
        this.missionBeginUnit = missionBeginUnit;
    }

    public void createUnitMission() throws PWCGException 
    {
        calcPlanesInFlight();
        createWaypointPackage();
        createPlanes();
        createWaypoints();
        setPlayerInitialPosition();
        createActivation();
        createFormation();
        setFlightPayload();
        moveAirstartCloseToInitialWaypoint();
    }
    
    protected void calcPlanesInFlight() throws PWCGException
    {
        numPlanesInFlight = calcNumPlanes();
        if (numPlanesInFlight > 8)
        {
            numPlanesInFlight = 8;
        }
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

    protected void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder flightPayloadHelper = new FlightPayloadBuilder(this);
        flightPayloadHelper.setFlightPayload();
    }

    protected void createWaypoints() throws PWCGException
    {
        Coordinate startPosition = flightInformation.getDepartureAirfield().getTakeoffLocation().getPosition().copy();
        List<McuWaypoint> waypointList = createWaypoints(flightInformation.getMission(), startPosition);
        waypointPackage.setWaypoints(waypointList);
    }

    protected void setPlayerInitialPosition() throws PWCGException
    {
        if (flightInformation.isPlayerFlight())
        {
            if (!flightInformation.isAirStart())
            {
                createTakeoff();
            }
            else
            {
                advancePlayerAirStart();
            }
            
            FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(flightInformation.getCampaign(), this);
            flightPositionHelperPlayerStart.createPlayerPlanePosition();

            createLanding();
        }
    }

    protected void moveAirstartCloseToInitialWaypoint() throws PWCGException
    {
        if (flightInformation.isAirStart())
        {
            FlightPositionHelperPlayerStart flightPositionHelperPlayerStart = new FlightPositionHelperPlayerStart(flightInformation.getCampaign(), this);
            flightPositionHelperPlayerStart.createPlayerPlanePosition();
        }
    }

    public boolean isBombingFlight()
    {
        if (flightInformation.getFlightType() == FlightTypes.BOMB ||
            flightInformation.getFlightType() == FlightTypes.LOW_ALT_BOMB ||
            flightInformation.getFlightType() == FlightTypes.GROUND_ATTACK ||
            flightInformation.getFlightType() == FlightTypes.DIVE_BOMB ||
            flightInformation.getFlightType() == FlightTypes.ANTI_SHIPPING ||
            flightInformation.getFlightType() == FlightTypes.STRATEGIC_BOMB)
        {
            return true;
        }
        
        return false;
    }

    protected int modifyNumPlanes(int numPlanes)
    {
        if (numPlanes % 2 == 1)
        {
            return numPlanes + 1;
        }
        
        return numPlanes;
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

    protected void createLanding() throws PWCGException, PWCGException
    {
        LandingBuilder landingBuilder = new LandingBuilder(flightInformation.getCampaign());
        landing = landingBuilder.createLanding(flightInformation.getDepartureAirfield());
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

    protected void advancePlayerAirStart()
    {
        List<McuWaypoint> keptWaypoints = new ArrayList<McuWaypoint>();
        List<McuWaypoint> waypoints = waypointPackage.getWaypointsForLeadPlane();
        boolean keepIt = false;
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_INGRESS) ||
                waypoint.getWpAction().equals(WaypointAction.WP_ACTION_RENDEVOUS) ||
                waypoint.isTargetWaypoint())
            {
                keepIt = true;
            }

            if (keepIt)
            {
                keptWaypoints.add(waypoint);
            }
        }

        waypointPackage.setWaypoints(keptWaypoints);
    }

    public void resetFlightForPlayerEscort(Coordinate rendezvousCoords, Coordinate targetCoordinates) throws PWCGException
    {        
        List<McuWaypoint> originalFlightWaypoints = waypointPackage.getWaypointsForLeadPlane();
        // List<McuWaypoint> modifiedFlightWaypoints = new ArrayList<McuWaypoint>();
        
        // How far should we go from the rendezvous to the first WP
        double distanceBetweenAirStartAndFirstWP = 5000;
        double distanceBetweenAirStartAndTarget = MathUtils.calcDist(rendezvousCoords, targetCoordinates);
        if (distanceBetweenAirStartAndTarget < distanceBetweenAirStartAndFirstWP)
        {
            distanceBetweenAirStartAndFirstWP = distanceBetweenAirStartAndTarget / 2;
        }
        
        // Angle from rendezvous to ingress WP
        double angleToFirstWP = MathUtils.calcAngle(rendezvousCoords, targetCoordinates);
                        
        // reset the ingress and egress WPs
        for (McuWaypoint waypoint : originalFlightWaypoints)
        {
            double wpAltitiude = waypoint.getPosition().getYPos();
            
            if (waypoint.getWpAction().equals(WaypointAction.WP_ACTION_INGRESS) || waypoint.getWpAction().equals(WaypointAction.WP_ACTION_EGRESS))
            {
                Coordinate newIngressPosition = MathUtils.calcNextCoord(rendezvousCoords, angleToFirstWP, distanceBetweenAirStartAndFirstWP);
                newIngressPosition.setYPos(wpAltitiude);
                waypoint.setPosition(newIngressPosition);
            }
        }

        FlightPositionHelperAirStart flightPositionHelperAirStart = new FlightPositionHelperAirStart(flightInformation.getCampaign(), this);
        flightPositionHelperAirStart.createPlanePositionAirStart(rendezvousCoords, new Orientation());
    }

    public void finalizeFlight() throws PWCGException 
    {
        FlightFinalizer finalizer = new FlightFinalizer(this);
        finalizer.finalizeFlight();
    }

    protected void createPlanes() throws PWCGException 
    {        
        PlaneMCUFactory planeGeneratorPlayer = new PlaneMCUFactory(flightInformation.getCampaign(), flightInformation.getSquadron(), this);
        planes = planeGeneratorPlayer.createPlanesForFlight(numPlanesInFlight);
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

    public void createFormation() throws PWCGException
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

    public void createTakeoff() throws PWCGException
    {
        takeoff = new McuTakeoff();
        takeoff.setPosition(flightInformation.getDepartureAirfield().getTakeoffLocation().getPosition().copy());
        takeoff.setOrientation(flightInformation.getDepartureAirfield().getTakeoffLocation().getOrientation().copy());
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

    public Coordinate getCoordinatesToIntersectWithPlayer() throws PWCGException 
    {
        List<McuWaypoint> potentialWaypoints = waypointPackage.getWaypointsForLeadPlane();

        if (potentialWaypoints.size() == 0)
        {
            List<Unit> linkedUnits = flightInformation.getMission().getMissionFlightBuilder().getPlayerFlight().getLinkedUnits();
            for (Unit linkedUnit : linkedUnits)
            {
                if (linkedUnit instanceof Flight)
                {
                    Flight linkedFlight = (Flight) linkedUnit;

                    potentialWaypoints = linkedFlight.getWaypointsNoSearch().getWaypointsForLeadPlane();
                    if (potentialWaypoints != null && potentialWaypoints.size() > 0)
                    {
                        break;
                    }
                }
                else
                {
                    throw new PWCGMissionGenerationException("getTargetCoordinates: Attempt to get waypoints.getWaypoints() for non flight unit "
                                    + linkedUnit.getName());
                }
            }
        }

        List<McuWaypoint> selectedWaypoints = new ArrayList<McuWaypoint>();
        for (int i = 0; i < potentialWaypoints.size(); ++i)
        {
            McuWaypoint playerWaypoint = potentialWaypoints.get(i);
            if (playerWaypoint.getName().contains(WaypointType.RECON_WAYPOINT.getName()))
            {
                selectedWaypoints.add(playerWaypoint);
            }
        }

        Coordinate targetCoordinates = null;

        if (selectedWaypoints.size() == 0)
        {
            targetCoordinates = flightInformation.getMission().getMissionFlightBuilder().getPlayerFlight().getPlanes().get(0).getPosition().copy();
        }
        else
        {
            McuWaypoint selectedWaypoint = selectedWaypoints.get(RandomNumberGenerator.getRandom(selectedWaypoints.size()));
            targetCoordinates = selectedWaypoint.getPosition().copy();
        }

        return targetCoordinates;
    }

    /**
     * Set the orientation of a waypoint
     * @throws PWCGException 
     * 
     * @
     */
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

    /**
     * @throws PWCGException 
     * @
     */
    public void moveToStartPosition() throws PWCGException 
    {
        // Never move the player flight
        if (flightInformation.isPlayerFlight())
        {
            return;
        }

        // Do not move a scramble opposing flights
        if (flightInformation.getFlightType() == FlightTypes.SCRAMBLE_OPPOSE)
        {
            return;
        }

        // Flights escorted by the player go to the bombing approach
        if (!flightInformation.isVirtual())
        {
            if (waypointPackage instanceof ActualWaypointPackage)
            {
                ActualWaypointPackage actualWaypoints = (ActualWaypointPackage) waypointPackage;
                actualWaypoints.movePlayerEscortFlightToBombApproach();

                return;
            }
        }

        // Set the mission start time adjustment to +/- 10 minutes
        // The mission will be advanced or delayed when VWPs are assigned
        missionStartTimeAdjustment = 15 - RandomNumberGenerator.getRandom(30);

        // Provide the same adjustment to a virtual escort
        if (this.virtualEscortFlight != null)
        {
            virtualEscortFlight.setMissionStartTimeAdjustment(missionStartTimeAdjustment);
        }
    }
    
    /**
     * Link a set of MCUs (WP, attackArea, etc) to a plane.
     * For virtual flights every plane needs a set of links
     * FOr non spawned flights only the leader need be linked
     * 
     * @param plane
     * @param itemsToLink
     */
    protected void linkWPToPlane(PlaneMCU plane, List<McuWaypoint>waypointsToLink)
    {
        // Get the flight leader entity
        for (BaseFlightMcu mcu : waypointsToLink)
        {
            mcu.clearObjects();
            mcu.setObject(plane.getLinkTrId());
        }
    }

    
    
    /**
     * @return
     */
    public List<McuWaypoint> getAllWaypoints()
    {
        List<McuWaypoint> returnWaypoints = waypointPackage.getWaypointsForLeadPlane();
        if (waypointPackage.getWaypointsForLeadPlane().size() == 0)
        {
            List<Unit> linkedUnits = flightInformation.getMission().getMissionFlightBuilder().getPlayerFlight().getLinkedUnits();
            for (Unit linkedUnit : linkedUnits)
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

    

    
    /**
     * @return
     */
    protected List<McuWaypoint> getAllWaypointsForPlane(PlaneMCU plane)
    {
        List<McuWaypoint> returnWaypoints = waypointPackage.getWaypointsForPlane(plane);
        if (waypointPackage.getWaypointsForLeadPlane().size() == 0)
        {
            List<Unit> linkedUnits = flightInformation.getMission().getMissionFlightBuilder().getPlayerFlight().getLinkedUnits();
            for (Unit linkedUnit : linkedUnits)
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

    /**
     * @return
     */
    public WaypointPackage getLinkedWaypoints()
    {
        for (Unit linkedUnit : linkedUnits)
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
        for (McuWaypoint wp : getAllWaypoints())
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

    public double calcPlayerFlightDistance() 
    {
        Flight myFlight = flightInformation.getMission().getMissionFlightBuilder().getPlayerFlight();
        return myFlight.calcFlightDistance();

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

    public List<BaseFlightMcu> getAllMissionPoints()
    {
        List<BaseFlightMcu> allMissionPoints = new ArrayList<BaseFlightMcu>();
        
        List<McuWaypoint> missionWP = waypointPackage.getWaypointsForLeadPlane();
        if (missionWP != null)
        {
            for (McuWaypoint waypoint : missionWP)
            {
                allMissionPoints.add(waypoint.copy());
            }
        }

        return allMissionPoints;
    }

    public List<BaseFlightMcu> getAllMissionPointsForPlane(PlaneMCU plane)
    {
        List<BaseFlightMcu> allMissionPointsForPlane = new ArrayList<BaseFlightMcu>();
        
        List<McuWaypoint> missionWP = waypointPackage.getWaypointsForPlane(plane);
        if (missionWP != null)
        {
            for (McuWaypoint waypoint : missionWP)
            {
                allMissionPointsForPlane.add(waypoint.copy());
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

    public boolean isNightFlight()
    {
        return nightFlight;
    }

    public void setNightFlight(boolean nightFlight)
    {
        this.nightFlight = nightFlight;
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

    public int getFirstContactWithEnemy()
    {
        return firstContactWithEnemy;
    }

    public Flight getFirstContactWithEnemyFlight()
    {
        return firstContactWithEnemyFlight;
    }

    public void setFirstContactWithEnemy(int firstContactWithEnemy, Flight enemyFlight)
    {
        if (this.firstContactWithEnemy == -1 || this.firstContactWithEnemy > firstContactWithEnemy)
        {
            this.firstContactWithEnemy = firstContactWithEnemy;
            this.firstContactWithEnemyFlight = enemyFlight;
            // Make the escort move in lock step
            if (virtualEscortFlight != null)
            {
                virtualEscortFlight.setFirstContactWithEnemy(firstContactWithEnemy, enemyFlight);

            }
        }
    }

    public void createEscortForPlayerFlight() throws PWCGException 
    {
        PlayerEscortBuilder playerEscortBuilder = new PlayerEscortBuilder();
        Flight escortForPlayerFlight = playerEscortBuilder.createEscortForPlayerFlight(this);
        addLinkedUnit(escortForPlayerFlight);
    }
    
    public void createVirtualEscortFlight() throws PWCGException 
    {
        Squadron friendlyFighterSquadron = PWCGContextManager.getInstance().getSquadronManager().getNearbyFriendlySquadronByRole(
                flightInformation.getCampaign(),
                flightInformation.getSquadron().determineSquadronCountry(flightInformation.getCampaign().getDate()), 
                Role.ROLE_FIGHTER);

        if (friendlyFighterSquadron != null)
        {
            MissionBeginUnit missionBeginUnitEscort = new MissionBeginUnit();
            missionBeginUnitEscort.initialize(this.getPosition());

            FlightInformation opposingFlightInformation = FlightInformationFactory.buildAiFlightInformation(friendlyFighterSquadron, flightInformation.getMission(), FlightTypes.ESCORT, flightInformation.getTargetCoords().copy());
            virtualEscortFlight = new VirtualEscortFlight(opposingFlightInformation, missionBeginUnitEscort, this);
            virtualEscortFlight.createUnitMission();
            virtualEscortFlight.createEscortPositionCloseToFirstWP();

            addLinkedUnit(virtualEscortFlight);
        }
    }

    public static boolean isFighterMission(FlightTypes flightType)
    {
        boolean isFighterMission = false;
        if (flightType == FlightTypes.PATROL ||
            flightType == FlightTypes.OFFENSIVE ||
            flightType == FlightTypes.INTERCEPT ||
            flightType == FlightTypes.ESCORT ||
            flightType == FlightTypes.SCRAMBLE ||
            flightType == FlightTypes.SCRAMBLE_OPPOSE)
        {
            isFighterMission = true;
        }

        return isFighterMission;
    }

    public boolean isReconOrAttackMission(FlightTypes flightType)
    {
        boolean isReconOrAttackMission = false;
        if (flightType == FlightTypes.CONTACT_PATROL ||
            flightType == FlightTypes.RECON)
        {
            isReconOrAttackMission = true;
        }
        
        if (this.isBombingFlight())
        {
            isReconOrAttackMission = true;
        }
        return isReconOrAttackMission;
    }

    public boolean isStrategicMission(FlightTypes flightType)
    {
        boolean isStrategicMission = false;
        if (flightType == FlightTypes.STRATEGIC_BOMB)
        {
            isStrategicMission = true;
        }

        return isStrategicMission;
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

    public int getMissionStartTimeAdjustment()
    {
        return missionStartTimeAdjustment;
    }

    public void setMissionStartTimeAdjustment(int missionStartTimeAdjustment)
    {
        this.missionStartTimeAdjustment = missionStartTimeAdjustment;
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

    private boolean isAggressivePlane(PlaneType plane)
    {
        if (plane.isRole(Role.ROLE_FIGHTER))
        {
            if (!plane.isRole(Role.ROLE_RECON))
            {
                return true;
            }
        }

        if (flightInformation.getFlightType() == FlightTypes.ESCORT ||
            flightInformation.getFlightType() == FlightTypes.INTERCEPT ||
            flightInformation.getFlightType() == FlightTypes.OFFENSIVE ||
            flightInformation.getFlightType() == FlightTypes.PATROL ||
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE_OPPOSE ||
            flightInformation.getFlightType() == FlightTypes.SCRAMBLE)
        {
            return true;
        }

        return false;
    }

    protected String getMissionObjectiveLocation(Unit linkedUnit) throws PWCGException 
    {
        String objectiveLocation = "";
        
        if (linkedUnit.getCountry().isEnemy(flightInformation.getCampaign().determineCountry()))
        {
            if (linkedUnit instanceof AirfieldStaticGroup)
            {
                AirfieldStaticGroup target = (AirfieldStaticGroup)linkedUnit;
                if (target != null)
                {
                    
                    String airfieldName = target.getAirfield().getName();
                    if (!airfieldName.contains("Group"))
                    {
                        objectiveLocation = " at " + airfieldName;
                    }
                    else
                    {
                        objectiveLocation = getObjectiveName(linkedUnit);
                    }
                }
            }
            else if (linkedUnit instanceof GroundUnit)
            {
                objectiveLocation = getObjectiveName(linkedUnit);
            }
        }
        
        return objectiveLocation;
    }

    private String getObjectiveName(Unit linkedUnit) throws PWCGException
    {
        GroundUnit groundUnit = (GroundUnit)linkedUnit;             
        String targetName =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(groundUnit.getPosition().copy()).getName();
        return " near " + targetName;
    }

    public boolean isFighterFlight()
    {
        if (this.flightInformation.getFlightType() == FlightTypes.PATROL           ||
            this.flightInformation.getFlightType() == FlightTypes.LOW_ALT_PATROL   ||
            this.flightInformation.getFlightType() == FlightTypes.OFFENSIVE        ||
            this.flightInformation.getFlightType() == FlightTypes.INTERCEPT        ||
            this.flightInformation.getFlightType() == FlightTypes.LOW_ALT_CAP      ||
            this.flightInformation.getFlightType() == FlightTypes.ESCORT           ||
            this.flightInformation.getFlightType() == FlightTypes.SCRAMBLE         ||
            this.flightInformation.getFlightType() == FlightTypes.SCRAMBLE_OPPOSE  ||
            this.flightInformation.getFlightType() == FlightTypes.HOME_DEFENSE     ||
            this.flightInformation.getFlightType() == FlightTypes.LONE_WOLF        ||
            this.flightInformation.getFlightType() == FlightTypes.BALLOON_BUST     ||
            this.flightInformation.getFlightType() == FlightTypes.BALLOON_DEFENSE  ||
            this.flightInformation.getFlightType() == FlightTypes.SCRAMBLE_OPPOSE)
        {
            return true;
        }
            
        return false;
    }
    
    protected String formMissionObjectiveLocation(Coordinate targetLocation) throws PWCGException 
    {
        String missionObjectiveLocation = "";
        String targetName =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(targetLocation).getName();
        if (!targetName.isEmpty())
        {
            missionObjectiveLocation =   " near " + targetName;
        }
        
        return missionObjectiveLocation;
    }

    public Coordinate findFirstWaypointPosition()
    {
        // after we have created the mission, move the bombers such that they start near the ingress point
        List <McuWaypoint> wayPoints = getAllWaypoints();
        return wayPoints.get(0).getPosition().copy();
     }

    public Coordinate findIngressWaypointPosition()
    {
        // after we have created the mission, move the bombers such that they start near the ingress point
        List <McuWaypoint> wayPoints = getAllWaypoints();
        for (McuWaypoint waypoint : wayPoints)
        {
            if (waypoint.getName().equals(WaypointType.INGRESS_WAYPOINT.getName()))
            {
                return waypoint.getPosition();
            }
        }
        
        return wayPoints.get(0).getPosition().copy();
    }

    public int getMaximumFlightAltitude()
    {
        int altitude = 100;
        List<McuWaypoint> waypoints = getAllWaypoints();
        for (McuWaypoint waypoint : waypoints)
        {
            int thisAlt = new Double(waypoint.getPosition().getYPos()).intValue();
            if (thisAlt > altitude)
            {
                altitude = thisAlt;
            }
        }
        
        return altitude;
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

    public void linkGroundUnitsToFlight(GroundUnitCollection groundUnits) throws PWCGException
    {
        for (Unit groundUnit : groundUnits.getAllGroundUnits())
        {
            addLinkedUnit(groundUnit);              
        }
        
        setTargetDefinition(groundUnits.getTargetDefinition());
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
        return targetDefinition.getTargetCategory();
    }

    public void setTargetDefinition(TargetDefinition targetDefinition)
    {
        this.targetDefinition = targetDefinition;
    }

    public TargetDefinition getTargetDefinition()
    {
        return targetDefinition;
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

    public Coordinate getTargetCoords() throws PWCGException
    {
        return flightInformation.getTargetCoords();
    }

    public boolean isPlayerFlight()
    {
        return flightInformation.isPlayerFlight();
    }

    public boolean isVirtual()
    {
        return flightInformation.isVirtual();
    }

    public boolean isFriendly() throws PWCGException
    {
        return flightInformation.isFriendly();
    }
    
    public boolean isAirStart() throws PWCGException
    {
        return flightInformation.isAirStart();
    } 
}
