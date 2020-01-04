package ie.wit.hillforts.models.firebase

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import ie.wit.hillforts.helpers.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import ie.wit.hillforts.models.HillfortsModel
import ie.wit.hillforts.models.HillfortsStore
import ie.wit.hillforts.models.generateRandomId
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File

class HillfortFireStore(val context: Context) : HillfortsStore, AnkoLogger {

    val hillforts = ArrayList<HillfortsModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<HillfortsModel> {
        return hillforts
    }

    override fun findSpecific(): List<HillfortsModel> {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        var list = hillforts.filter { it.userid == currentFirebaseUser!!.uid }
        return list.toMutableList()
    }

    override fun findFavourite(): List<HillfortsModel> {
        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser
        var list = hillforts.filter { it.userid == currentFirebaseUser!!.uid && it.favourite == true }
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
            hillfort.id = generateRandomId()
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
            updateImage(hillfort)
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
            foundHillfort.rating = hillfort.rating
            foundHillfort.favourite = hillfort.favourite

            foundHillfort.additionalNotes = hillfort.additionalNotes

            if(hillfort.visited){
                foundHillfort.dateVisited = hillfort.dateVisited
            } else {
                foundHillfort.dateVisited = ""
            }
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
        if ((hillfort.image.length) > 0 && (hillfort.image[0] != 'h')) {
            updateImage(hillfort)
        }
    }

    override fun delete(hillfort: HillfortsModel) {
        val storage = FirebaseStorage.getInstance()
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
        // Create a storage reference from our app
//        val storageRef = storage.reference
//
//        // Create a reference to the file to delete
//        val desertRef = storageRef.child("users").child(userId).child("hillforts").child(hillfort.image)
//
//        // Delete the file
//        desertRef.delete().addOnSuccessListener {
//            // File deleted successfully
//        }.addOnFailureListener {
//            // Uh-oh, an error occurred!
//        }
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
        st = FirebaseStorage.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }

    fun updateImage(hillfort: HillfortsModel) {
        if (hillfort.image != "") {
            val fileName = File(hillfort.image)
            val imageName = fileName.getName()

            var imageRef = st.child(userId + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(context, hillfort.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        hillfort.image = it.toString()
                        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                    }
                }
            }
        }
    }
}