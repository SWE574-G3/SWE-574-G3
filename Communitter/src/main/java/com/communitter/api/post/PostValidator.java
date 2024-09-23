package com.communitter.api.post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class PostValidator {

    private static final String datePattern = "yyyy-MM-dd";

    public static boolean validatePost(Post post){
        Set<PostField> postFields = post.getPostFields();
        for (PostField postField:postFields){
           if(!validatePostField(postField)) return false;
        }
        return true;
    }

    private static boolean validatePostField(PostField postField){
        String fieldType =postField.getDataField().getDataFieldType().getType();
        boolean result = false;
        switch (fieldType){
            case ("string"):
                result= true;
                break;
            case ("image"):
                result= true;
                break;
            case ("date"):
                result= isDate(postField.getValue());
                break;
            case("number"):
                result= isNumeric(postField.getValue());
                break;
            case ("geolocation"):
                result= isGeolocation(postField.getValue());
                break;
        }
        return result;
    }
    private static boolean isNumeric(String value){
        try {
            Double.parseDouble(value);  // Attempt to parse as a double
            return true;  // If successful, it's numeric
        } catch (NumberFormatException e) {
            return false;  // If parsing fails, it's not numeric
        }
    }

    private static boolean isDate(String dateStr){
        SimpleDateFormat dateFormat =new SimpleDateFormat(datePattern);
        dateFormat.setLenient(false);
        try {
            Date date = dateFormat.parse(dateStr);  // Try to parse the string as a Date
            return true;  // If successful, it's a valid date
        } catch (ParseException e) {
            return false;  // If parsing fails, it's not a valid date
        }
    }

    private static boolean isGeolocation(String coords){
        String [] latLong = coords.split(",");
        if (latLong.length!=2) return false;
        String latit = latLong[0].trim();
        String longit = latLong[1].trim();
        try{
            double latitude =Double.parseDouble(latit);
            double longitude =Double.parseDouble(longit);
            if(latitude<-90 || latitude>90) return false;
            if(longitude<-180 || longitude>180) return false;
            return true;
        }catch (NumberFormatException exc){
            return false;
        }
    }
}
