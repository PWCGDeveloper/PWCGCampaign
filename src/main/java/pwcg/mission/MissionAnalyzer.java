package pwcg.mission;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.mcu.McuWaypoint;

public class MissionAnalyzer 
{
	public MissionAnalyzer()
	{
	}
	
	public void analyze(Mission mission) throws PWCGIOException 
	{
		try
        {
		    File reportDir = new File(PWCGContext.getInstance().getDirectoryManager().getPwcgReportDir());
		    if (!reportDir.exists())
		    {
		        reportDir.mkdirs();
		    }
            Campaign campaign = mission.getCampaign();
            String filename = PWCGContext.getInstance().getDirectoryManager().getPwcgReportDir() + campaign.getCampaignData().getName() + " MissionAnalysis.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write("Player flights");
            writer.newLine();
            for (IFlight playerFlight: mission.getMissionFlightBuilder().getPlayerFlights())
            {
            	analyzeFlight (writer, playerFlight, false);
            }
            writer.newLine();
            writer.newLine();
            writer.newLine();

            List<IFlight> alliedFlights = mission.getMissionFlightBuilder().getAiFlights();
            for (int i = 1; i < alliedFlights.size(); ++i)
            {
            	writer.write("IFlight");
            	writer.newLine();
            	IFlight flight = alliedFlights.get(i);
            	analyzeFlight (writer, flight, false);
            	writer.newLine();
            	writer.newLine();
            	writer.newLine();
            }
            		
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	private void analyzeFlight(BufferedWriter writer, IFlight flight, boolean dumpWP) throws PWCGIOException 
	{
		try
        {
            NumberFormat numberFormat = new DecimalFormat("###.0");

            writer.write("  Mission type: " + flight.getFlightType());
            writer.newLine();
            writer.write("  Airfield    : " + flight.getFlightInformation().getAirfield().getName());
            writer.newLine();
            writer.write("  Aircraft    : " + flight.getFlightPlanes().getFlightLeader().getType());
            writer.newLine();
            writer.write("  Player Contact    : " + flight.getFlightPlayerContact().getFirstContactWithPlayer());
            writer.newLine();
            List<McuWaypoint> waypoints = flight.getWaypointPackage().getAllWaypoints();
            
            if (dumpWP)
            {
            	double totalDist = 0.0;
            	
            	for (int i = 1; i < waypoints.size(); ++i)
            	{
            		McuWaypoint previousWP = waypoints.get(i-1);
            		McuWaypoint thisWP = waypoints.get(i);
            		
            		if (i == 1)
            		{
            			writer.write("  Start WP      : " + previousWP.getDesc());				
            			writer.newLine();
            			writer.write("  WP Position : " + numberFormat.format(previousWP.getPosition().getZPos()) + ", " + numberFormat.format(previousWP.getPosition().getXPos()));				
            			writer.newLine();
            			writer.write("  WP Altitude  : " + numberFormat.format(previousWP.getPosition().getYPos()));				
            			writer.newLine();
            			writer.newLine();
            		}
            		
            		double dist = MathUtils.calcDist(previousWP.getPosition(), thisWP.getPosition());
            		totalDist += dist;
            		
            		writer.write("  Waypoint      : " + thisWP.getDesc());				
            		writer.newLine();
                    writer.write("  WP Position : " + numberFormat.format(previousWP.getPosition().getZPos()) + ", " + numberFormat.format(previousWP.getPosition().getXPos()));                
                    writer.newLine();
            		writer.write("  Position Alt  : " + numberFormat.format(thisWP.getPosition().getYPos()));				
            		writer.newLine();
            		writer.write("  Distance      : " + numberFormat.format(dist));				
            		writer.newLine();
            		writer.write("  Heading       : " + numberFormat.format((MathUtils.calcAngle(previousWP.getPosition(), thisWP.getPosition()))));				
            		writer.newLine();
            		writer.newLine();
            	}
            	
            	writer.write("  Total Distance : " + numberFormat.format(totalDist));				
            	writer.newLine();		
            	writer.newLine();		
            }
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
         }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

    
    /**
     * @param flights
     * @return
     * @throws PWCGException 
     */
    List<IFlight> getAlliedFlights(List<IFlight> flights) throws PWCGException
    {
        List<IFlight> alliedFlights = new ArrayList<IFlight>();
        for (IFlight flight : flights)
        {
            if (flight.getFlightInformation().getCountry().getSide() == Side.ALLIED)
            {
                alliedFlights.add(flight);
            }
        }
        
        return alliedFlights;
    }
    
    /**
     * @param flights
     * @return
     * @throws PWCGException 
     */
    List<IFlight> getAxisFlights(List<IFlight> flights) throws PWCGException
    {
        List<IFlight> axisFlights = new ArrayList<IFlight>();
        for (IFlight flight : flights)
        {
            if (flight.getFlightInformation().getCountry().getSide() == Side.AXIS)
            {
                axisFlights.add(flight);
            }
        }
        
        return axisFlights;
    }

}
