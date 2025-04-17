package com.example.instagram.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.instagram.R
import com.example.instagram.cppdetails
import com.example.instagram.databinding.FragmentDashboardBinding
import com.example.instagram.javadetails
import com.example.instagram.pythondetails
import com.example.instagram.androiddetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val enrolledContainer = binding.enrolledCourseContainer

        val uid = auth.currentUser?.uid
        uid?.let {
            db.collection("User").document(it).get()
                .addOnSuccessListener { document ->
                    val enrolledCourses = document.get("enrolledCourses") as? List<String> ?: emptyList()

                    enrolledCourses.forEach { course ->
                        val cardView = layoutInflater.inflate(R.layout.single_course_card, null)

                        val title = cardView.findViewById<TextView>(R.id.courseName)
                        val image = cardView.findViewById<ImageView>(R.id.courseImage)
                        val button = cardView.findViewById<Button>(R.id.goToCourseButton) // ID reused

                        when (course) {
                            "Python" -> {
                                title.text = "Python Programming Course"
                                image.setImageResource(R.drawable.python)
                                button.text = "Continue Learning"
                                button.setOnClickListener {
                                    startActivity(Intent(requireContext(), pythondetails::class.java))
                                }
                            }
                            "Java" -> {
                                title.text = "Java Programming Course"
                                image.setImageResource(R.drawable.java)
                                button.text = "Continue Learning"
                                button.setOnClickListener {
                                    startActivity(Intent(requireContext(), javadetails::class.java))
                                }
                            }
                            "C++" -> {
                                title.text = "C++ Programming Course"
                                image.setImageResource(R.drawable.cplusplus)
                                button.text = "Continue Learning"
                                button.setOnClickListener {
                                    startActivity(Intent(requireContext(), cppdetails::class.java))
                                }
                            }
                            "Android" -> {
                                title.text = "Android Development Course"
                                image.setImageResource(R.drawable.android)
                                button.text = "Continue Learning"
                                button.setOnClickListener {
                                    startActivity(Intent(requireContext(), androiddetails::class.java))
                                }
                            }
                            else -> {
                                title.text = "$course Course"
                                image.setImageResource(R.drawable.python) // fallback
                                button.visibility = View.GONE
                            }
                        }

                        val layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        layoutParams.setMargins(0, 0, 0, 32)

                        enrolledContainer.addView(cardView, layoutParams)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to load enrolled courses.", Toast.LENGTH_SHORT).show()
                }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
