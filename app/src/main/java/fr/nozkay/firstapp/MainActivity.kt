package fr.nozkay.firstapp

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.nozkay.firstapp.classicmode.GameActivity
import kotlin.random.Random

//  MainActivity.kt

data class gameJoin(
    val host: String,
    val code: String,
    val nb : Int
)
data class Game(
    val id: String = "",
    val players: List<String> = listOf(),
    val playerPoints: Map<String, Int> = mapOf(),
    val host: String = "",
    val start: Boolean,
    val mdj: String,
    val mode: String,
    val playerLobby: Map<String, Boolean> = mapOf(),
    val temp: Int,
    val private: Boolean
    )
class MainActivity : AppCompatActivity() {
    public lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connect = findViewById<Button>(R.id.createLobby)
        val listeGame = findViewById<ListView>(R.id.gameJoin)
        val pseudo = findViewById<TextView>(R.id.pseudo)
        val join = findViewById<Button>(R.id.joinLobbyCode)
        val code = generateRandomCode()
        mediaPlayer = MediaPlayer.create(this, R.raw.quizmusic)
        if (!(mediaPlayer.isPlaying)) {
            mediaPlayer.start()
        }
        connect.setOnClickListener {
            val txtEmail = pseudo.text.toString()
            if (txtEmail.isEmpty() || txtEmail.contentEquals("Pseudo")) {
                Toast.makeText(this, "Veuillez entrez un pseudo", Toast.LENGTH_LONG).show()
            } else {
                val game = Game(
                    id = code,
                    players = listOf(pseudo.text.toString()),
                    playerPoints = mapOf(pseudo.text.toString() to 0),
                    host = pseudo.text.toString(),
                    start = false,
                    mdj = "Math",
                    mode = "",
                    playerLobby = mapOf(pseudo.text.toString() to false),
                    temp = 60,
                    private = true
                )
                Firebase.firestore.collection("Game")
                    .document(code)
                    .set(game)
                val intentToLobbyActivity = Intent(this, LobbyActivity::class.java)
                intentToLobbyActivity.putExtra("code", code)
                intentToLobbyActivity.putExtra("pseudo", pseudo.text.toString())
                startActivity(intentToLobbyActivity)
            }
        }
        val handler = Handler()
        val colRef = Firebase.firestore.collection("Game")
        val listGame = arrayListOf<gameJoin>()
        val runnable = object : Runnable {
            override fun run() {
                colRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents.size() != listGame.size){
                            listGame.clear()
                            for (document in documents) {
                                val players = document["players"] as List<String>
                                listGame.add(
                                    gameJoin(
                                        host = document.getString("host").toString(),
                                        code = document.getString("id").toString(),
                                        nb = players.size
                                    )
                                )
                            }
                            val adapter = GameJoinAdapter(this@MainActivity,R.layout.gamejoin,listGame)
                            listeGame.adapter = adapter
                            listeGame.setOnItemClickListener{parent,view, position, id ->
                                if(pseudo.text.isEmpty()){
                                    Toast.makeText(this@MainActivity,"Vous n'avez pas mis de pseudo !", Toast.LENGTH_SHORT)
                                }
                                else{
                                    val gameRef = Firebase.firestore.collection("Game").document(listGame[position].code)
                                    gameRef.get().addOnSuccessListener { documentSnapshot -> if (documentSnapshot.exists()) {
                                        gameRef.update("players", FieldValue.arrayUnion(pseudo.text.toString()))
                                        gameRef.update("playerPoints.${pseudo.text.toString()}", 0)
                                        gameRef.update("playerLobby.${pseudo.text.toString()}", true)
                                        val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                                        intent.putExtra("code", listGame[position].code.toString())
                                        intent.putExtra("pseudo", pseudo.text.toString())
                                        startActivity(intent)
                                    } }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        println("Erreur lors de la récupération des documents: $exception")
                    }
                handler.postDelayed(this, 1000)
            }
        }
        val overlay = findViewById<View>(R.id.overlay)
        join.setOnClickListener{
            if(pseudo.text.isEmpty()){
                Toast.makeText(this@MainActivity, "Veuillez mettre un pseudo", Toast.LENGTH_LONG)
            }
            else{
                overlay.visibility = View.VISIBLE
                val dialog = Dialog(this@MainActivity)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.popup_join)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val codeJ = dialog.findViewById<TextView>(R.id.codePopup)
                val confirm = dialog.findViewById<Button>(R.id.joinPopup)
                confirm.setOnClickListener{
                    if(codeJ.text.isEmpty()){
                        Toast.makeText(this@MainActivity, "Veuillez indiquez un code", Toast.LENGTH_LONG)
                    }
                    else{
                        val gameRef = Firebase.firestore.collection("Game").document(codeJ.text.toString())
                        gameRef.get().addOnSuccessListener{ document ->  if(document != null && document.exists()) {
                            gameRef.update("players", FieldValue.arrayUnion(pseudo.text.toString()))
                            gameRef.update("playerPoints.${pseudo.text.toString()}", 0)
                            gameRef.update("playerLobby.${pseudo.text.toString()}", true)
                            val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                            intent.putExtra("code", codeJ.text.toString())
                            intent.putExtra("pseudo", pseudo.text.toString())
                            startActivity(intent)
                        }}
                    }
                }
                dialog.show()
            }
        }
        handler.postDelayed(runnable, 1000)
    }

    override fun onBackPressed() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    fun generateRandomCode(): String {
        val charPool = ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}