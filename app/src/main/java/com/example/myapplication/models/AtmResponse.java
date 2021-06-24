package com.example.myapplication.models;

import java.io.Serializable;

public class AtmResponse implements Serializable
{
    private Points[] points;

    public Points[] getPoints ()
    {
        return points;
    }

    public void setPoints (Points[] points)
    {
        this.points = points;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [points = "+points+"]";
    }

    public class Points
    {
        private String address;

        private double distance;

        private String town;

        private String active;

        private String usdSupport;

        private String cityCode;

        private double latitude;

        private String atmBranchType;

        private String cityName;

        private String name;

        private String eurSupport;

        private String insertMoney;

        private double longitude;


        public String getAddress ()
        {
            return address;
        }

        public void setAddress (String address)
        {
            this.address = address;
        }

        public double getDistance ()
        {
            return distance;
        }

        public void setDistance (double distance)
        {
            this.distance = distance;
        }

        public String getTown ()
        {
            return town;
        }

        public void setTown (String town)
        {
            this.town = town;
        }

        public String getUsdSupport ()
        {
            return usdSupport;
        }

        public void setUsdSupport (String usdSupport)
        {
            this.usdSupport = usdSupport;
        }

        public String getCityCode ()
        {
            return cityCode;
        }

        public void setCityCode (String cityCode)
        {
            this.cityCode = cityCode;
        }

        public double getLatitude ()
        {
            return latitude;
        }

        public void setLatitude (double latitude)
        {
            this.latitude = latitude;
        }

        public String getActive ()
        {
            return active;
        }

        public void setActive (String active)
        {
            this.active = active;
        }

        public String getAtmBranchType ()
        {
            return atmBranchType;
        }

        public void setAtmBranchType (String atmBranchType)
        {
            this.atmBranchType = atmBranchType;
        }

        public String getCityName ()
        {
            return cityName;
        }

        public void setCityName (String cityName)
        {
            this.cityName = cityName;
        }

        public String getName ()
        {
            return name;
        }

        public void setName (String name)
        {
            this.name = name;
        }

        public String getEurSupport ()
        {
            return eurSupport;
        }

        public void setEurSupport (String eurSupport)
        {
            this.eurSupport = eurSupport;
        }

        public String getInsertMoney ()
        {
            return insertMoney;
        }

        public void setInsertMoney (String insertMoney)
        {
            this.insertMoney = insertMoney;
        }

        public double getLongitude ()
        {
            return longitude;
        }

        public void setLongitude (double longitude)
        {
            this.longitude = longitude;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [address = "+address+", distance = "+distance+", town = "+
                    town+", usdSupport = "+usdSupport+", cityCode = "+cityCode+", latitude = "+
                    latitude+", active = "+active+", atmBranchType = "+atmBranchType+", cityName = "+cityName+", name = "+
                    name+", eurSupport = "+eurSupport+", insertMoney = "+insertMoney+", longitude = "+longitude+"]";
        }
    }

}
