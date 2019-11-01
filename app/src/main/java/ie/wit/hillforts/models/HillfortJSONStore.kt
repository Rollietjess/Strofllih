package ie.wit.hillforts.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.wit.hillforts.helpers.exists
import ie.wit.hillforts.helpers.read
import ie.wit.hillforts.helpers.write
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortsModel>>() {}.type


fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore : HillfortsStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortsModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HillfortsModel> {
        return hillforts
    }

    override fun findSpecific() : MutableList<HillfortsModel> {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        var list = hillforts.filter { it.userid == currentFirebaseUser!!.uid }
        return list.toMutableList()
    }

    override fun create(hillfort: HillfortsModel) {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        hillfort.id = generateRandomId()
        hillfort.userid = currentFirebaseUser!!.uid
        hillforts.add(hillfort)
        serialize()
    }


    override fun update(hillfort: HillfortsModel) {
        var foundHillfort: HillfortsModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            foundHillfort.visited = hillfort.visited
            foundHillfort.dateVisited = hillfort.dateVisited
            foundHillfort.additionalNotes = hillfort.additionalNotes
        }
        serialize()
    }

    override fun delete(hillfort: HillfortsModel) {
        hillforts.remove(hillfort)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}