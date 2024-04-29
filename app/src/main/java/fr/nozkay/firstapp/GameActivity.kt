package fr.nozkay.firstapp

import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.media.AsyncPlayer
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Integer.parseInt
import java.text.Normalizer
import java.util.Locale
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val mdj = intent.getStringExtra("mdj")
        val question = findViewById<TextView>(R.id.question)
        val resultat = findViewById<TextView>(R.id.réponse)
        val timer = findViewById<TextView>(R.id.Time)
        val passe = findViewById<Button>(R.id.Passe)
        val points = findViewById<TextView>(R.id.points)
        var point = 0
        val DELAY_MS: Long = 100
        val handler = Handler()
        val codeJoin = intent.getStringExtra("code")
        val pseudo = intent.getStringExtra("pseudo")
        var temp = 60
        val gameRef = Firebase.firestore.collection("Game").document(codeJoin.toString())
        gameRef.update("playerLobby.${pseudo.toString()}",false)
        gameRef.get().addOnSuccessListener { document ->  if(document != null && document.exists()) {
            val temp2 = document.getString("temp").toString()
            temp = parseInt(temp2)
        }}
        mediaPlayer = MediaPlayer.create(this,R.raw.quizmusic)
        mediaPlayer.stop()
        if(mdj.toString().equalsIgnoreCaseWithAccent("Math")){
            var operationString = generateRandomOperation()
            var splitOperation = operationString.split(" = ")
            val operation = splitOperation[0]
            var result = splitOperation[1].toInt()
            question.text = operation
            resultat.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val userAnswer = s.toString().toIntOrNull()
                    if (userAnswer != null && userAnswer == result) {
                        operationString = generateRandomOperation()
                        splitOperation = operationString.split(" = ")
                        question.text = splitOperation[0]
                        result = splitOperation[1].toInt()
                        resultat.setText("")
                        point += 1
                        gameRef.update("playerPoints.${pseudo.toString()}",point)
                    }
                }
            })
            passe.setOnClickListener{
                operationString = generateRandomOperation()
                splitOperation = operationString.split(" = ")
                question.text = splitOperation[0]
                result = splitOperation[1].toInt()
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        if(mdj == "Anglais"){
            var anglais = getWord()
            var anglaislist = listOf("")
            question.text = anglais.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(anglais.second)) {
                    do{
                        anglais = getWord()
                    }while(anglaislist.contains(anglais.first))
                    question.text = anglais.first
                    resultat.setText("")
                    point += 1
                    gameRef.update("playerPoints.${pseudo.toString()}", point)
                }
            }
            resultat.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { handler.removeCallbacks(checkAnswerRunnable) }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    handler.postDelayed(checkAnswerRunnable, DELAY_MS)
                }
            })
            passe.setOnClickListener{
                do{
                    anglais = getWord()
                }while(anglaislist.contains(anglais.first))
                question.text = anglais.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        if(mdj == "Geo"){
            var capitale = getCapital()
            var capitalelist = listOf("")
            question.text = capitale.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(capitale.second)) {
                    do{
                        capitale = getCapital()
                    }while(capitalelist.contains(capitale.first))
                    question.text = capitale.first
                    resultat.setText("")
                    point += 1
                    gameRef.update("playerPoints.${pseudo.toString()}",point)
                }
            }
            resultat.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { handler.removeCallbacks(checkAnswerRunnable) }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    handler.postDelayed(checkAnswerRunnable, DELAY_MS)
                }
            })
            passe.setOnClickListener{
                do{
                    capitale = getCapital()
                }while(capitalelist.contains(capitale.first))
                question.text = capitale.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        countDownTimer = object : CountDownTimer(temp * 60 * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "${millisUntilFinished / 1000}"
                points.text = "Votre nombre de point:\n${point}"
            }
            override fun onFinish() {
                val intent = Intent(this@GameActivity, FinActivity::class.java)
                intent.putExtra("code", codeJoin)
                intent.putExtra("tournoi", intent.getStringExtra("tournoi"))
                intent.putExtra("pseudo", pseudo)
                startActivity(intent)
                finish()
            }
        }
        countDownTimer.start()
    }

    fun generateRandomOperation(): String {
        var result: Int
        var operator: String
        var num1: Int
        var num2: Int

        do {
            operator = when (Random.nextInt(4)) {
                0 -> "+"
                1 -> "-"
                2 -> "x"
                else -> "÷"
            }
            if(operator == "x"){
                num1 = Random.nextInt(2, 20)
                num2 = Random.nextInt(2, 20)
            }
            else{
                num1 = Random.nextInt(2, 50)
                num2 = Random.nextInt(2, 50)
            }
            result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "x" -> num1 * num2
                else -> {
                    val quotient = num1 / num2
                    num1 = quotient * num2
                    quotient
                }
            }
        } while (result.toDouble() != num1.toDouble() / num2.toDouble() && num1 == num2)

        return "$num1 $operator $num2 = $result"
    }

    fun getCapital(): Pair<String,String>{
        val europeanCapitals = listOf(
            "Washington D.C." to "États-Unis", "Moscou" to "Russie", "Pékin" to "Chine", "Tokyo" to "Japon", "New Delhi" to "Inde",
            "Brasília" to "Brésil", "Ottawa" to "Canada", "Canberra" to "Australie", "Le Caire" to "Égypte", "Mexico" to "Mexique",
            "Ankara" to "Turquie", "Bangkok" to "Thaïlande", "Séoul" to "Corée du Sud", "Riyad" to "Arabie Saoudite", "Bagdad" to "Irak",
            "Kiev" to "Ukraine", "Berlin" to "Allemagne", "Londres" to "Royaume-Uni", "Madrid" to "Espagne", "Rome" to "Italie",
            "Buenos Aires" to "Argentine", "Paris" to "France", "Téhéran" to "Iran", "Dubaï" to "Émirats Arabes Unis", "Caracas" to "Venezuela",
            "Bogota" to "Colombie", "Damas" to "Syrie", "Alger" to "Algérie", "Cotonou" to "Bénin", "Helsinki" to "Finlande",
            "Bucarest" to "Roumanie", "Sofia" to "Bulgarie", "Dublin" to "Irlande", "Vilnius" to "Lituanie", "Tallinn" to "Estonie",
            "Bratislava" to "Slovaquie", "Riga" to "Lettonie", "Luxembourg" to "Luxembourg", "Nicosie" to "Chypre", "La Valette" to "Malte",
            "Ljubljana" to "Slovénie", "Andorre-la-Vieille" to "Andorre", "Bern" to "Suisse", "Saint-Marin" to "Saint-Marin", "Vaduz" to "Liechtenstein",
            "Monaco" to "Monaco", "Vatican" to "Vatican"
        )
        val randomIndex = Random.nextInt(europeanCapitals.size)
        val (capital, country) = europeanCapitals[randomIndex]
        return Pair("Quel est le pays qui à pour capitale:\n$capital", country)
    }

    fun getWord(): Pair<String,String> {
        val englishWords = listOf(
            "hello", "house", "cat", "dog", "book", "tree", "computer", "friend", "school", "car",
            "window", "table", "chair", "phone", "flower", "music", "sun", "moon", "star", "game",
            "water", "food", "family", "city", "country", "color", "animal", "bird", "fish", "house",
            "baby", "boy", "girl", "man", "woman", "street", "garden", "park", "room", "bed",
            "lamp", "light", "dark", "hot", "cold", "warm", "big", "small", "fast", "slow",
            "high", "low", "heavy", "light", "soft", "hard", "good", "bad", "old", "new",
            "happy", "sad", "angry", "tired", "quiet", "loud", "clean", "dirty", "full", "empty",
            "early", "late", "easy", "difficult", "interesting", "boring", "beautiful", "ugly", "rich", "poor",
            "healthy", "sick", "strong", "weak", "simple", "complex", "kind", "mean", "funny", "serious",
            "strange", "normal", "brave", "fearful", "calm", "nervous", "proud", "shy", "busy", "free",
            "peace", "love", "hope", "dream", "smile", "laugh", "cry", "dance", "sing", "play",
            "run", "walk", "jump", "swim", "climb", "drive", "fly", "travel", "read", "write",
            "learn", "teach", "study", "work", "rest", "relax", "meditate", "breathe", "eat", "drink",
            "sleep", "wake", "listen", "hear", "see", "watch", "touch", "feel", "taste", "smell",
            "talk", "speak", "communicate", "understand", "think", "imagine", "create", "build", "destroy", "change",
            "grow", "shrink", "expand", "contract", "rise", "fall", "begin", "end", "start", "finish"
        )
        val frenchTranslations = listOf(
            "bonjour", "maison", "chat", "chien", "livre", "arbre", "ordinateur", "ami", "ecole", "voiture",
            "fenêtre", "table", "chaise", "téléphone", "fleur", "musique", "soleil", "lune", "étoile", "jeu",
            "eau", "nourriture", "famille", "ville", "pays", "couleur", "animal", "oiseau", "poisson", "maison",
            "bébé", "garçon", "fille", "homme", "femme", "rue", "jardin", "parc", "chambre", "lit",
            "lampe", "lumière", "sombre", "chaud", "froid", "tiède", "grand", "petit", "rapide", "lent",
            "haut", "bas", "lourd", "léger", "doux", "dur", "bon", "mauvais", "vieux", "nouveau",
            "heureux", "triste", "en colère", "fatigué", "calme", "fort", "propre", "sale", "plein", "vide",
            "tôt", "tard", "facile", "difficile", "intéressant", "ennuyeux", "beau", "moche", "riche", "pauvre",
            "sain", "malade", "fort", "faible", "simple", "complexe", "gentil", "méchant", "drôle", "sérieux",
            "étrange", "normal", "courageux", "craintif", "calme", "nerveux", "fier", "timide", "occupé", "libre",
            "paix", "amour", "espoir", "rêve", "sourire", "rire", "pleurer", "danser", "chanter", "jouer",
            "courir", "marcher", "sauter", "nager", "grimper", "conduire", "voler", "voyager", "lire", "écrire",
            "apprendre", "enseigner", "étudier", "travailler", "se reposer", "se relaxer", "méditer", "respirer", "manger", "boire",
            "dormir", "se réveiller", "écouter", "entendre", "voir", "regarder", "toucher", "ressentir", "goûter", "sentir",
            "parler", "dire", "communiquer", "comprendre", "penser", "imaginer", "créer", "construire", "détruire", "changer",
            "grandir", "rétrécir", "s'étendre", "se contracter", "s'élever", "tomber", "commencer", "terminer", "démarrer", "finir"
        )
        val randomIndex = Random.nextInt(englishWords.size)
        val englishWord = englishWords[randomIndex]
        val frenchTranslation = frenchTranslations[randomIndex]
        return Pair("Traduisez: $englishWord", frenchTranslation)
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
