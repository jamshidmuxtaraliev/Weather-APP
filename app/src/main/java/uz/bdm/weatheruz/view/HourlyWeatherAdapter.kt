package uz.bdm.weatheruz.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.hourly_weather_item_layout.view.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.service.Functions
import kotlin.math.roundToInt

interface HourlyAdapterListener {
    fun onClickHourly(item: Any?)
}

class HourlyWeatherAdapter(val list: List<Dayly>, val callback: HourlyAdapterListener) :
    RecyclerView.Adapter<HourlyWeatherAdapter.ItemHolder>() {
    class ItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hourly_weather_item_layout, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.itemView.weekDay.text = Functions().getWeekName("${item.dt_txt.subSequence(0,10)}")
        holder.itemView.time_hourly.text = Functions().timeStampToLocalDate(item.dt.toLong())
        Glide.with(holder.itemView.img_hourly.context)
            .load(Constants.IMAGE_URL + item.weather[0].icon + ".png")
            .into(holder.itemView.img_hourly)
        holder.itemView.temp_hourly.text = item.main.temp.roundToInt().toString()+"°C"

        holder.itemView.setOnClickListener {
            callback.onClickHourly(item)
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }
}