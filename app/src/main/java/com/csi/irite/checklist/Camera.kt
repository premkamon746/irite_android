package com.csi.irite.checklist

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import ernestoyaquello.com.verticalstepperform.Step

public class Camera(stepTitle: String) : Step<String>(stepTitle) {
    private lateinit var userNameView: EditText

    override fun createStepContentLayout(): View {
        userNameView = EditText(context).apply {
            isSingleLine = true
            hint = "Your Name"
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    markAsCompletedOrUncompleted(true)
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
        return userNameView
    }

    override fun isStepDataValid(stepData: String): IsDataValid {
        val isNameValid = stepData.length >= 3
        val errorMessage = if (!isNameValid) "3 characters minimum" else ""
        return IsDataValid(isNameValid, errorMessage)
    }

    override fun getStepData(): String {
        val userName = userNameView.text
        return userName?.toString() ?: ""
    }

    override fun getStepDataAsHumanReadableString(): String {
        val userName = getStepData()
        return if (userName.isNotEmpty()) userName else "(Empty)"
    }

    override fun onStepOpened(animated: Boolean) {
    }

    override fun onStepClosed(animated: Boolean) {
    }

    override fun onStepMarkedAsCompleted(animated: Boolean) {
    }

    override fun onStepMarkedAsUncompleted(animated: Boolean) {
    }

    override fun restoreStepData(stepData: String) {
        userNameView.setText(stepData)
    }
}


