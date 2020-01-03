package ie.wit.hillforts.models.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.HillfortsStore
import ie.wit.hillforts.models.generateRandomId
import org.jetbrains.anko.info

class HillfortFireStore(val context: Context) : HillfortsStore, AnkoLogger {

    val hillforts = ArrayList<HillfortsModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<HillfortsModel> {
        return hillforts
    }

    override fun findSpecific(): List<HillfortsModel> {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        var list = hillforts.filter { it.userid == currentFirebaseUser!!.uid }
        return list.toMutableList()
    }

    override fun findById(id: Long): HillfortsModel? {
        val foundHillfort: HillfortsModel? = hillforts.find { p -> p.id == id }
        return foundHillfort
    }

    override fun create(hillfort: HillfortsModel) {
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillfort.fbId = key
            val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
            hillfort.userid = currentFirebaseUser!!.uid
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
        }
    }

    override fun update(hillfort: HillfortsModel) {
        var foundHillfort: HillfortsModel? = hillforts.find { p -> p.fbId == hillfort.fbId }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.image = hillfort.image
            foundHillfort.image1 = hillfort.image1
            foundHillfort.image2 = hillfort.image2
            foundHillfort.image3 = hillfort.image3
//            foundHillfort.lat = hillfort.lat
//            foundHillfort.lng = hillfort.lng
//            foundHillfort.zoom = hillfort.
            foundHillfort.location = hillfort.location
            foundHillfort.visited = hillfort.visited

            foundHillfort.additionalNotes = hillfort.additionalNotes

            if(hillfort.visited){
                foundHillfort.dateVisited = hillfort.dateVisited
            } else {
                foundHillfort.dateVisited = ""
            }
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)

    }

    override fun delete(hillfort: HillfortsModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun getTotalHillforts(): Int {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        var list = hillforts.filter { it.userid == currentFirebaseUser!!.uid }
        return list.size
    }

    override fun getVisitedHillforts(): Int {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        var listVisited = hillforts.filter { it.userid == currentFirebaseUser!!.uid && it.visited }
        info("list: " + hillforts )
        return listVisited.size
    }

    override fun clear() {
        hillforts.clear()
    }

    fun fetcHillforts(hillfortssReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillfortsModel>(HillfortsModel::class.java) }
                hillfortssReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }
}