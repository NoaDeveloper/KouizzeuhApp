package fr.nozkay.firstapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

data class Game(
    val id: String = "",
    val players: List<String> = listOf(),
    val playerPoints: Map<String, Int> = mapOf(),
    val host: String = "",
    val start: Boolean,
    val mdj: String,
)
class MainActivity : AppCompatActivity() {
    public lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val connect = findViewById<Button>(R.id.createLobby)
        val lobby = findViewById<Button>(R.id.connectLobby)
        val pseudo = findViewById<EditText>(R.id.pseudo)
        val CodeJoin = findViewById<EditText>(R.id.codeJoin)
        val code = generateRandomCode()
        mediaPlayer = MediaPlayer.create(this,R.raw.quizmusic)
        if(!(mediaPlayer.isPlaying)){
            mediaPlayer.start()
        }
        connect.setOnClickListener{
            val txtEmail = pseudo.text.toString()
            if(txtEmail.isEmpty() || txtEmail.contentEquals("Pseudo")){
                Toast.makeText(this, "Veuillez entrez un pseudo", Toast.LENGTH_LONG).show()
            }
            else{
                val game = Game(
                    id = code,
                    players = listOf(pseudo.text.toString()),
                    playerPoints = mapOf(pseudo.text.toString() to 0),
                    host = pseudo.text.toString(),
                    start = false,
                    mdj = "Math"
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
        lobby.setOnClickListener{
            val txtEmail = pseudo.text.toString()
            if(txtEmail.isEmpty() || txtEmail.contentEquals("Pseudo")){
                Toast.makeText(this, "Veuillez entrez un pseudo", Toast.LENGTH_LONG).show()
            }
            if(!(CodeJoin.text.toString().isEmpty() || CodeJoin.text.toString().contentEquals("Code"))){
                val gameRef = Firebase.firestore.collection("Game").document(CodeJoin.text.toString())
                gameRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            gameRef.update("players", FieldValue.arrayUnion(pseudo.text.toString()))
                                .addOnSuccessListener {
                                    gameRef.update("playerPoints.${pseudo.text.toString()}", 0)
                                        .addOnSuccessListener {
                                            val intentToLobbyActivity = Intent(this, LobbyActivity::class.java)
                                            intentToLobbyActivity.putExtra("code", CodeJoin.text.toString())
                                            intentToLobbyActivity.putExtra("pseudo", pseudo.text.toString())
                                            startActivity(intentToLobbyActivity)
                                        }
                                        .addOnFailureListener { e -> Log.e(TAG, "Erreur lors de la mise à jour des points du joueur", e)
                                            Toast.makeText(this, "Une erreur s'est produite, veuillez réessayer", Toast.LENGTH_LONG).show()
                                        }
                                }
                                .addOnFailureListener { e ->
                                    Log.e(
                                        TAG,
                                        "Erreur lors de la mise à jour de la liste des joueurs",
                                        e
                                    )
                                    Toast.makeText(
                                        this,
                                        "Une erreur s'est produite, veuillez réessayer",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    }
            }
        }
    }

    fun generateRandomCode(): String {
        val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..6)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}