package com.example.passwordgenerator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnGenerate = findViewById<Button>(R.id.button_generate);
        btnGenerate.isEnabled = false;
        val swtUppercase = findViewById<SwitchCompat>(R.id.switch_uppercase);
        val swtLowercase = findViewById<SwitchCompat>(R.id.switch_lowercase);
        val swtNumber = findViewById<SwitchCompat>(R.id.switch_number);
        val swtSpecialCharacters = findViewById<SwitchCompat>(R.id.switch_specialCharacters);
        val swtAvoidRepetitions = findViewById<SwitchCompat>(R.id.switch_avoidRepetitions);
        val slider = findViewById<Slider>(R.id.slider);
        val txtPassword = findViewById<TextView>(R.id.txtPassword);
        val passwordField = findViewById<LinearLayout>(R.id.passwordField);
        val copyIcon = findViewById<ImageButton>(R.id.copyIcon);

        fun updateBtn(){
            btnGenerate.isEnabled = swtUppercase.isChecked || swtLowercase.isChecked || swtNumber.isChecked || swtSpecialCharacters.isChecked
        }

        swtUppercase.setOnCheckedChangeListener { _, _ -> updateBtn() }
        swtLowercase.setOnCheckedChangeListener { _, _ -> updateBtn() }
        swtNumber.setOnCheckedChangeListener { _, _ -> updateBtn() }
        swtSpecialCharacters.setOnCheckedChangeListener { _, _ -> updateBtn() }

        btnGenerate.setOnClickListener {
            val password = generatePassword(swtUppercase.isChecked, swtLowercase.isChecked, swtNumber.isChecked,
                swtSpecialCharacters.isChecked, swtAvoidRepetitions.isChecked, slider.value.toInt());

            passwordField.visibility = View.VISIBLE
            copyIcon.visibility = View.VISIBLE
            txtPassword.text = password
        }

        copyIcon.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Password", txtPassword.text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Password Copied!", Toast.LENGTH_SHORT).show()
        }
    }

    fun generatePassword(uppercase: Boolean, lowercase: Boolean, number: Boolean, specialChar: Boolean, avoidRep: Boolean, size: Int): String{
        val numList = ('0'..'9').toList()
        val upperList = ('A'..'Z').toList()
        val lowerList = ('a'..'z').toList()
        val specialList = listOf('!', '@', '#', '$', '%', '&', '*', '?', ')', '(')

        var all = listOf<Char>().toMutableList()

        if(number) all += numList
        if(uppercase) all += upperList
        if(lowercase) all += lowerList
        if(specialChar) all += specialList

        var password = ""
        repeat(size){
            var c = all.random()
            if(avoidRep){
                repeat(size){
                    if(c in password){
                        c = all.random()
                    } else{
                        return@repeat
                    }
                }
            }
            password += c
        }
        return password;
    }
}