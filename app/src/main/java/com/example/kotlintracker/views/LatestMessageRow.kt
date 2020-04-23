package com.example.kotlintracker.views

import com.example.kotlintracker.R
import com.example.kotlintracker.models.ChatMessage
import com.example.kotlintracker.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){
    var chatPartnerUser: User?=null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.message_textview_latest_message.text=chatMessage.text
        val chatPartnerId:String
        if (chatMessage.fromID== FirebaseAuth.getInstance().uid){
            chatPartnerId=chatMessage.fromID
        }else{
            chatPartnerId=chatMessage.fromID
        }

        val ref= FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser=p0.getValue(User::class.java)
                viewHolder.itemView.username_textview_latest_message.text=chatPartnerUser?.username

                val targetImageView=viewHolder.itemView.imageview_latest_message
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })


        viewHolder.itemView.username_textview_latest_message.text="WHTEVER"

    }
    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }
}