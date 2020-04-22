package com.example.kotlintracker.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlintracker.R
import com.example.kotlintracker.models.ChatMessage
import com.example.kotlintracker.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG="ChatLog"
    }
    val adapter=GroupAdapter<GroupieViewHolder>()

    var toUser: User?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
       recyclerview_chat_log.adapter=adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //val username=intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title=toUser?.username


     //setupDummyData()
        listenForMessages()


        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send msg")
            performSendMessage()
        }
    }
    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               val chatMessage= p0.getValue(ChatMessage::class.java)
                Log.d(TAG, chatMessage?.text)
                if(chatMessage!= null) {
                    Log.d(TAG, chatMessage.text)

                    if(chatMessage.fromID==FirebaseAuth.getInstance().uid){

                        val currentUser=LatestMessagesActivity.currentUser?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))
                    } else{

                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }


    private fun performSendMessage(){
    //how to send a msg to FB
     val text= edittext_chat_log.text.toString()
        val fromID=FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId=user.uid
        if (fromID==null)return

        val reference= FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage= ChatMessage(reference.key!!, text,fromID,toId,System.currentTimeMillis()/1000 )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our Chat message: ${reference.key}")
            }

    }

}

class ChatFromItem(val text:String, val user: User): Item<GroupieViewHolder>() {
    override fun bind(GroupieViewHolder: GroupieViewHolder, Position: Int){
        GroupieViewHolder.itemView.textview_from_row.text=text


        val uri=user.profileImageUrl
        val targetImageView=GroupieViewHolder.itemView.imageview_chat_from_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
    }
class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    override fun bind(GroupieViewHolder: GroupieViewHolder, Position: Int){
        GroupieViewHolder.itemView.textview_to_row.text=text

        //load user image into star
        val uri=user.profileImageUrl
        val targetImageView=GroupieViewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}