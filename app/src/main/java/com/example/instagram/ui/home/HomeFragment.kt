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
import com.example.instagram.CourseDetailActivity
import com.example.instagram.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
//import com.example.instagram.CourseDetailActivity // <-- Import your activity

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

        // ⬇️ Add Click Listeners for each "Get Started" button
        binding.pythonbtn.setOnClickListener {
            openCourseDetails("Python")
        }

        binding.javabtn.setOnClickListener {
            openCourseDetails("Java")
        }

        binding.cppbtn.setOnClickListener {
            openCourseDetails("C++")
        }

        binding.androidbtn.setOnClickListener {
            openCourseDetails("Android")
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

    private fun openCourseDetails(courseName: String) {
        val intent = Intent(requireContext(), CourseDetailActivity::class.java)
        intent.putExtra("courseName", courseName)
        startActivity(intent)
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

    private fun startTypingAnimation(fullText: String, textView: android.widget.TextView, onComplete: (() -> Unit)? = null) {
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
