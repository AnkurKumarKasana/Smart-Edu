package com.example.instagram.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.instagram.AIAssistantActivity
import com.example.instagram.androiddetails
import com.example.instagram.cppdetails
import com.example.instagram.databinding.FragmentHomeBinding
import com.example.instagram.javadetails
import com.example.instagram.pythondetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        startTypingAnimation(" ", binding.welcomeText) {
            fetchAndUpdateUserName()
        }

        // Course buttons
        binding.pythonbtn.setOnClickListener {
            val intent = Intent(requireContext(), pythondetails::class.java)
            startActivity(intent)
        }

        binding.javabtn.setOnClickListener {
            val intent = Intent(requireContext(), javadetails::class.java)
            startActivity(intent)
        }

        binding.cppbtn.setOnClickListener {
            val intent = Intent(requireContext(), cppdetails::class.java)
            startActivity(intent)
        }

        binding.androidbtn.setOnClickListener {
            val intent = Intent(requireContext(), androiddetails::class.java)
            startActivity(intent)
        }



        // ✅ AI Assistant FAB click
        binding.aiAssistantButton.setOnClickListener {
            val intent = Intent(requireContext(), AIAssistantActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.apply {
            statusBarColor = Color.GRAY
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun fetchAndUpdateUserName() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("User").document(uid).get()
            .addOnSuccessListener { document ->
                val name = document.getString("name")
                if (!name.isNullOrEmpty()) {
                    startTypingAnimation("Welcome, $name", binding.welcomeText)
                }
            }
    }

    private fun startTypingAnimation(
        fullText: String,
        textView: android.widget.TextView,
        onComplete: (() -> Unit)? = null
    ) {
        val delay: Long = 50
        var index = 0

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (index <= fullText.length) {
                    textView.text = fullText.substring(0, index)
                    index++
                    handler.postDelayed(this, delay)
                } else {
                    onComplete?.invoke()
                }
            }
        }
        handler.post(runnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
