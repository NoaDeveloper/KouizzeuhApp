package fr.nozkay.firstapp.classicmode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fr.nozkay.firstapp.R

//  FinActivity.kt
val themes = mutableListOf<String>("➕ Math","\uD83C\uDF0D Geographie","\uD83C\uDDEC\uD83C\uDDE7 Anglais","\uD83C\uDDEB\uD83C\uDDF7 Français","\uD83D\uDE80 Sciences","⚽️ Sports")

class FinActivity : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private var timerValue = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin)
        val codeJoin = intent.getStringExtra("code")
        val pseudo = intent.getStringExtra("pseudo")
        val gameRef = Firebase.firestore.collection("Game").document(codeJoin.toString())
        gameRef.get().addOnSuccessListener { documentSnapshot -> if (documentSnapshot.exists()) {
            if(documentSnapshot.getString("mode").toString() == "\uD83C\uDFC6 Tournoi"){
                themes.remove(documentSnapshot.getString("mdj").toString())
                if(themes.size > 0){
                    val intentToGame = Intent(this@FinActivity, GameActivity::class.java)
                    gameRef.update("temp",1)
                    gameRef.update("mdj",themes[0])
                    intentToGame.putExtra("mdj", themes[0])
                    intentToGame.putExtra("code", codeJoin)
                    intentToGame.putExtra("pseudo", pseudo)
                    startActivity(intentToGame)
                }
                else{
                    timer(codeJoin.toString(), pseudo.toString())
                }
            }else{
                timer(codeJoin.toString(), pseudo.toString())
            }
        }
        }
    }
    fun timer(codeJoin : String, pseudo: String){
        var playerPointsMap = mapOf<String,Any>()
        val gameRef = Firebase.firestore.collection("Game").document(codeJoin.toString())
        countDownTimer = object : CountDownTimer(timerValue * 1000L, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val playerPointsMapJson = Gson().toJson(playerPointsMap)
                val intent = Intent(this@FinActivity, Classement::class.java)
                intent.putExtra("playerPointsMapJson", playerPointsMapJson)
                intent.putExtra("code", codeJoin)
                intent.putExtra("pseudo", pseudo)
                startActivity(intent)
                finish()
            }
        }
        countDownTimer.start()
        gameRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                playerPointsMap = documentSnapshot.get("playerPoints") as? Map<String, Any> ?: emptyMap()
            }
        }
    }

    override fun onBackPressed() {
    }
}