package com.example.instagram

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AIAssistantActivity : AppCompatActivity() {

    private lateinit var userInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    private val apiKey = "AIzaSyAcmcDDL6XIpYOeAeLg-VeCIMpjrmj_7Mk"
    private val client = OkHttpClient()
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aiassistant)
        supportActionBar?.hide()
        userInput = findViewById(R.id.userInput)
        sendButton = findViewById(R.id.sendButton)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)

        chatAdapter = ChatAdapter(messages)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        sendButton.setOnClickListener {
            val inputText = userInput.text.toString()
            if (inputText.isNotBlank()) {
                val timestamp = getCurrentTime()
                chatAdapter.addMessage(ChatMessage(inputText, true, timestamp))
                userInput.text.clear()

                // Show typing...
                chatAdapter.addMessage(ChatMessage("Typing...", false, getCurrentTime()))

                sendToGemini(inputText)
            }
        }
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return formatter.format(Date())
    }

    private fun sendToGemini(userMessage: String) {
        val jsonBody = """
            {
              "contents": [{
                "parts": [{
                  "text": "$userMessage"
                }]
              }]
            }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$apiKey")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody))
            .build()

        client.newCall(request).enqueue(object : Callback {
            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onFailure(call: Call, e: IOException) {
                updateLastBotMessage("Bot: Failed to respond 😢")
            }

            @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("GeminiResponse", "Raw response: $responseBody")
                val reply = parseGeminiReply(responseBody)
                updateLastBotMessage(reply)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private fun updateLastBotMessage(message: String) {
        runOnUiThread {
            messages.removeLast()
            chatAdapter.notifyItemRemoved(messages.size)
            chatAdapter.addMessage(ChatMessage(message, false, getCurrentTime()))
            chatRecyclerView.scrollToPosition(messages.size - 1)
        }
    }

    private fun parseGeminiReply(json: String?): String {
        return try {
            val geminiResponse = gson.fromJson(json, GeminiResponse::class.java)
            geminiResponse.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text ?: "No response from Gemini."
        } catch (e: Exception) {
            Log.e("GeminiAPI", "Parse error: ${e.message}")
            "Error parsing Gemini response."
        }
    }

    data class GeminiResponse(val candidates: List<Candidate>?)
    data class Candidate(val content: Content)
    data class Content(val parts: List<Part>)
    data class Part(val text: String)
}
