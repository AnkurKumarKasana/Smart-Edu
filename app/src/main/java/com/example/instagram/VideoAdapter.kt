package com.example.instagram

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

data class VideoItem(val title: String = "", val description: String = "", val videoUrl: String = "")

class VideoAdapter(
    private val context: Context,
    private val videoList: List<VideoItem>,
    private var watchedCount: Int,
    private val onWatched: () -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoTitle: TextView = view.findViewById(R.id.videoTitle)
        val videoDescription: TextView = view.findViewById(R.id.videoDescription)
        val videoThumbnail: ImageView = view.findViewById(R.id.videoThumbnail)
        val playButton: ImageView = view.findViewById(R.id.playButton)
        val videoView: VideoView = view.findViewById(R.id.videoView)

        fun bind(video: VideoItem, position: Int) {
            videoTitle.text = video.title
            videoDescription.text = video.description

            // Load thumbnail from Cloudinary or fallback image


            playButton.setOnClickListener {
                videoThumbnail.visibility = View.GONE
                playButton.visibility = View.GONE
                videoView.visibility = View.VISIBLE

                val mediaController = MediaController(context)
                mediaController.setAnchorView(videoView)
                videoView.setMediaController(mediaController)
                videoView.setVideoURI(Uri.parse(video.videoUrl))
                videoView.requestFocus()
                videoView.start()

                // Update progress only once per new video
                if (position == watchedCount) {
                    watchedCount++
                    onWatched()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_item_layout, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videoList[position], position)
    }

    override fun getItemCount(): Int = videoList.size


}
