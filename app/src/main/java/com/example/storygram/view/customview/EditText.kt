package com.example.storygram.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storygram.R
import com.example.storygram.utils.isEmailValid

class EditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {
    init {
        when (id) {
            R.id.ed_login_password, R.id.ed_register_password -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        error = if (s.toString().length < 8) {
                            context.getString(R.string.password_alert)
                        } else {
                            null
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }

            R.id.ed_login_email, R.id.ed_register_email -> {
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        error = if (!isEmailValid(s.toString())) {
                            context.getString(R.string.invalid_email)
                        } else {
                            null
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {

                    }

                })
            }
        }
    }

}