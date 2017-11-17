/**
 * Created by Samson on 12/11/2017.
 */

public class CheckingHandler {
    public CheckingHandler(){

    }

    public static boolean inputCheck(String id, String duration, String weekDayCost, String weekEndCost){
        if (idCheck(id) &&
                durationCheck(duration) &&
                costCheck(weekDayCost,weekEndCost)
                )  return true;
        return false;
    }
    public static boolean idCheck(String id){
        if (id.matches("[A-Z]{2}[0-9]{3}")) return true;
        return false;
    }
    public static boolean durationCheck(String duration){
        if (duration.matches("\\d+")) return true;
        return false;
    }
    public static boolean costCheck(String weekDayCost, String weekEndCost){
        if (weekDayCost.matches("\\d+") && weekEndCost.matches("\\d+")) return true;
        else if (weekDayCost.matches("\\d+") && weekEndCost.isEmpty()) return true;
        else if (weekEndCost.matches("\\d+") && weekDayCost.isEmpty()) return true;
        return false;
    }
}
