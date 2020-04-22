package com.example.kotlintracker.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.kotlintracker.R
import com.example.kotlintracker.messages.LatestMessagesActivity
import com.example.kotlintracker.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        register_button_registration.setOnClickListener {
          performRegister()
        }

        already_have_account_text_view.setOnClickListener {
    Log.d("RegisterActivity","Try to show login activity")

            //launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            }

        selectphoto_button_register.setOnClickListener {
            Log.d("RegisterActivity","Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }
    }
    var selectedPhotoUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==0 && resultCode==Activity.RESULT_OK && data != null)
        {
            //proceed to check wht selected image was
            Log.d("RegisterActivity","Photo was selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,  selectedPhotoUri)

            selectphoto_imageview_registration.setImageBitmap(bitmap)
            selectphoto_button_register.alpha=0f
            //val bitmapDrawable = BitmapDrawable(bitmap)
            //selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }
    private fun performRegister(){
        val email = email_edittext_registration.text.toString()
        val password = password_edittext_registration.text.toString()
        if (email.isEmpty()||password.isEmpty()) {
            Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
            return

        }

        Log.d("RegisterActivity","Email is:"+email)
        Log.d("RegisterActivity","Password: $password")

        //firebase authentiction
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                //else if sucessful
                Log.d("RegisterActivity","Sucessfully Created user with Uid:${it.result?.user?.uid}")
                uploadImageToFirebaseStorge()
            }
            .addOnFailureListener {
                Log.d("RegisterActivity","Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create User: ${it.message}", Toast.LENGTH_SHORT).show()
            }

    }
    private fun uploadImageToFirebaseStorge(){
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref= FirebaseStorage.getInstance().getReference("/images/$filename")
        val addOnSuccessListener = ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully Uploaded Image:${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {

                    Log.d("RegisterActivity", "File Location:$it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                //logging
            }

    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid,username_edittext_registration.text.toString(),profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity","Saved User to FB database")
                //below line takes you directly to home without going to previous screen
                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }



    }
}
