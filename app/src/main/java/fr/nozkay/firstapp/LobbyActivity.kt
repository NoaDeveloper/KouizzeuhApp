package fr.nozkay.firstapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.nozkay.firstapp.classicmode.GameActivity
import java.lang.Integer.parseInt
import java.text.Normalizer
import java.util.Locale

//  LobbyActivity.kt

class LobbyActivity : AppCompatActivity() {
    lateinit var theme: Spinner
    lateinit var mode: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val code = intent.getStringExtra("code")
        val cod = findViewById<TextView>(R.id.code)
        cod.text = code
        val gameRef = Firebase.firestore.collection("Game").document(code.toString())
        val temp = findViewById<TextView>(R.id.TimeGame)
        val pseudo = intent.getStringExtra("pseudo")
        val accueil = findViewById<Button>(R.id.acceuil)
        val themes = listOf("➕ Math","\uD83C\uDF0D Geographie","\uD83C\uDDEC\uD83C\uDDE7 Anglais","\uD83C\uDDEB\uD83C\uDDF7 Français","\uD83D\uDE80 Sciences","⚽️ Sports")
        theme = findViewById<Spinner>(R.id.themes)
        val adapter = ArrayAdapter(this,R.layout.spinner_item,themes)
        theme.adapter = adapter
        theme.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gameRef.update("mdj",themes[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                gameRef.update("mdj",themes[0])
            }

        })
        val modes = listOf("\uD83E\uDDC9 Classique","\uD83E\uDD3C\u200D Equipes","\uD83C\uDFC6 Tournoi")
        mode = findViewById<Spinner>(R.id.mode)
        val adapter2 = ArrayAdapter(this,R.layout.spinner_item,modes)
        mode.adapter = adapter2
        mode.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gameRef.update("mode",modes[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                gameRef.update("mode",modes[0])
            }

        })
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (source.toString().matches(Regex("[0-9]*"))) {
                null
            } else {
                ""
            }
        }
        temp.filters = arrayOf(inputFilter)
        gameRef.update("playerLobby.${pseudo.toString()}",true)
        gameRef.get().addOnSuccessListener { document ->  if(document != null && document.exists()) {
            val host = document.getString("host").toString()
            if(!(host.equalsIgnoreCaseWithAccent(pseudo.toString()))){
                temp.visibility = View.GONE
            }
        }}
        accueil.setOnClickListener{
            gameRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    gameRef.update("players", FieldValue.arrayRemove(pseudo.toString()))
                    gameRef.update("playerPoints", FieldValue.arrayRemove(pseudo.toString()))
                    gameRef.update("playerLobby", FieldValue.arrayRemove(pseudo.toString()))
                    val intent = Intent(this@LobbyActivity, MainActivity::class.java)
                    startActivity(intent)
                    val list = document["player"] as List<String>
                    if(list.size <= 0){
                        gameRef.delete()
                    }
                }
            }
        }
        val start = findViewById<Button>(R.id.startgame)
        start.setOnClickListener {
            gameRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val playerLobby = document["playerLobby"] as Map<String, Boolean>
                    val players = document["players"] as List<String>
                    val allPlayersInLobby = players.all { playerLobby[it] ?: false }

                    if (allPlayersInLobby) {
                        val host = document.getString("host").toString()
                        if (host.equals(pseudo.toString(), ignoreCase = true)) {
                            gameRef.update("start", true)
                        } else {
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Tous les joueurs ne sont pas prêts dans le lobby !", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        val handler = Handler()
        val pl = findViewById<TextView>(R.id.nbjoueur)
        val runnable = object : Runnable {
            override fun run() {
                gameRef.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            if(!(temp.text.isEmpty()) && parseInt(temp.text.toString()) > 0){
                                gameRef.update("temp",parseInt(temp.text.toString()))
                            }
                            val start = documentSnapshot.getBoolean("start")
                            val players = documentSnapshot.get("players") as? List<String> ?: emptyList()
                            var texte = "Joueurs: ${players.size}/20\n"
                            for(i in players){
                                texte += "${i}, "
                            }
                            pl.text = texte
                            if (start == true) {
                                gameRef.get().addOnSuccessListener { document -> if (document != null && document.exists()) {
                                    if(document.getString("mode").toString() == "\uD83C\uDFC6 Tournoi"){
                                        val intentToGame = Intent(this@LobbyActivity, GameActivity::class.java)
                                        intentToGame.putExtra("mdj", "➕ Math")
                                        gameRef.update("temp",1)
                                        gameRef.update("mdj","➕ Math")
                                        intentToGame.putExtra("code", code)
                                        intentToGame.putExtra("pseudo", pseudo)
                                        startActivity(intentToGame)
                                    }
                                    else{
                                        val intentToGame = Intent(this@LobbyActivity, GameActivity::class.java)
                                        intentToGame.putExtra("mdj", document.getString("mdj").toString())
                                        intentToGame.putExtra("code", code)
                                        intentToGame.putExtra("pseudo", pseudo)
                                        startActivity(intentToGame)
                                    }
                                }}
                                handler.removeCallbacks(this)
                            }
                        } else {
                            Log.d(TAG, "Le document n'existe pas")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Erreur lors de la récupération du document", e)
                    }
                handler.postDelayed(this, 1000)
            }
        }
        handler.postDelayed(runnable, 1000)

    }

    override fun onBackPressed() {
        val code = intent.getStringExtra("code")
        val pseudo = intent.getStringExtra("pseudo")
        val gameRef = Firebase.firestore.collection("Game").document(code.toString())
        gameRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                gameRef.update("players", FieldValue.arrayRemove(pseudo.toString()))
                gameRef.update("playerPoints", FieldValue.arrayRemove(pseudo.toString()))
                gameRef.update("playerLobby", FieldValue.arrayRemove(pseudo.toString()))
                val intent = Intent(this@LobbyActivity, MainActivity::class.java)
                startActivity(intent)
            }
            val list = document["player"] as List<String>
            if(list.size <= 0){
                gameRef.delete()
            }
        }
    }

    fun String.normalize(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "")
            .toLowerCase(Locale.getDefault())
    }

    fun String.equalsIgnoreCaseWithAccent(other: String): Boolean {
        return this.normalize() == other.normalize()
    }
}