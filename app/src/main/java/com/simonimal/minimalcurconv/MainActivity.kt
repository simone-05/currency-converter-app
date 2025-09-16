package com.simonimal.minimalcurconv

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.EditorInfo
import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var cur1_editText: EditText
    private lateinit var cur2_editText: EditText
    private lateinit var ratio_editText: EditText
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cur1_editText = findViewById(R.id.cur1_editText)
        cur2_editText = findViewById(R.id.cur2_editText)
        ratio_editText = findViewById(R.id.ratio_editText)

        val go_button: Button = findViewById(R.id.go_button)
        val clear_button: Button = findViewById(R.id.clear_button)

        sharedPreferences = getSharedPreferences("CurrencyPrefs", MODE_PRIVATE)

        loadSavedValues()   // loads the fields from past sessions

        goConvertScreen()   // try to jump to the conversion screen if the fields are already filled from past session

        go_button.setOnClickListener {
            if (cur1_editText.text.isNullOrEmpty() || cur2_editText.text.isNullOrEmpty() || ratio_editText.text.isNullOrEmpty()) {
                Snackbar.make(go_button, "Please fill in all fields", Snackbar.LENGTH_SHORT).show()
                false
            }
            goConvertScreen()
        }

        clear_button.setOnClickListener {
            cur1_editText.setText("")
            cur2_editText.setText("")
            ratio_editText.setText("")
        }

        ratio_editText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (cur1_editText.text.isNullOrEmpty() || cur2_editText.text.isNullOrEmpty() || ratio_editText.text.isNullOrEmpty()) {
                    return@setOnEditorActionListener false
                }
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(ratio_editText.windowToken, 0)
                goConvertScreen()
            }
            return@setOnEditorActionListener true
        }
    }

    override fun onPause() {
        super.onPause()
        saveValues(sharedPreferences)
    }

    override fun onDestroy() {
        super.onDestroy()
        saveValues(sharedPreferences)
    }

    private fun goConvertScreen() {
        val cur1 = cur1_editText.text.toString().trim()
        val cur2 = cur2_editText.text.toString().trim()
        val ratio = ratio_editText.text.toString().trim()

        if (cur1.isNotEmpty() && cur2.isNotEmpty() && ratio.isNotEmpty()) {
            val intent = Intent(this, ConvertActivity::class.java)

            intent.putExtra("cur1", cur1)
            intent.putExtra("cur2", cur2)
            if (ratio[0] == '.') {
                intent.putExtra("ratio", "0" + ratio)
            } else {
                intent.putExtra("ratio", ratio)
            }

            startActivity(intent)
        }
    }

    private fun saveValues(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()

        editor.putString("cur1", cur1_editText.text.toString().trim())
        editor.putString("cur2", cur2_editText.text.toString().trim())
        editor.putString("ratio", ratio_editText.text.toString().trim())

        editor.apply()
    }

    private fun loadSavedValues() {
        val cur1 = sharedPreferences.getString("cur1", "")
        val cur2 = sharedPreferences.getString("cur2", "")
        val ratio = sharedPreferences.getString("ratio", "")

        cur1_editText.setText(cur1)
        cur2_editText.setText(cur2)
        ratio_editText.setText(ratio)
    }
}