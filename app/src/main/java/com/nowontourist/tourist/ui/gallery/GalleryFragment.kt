package com.nowontourist.tourist.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.ListenerRegistration
import com.nowontourist.tourist.databinding.FragmentGalleryBinding
import com.nowontourist.tourist.util.firebaseAuth
import com.nowontourist.tourist.util.firebaseDatabase

class GalleryFragment : Fragment() {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val galleryAdapter by lazy { GalleryAdapter() }
    private var snapshotListener: ListenerRegistration? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initFirebaseListener()
        binding.btnAdd.setOnClickListener {
            if(firebaseAuth.currentUser != null) {
                startActivity(Intent(context, EditGalleryActivity::class.java))
            } else {
                Snackbar.make(binding.root, "로그인하여 주세요!", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView(){
        binding.rvGallery.adapter = galleryAdapter.apply {
            list = listOf()
            setOnItemClickListener { position ->
                startActivity(Intent(context, GalleryDetailActivity::class.java).apply {
                    putExtra(GalleryDetailActivity.KEY_DATA, galleryAdapter.list[position])
                })
            }
        }
    }

    private fun initFirebaseListener() {
        snapshotListener = firebaseDatabase.collection("gallery")
            .addSnapshotListener { snapshot, error ->
                if(error != null) {
                    return@addSnapshotListener
                }

                if(snapshot != null && !snapshot.isEmpty) {
                    val list = snapshot.documents.map {
                        it.toObject(GalleryItem::class.java)!!
                    }
                    galleryAdapter.list = list
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snapshotListener?.remove()
        _binding = null
    }
}