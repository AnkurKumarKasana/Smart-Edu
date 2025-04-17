package com.example.instagram

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
class VideoAdapter(
    private val videoUrls: List<String>,
    private val descriptions: List<String>,
    private val initialWatchedCount: Int,
    private val onVideoWatched: () -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val watchedSet = mutableSetOf<String>()

    init {
        // Assume first N videos were already watched
        for (i in 0 until initialWatchedCount.coerceAtMost(videoUrls.size)) {
            watchedSet.add(videoUrls[i])
        }
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val videoView: VideoView = view.findViewById(R.id.videoView)
        val descriptionText: TextView = view.findViewById(R.id.videoDescription)
        val videoOrder: TextView = view.findViewById(R.id.videoOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val videoUrl = videoUrls[position]
        val description = descriptions[position]

        holder.videoOrder.text = "Video ${position + 1}"
        holder.descriptionText.text = description
        val uri = Uri.parse(videoUrl)

        holder.videoView.setVideoURI(uri)
        val mediaController = MediaController(holder.itemView.context)
        mediaController.setAnchorView(holder.videoView)
        holder.videoView.setMediaController(mediaController)

        holder.videoView.setOnCompletionListener {
            if (watchedSet.add(videoUrl)) {
                onVideoWatched()
            }
        }
    }

    override fun getItemCount(): Int = videoUrls.size
}
