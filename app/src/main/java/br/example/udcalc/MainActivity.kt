package br.example.udcalc

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs


class MainActivity : AppCompatActivity() {
    private var tvSelectedDate: TextView? = null
    private var tvRemaining: TextView? = null
    private var tvRemainingWarning: TextView? = null
    private var tvRemainingText: TextView? = null
    private var btnDatePicker: Button? = null

    private var expectancy: String? = null
    private var selectedYear: Int? = null

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spCountrySelect: Spinner = findViewById(R.id.spCountrySelect)
        val countries = countryLoader()

        val adapter = CustomAdapter(applicationContext, countries)
        spCountrySelect.adapter = adapter

        btnDatePicker = findViewById(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvRemaining = findViewById(R.id.tvRemaining)
        tvRemainingWarning = findViewById(R.id.tvRemainingWarning)
        tvRemainingText = findViewById(R.id.tvRemainingText)

        btnDatePicker?.setOnClickListener {
            clickDatePicker()
        }

        spCountrySelect.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                expectancy = countries[spCountrySelect.selectedItemPosition].value
                if(selectedYear !== null) calcExpectancy()
            }
        }
    }

    private fun calcExpectancy() {
        val currentYear = calendar.get(Calendar.YEAR)
        val personYears = currentYear - selectedYear!!;
        val formattedExpectancy = expectancy!!.toFloat()

        val remainingMinutesToLive = (formattedExpectancy - personYears) * 525948

        tvRemaining!!.text = String.format("%.0f", abs(remainingMinutesToLive))

        if (personYears > formattedExpectancy) {
            tvRemainingWarning?.visibility = View.VISIBLE
            tvRemainingText?.text = "minutes ago"
            return
        }

        tvRemainingWarning?.visibility = View.INVISIBLE
        tvRemainingText?.text = "minutes Until Death"
    }

    private fun clickDatePicker() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

        DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                selectedYear = year
                tvSelectedDate?.text = dateFormatter(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun countryLoader(): MutableList<Data> {
        val reader = getAssetJsonData(applicationContext)
        val json = JSONObject(reader)
        val countriesArray = json.getJSONArray("countries")

        val dataList: MutableList<Data> = ArrayList()

        for (i in 0 until countriesArray.length()) {
            val jsonObject = countriesArray.getJSONObject(i)
            dataList.add(Data(jsonObject.getString("country"), jsonObject.getString("value")))
        }

        return dataList
    }

    private fun dateFormatter(year: Int, month: Int, dayOfMonth: Int): String? {
        return LocalDate.of(year, month, dayOfMonth).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }


}