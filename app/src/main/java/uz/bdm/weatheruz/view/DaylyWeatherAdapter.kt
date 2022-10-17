package uz.bdm.weatheruz.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.day_weatheer_item_layout.view.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.service.Functions
import kotlin.math.roundToInt

interface DaylyAdapterListener {
    fun onClickDay(item: Any?)
}

class DaylyWeatherAdapter(val list: List<Dayly>, val callback: DaylyAdapterListener) :
    RecyclerView.Adapter<DaylyWeatherAdapter.ItemHolder>() {
    class ItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.day_weatheer_item_layout, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.itemView.weekNameToday.text =
            Functions().getWeekName("${item.dt_txt.subSequence(0, 10)}")//.subSequence(0,2)
        holder.itemView.dateCurrentDayly.text =
            item.dt_txt.subSequence(5, 10).toString().replace("-", "/")
        Glide.with(holder.itemView.iconDayly.context)
            .load(Constants.IMAGE_URL + item.weather[0].icon + ".png")
            .into(holder.itemView.iconDayly)
        holder.itemView.weatherNameDayly.text = item.weather[0].main
        holder.itemView.minMaxTempDayly.text =
            item.main.temp_min.roundToInt().toString() + "°" + item.main.temp_max.roundToInt()
                .toString() + "°"

        holder.itemView.setOnClickListener {
            callback.onClickDay(item)
        }
    }

    override fun getItemCount(): Int {
        return list.count()
    }
}