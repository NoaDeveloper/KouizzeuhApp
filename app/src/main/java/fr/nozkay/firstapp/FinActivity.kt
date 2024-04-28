package fr.nozkay.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class FinActivity : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private var timerValue = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fin)
        var playerPointsMap = mapOf<String,Any>()
        val codeJoin = intent.getStringExtra("code")
        val pseudo = intent.getStringExtra("pseudo")
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
}