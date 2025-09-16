package com.simonimal.minimalcurconv

import java.text.DecimalFormat
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class ConvertActivity : AppCompatActivity() {
    private lateinit var cur1_inputLayout: TextInputLayout
    private lateinit var cur2_inputLayout: TextInputLayout
    private lateinit var cur1_editText: TextInputEditText
    private lateinit var cur2_editText: TextInputEditText
    private var isUpdating = false
    private var ratio = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)

        cur1_inputLayout = findViewById(R.id.cur1_inputLayout)
        cur2_inputLayout = findViewById(R.id.cur2_inputLayout)

        cur1_editText = findViewById(R.id.cur1_editText)
        cur2_editText = findViewById(R.id.cur2_editText)

        val cur1 = intent.getStringExtra("cur1")
        val cur2 = intent.getStringExtra("cur2")
        val ratioString = intent.getStringExtra("ratio")

        cur1_inputLayout.setSuffixText(cur1)
        cur2_inputLayout.setSuffixText(cur2)

        ratio = ratioString?.toDoubleOrNull() ?: 1.0

        cur1_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val input_value = editable.toString().toDoubleOrNull()
                if (input_value == null || input_value == 0.0) {
                    cur2_editText.setText("")
                } else {
                    val decimalFormat = DecimalFormat("#.####")
                    val formattedResult = decimalFormat.format(input_value * ratio)
                    cur2_editText.setText(formattedResult)
                    cur2_editText.setSelection(formattedResult.length)
                }
                isUpdating = false
            }
        })

        cur2_editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (isUpdating) return
                isUpdating = true
                val input_value = editable.toString().toDoubleOrNull()
                if (input_value == null || input_value == 0.0) {
                    cur1_editText.setText("")
                } else {
                    val decimalFormat = DecimalFormat("#.####")
                    val formattedResult = decimalFormat.format(input_value / ratio)
                    cur1_editText.setText(formattedResult)
                    cur1_editText.setSelection(formattedResult.length)
                }
                isUpdating = false
            }
        })

        val goBack_button: Button = findViewById(R.id.goBack_button)
        val clear_button: Button = findViewById(R.id.clear_button)

        goBack_button.setOnClickListener {
            finish()
        }

        clear_button.setOnClickListener {
            cur1_editText.setText("")
            cur2_editText.setText("")
        }

        cur1_editText.requestFocus()
    }
}