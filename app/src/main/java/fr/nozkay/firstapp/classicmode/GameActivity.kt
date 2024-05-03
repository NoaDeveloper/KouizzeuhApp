package fr.nozkay.firstapp.classicmode

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.nozkay.firstapp.R
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
        val theme = findViewById<TextView>(R.id.themeText)
        var point = 0
        val DELAY_MS: Long = 100
        val handler = Handler()
        val codeJoin = intent.getStringExtra("code")
        val pseudo = intent.getStringExtra("pseudo")
        val gameRef = Firebase.firestore.collection("Game").document(codeJoin.toString())
        gameRef.update("playerLobby.${pseudo.toString()}",false)
        mediaPlayer = MediaPlayer.create(this, R.raw.quizmusic)
        mediaPlayer.stop()
        theme.text = "Theme Actuel: " + mdj
        if(mdj.toString().equalsIgnoreCaseWithAccent("➕ Math")){
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
        if(mdj == "\uD83C\uDDEC\uD83C\uDDE7 Anglais"){
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
        if(mdj == "\uD83C\uDF0D Geographie"){
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
        if(mdj == "\uD83C\uDDEB\uD83C\uDDF7 Français"){
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
        if(mdj == "⚽️ Sports"){
            var mot = getSport()
            var motList = mutableListOf<String>()
            question.text = mot.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(mot.second)) {
                    do{
                        mot = getSport()
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
                    mot = getSport()
                }while(motList.contains(mot.first))
                motList.add(mot.first)
                question.text = mot.first
                resultat.setText("")
                point -= 1
                gameRef.update("playerPoints.${pseudo.toString()}",point)
            }
        }
        if(mdj == "\uD83D\uDE80 Sciences"){
            var mot = getScience()
            var motList = mutableListOf<String>()
            question.text = mot.first
            val checkAnswerRunnable = Runnable {
                val userAnswer = resultat.text.toString()
                if (userAnswer.equalsIgnoreCaseWithAccent(mot.second)) {
                    do{
                        mot = getScience()
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
                    mot = getScience()
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

    fun getSport(): Pair<String, String>{
        val sports = listOf(
            "Cristiano Ronaldo" to "Football",
            "Lionel Messi" to "Football",
            "LeBron James" to "Basketball",
            "Tom Brady" to "Football américain",
            "Michael Jordan" to "Basketball",
            "Roger Federer" to "Tennis",
            "Usain Bolt" to "Athlétisme",
            "Serena Williams" to "Tennis",
            "Rafael Nadal" to "Tennis",
            "Lewis Hamilton" to "Formule 1",
            "Mohamed Salah" to "Football",
            "Zlatan Ibrahimovic" to "Football",
            "Kevin Durant" to "Basketball",
            "Novak Djokovic" to "Tennis",
            "Simone Biles" to "Gymnastique",
            "Megan Rapinoe" to "Football",
            "Stephen Curry" to "Basketball",
            "Luka Modric" to "Football",
            "Serena Williams" to "Tennis",
            "Lindsey Vonn" to "Ski",
            "Michael Phelps" to "Natation",
            "Caroline Wozniacki" to "Tennis",
            "Marcel Hirscher" to "Ski",
            "Marta Vieira da Silva" to "Football",
            "Neeraj Chopra" to "Lancer du javelot",
            "Gareth Bale" to "Football",
            "Yohan Blake" to "Athlétisme",
            "Usain Bolt" to "Athlétisme",
            "Tiger Woods" to "Golf",
            "Katie Ledecky" to "Natation",
            "Rory McIlroy" to "Golf",
            "Virat Kohli" to "Cricket",
            "Naomi Osaka" to "Tennis",
            "Ronda Rousey" to "MMA",
            "Anthony Joshua" to "Boxe",
            "Sachin Tendulkar" to "Cricket",
            "Manny Pacquiao" to "Boxe",
            "Zinedine Zidane" to "Football",
            "Tony Parker" to "Basketball",
            "Didier Deschamps" to "Football",
            "Thierry Henry" to "Football",
            "Sébastien Loeb" to "Rallye",
            "Yannick Noah" to "Tennis",
            "Michel Platini" to "Football",
            "Alain Prost" to "Formule 1",
            "Karim Benzema" to "Football",
            "Nikola Karabatic" to "Handball",
            "Teddy Riner" to "Judo",
            "Laurent Blanc" to "Football",
            "Antoine Griezmann" to "Football",
            "Jo-Wilfried Tsonga" to "Tennis",
            "Marie-José Pérec" to "Athlétisme",
            "Jean Alesi" to "Formule 1",
            "Camille Lacourt" to "Natation",
            "Raphaël Varane" to "Football",
            "Paul Pogba" to "Football",
            "Marion Bartoli" to "Tennis",
            "Lilian Thuram" to "Football",
            "Nicolas Batum" to "Basketball",
            "Kilian Mbappé" to "Football",
            "Marie-Amélie Le Fur" to "Athlétisme",
            "David Douillet" to "Judo",
            "Florent Manaudou" to "Natation",
            "Franck Ribéry" to "Football",
            "Laura Flessel" to "Escrime",
            "Guy Forget" to "Tennis",
            "Sofiane Oumiha" to "Boxe",
        )
        val randomIndex = Random.nextInt(sports.size)
        val (personne, sport) = sports[randomIndex]
        return Pair("Quel est le sport que pratique (ou pratiquais avant) :\n$personne", sport)
    }

    fun getScience(): Pair<String, String>{
        val questions = listOf(
            "Quelle est la formule chimique de l'eau?" to "H2O",
            "Quel est l'élément chimique le plus abondant dans l'univers?" to "Hydrogène",
            "Quelle est la planète la plus proche du soleil?" to "Mercure",
            "Quelle est la vitesse de la lumière dans le vide?" to "299,792,458 m/s",
            "Quelle est la loi de la gravitation universelle?" to "Loi de Newton",
            "Quel est le plus petit élément constitutif de la matière?" to "Atome",
            "Quelle est la plus grande planète du système solaire?" to "Jupiter",
            "Quel est le nombre de chromosomes dans une cellule humaine?" to "46",
            "Quelle est la molécule responsable de la coloration des feuilles en vert?" to "Chlorophylle",
            "Quel est l'organe du corps humain chargé de filtrer le sang?" to "Rein",
            "Quelle est la réaction chimique de base dans la photosynthèse?" to "CO2 + H2O -> Glucose + O2",
            "Quel est l'élément chimique le plus lourd naturellement produit?" to "Oganesson",
            "Quelle est la force qui maintient les planètes en orbite autour du soleil?" to "Gravité",
            "Quel est l'organe du corps humain qui produit l'insuline?" to "Pancréas",
            "Quelle est la découverte qui a révolutionné la physique au début du 20e siècle?" to "Théorie de la relativité",
            "Quelle est la distance moyenne entre la Terre et la Lune?" to "384,400 km",
            "Quel est l'organe du corps humain qui produit la bile?" to "Foie",
            "Quel est le processus par lequel les plantes produisent de la nourriture?" to "Photosynthèse",
            "Quel est le nom de l'enzyme qui décompose les protéines dans l'estomac?" to "Pepsine",
            "Quelle est la force qui maintient les atomes ensemble dans une molécule?" to "Liaison chimique",
            "Quelle est la distance entre la Terre et le Soleil?" to "149,597,870.7 km",
            "Quel est le composant principal de l'air que nous respirons?" to "Azote",
            "Quel est le plus petit os du corps humain?" to "Étrier",
            "Quel est le liquide rouge transportant l'oxygène dans le corps?" to "Sang",
            "Quel est le nom de la théorie scientifique de l'évolution?" to "Théorie de l'évolution",
            "Quel est le nom de la réaction chimique responsable de la combustion?" to "Oxydation",
            "Quel est le nom de l'organe sensoriel responsable de la vision?" to "Œil",
            "Quelle est la force qui maintient les électrons autour du noyau atomique?" to "Force électromagnétique",
            "Quelle est la partie du cerveau responsable du langage?" to "Cortex cérébral",
            "Quel est l'élément chimique principal constituant la croûte terrestre?" to "Oxygène",
            "Quelle est la molécule responsable du stockage de l'énergie dans les cellules?" to "ATP",
        )
        val randomIndex = Random.nextInt(questions.size)
        val (question, reponse) = questions[randomIndex]
        return Pair("$question", reponse)
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
            "Il pleuvait des cordes quand nous sommes arrivés à la gare." to "Imparfait",
            "Elle chante une chanson magnifique." to "Présent",
            "Nous irons au cinéma demain soir." to "Futur",
            "J'avais fini mes devoirs avant de sortir." to "Plus-que-parfait",
            "Tu aurais dû me prévenir plus tôt." to "Conditionnel",
            "Ils ont mangé au restaurant hier soir." to "Passé composé",
            "Il doit arriver bientôt." to "Présent",
            "Elle se réveille toujours tôt le matin." to "Présent",
            "Nous devrions partir avant la fin du film." to "Conditionnel",
            "Il sera bientôt l'heure de partir." to "Futur",
            "Elle avait toujours rêvé de visiter Paris." to "Plus-que-parfait",
            "Je pourrais t'aider si tu me le demandais." to "Conditionnel",
            "Nous sommes partis en vacances l'année dernière." to "Passé composé",
            "Il a plu toute la journée." to "Passé composé",
            "Elle ferme la porte doucement." to "Présent",
            "Tu serais surpris de la réponse." to "Conditionnel",
            "Nous allions souvent à la plage quand nous étions enfants." to "Imparfait",
            "Il va faire beau demain." to "Futur proche",
            "Elle était fatiguée après sa journée de travail." to "Imparfait",
            "Tu as déjà vu ce film?" to "Présent",
            "Nous avons passé de bonnes vacances." to "Passé composé",
            "Il fera bientôt nuit." to "Futur",
            "Elle aurait aimé partir en vacances." to "Conditionnel passé",
            "Nous mangions quand il est arrivé." to "Imparfait",
            "Elle prépare le dîner en ce moment." to "Présent",
            "Je verrai ce que je peux faire." to "Futur simple",
            "Il aurait pu gagner s'il avait essayé." to "Conditionnel passé",
            "Elles sont allées au parc cet après-midi." to "Passé composé",
            "Il fait toujours ses devoirs après l'école." to "Présent"
        )
        val randomIndex = Random.nextInt(mots.size)
        val (mauvais, good) = mots[randomIndex]
        return Pair("À quel temps est cette phrase ?:\n$mauvais", good)
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
    override fun onBackPressed() {
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
