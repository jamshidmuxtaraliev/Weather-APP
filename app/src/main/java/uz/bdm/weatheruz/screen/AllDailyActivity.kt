package uz.bdm.weatheruz.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_all_daily.*
import kotlinx.android.synthetic.main.activity_all_hourly.*
import uz.bdm.weatheruz.Constants
import uz.bdm.weatheruz.R
import uz.bdm.weatheruz.model.Dayly
import uz.bdm.weatheruz.view.DailyViewPagerAdapter
import uz.bdm.weatheruz.view.HourlyViewPagerAdapter

class AllDailyActivity : AppCompatActivity() {
    lateinit var items:List<Dayly>
    lateinit var item:Dayly

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_daily)

        btnBackDaily.setOnClickListener {
            finish()
        }

        if (intent.hasExtra(Constants.EXTRA_DATA)) {
            items = intent.getSerializableExtra(Constants.EXTRA_DATA) as List<Dayly>
        }else{
            items = MainActivity.allDaylyList
        }

        if (intent.hasExtra(Constants.EXTRA_DATA_2)) {
            item = intent.getSerializableExtra(Constants.EXTRA_DATA_2) as Dayly
        }else{
            item = items[0]
        }

        viewPagerDaily.adapter = DailyViewPagerAdapter(supportFragmentManager, items)
        tablayoutDaily.setupWithViewPager(viewPagerDaily)
        tablayoutDaily.getTabAt(items.indexOf(item))?.select()
    }
}