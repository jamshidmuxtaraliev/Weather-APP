package uz.bdm.weatheruz.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_daily.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.view.AllDailyForHoursAdapter
import uz.bdm.weatheruz.view.AllDailyForHoursAdapterListener
import java.io.Serializable

class DailyFragment(var item: Dayly) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var kunlist = ArrayList<Dayly>()
        MainActivity.allDaylyList.forEach {
            if (it.dt_txt.subSequence(8, 10).toString().toInt() == item.dt_txt.subSequence(8, 10)
                    .toString().toInt()
            ) {
                kunlist?.add(it)
            }
        }

        recyclerDailyFragment.layoutManager = LinearLayoutManager(requireActivity())
        recyclerDailyFragment.adapter = AllDailyForHoursAdapter(
            kunlist,
            object : AllDailyForHoursAdapterListener {
                override fun onClickHour(item: Dayly) {
                    val intent = Intent(requireActivity(), AllHourlyActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DATA_2, item as Serializable)
                    startActivity(intent)
                }
            })
    }
}