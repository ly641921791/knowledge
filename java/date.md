Date
-

###### 获得当天的0点

``` java
    private static final long TIMESTAMP_OF_DAY = 24 * 3600 * 1000L;

    public static Date floorDay(long currentTimestamp) {
        return new Date(currentTimestamp / TIMESTAMP_OF_DAY * TIMESTAMP_OF_DAY - TimeZone.getDefault().getRawOffset());
    }

    public static Date floorDay(Date currentTime) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
```