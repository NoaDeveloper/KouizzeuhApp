package fr.nozkay.firstapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GameJoinAdapter(
    var mContext : Context,
    var resource : Int,
    var values : ArrayList<gameJoin>
): ArrayAdapter<gameJoin>(mContext, resource, values){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val game = values[position]
        val itemView = LayoutInflater.from(mContext).inflate(resource,parent,false)
        val host = itemView.findViewById<TextView>(R.id.partieDe)
        val code = itemView.findViewById<TextView>(R.id.CodeGame)
        val nbjoueur = itemView.findViewById<TextView>(R.id.nbJoueur)
        host.text = "Partie de " + game.host
        code.text = "Code: " + game.code
        nbjoueur.text = game.nb.toString() + "/20"
        return itemView
    }
}

