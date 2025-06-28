package com.csi.irite.helper

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.csi.irite.MainActivity
import com.csi.irite.R
import com.csi.irite.utils.ViewAnimation
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class UiViewHelper(var context: Context, val v: View, val childFragmentMGR: FragmentManager) {
    val hp = DateTimeHelper()
    var debugBorder:Boolean? = false
    private val LOADING_DURATION = 20000L
    fun CreateSection(t1:String, tag1: String, t2:String, tag2: String): LinearLayout {
        val hor = createLinearLayoutHORIZONTAL()
        val vert1 = createLinearLayoutVERTICAL()
        val t1 = createTextView(t1)
        val e1 = createEditText("", tag1)
        vert1.addView(t1)
        vert1.addView(e1)
        hor.addView(vert1)

        hor.addView(createSpacing())

        val vert2 = createLinearLayoutVERTICAL()
        val t2 = createTextView(t2)
        val e2 = createEditText("",tag2)
        vert2.addView(t2)
        vert2.addView(e2)
        hor.addView(vert2)
        return hor
    }

    fun createFreeSection(strings: List<List<String>>): LinearLayout{
        val hor = createLinearLayoutHORIZONTAL()
        for (str in strings) {
            val name = str[0]
            val value = str[1]
            val tag = str[2]
            val vert = createLinearLayoutVERTICAL()
            val t1 = createTextView(name)
            val e1 = createEditText(value,tag)
            vert.addView(t1)
            vert.addView(e1)
            hor.addView(vert)
            hor.addView(createSpacing()) // add specing
        }
        return hor
    }

    fun crateRadio(strings: List<String>,_tag:String,radioValue:String="", preTextView:Boolean=true,txt:String = "", suffTextField:Boolean=true, suffID:String, suffxText:String = "",):LinearLayout{
        val radioGroup = RadioGroup(context).apply {
            tag = _tag
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = RadioGroup.HORIZONTAL
            setPadding(0, resources.getDimensionPixelSize(R.dimen.spacing_large), 0, 0)
        }

        if(preTextView){
            val t = createTextView(txt)
            radioGroup.addView(t)
        }


        for (str in strings) {
            val b = createRadioButton(str)
            radioGroup.addView(b)
        }

        if(suffTextField){
            var e:View? = null
            if(suffxText != "" && suffID == "") {
                radioGroup.addView(createSpacing())
                e = createTextViewBold(suffxText)
            }else{
                e = createEditText(suffxText, suffID)
            }
            radioGroup.addView(e)
        }

        val hor = createLinearLayoutHORIZONTAL()
        val hor2 = createLinearLayoutHORIZONTAL()
        hor.addView(radioGroup)
        hor2.addView(hor)
        hor2.addView(createSpacing())

        for (i in 0 until radioGroup.childCount) {

            val radioButton = radioGroup.getChildAt(i)
            if (radioButton is RadioButton) {
                if (radioButton.text.toString() == radioValue) {
                    radioButton.isChecked = true
                    break
                }
            }
        }

        return hor2
    }

    fun createRadioButton(txt:String): RadioButton {
        val radioButton = RadioButton(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            isEnabled = true
            id = View.generateViewId()
            setPadding(
                resources.getDimensionPixelSize(R.dimen.spacing_medium), // Horizontal padding
                resources.getDimensionPixelSize(R.dimen.spacing_middle), // Vertical padding
                resources.getDimensionPixelSize(R.dimen.spacing_medium), // Horizontal padding
                resources.getDimensionPixelSize(R.dimen.spacing_middle) // Vertical padding
            )
            text = resources.getString(R.string.from_asset_request_phone)
            //setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1)
            setTextColor(context.resources.getColor(R.color.grey_60))

            setText(txt)
        }
        return radioButton
    }

    fun createFreeFlexSection(strings: List<List<String>>): LinearLayout{
        val hor = createLinearLayoutHORIZONTAL()
        for (str in strings) {
            val name = str[0]
            val value = str[1]
            val type = str[2]
            val tag = str[3]
            val vert = createLinearLayoutVERTICAL()
            val t1 = createTextView(name)
            val e1 = selectViewField(type, value, tag)
            vert.addView(t1)
            vert.addView(e1)
            hor.addView(vert)
            hor.addView(createSpacing()) // add specing
        }
        return hor
    }

    fun createFreeFlexSectionView(strings: List<List<String>>): LinearLayout{
        val hor = createLinearLayoutHORIZONTAL()
        for (str in strings) {
            val name = str[0]
            val value = str[1]
            val type = str[2]
            val tag = str[3]
            val vert = createLinearLayoutHORIZONTAL()
            val t1 = createTextView(name)
            val e1 = selectViewField(type, value, tag)
            vert.addView(t1)
            vert.addView(e1)
            hor.addView(vert)
            hor.addView(createSpacing()) // add specing
        }
        return hor
    }

    fun createTextView(txt:String): TextView {
        val textView = TextView(context).apply {
            // Set text
            text = resources.getString(R.string.from_asset_request_place)
            LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            // Set text appearance
            //setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1)
            // Set text color
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            setTextColor(context.resources.getColor(R.color.black))
            // Set layout parameters

            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT // Set the weight (adjust as needed)
            ).apply {
                // Set margins (this code remains the same)
                setMargins(
                    context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    context.resources.getDimensionPixelSize(R.dimen.spacing_large),
                    context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    context.resources.getDimensionPixelSize(R.dimen.spacing_medium)
                )
            }
            setText(txt)
        }

        if(debugBorder == true) {
            enaleLine(textView, Color.BLUE)
        }

        return textView
    }
    //val layoutParams = LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)

    fun createTextViewBold(txt:String): TextView {
        val textView = TextView(context).apply {
            // Set text
            text = resources.getString(R.string.from_asset_request_place)

            setTextColor(context.resources.getColor(R.color.black))
            // Set layout parameters
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.LEFT
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
                ViewGroup.MarginLayoutParams.WRAP_CONTENT  // Set the weight (adjust as needed)
            ).apply {
                // Set margins (this code remains the same)
                setMargins(
                    context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    context.resources.getDimensionPixelSize(R.dimen.spacing_large),
                    context.resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    context.resources.getDimensionPixelSize(R.dimen.spacing_medium)
                )
            }
            setText(txt)
        }

        if(debugBorder == true) {
            enaleLine(textView, Color.YELLOW)
        }
        return textView
    }


    fun createEditText(txt:String="", _tag: String): EditText{
        val editText = EditText(context).apply {
            // Set style
            //val style = R.style.Widget.MaterialComponents.TextInputLayout.OutlinedBox
            //TextInputLayout.applyStyle(style, this)
            tag = _tag
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                // Set margins
                /*setMargins(
                    resources.getDimensionPixelSize(R.dimen.margin_left),
                    resources.getDimensionPixelSize(R.dimen.margin_top),
                    resources.getDimensionPixelSize(R.dimen.margin_right),
                    resources.getDimensionPixelSize(R.dimen.margin_bottom)
                )*/
            }


            // Set background
            setBackgroundResource(R.drawable.edit_text_round_bg_white)

            // Set minHeight
            minHeight = resources.getDimensionPixelSize(R.dimen.spacing_xmlarge)

            // Set paddingLeft and paddingRight
            setPadding(
                resources.getDimensionPixelSize(R.dimen.spacing_middle),
                paddingTop,
                resources.getDimensionPixelSize(R.dimen.spacing_middle),
                paddingBottom
            )

            setTextColor(resources.getColor(R.color.black))
            setText(txt)

        }

        if(debugBorder == true) {
            enaleLine(editText, Color.GREEN)
        }
        return editText
    }

    fun enaleLine(view:View, borderColor:Int){
        //val borderColor = Color.GREEN // Change the color as needed
        val borderWidth = 5 // Change the width as needed

        // Create a GradientDrawable to set the border properties
        val borderDrawable = GradientDrawable()
        borderDrawable.setStroke(borderWidth, borderColor)
        borderDrawable.cornerRadius = 8f // Set corner radius if needed

        // Set the drawable as background for the LinearLayout
        view.background = borderDrawable/**/
    }

    fun createLinearLayoutVERTICAL(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(

            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f // weight for this view
        )
        linearLayout.orientation = LinearLayout.VERTICAL

        if(debugBorder == true) {
            enaleLine(linearLayout, Color.RED)
        }

        return linearLayout
    }

    fun createLinearLayoutHORIZONTAL(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1.0f
        )
        linearLayout.orientation = LinearLayout.HORIZONTAL

        if(debugBorder == true) {
            enaleLine(linearLayout, Color.RED)
        }

        return linearLayout
    }

    fun createSpacing(): View {
        val spacingView = View(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                context.resources.getDimensionPixelSize(R.dimen.spacing_large),
                ViewGroup.MarginLayoutParams.WRAP_CONTENT,
            ).apply {
            }
        }

        if(debugBorder == true) {
            enaleLine(spacingView, Color.BLACK)
        }
        return spacingView
    }

    fun crateAutocompletTextView(txt:String, _tag: String):AutoCompleteTextView{
        val autoCompleteTextView = AutoCompleteTextView(context).apply {
            tag = _tag
            //layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimensionPixelSize(R.dimen.spacing_xmlarge))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_grey, 0)
            //setTextAppearance(context, R.style.TextAppearance_AppCompat_Body1)
            setBackgroundResource(R.drawable.edit_text_round_bg_white)
            isFocusable = false
            isFocusableInTouchMode = false
            maxLines = 1
            isSingleLine = true
            setPadding(
                resources.getDimensionPixelSize(R.dimen.spacing_middle),
                paddingTop,
                resources.getDimensionPixelSize(R.dimen.spacing_middle),
                paddingBottom
            )
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            ).apply {
                // Set margins
                /*setMargins(
                    resources.getDimensionPixelSize(R.dimen.margin_left),
                    resources.getDimensionPixelSize(R.dimen.margin_top),
                    resources.getDimensionPixelSize(R.dimen.margin_right),
                    resources.getDimensionPixelSize(R.dimen.margin_bottom)
                )*/
            }

            minHeight = resources.getDimensionPixelSize(R.dimen.spacing_xmlarge)
            setText(txt)
            setTextColor(resources.getColor(R.color.black))
        }

        if(debugBorder == true) {
            enaleLine(autoCompleteTextView, Color.YELLOW)
        }
        return autoCompleteTextView
    }

    fun createTextInputLayout():TextInputLayout{
        val textInputLayout = TextInputLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,1.0f)
        }
        return textInputLayout
    }

    fun createDateField(txt:String="", tag:String):TextInputLayout{
        val textInputLayout = createTextInputLayout()
        val autoComplete = crateAutocompletTextView(txt, tag)
        textInputLayout.addView(autoComplete)

        hp.eventSelectDate(autoComplete, childFragmentMGR)
        return textInputLayout
    }

    fun createTimeField(txt:String="", tag: String):TextInputLayout{
        val textInputLayout = createTextInputLayout()
        val autoComplete = crateAutocompletTextView(txt,tag)
        textInputLayout.addView(autoComplete)

        hp.eventSelectTime(autoComplete, context)
        return textInputLayout
    }

    fun selectViewField(field:String, txt:String="", tag:String):View {
        when (field) {
            "date" -> return createDateField(txt, tag)
            "time" -> return createTimeField(txt, tag)
            "edit" -> return createEditText(txt, tag)
            "text" -> return createTextView(txt)
            "text_bold" -> return createTextViewBold(txt)
            "view"->return createSpacing()
            "button"->return createSaveButton(txt, tag)
        }
        return View(context)
    }


    fun createSaveButton(txt:String, _tag: String):MaterialButton{
        val materialButton = MaterialButton(context).apply {
            // Apply the style programmatically
            tag = _tag
            val style = R.style.Button_Primary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setAutoSizeTextTypeUniformWithConfiguration(1, 100, 1, TypedValue.COMPLEX_UNIT_SP)
            }
            setTextAppearance(context, style)
            // Set layout parameters
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, resources.getDimensionPixelSize(R.dimen.spacing_xxxlarge))
            text = txt
            icon = context.resources.getDrawable(R.drawable.ic_add)
            iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
        }

        if(debugBorder == true) {
            enaleLine(materialButton, Color.YELLOW)
        }
        return materialButton
    }

    fun findRadioButtonByText(radioGroup: RadioGroup, text: String): RadioButton? {
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is RadioButton) {
                if (child.text.toString() == text) {
                    return child
                }
            }
        }
        return null
    }

    fun retrieveRadio(reportType:RadioGroup,view:View,oview:View, radioGroupID:String=""){
        val checkedRadioButtonId = reportType.checkedRadioButtonId
        val radioGroup: RadioGroup = view.findViewWithTag<RadioGroup>(radioGroupID)
        if (checkedRadioButtonId != -1) {
            val oRadioButton: RadioButton = oview.findViewById(checkedRadioButtonId)
            val radioButton = findRadioButtonByText(radioGroup, oRadioButton.text.toString())
            radioButton!!.isChecked = true
        }
    }

    fun retrieveRadio(radioGroup:RadioGroup,view:View):String{
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId

        if (selectedRadioButtonId != -1) {
            // At least one RadioButton is selected
            val radioButton: RadioButton = view.findViewById(selectedRadioButtonId)

            // Get the text of the selected RadioButton
            val selectedValue = radioButton.text.toString()

            // Now you have the selected value
            return selectedValue
        }
        return ""
    }

    /*fun unsetLoading() {
        val weakActivity = context as MainActivity
        val lyt_progress = weakActivity.findViewById<View>(R.id.lyt_progress) as LinearLayout
        lyt_progress.visibility = View.GONE
        lyt_progress.setAlpha(1.0f)

    }*/

    fun createRecyclerView(_tag:String=""): RecyclerView {
        val recyclerView = RecyclerView(context).apply {
            tag = _tag
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
            isVerticalScrollBarEnabled = true
            isDrawingCacheEnabled = true
        }
        return recyclerView
    }

}

