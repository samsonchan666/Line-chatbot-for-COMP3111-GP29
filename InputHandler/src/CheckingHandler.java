/**
 * Created by Samson on 12/11/2017.
 */

public class CheckingHandler {
    public CheckingHandler(){

    }

    public static boolean inputCheck(String id, String duration, String weekDayCost, String weekEndCost, String day){
        if (idCheck(id) &&
                durationCheck(duration) &&
                costCheck(weekDayCost,weekEndCost) &&
                dateCheck(day)
                )  return true;
        return false;
    }
    public static boolean inputCheck(String keyword){
        if (keyword.matches("((\\w)+,)*(\\w)+")) return true;
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
    public static boolean dateCheck(String date){
        if (date.matches("((([1-3][0-9])|([0]?[1-9]))\\/(([1][0-2])|([0]?[1-9]))\\/[0-9]{4},)*((([1-3][0-9])|([0]?[1-9]))\\/(([1][0-2])|([0]?[1-9]))\\/[0-9]{4})+")) return true;
        return false;
    }
}
