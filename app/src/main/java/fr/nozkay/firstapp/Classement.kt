package fr.nozkay.firstapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Layout
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

//  Classement.kt

class Classement : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private var timerValue = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classement)
        val handler = Handler()
        val playerPointsMapJson = intent.getStringExtra("playerPointsMapJson")
        val codeJoin = intent.getStringExtra("code")
        val playerPointsMap = Gson().fromJson<Map<String, Any>>(playerPointsMapJson, object : TypeToken<Map<String, Any>>() {}.type)
        val playerPointsList = playerPointsMap.toList()
        val sortedPlayerPointsList = playerPointsList.sortedByDescending { it.second as Comparable<Any> }
        val text = findViewById<TextView>(R.id.classModif)
        val nb = sortedPlayerPointsList.size
        val gameRef = Firebase.firestore.collection("Game").document(codeJoin.toString())
        var texte = ""
        for (i in sortedPlayerPointsList.indices) {
            val playerName = sortedPlayerPointsList[i].first
            val playerPoints = sortedPlayerPointsList[i].second as Double
            if(i + 1 == 1){
                texte += "${i + 1}er | $playerName avec ${playerPoints.toInt()} points\n\n"
            }
            else{
                texte += "${i + 1}ème | $playerName avec ${playerPoints.toInt()} points\n\n"
            }
            text.text = texte
            text.setTextColor(Color.WHITE)
            handler.postDelayed({
                val animation = AlphaAnimation(0f, 1f)
                animation.duration = 1000
                text.startAnimation(animation)
            }, i * 3000L)
        }
        val button = findViewById<Button>(R.id.revenirLobby)
        button.setOnClickListener{
            gameRef.update("start", false)
            val intentToGame = Intent(this@Classement, LobbyActivity::class.java)
            intentToGame.putExtra("code", intent.getStringExtra("code"))
            intentToGame.putExtra("pseudo", intent.getStringExtra("pseudo"))
            startActivity(intentToGame)
        }


    }
}