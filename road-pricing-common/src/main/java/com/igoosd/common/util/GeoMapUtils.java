package com.igoosd.common.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * 2018/3/27.
 */
public class GeoMapUtils {


    /**
     * 地图距离计算
     * @param startLocation
     * @param endLocation
     * @return
     */
    public static double distance(GeoLocation startLocation,GeoLocation endLocation) {
        if ((startLocation == null) || (endLocation == null))
        {
            throw new IllegalArgumentException("非法坐标值，不能为null");
        }
        double d1 = 0.01745329251994329D;
        double d2 = startLocation.longitude;
        double d3 = startLocation.latitude;
        double d4 = endLocation.longitude;
        double d5 = endLocation.latitude;
        d2 *= d1;
        d3 *= d1;
        d4 *= d1;
        d5 *= d1;
        double d6 = Math.sin(d2);
        double d7 = Math.sin(d3);
        double d8 = Math.cos(d2);
        double d9 = Math.cos(d3);
        double d10 = Math.sin(d4);
        double d11 = Math.sin(d5);
        double d12 = Math.cos(d4);
        double d13 = Math.cos(d5);
        double[] arrayOfDouble1 = new double[3];
        double[] arrayOfDouble2 = new double[3];
        arrayOfDouble1[0] = (d9 * d8);
        arrayOfDouble1[1] = (d9 * d6);
        arrayOfDouble1[2] = d7;
        arrayOfDouble2[0] = (d13 * d12);
        arrayOfDouble2[1] = (d13 * d10);
        arrayOfDouble2[2] = d11;
        double d14 = Math.sqrt((arrayOfDouble1[0] - arrayOfDouble2[0]) * (arrayOfDouble1[0] - arrayOfDouble2[0])
                + (arrayOfDouble1[1] - arrayOfDouble2[1]) * (arrayOfDouble1[1] - arrayOfDouble2[1])
                + (arrayOfDouble1[2] - arrayOfDouble2[2]) * (arrayOfDouble1[2] - arrayOfDouble2[2]));

        return (Math.asin(d14 / 2.0D) * 12742001.579854401D);
    }


    /**
     * 坐标格式为  '纬度,经度' 字符串
     * @param startLocation
     * @param endLocation
     * @return
     */
    public static double distance(String startLocation,String endLocation){
        String[] strs1 = startLocation.split(",");
        String[] strs2 =endLocation.split(",");
        GeoLocation start =new GeoLocation(Double.valueOf(strs1[1]),Double.valueOf(strs1[0]));
        GeoLocation end =new GeoLocation(Double.valueOf(strs2[1]),Double.valueOf(strs2[0]));
        return distance(start,end);
    }


    public static final class GeoLocation {
        /**
         * 纬度 (垂直方向)
         */
        public final double latitude;
        /**
         * 经度 (水平方向)
         */
        public final double longitude;
        /**
         * 格式化
         */
        public final static DecimalFormat format = new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.US));

        /**
         * 使用传入的经纬度构造，一对经纬度值代表地球上一个地点。
         *
         * @param longitude 地点的经度，在-180 与180 之间的double 型数值。
         * @param latitude  地点的纬度，在-90 与90 之间的double 型数值。
         */
        public GeoLocation(double longitude, double latitude) {
            this(longitude, latitude, true);
        }

        public GeoLocation(double longitude, double latitude, boolean isCheck) {
            if (isCheck) {
                if ((-180.0D <= longitude) && (longitude < 180.0D))
                    this.longitude = parse(longitude);
                else {
                    throw new IllegalArgumentException("the longitude range [-180, 180].");
                    // this.longitude = parse(((longitude - 180.0D) % 360.0D + 360.0D) %
                    // 360.0D - 180.0D);
                }

                if ((latitude < -90.0D) || (latitude > 90.0D)) {
                    throw new IllegalArgumentException("the latitude range [-90, 90].");
                }
                this.latitude = latitude;
                // this.latitude = parse(Math.max(-90.0D, Math.min(90.0D, latitude)));
            } else {
                this.latitude = latitude;
                this.longitude = longitude;
            }
        }

        /**
         * 解析
         *
         * @param d
         * @return
         */
        private static double parse(double d) {
            return Double.parseDouble(format.format(d));
        }


        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            long temp;
            temp = Double.doubleToLongBits(latitude);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(longitude);
            result = prime * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            GeoLocation other = (GeoLocation) obj;
            if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
                return false;
            if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
                return false;
            return true;
        }

        public String toString() {
            return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
        }
    }
}
