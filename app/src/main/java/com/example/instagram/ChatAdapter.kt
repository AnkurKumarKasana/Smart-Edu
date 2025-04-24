package com.example.instagram

import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val timeStamp: TextView = itemView.findViewById(R.id.timeStamp)
        val messageContainer: LinearLayout = itemView.findViewById(R.id.messageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_message_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageText.text = message.message
        holder.timeStamp.text = message.timestamp

        if (message.isUser) {
            holder.messageContainer.gravity = Gravity.END
            holder.messageText.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_user_message)
            holder.profileImage.setImageResource(R.drawable.ic_user)
        } else {
            holder.messageContainer.gravity = Gravity.START
            holder.messageText.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.bg_ai_message)
            holder.profileImage.setImageResource(R.drawable.ic_bot)
        }
    }

    override fun getItemCount(): Int = messages.size

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }
}
