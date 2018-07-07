package pwcg.mission;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
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
            Campaign campaign = mission.getCampaign();
            String filename = PWCGContextManager.getInstance().getDirectoryManager().getPwcgReportDir() + campaign.getName() + " MissionAnalysis.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write("Player flight");
            writer.newLine();
            Flight myFlight = mission.getMissionFlightBuilder().getPlayerFlight();
            analyzeFlight (writer, myFlight, false);
            writer.newLine();
            writer.newLine();
            writer.newLine();

            List<Flight> alliedFlights = mission.getMissionFlightBuilder().getMissionFlights();
            for (int i = 1; i < alliedFlights.size(); ++i)
            {
            	writer.write("Flight");
            	writer.newLine();
            	Flight flight = alliedFlights.get(i);
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}
	
	private void analyzeFlight(BufferedWriter writer, Flight flight, boolean dumpWP) throws PWCGIOException 
	{
		try
        {
            NumberFormat numberFormat = new DecimalFormat("###.0");

            writer.write("  Mission type: " + flight.getFlightType());
            writer.newLine();
            writer.write("  Airfield    : " + flight.getAirfield().getName());
            writer.newLine();
            writer.write("  Aircraft    : " + flight.getPlanes().get(0).getType());
            writer.newLine();
            writer.write("  Player Contact    : " + flight.getFirstContactWithPlayer());
            writer.newLine();
            List<McuWaypoint> waypoints = flight.getAllWaypoints();
            
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
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
         }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

    
    /**
     * @param flights
     * @return
     * @throws PWCGException 
     */
    List<Flight> getAlliedFlights(List<Flight> flights) throws PWCGException
    {
        List<Flight> alliedFlights = new ArrayList<Flight>();
        for (Flight flight : flights)
        {
            if (flight.getCountry().getSide() == Side.ALLIED)
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
    List<Flight> getAxisFlights(List<Flight> flights) throws PWCGException
    {
        List<Flight> axisFlights = new ArrayList<Flight>();
        for (Flight flight : flights)
        {
            if (flight.getCountry().getSide() == Side.AXIS)
            {
                axisFlights.add(flight);
            }
        }
        
        return axisFlights;
    }

}
