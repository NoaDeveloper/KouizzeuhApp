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

//  GameActivity.kt

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
        val gameRef = Firebase.firestore.collection("Game").document(codeJoin.toString())
        gameRef.update("playerLobby.${pseudo.toString()}",false)
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
            var anglaislist = mutableListOf<String>()
            question.text = anglais.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(anglais.second)) {
                    do{
                        anglais = getWord()
                    }while(anglaislist.contains(anglais.first))
                    anglaislist.add(anglais.first)
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
                anglaislist.add(anglais.first)
                question.text = anglais.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        if(mdj == "Geo"){
            var capitale = getCapital()
            var capitalelist = mutableListOf<String>()
            question.text = capitale.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(capitale.second)) {
                    do{
                        capitale = getCapital()
                    }while(capitalelist.contains(capitale.first))
                    capitalelist.add(capitale.first)
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
                capitalelist.add(capitale.first)
                question.text = capitale.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        if(mdj == "Français"){
            var mot = getMot()
            var motList = mutableListOf<String>()
            question.text = mot.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(mot.second)) {
                    do{
                        mot = getMot()
                    }while(motList.contains(mot.first))
                    motList.add(mot.first)
                    question.text = mot.first
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
                    mot = getMot()
                }while(motList.contains(mot.first))
                motList.add(mot.first)
                question.text = mot.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        if(mdj == "Culture"){
            var mot = general()
            var motList = mutableListOf<String>()
            question.text = mot.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(mot.second)) {
                    do{
                        mot = general()
                    }while(motList.contains(mot.first))
                    motList.add(mot.first)
                    question.text = mot.first
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
                    mot = general()
                }while(motList.contains(mot.first))
                motList.add(mot.first)
                question.text = mot.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        gameRef.get().addOnSuccessListener { document ->  if(document != null && document.exists()) {
            val temp = parseInt(document.get("temp").toString())
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
        }}
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

    fun getMot(): Pair<String,String>{
        val mots = listOf(
            "apprendre" to "aprendre", "beauté" to "bauté", "drapeau" to "drâpeau", "essentiel" to "essantiel",
            "fête" to "fète", "gênant" to "génant", "hôpital" to "hospital", "inviter" to "invité", "jaune" to "jone",
            "lait" to "laît", "montagne" to "montagne", "noël" to "noel", "œuf" to "euf", "poussière" to "poussière",
            "quatre" to "quatre", "rencontrer" to "rencontrer", "soleil" to "soleil", "théâtre" to "théatre", "uniforme" to "uniforme",
            "vélo" to "vélo", "wagon" to "waggon", "xylophone" to "xylophone", "yacht" to "yacht", "zéro" to "zéro",
            "aider" to "aidére", "boîte" to "boite", "chanson" to "chançon", "doux" to "dou", "école" to "ecole",
            "famille" to "famile", "garçon" to "garsone", "heureux" to "heurteux", "île" to "ile", "jour" to "jour",
            "koala" to "coala", "lune" to "lune", "mille" to "mile", "nouveau" to "nouvo", "oignon" to "oignon",
            "piano" to "pianno", "question" to "quession", "renard" to "ranard", "sourire" to "sourire", "tortue" to "tortue",
            "uniforme" to "ûniforme", "vache" to "vâche", "wagon" to "waggon", "xénophobe" to "xenophobe", "yoga" to "yoga",
            "zèbre" to "zèbre", "ananas" to "ananas", "biscuit" to "biscuit", "cactus" to "cactus", "dragon" to "dragon",
            "écharpe" to "echarpe", "flamme" to "flamme", "girafe" to "giraphe", "hibou" to "hîbou", "igloo" to "iglou",
            "jardin" to "Gardin", "koala" to "quoala", "lampe" to "lampe", "manège" to "manège", "nénuphar" to "nénufar",
            "oignon" to "oinon", "parapluie" to "parapluie", "quiche" to "quiche", "raisin" to "raisin", "sirène" to "sirene",
            "tortue" to "tortu", "uniforme" to "uniforme", "verre" to "verre", "wagon" to "waggon", "xylophone" to "xylophone",
            "yaourt" to "yaourtte", "zeppelin" to "zeplin", "abeille" to "abeile", "bougie" to "boûgie", "chapeau" to "châpeau",
            "diamant" to "diament", "éléphant" to "éléfant", "fraise" to "fraize"
        )
        val randomIndex = Random.nextInt(mots.size)
        val (good, mauvais) = mots[randomIndex]
        return Pair("Corrige le mot:\n$mauvais", good)
    }
    fun general(): Pair<String,String>{
        val mots = listOf(
            "Quel célèbre empereur romain a été assassiné en 44 av. J.-C. ?" to "César",
            "Quel événement a marqué la fin de la Seconde Guerre mondiale en Europe en 1945 ?" to "Capitulation Allemande",
            "Quel explorateur a découvert l'Amérique en 1492 ?" to "Christophe Colomb",
            "Quel empire a été fondé par Napoléon Bonaparte au début du XIXe siècle ?" to "Empire français",
            "Quelle guerre a opposé les États-Unis et l'Union soviétique de manière indirecte entre 1947 et 1991 ?" to "Guerre froide",
            "Quel est le plus long fleuve de France selon la partie coulant sur le territoire ?" to "La Loire",
            "Quel est le plus grand lac d'eau douce en Afrique ?" to "Le lac Victoria",
            "Quelle est la plus grande chaîne de montagnes du monde ?" to "L'Himalaya",
            "Quel est le désert le plus grand du monde ?" to "Le Sahara",
            "Quel est le plus grand archipel du monde ?" to "L'Indonésie",
            "Qui a réalisé le film 'Forrest Gump' ?" to "Robert Zemeckis",
            "Dans quel film l'acteur Tom Hanks joue-t-il un rôle de naufragé ?" to "Seul au monde",
            "Quel film d'animation met en scène des jouets qui prennent vie lorsque les humains ne les regardent pas ?" to "Toy Story",
            "Quelle actrice a remporté un Oscar pour son rôle dans 'La La Land' ?" to "Emma Stone",
            "Quel film d'Alfred Hitchcock met en scène un meurtrier utilisant un couteau de cuisine comme arme ?" to "Psychose",
            "Quel est le nom du créateur de Facebook ?" to "Mark Zuckerberg",
            "Dans quel pays est né le site de partage de vidéos YouTube ?" to "États-Unis",
            "Quel réseau social est principalement utilisé pour le partage de messages courts appelés 'tweets' ?" to "Twitter",
            "Quel est le nom du co-fondateur de Twitter ?" to "Jack Dorsey",
            "Quel est le réseau social qui permet aux utilisateurs de partager des photos et des vidéos de courtes durées ?" to "Instagram",
            "Quel est le nom de la couche protectrice de gaz entourant la Terre ?" to "L'atmosphère",
            "Quel est le phénomène météorologique caractérisé par des vents violents tournant en spirale ?" to "cyclone",
            "Quel est le nom du processus par lequel l'eau s'évapore des plantes et des sols vers l'atmosphère ?" to "transpiration végétale",
            "Quelle est la couche la plus externe de la Terre, composée de plaques mobiles ?" to "lithosphère",
            "Quel est le nom du phénomène naturel de réchauffement de la Terre dû à l'augmentation des gaz à effet de serre ?" to "réchauffement climatique",
        )
        val randomIndex = Random.nextInt(mots.size)
        val (question, reponse) = mots[randomIndex]
        return Pair(question, reponse)
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
