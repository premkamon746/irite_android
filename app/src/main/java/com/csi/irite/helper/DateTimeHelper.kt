package com.csi.irite.helper

import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioButton
import com.csi.irite.R
import com.csi.irite.utils.Tools
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import androidx.fragment.app.FragmentManager
import android.content.Context

class DateTimeHelper {

    fun eventSelectDate(view: View, autoCompleteTextViewId: Int, childFragmentMGR: FragmentManager){
        //R.id.bt_event_date
        view.findViewById<View>(autoCompleteTextViewId).setOnClickListener(View.OnClickListener { v ->
            dialogDatePickerLight( v,childFragmentMGR )
        })
    }

    fun eventSelectTime(view: View, autoCompleteTextViewId: Int, ctx : Context){
        view.findViewById<View>(autoCompleteTextViewId).setOnClickListener(View.OnClickListener { v ->
            dialogTimePicker(v,ctx)
        })
    }

    fun eventSelectDate(view: View, childFragmentMGR: FragmentManager){
        //R.id.bt_event_date
        view.setOnClickListener(View.OnClickListener { v ->
            dialogDatePickerLight( v,childFragmentMGR )
        })
    }

    fun eventSelectTime(view: View, ctx : Context){
        view.setOnClickListener(View.OnClickListener { v ->
            dialogTimePicker(v,ctx)
        })
    }

    fun controlRadio(view: View){
        // Inflate the layout for this fragment
        val radioButton: RadioButton = view.findViewById(R.id.common_from_radio_other)
        val editText: EditText = view.findViewById(R.id.common_from_text_other)
        // Set OnCheckedChangeListener for the RadioButton
        val isChecked = radioButton.isChecked()
        if (isChecked) {
            editText.isEnabled = true
        } else {
            editText.isEnabled = false
        }
        radioButton.setOnCheckedChangeListener { _, isChecked ->
            // Update EditText state based on RadioButton check
            editText.isEnabled = isChecked
        }
    }

    fun defaultToday(view: View, autoCompleteTextViewId: Int){
        val autoCompleteTextView: AutoCompleteTextView = view.findViewById(autoCompleteTextViewId)
        // Get today's date in desired format
        val today = SimpleDateFormat("dd/MM/yyyy").format(Date())  // Adjust format as needed

        // Set the default text
        autoCompleteTextView.setText(today)
    }

    fun setCurrentTime(view: View, autoCompleteTextViewId: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val formattedTime = String.format("%02d:%02d", hour, minute)

        val editText = view.findViewById<EditText>(autoCompleteTextViewId)
        editText.setText(formattedTime)
    }



    private fun dialogDatePickerLight(v: View, childFragmentMGR: FragmentManager ) {
        val curCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val date = calendar.timeInMillis
                (v as EditText).setText(Tools.getFormattedDateNormal(date))
            },
            curCalendar.get(Calendar.YEAR),
            curCalendar.get(Calendar.MONTH),
            curCalendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set theme and colors
        datePicker.setThemeDark(false)
        //datePicker.setAccentColor(resources.getColor(R.color.colorPrimary))
        //datePicker.minDate = curCalendar
        datePicker.show(childFragmentMGR, "Expiration Date")
    }


    private fun dialogTimePicker(v: View, ctx : Context) {
        val cal = Calendar.getInstance()
        val timePickerDialog = android.app.TimePickerDialog(
            ctx,
            { view, hourOfDay, minute ->
                // Format the selected time (optional)
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                (v as EditText).setText(formattedTime)
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true // Use 24-hour format (optional)
        )
        timePickerDialog.show()
    }
}