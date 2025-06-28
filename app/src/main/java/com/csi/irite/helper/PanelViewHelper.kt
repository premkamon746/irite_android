package com.csi.irite.helper

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import com.csi.irite.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class PanelViewHelper(var context:Context, var nested_scroll_view:NestedScrollView) {

    var expandInputLayouts  = ArrayList<LinearLayout>()
    fun createPanel(layoutId: Int, text:String): CardView{
        return createPanels(layoutId, null, text, true)
    }

    fun createPanel(view: View, text:String): CardView{
        return createPanels(0, view, text, false)

    }
    fun createPanels(layoutId: Int, views: View?, text:String, resortID:Boolean = true): CardView {

        val topBar = LinearLayout(context)
        topBar.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        topBar.setBackgroundColor(context.resources.getColor(android.R.color.white))
        topBar.gravity = Gravity.CENTER_VERTICAL
        //topBar.minHeight = context.resources.getDimensionPixelSize(actionBarSize)
        topBar.orientation = LinearLayout.HORIZONTAL

        val spacingView = View(context)
        spacingView.layoutParams = LinearLayout.LayoutParams(
            context.resources.getDimensionPixelSize(R.dimen.spacing_large),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val titleText = TextView(context)
        titleText.layoutParams = LinearLayout.LayoutParams(
            0,  // Width set to 0dp for weight distribution
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f  // Weight of 1 for proportional distribution
        )
        titleText.setTextSize(25f)
        titleText.setText(text)

        titleText.setTypeface(null, Typeface.BOLD)
        titleText.setTextColor(context.resources.getColor(R.color.grey_80))

        val toggleButton = ImageButton(context)
        toggleButton.layoutParams = LinearLayout.LayoutParams(
            context.resources.getDimensionPixelSize(R.dimen.actionBarSize),//R.attr.actionBarSize
            context.resources.getDimensionPixelSize(R.dimen.actionBarSize)//R.attr.actionBarSize
        )
        toggleButton.setBackgroundResource(R.color.white)
        toggleButton.setImageResource(R.drawable.ic_expand_arrow)
        // Assuming tint is from a custom app namespace (replace if different)
        toggleButton.setColorFilter(context.resources.getColor(R.color.grey_80), PorterDuff.Mode.SRC_IN)

        topBar.addView(spacingView)
        topBar.addView(titleText)
        topBar.addView(toggleButton)

        val expandInputLayout = createExpandLayout()
        expandInputLayouts.add(expandInputLayout)

        if(resortID == true) {
            val includedLayout = LayoutInflater.from(context).inflate(layoutId, null)
            expandInputLayout.addView(includedLayout)
        }else{
            expandInputLayout.addView(views)
        }

        topBar.setOnClickListener { toggleSectionInput(toggleButton, expandInputLayout) }
        toggleButton.setOnClickListener { toggleSectionInput(toggleButton, expandInputLayout) }

        val linearLayoutTop = createLinearLayoutTop()
        linearLayoutTop.addView(topBar)
        linearLayoutTop.addView(expandInputLayout)
        val cardView = createCardView()
        cardView.addView(linearLayoutTop)
        return cardView
    }

    //Contain form field
    fun createExpandLayout(): LinearLayout {

        val expandInputLayout = LinearLayout(context)

        expandInputLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val paddingInPx = context.resources.getDimensionPixelSize(R.dimen.spacing_mxlarge) // Adjust the padding size as needed
        expandInputLayout.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        expandInputLayout.orientation = LinearLayout.VERTICAL
        expandInputLayout.setBackgroundColor(context.resources.getColor(R.color.grey_5))
        expandInputLayout!!.setVisibility(View.GONE)
        return expandInputLayout
    }

    fun createLinearLayoutTop(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        return linearLayout
    }

    fun createCardView(): CardView {
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.MATCH_PARENT,
            ViewGroup.MarginLayoutParams.WRAP_CONTENT
        ).apply {
            // Set margins
            setMargins(
                context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                context.resources.getDimensionPixelSize(R.dimen.spacing_medium)
            )
        }

        val cardView = CardView(context)
        cardView.layoutParams = layoutParams
        //cardView.marginTop(context.resources.getDimensionPixelSize(R.dimen.spacing_medium))
        //marginTop = context.resources.getDimensionPixelSize(R.dimen.spacing_medium)
        return cardView
    }

    fun createNestestChild(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(

            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL
        nested_scroll_view.addView(linearLayout)
        return linearLayout
    }



    fun toggleSectionInput(view: View, expandInputLayout:LinearLayout) {
        val show: Boolean = toggleArrow(view)
        if (show) {
            /*ViewAnimation.expand(expandInputLayout, object : ViewAnimation.AnimListener {
                override fun onFinish() {
                    Tools.nestedScrollTo(nested_scroll_view, expandInputLayout)
                }
            })*/
            expandInputLayout.setVisibility(View.VISIBLE)
        } else {
            expandInputLayout.setVisibility(View.GONE)
            //ViewAnimation.collapse(expandInputLayout)
        }
    }

    fun expandAll(){
        for (expandInputLayout in expandInputLayouts){
            expandInputLayout.setVisibility(View.VISIBLE)
        }
    }

    fun closeAll(){
        for (expandInputLayout in expandInputLayouts){
            expandInputLayout.setVisibility(View.GONE)
        }
    }

    fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    fun retreveRadio(view: View, radioGroup:RadioGroup, value: String){

        /*val checkedButton: RadioButton? = (0 until radioGroup.childCount)
            .map { radioGroup.getChildAt(it) as RadioButton }
            .firstOrNull { it.text.toString() == value && it.isChecked }*/
        for (i in 0 until radioGroup.childCount) {
            if(radioGroup.getChildAt(i)  is  RadioButton) {
                val radioButton = radioGroup.getChildAt(i) as RadioButton
                Log.d("xxxxxx", value + " " + radioButton.text.toString())
                if (radioButton.text.toString() == value) {
                    radioButton.isChecked = true
                    return  // Exit the loop after finding the match
                }
            }
        }
    }

    fun getRadioValue(view:View, fieldID:Int):String{
        val man: RadioGroup = view.findViewById(fieldID)
        val selectedId = man.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton = view.findViewById<RadioButton>(selectedId)
            return selectedRadioButton.text.toString()
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTimeToLong(dateTimeString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        return dateTime.toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTimeToLongTemp(dateTimeString: String): Long {
        val formatter = DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        return dateTime.toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertLongToDateTime(epochTime: Long?): LocalDateTime {
        //return LocalDateTime.ofInstant(epochTime?.let { Instant.ofEpochMilli(it) }, ZoneId.systemDefault())
        return Instant.ofEpochMilli(epochTime!!).atZone(ZoneId.systemDefault()).toLocalDateTime()
    }


}