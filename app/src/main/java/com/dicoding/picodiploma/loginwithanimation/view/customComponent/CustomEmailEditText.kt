package com.dicoding.picodiploma.loginwithanimation.view.customComponent

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class CustomEmailEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var errorIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_x) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmailFormat()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return false
    }

    private fun validateEmailFormat() {
        val email = text.toString().trim()
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error = context.getString(R.string.error_email)
        } else {
            error = null
        }
    }
}