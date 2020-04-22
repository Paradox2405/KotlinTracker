package com.example.kotlintracker.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlintracker.R
import com.example.kotlintracker.models.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        //supportActionBar?.title="Chat Log"

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //val username=intent.getStringExtra(NewMessageActivity.USER_KEY)
        supportActionBar?.title=user.username


        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        recyclerview_chat_log.adapter=adapter
    }
}

class ChatFromItem: Item<GroupieViewHolder>() {
    override fun bind(GroupieViewHolder: GroupieViewHolder, Position: Int){

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
    }
class ChatToItem: Item<GroupieViewHolder>() {
    override fun bind(GroupieViewHolder: GroupieViewHolder, Position: Int){

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}