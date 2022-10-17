package uz.bdm.weatheruz.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.hourly_by_daily_item_layout.view.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.service.Functions

interface AllDailyForHoursAdapterListener {
    fun onClickHour(item: Dayly)
}

class AllDailyForHoursAdapter(
    val items: List<Dayly>,
    val callback: AllDailyForHoursAdapterListener
) : RecyclerView.Adapter<AllDailyForHoursAdapter.ItemHolder>() {
    class ItemHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.hourly_by_daily_item_layout, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        var item = items[position]
        Glide.with(holder.itemView.iconka.context)
            .load(Constants.IMAGE_URL + item.weather[0].icon + ".png").into(holder.itemView.iconka)
        holder.itemView.oclock.text = Functions().timeStampToLocalDate(item.dt.toLong())
        holder.itemView.text.text = item.weather[0].main

        holder.itemView.setOnClickListener {
            callback.onClickHour(item)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}