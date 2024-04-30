package fr.nozkay.firstapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt
import java.text.Normalizer
import java.util.Locale

//  LobbyActivity.kt

class LobbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        val code = intent.getStringExtra("code")
        val cod = findViewById<TextView>(R.id.code)
        cod.text = code
        val gameRef = Firebase.firestore.collection("Game").document(code.toString())
        val math = findViewById<Button>(R.id.math)
        val geo = findViewById<Button>(R.id.geo)
        val temp = findViewById<TextView>(R.id.TimeGame)
        val pseudo = intent.getStringExtra("pseudo")
        val anglais = findViewById<Button>(R.id.anglais)
        val fr = findViewById<Button>(R.id.français)
        val culture = findViewById<Button>(R.id.cultureg)
        val blindtest = findViewById<Button>(R.id.blindtest)
        val accueil = findViewById<Button>(R.id.acceuil)
        var mdj = "Math"
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
                }
            }
        }
        math.setOnClickListener{
            gameRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val host = document.getString("host").toString()
                        if(host.equalsIgnoreCaseWithAccent(pseudo.toString())){
                            math.setBackgroundColor(resources.getColor(R.color.bleu))
                            geo.setBackgroundColor(resources.getColor(R.color.background))
                            anglais.setBackgroundColor(resources.getColor(R.color.background))
                            fr.setBackgroundColor(resources.getColor(R.color.background))
                            culture.setBackgroundColor(resources.getColor(R.color.background))
                            blindtest.setBackgroundColor(resources.getColor(R.color.background))
                            mdj = "Math"
                            gameRef.update("mdj","Math")
                        }
                        else{
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
        blindtest.setOnClickListener{
            gameRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val host = document.getString("host").toString()
                        if(host.equalsIgnoreCaseWithAccent(pseudo.toString())){
                            math.setBackgroundColor(resources.getColor(R.color.background))
                            geo.setBackgroundColor(resources.getColor(R.color.background))
                            anglais.setBackgroundColor(resources.getColor(R.color.background))
                            fr.setBackgroundColor(resources.getColor(R.color.background))
                            culture.setBackgroundColor(resources.getColor(R.color.background))
                            blindtest.setBackgroundColor(resources.getColor(R.color.bleu))
                            mdj = "BlindTest"
                            gameRef.update("mdj","BlindTest")
                        }
                        else{
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
        anglais.setOnClickListener{
            gameRef.get()
            .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val host = document.getString("host").toString()
                        if(host.equalsIgnoreCaseWithAccent(pseudo.toString())){
                            math.setBackgroundColor(resources.getColor(R.color.background))
                            geo.setBackgroundColor(resources.getColor(R.color.background))
                            fr.setBackgroundColor(resources.getColor(R.color.background))
                            culture.setBackgroundColor(resources.getColor(R.color.background))
                            anglais.setBackgroundColor(resources.getColor(R.color.bleu))
                            blindtest.setBackgroundColor(resources.getColor(R.color.background))
                            mdj = "Anglais"
                            gameRef.update("mdj","Anglais")
                        }
                        else{
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
        fr.setOnClickListener{
            gameRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val host = document.getString("host").toString()
                        if(host.equalsIgnoreCaseWithAccent(pseudo.toString())){
                            math.setBackgroundColor(resources.getColor(R.color.background))
                            geo.setBackgroundColor(resources.getColor(R.color.background))
                            anglais.setBackgroundColor(resources.getColor(R.color.background))
                            fr.setBackgroundColor(resources.getColor(R.color.bleu))
                            culture.setBackgroundColor(resources.getColor(R.color.background))
                            blindtest.setBackgroundColor(resources.getColor(R.color.background))
                            mdj = "Français"
                            gameRef.update("mdj","Français")
                        }
                        else{
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
        culture.setOnClickListener{
            gameRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val host = document.getString("host").toString()
                        if(host.equalsIgnoreCaseWithAccent(pseudo.toString())){
                            math.setBackgroundColor(resources.getColor(R.color.background))
                            geo.setBackgroundColor(resources.getColor(R.color.background))
                            anglais.setBackgroundColor(resources.getColor(R.color.background))
                            fr.setBackgroundColor(resources.getColor(R.color.background))
                            culture.setBackgroundColor(resources.getColor(R.color.bleu))
                            blindtest.setBackgroundColor(resources.getColor(R.color.background))
                            mdj = "Culture"
                            gameRef.update("mdj","Culture")
                        }
                        else{
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

        geo.setOnClickListener{
            gameRef.get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val host = document.getString("host").toString()
                        if(host.equalsIgnoreCaseWithAccent(pseudo.toString())){
                            math.setBackgroundColor(resources.getColor(R.color.background))
                            geo.setBackgroundColor(resources.getColor(R.color.bleu))
                            anglais.setBackgroundColor(resources.getColor(R.color.background))
                            fr.setBackgroundColor(resources.getColor(R.color.background))
                            culture.setBackgroundColor(resources.getColor(R.color.background))
                            blindtest.setBackgroundColor(resources.getColor(R.color.background))
                            mdj = "Geo"
                            gameRef.update("mdj","Geo")
                        }
                        else{
                            Toast.makeText(this, "Vous n'êtes pas l'host de la partie !", Toast.LENGTH_LONG).show()
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
                                    val intentToGame = Intent(this@LobbyActivity, GameActivity::class.java)
                                    intentToGame.putExtra("mdj", document.getString("mdj").toString())
                                    intentToGame.putExtra("code", code)
                                    intentToGame.putExtra("temp", parseInt(temp.text.toString()))
                                    intentToGame.putExtra("pseudo", pseudo)
                                    startActivity(intentToGame)
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

    fun String.normalize(): String {
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{M}".toRegex(), "")
            .toLowerCase(Locale.getDefault())
    }

    fun String.equalsIgnoreCaseWithAccent(other: String): Boolean {
        return this.normalize() == other.normalize()
    }
}