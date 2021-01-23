package com.svws.city;

/**
 * Created by IntelliJ IDEA.
 * User: sverma
 * Date: Jan 23, 2007
 * Time: 3:53:43 PM
 */
/**
 * This class is a bean for table "sv_city_distances"
 */
public class CityDistances
{
    private int city_distances_id;

    private String from_city;
    private String to_city;

    private String kms;
    private String appx_time_hrs;

    public int getCity_distances_id()
    {
        return city_distances_id;
    }

    public void setCity_distances_id( int CityDistances_id )
    {
        this.city_distances_id = CityDistances_id;
    }

    public String getFrom_city()
    {
        return from_city;
    }

    public void setFrom_city( String CityDistances_type )
    {
        this.from_city = CityDistances_type;
    }

    public String getKms()
    {
        return kms;
    }

    public void setKms( String CityDistances_desc )
    {
        this.kms = CityDistances_desc;
    }

    public String getAppx_time_hrs()
    {
        return appx_time_hrs;
    }

    public void setAppx_time_hrs( String appx_time_hrs)
    {
        this.appx_time_hrs = appx_time_hrs;
    }

    public String getTo_city()
    {
        return to_city;
    }

    public void setTo_city( String CityDistances_user )
    {
        this.to_city = CityDistances_user;
    }
}
