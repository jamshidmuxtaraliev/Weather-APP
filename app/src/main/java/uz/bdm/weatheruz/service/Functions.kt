package uz.bdm.weatheruz.service

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.*

class Functions {

    fun getWeekName(date: String): String {
        val inFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inFormat.parse(date)
        val outFormat = SimpleDateFormat("EE")
        return outFormat.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun timeStampToLocalDate(timeStamp: Long): String {
        val localTime = timeStamp.let {
            Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalTime()
        }
        return localTime.toString()
    }



//    fun windNavigate(degree:Double):Ret{
//        if (degree>337.5) return 'Northerly';
//        if (degree>292.5) return 'North Westerly';
//        if(degree>247.5) return 'Westerly';
//        if(degree>202.5) return 'South Westerly';
//        if(degree>157.5) return 'Southerly';
//        if(degree>122.5) return 'South Easterly';
//        if(degree>67.5) return 'Easterly';
//        if(degree>22.5){return 'North Easterly';}
//        return 'Northerly';
//    }
}