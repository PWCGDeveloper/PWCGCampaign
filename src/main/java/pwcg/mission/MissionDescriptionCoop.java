package pwcg.mission;

import java.util.ArrayList;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.options.MapWeather;
import pwcg.mission.options.MissionOptions;

public class MissionDescriptionCoop implements IMissionDescription
{
    private Mission mission = null;
    private Campaign campaign = null;

    private String author = "Brought to you by PWCGCampaign";
    private String title = "";

    private String coopPlayerHtmlTemplate = "<br>Coop Mission" + "<br> <DATE>";

    private String descCoopTemplate = "Date  <DATE>\n" + "\n" + "Weather Report \n" + "    <CLOUDS>\n" + "    <WIND>\n" + "\n" + "\n";

    private String campaignDateString = "";
    private ArrayList<String> alliedIntList = new ArrayList<String>();
    private ArrayList<String> axisIntList = new ArrayList<String>();
    private ArrayList<String> alliedIntHtmlList = new ArrayList<String>();
    private ArrayList<String> axisIntHtmlList = new ArrayList<String>();

    public MissionDescriptionCoop(Campaign campaign, Mission mission)
    {
        this.mission = mission;
        this.campaign = campaign;
        campaignDateString = DateUtils.getDateStringDashDelimitedYYYYMMDD(campaign.getDate());
    }

    public String createDescription() throws PWCGException
    {
        MapWeather mapWeather = PWCGContext.getInstance().getCurrentMap().getMapWeather();
        setClouds(mapWeather.getWeatherDescription());
        setWind(mapWeather.getWindLayers().get(0));

        MissionOptions missionOptions = PWCGContext.getInstance().getCurrentMap().getMissionOptions();
        setMissionDateTime(DateUtils.getDateAsMissionFileFormat(campaign.getDate()), missionOptions.getMissionTime().getMissionTime());

        buildTitleDescription(campaign.getCampaignData().getName());
        
        for (IFlight flight : mission.getMissionFlightBuilder().getAiFlights())
        {
            setFlight(flight);
        }

        return descCoopTemplate;
    }

    private void setMissionDateTime(String missionDate, String missionTime)
    {
        descCoopTemplate = replace(descCoopTemplate, "<DATE>", missionDate);
        coopPlayerHtmlTemplate = replace(coopPlayerHtmlTemplate, "<DATE>", missionDate);

        setTime(missionTime);
    }

    private void setTime(String missionTime)
    {
        descCoopTemplate = replace(descCoopTemplate, "<TIME>", missionTime);
    }

    public void setClouds(String replacement)
    {
        descCoopTemplate = replace(descCoopTemplate, "<CLOUDS>", replacement);
    }

    public void setWind(MapWeather.WindLayer layer) throws PWCGException
    {
        int windFrom = Double.valueOf(MathUtils.adjustAngle(layer.direction, 180)).intValue();
        String windCond = "Wind speed is " + layer.speed + " M/S." + "\n    Wind direction is " + windFrom + ".";

        descCoopTemplate = replace(descCoopTemplate, "<WIND>", windCond);
    }

    private String replace(String str, String pattern, String replacement)
    {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0)
        {
            result.append(str.substring(s, e));
            result.append(replacement);
            s = e + pattern.length();
        }
        result.append(str.substring(s));

        return result.toString();
    }

    private void buildTitleDescription(String campaignName)
    {
        this.title = campaignName + " " + campaignDateString + " Coop Mission";
    }

    public void setFlight(IFlight flight) throws PWCGException
    {
        Campaign campaign = PWCGContext.getInstance().getCampaign();

        String squadron = flight.getFlightInformation().getSquadron().determineDisplayName(campaign.getDate());
        String aircraft = flight.getFlightPlanes().getFlightLeader().getDisplayName();

        if (flight.getFlightInformation().getSquadron().determineSide() == Side.AXIS)
        {
            String axisInt = "    " + squadron + " flying " + aircraft;
            axisIntList.add(axisInt + "\n");

            String axisInthtml = "<br>    " + axisInt;
            axisIntHtmlList.add(axisInthtml);
        }
        else
        {
            String alliedInt = "    " + squadron + " flying " + aircraft;
            alliedIntList.add(alliedInt + "\n");

            String alliedInthtml = "<br>    " + alliedInt;
            alliedIntHtmlList.add(alliedInthtml);
        }
    }

    @Override
    public String getHtml()
    {
        return coopPlayerHtmlTemplate;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public String getAuthor()
    {
        return author;
    }
}
