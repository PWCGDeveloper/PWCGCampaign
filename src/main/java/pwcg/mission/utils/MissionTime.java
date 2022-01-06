package pwcg.mission.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionTime
{
    private String missionTime = "08:30:00";
    private List <String> missionTimes = new ArrayList<String>();

    public MissionTime(Date date)
    {        
        List <SunriseSunset> sunriseSunsets = new ArrayList<SunriseSunset>();
        
        sunriseSunsets.add(new SunriseSunset( 1,  1, 8,  0, 17, 30));
        sunriseSunsets.add(new SunriseSunset( 1, 21, 8,  0, 18, 00));
        sunriseSunsets.add(new SunriseSunset( 2,  6, 7, 30, 18, 30));
        sunriseSunsets.add(new SunriseSunset( 2, 21, 7,  0, 19, 00));
        sunriseSunsets.add(new SunriseSunset( 3,  6, 6, 45, 19, 30));
        sunriseSunsets.add(new SunriseSunset( 3, 21, 6, 15, 19, 30));
        sunriseSunsets.add(new SunriseSunset( 4,  6, 5, 45, 20, 00));
        sunriseSunsets.add(new SunriseSunset( 4, 21, 5, 15, 20, 30));
        sunriseSunsets.add(new SunriseSunset( 5,  6, 5,  0, 21, 00));
        sunriseSunsets.add(new SunriseSunset( 5, 21, 5,  0, 21, 00));
        sunriseSunsets.add(new SunriseSunset( 6,  6, 4, 30, 21, 00));
        sunriseSunsets.add(new SunriseSunset( 6, 21, 4, 30, 21, 00));
        sunriseSunsets.add(new SunriseSunset( 7,  6, 4, 30, 21, 30));
        sunriseSunsets.add(new SunriseSunset( 7, 21, 5,  0, 21, 00));
        sunriseSunsets.add(new SunriseSunset( 8,  6, 5, 15, 20, 30));
        sunriseSunsets.add(new SunriseSunset( 8, 21, 5, 30, 20, 00));
        sunriseSunsets.add(new SunriseSunset( 9,  6, 6,  0, 19, 30));
        sunriseSunsets.add(new SunriseSunset( 9, 21, 6, 15, 19, 00));
        sunriseSunsets.add(new SunriseSunset(10,  6, 6, 45, 18, 30));
        sunriseSunsets.add(new SunriseSunset(10, 21, 7,  0, 18, 00));
        sunriseSunsets.add(new SunriseSunset(11,  6, 7, 30, 17, 30));
        sunriseSunsets.add(new SunriseSunset(11, 21, 7, 45, 17, 30));
        sunriseSunsets.add(new SunriseSunset(12,  6, 7, 30, 17, 30));

        // TODO TC add night missions
        boolean isNightFlight = false;
        if (!isNightFlight)
        {
            SunriseSunset sunriseSunset = setDayMissionTimes(sunriseSunsets, date);
            setDayMissionTimes(sunriseSunset.sunriseHour, sunriseSunset.sunriseMinute, sunriseSunset.sunsetHour-1, sunriseSunset.sunsetMinute);
        }
        else
        {
            setNightMissionTimes();
        }
    }

    private SunriseSunset setDayMissionTimes(List <SunriseSunset> sunriseSunsets, Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SunriseSunset sunriseSunsetToReturn = sunriseSunsets.get(0);

        for (SunriseSunset sunriseSunset : sunriseSunsets)
        {
            if (month < sunriseSunset.month)
            {
                break;
            }
            else if (month == sunriseSunset.month)
            {
                if (day < sunriseSunset.day)
                {
                    break;
                }
            }

            sunriseSunsetToReturn = sunriseSunset;
        }
        
        return sunriseSunsetToReturn;
    }

    private void setDayMissionTimes(int startHour, int startMinute, int stopHour, int stopMinute)
    {
        for (int hour = startHour; hour < stopHour; ++hour)
        {
            if (hour == startHour)
            {
                missionTimes.add(makeTimeString(hour, startMinute));
                if (startMinute < 30)
                {
                    missionTimes.add(makeTimeString(hour, 30));
                }
            }
            else if (hour == stopHour)
            {
                missionTimes.add(makeTimeString(hour, 0));
                if (stopMinute == 30)
                {
                    missionTimes.add(makeTimeString(hour, 30));
                }
            }
            else
            {
                missionTimes.add(makeTimeString(hour, 0));
                missionTimes.add(makeTimeString(hour, 30));
            }
        }
    }

    private void setNightMissionTimes()
    {
        for (int hourIndex = 21; hourIndex < 27; ++hourIndex)
        {
            int hour = hourIndex;
            if (hour > 23)
            {
                hour = hour - 24;
            }

            missionTimes.add(makeTimeString(hour, 0));
            missionTimes.add(makeTimeString(hour, 30));
        }
    }

    private String makeTimeString(int hour, int minute)
    {
        return makeHourString(hour) + ":" + makeMinuteString(minute) + ":00";
    }

    private String makeHourString(int hour)
    {
        if (hour < 10)
        {
            return "0" + hour;
        }
        else
        {
            return (Integer.valueOf(hour).toString());
        }
    }

    private String makeMinuteString(int minute)
    {
        if (minute < 10)
        {
            return "0" + minute;
        }
        else
        {
            return (Integer.valueOf(minute).toString());
        }
    }

    public void generateMissionDateTime()
    {
        int index = RandomNumberGenerator.getRandom(missionTimes.size() - 2);        
        String missionTimeString = missionTimes.get(index);
        setMissionTime(missionTimeString);
    }

    public String getMissionTimeString()
    {
        return (missionTimes.get(getIndexForTime()));
    }

    public void setMissionTime(String missionTime)
    {
        this.missionTime = missionTime;
    }

    public int getIndexForTime()
    {
        String[] timeElements = missionTime.split(":");
        int missionHour = Integer.valueOf(timeElements[0]);
        int missionMinute = Integer.valueOf(timeElements[1]);
        
        for (int i = 0 ; i < missionTimes.size(); ++i)
        {
            String[] splitTime = missionTimes.get(i).split(":");
            int thisHour = Integer.valueOf(splitTime[0]);
            int thisMinute = Integer.valueOf(splitTime[1]);
            
            if (missionHour < thisHour)
            {
                return i;
            }
            else if (missionHour == thisHour)
            {
                if (missionMinute <= thisMinute)
                {
                    return i;
                }
            }
        }
        
        // If all else fails return 8:30 AM
        return 5;
    }

    public String getMissionTime()
    {
        return missionTime;
    }

    public List<String> getMissionTimes()
    {
        return missionTimes;
    }

    public void dump()
    {
        for (String missionTime : missionTimes)
        {
            PWCGLogger.log(LogLevel.DEBUG, "    " + missionTime);
        }
    }

    public  class SunriseSunset
    {
        public SunriseSunset (
                        int month,
                        int day,
                        int sunriseHour,
                        int sunriseMinute,
                        int sunsetHour,
                        int sunsetMinute)
        {
            this.month = month;
            this.day = day;
            this.sunriseHour = sunriseHour;
            this.sunriseMinute = sunriseMinute;
            this.sunsetHour = sunsetHour;
            this.sunsetMinute = sunsetMinute;
        }
        
        public int month = 3;
        public int day = 20;
        public int sunriseHour = 6;
        public int sunriseMinute = 30;
        public int sunsetHour = 18;
        public int sunsetMinute = 30;
    }

    public int getMissionHour()
    {
        String hourString = missionTime.substring(0, 2);
        return Integer.valueOf(hourString);
    }
}
