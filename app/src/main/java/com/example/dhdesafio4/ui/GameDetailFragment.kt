package com.example.dhdesafio4.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dhdesafio4.R
import com.example.dhdesafio4.data.GameItem
import com.example.dhdesafio4.databinding.GameItemDetailsBinding
import com.example.dhdesafio4.viewmodel.game.GameViewModelFactory
import com.example.dhdesafio4.viewmodel.game.details.GameDetailsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

class GameDetailFragment : Fragment() {
    private var _binding: GameItemDetailsBinding? = null

    private val binding get() = _binding!!

    private val args: GameDetailFragmentArgs  by navArgs()

    private val viewModel: GameDetailsViewModel by viewModels {
        GameViewModelFactory(
            FirebaseStorage.getInstance(),
            FirebaseAuth.getInstance(),
            FirebaseDatabase.getInstance()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = GameItemDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.followChanges(args.gameItem)

        viewModel.gameItemChanges.observe(viewLifecycleOwner) { gameItem ->
            setViewData(gameItem)
        }

        binding.run {
            val gameItem = args.gameItem
            setViewData(gameItem)
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            fbEdit.setOnClickListener {
                val action =
                    GameDetailFragmentDirections.actionGameDetailFragmentToGameAddFragment(
                        gameItem,
                        true
                    )
                findNavController().navigate(action)
            }
        }
    }

    private fun setViewData(gameItem: GameItem) {
        binding.run {
            tvContent.text = gameItem.description
            tvCreated.text = getString(R.string.created_at, gameItem.createdAt)
            tvTitle.text = gameItem.name
            toolbarTitle.text = gameItem.name
            Picasso.get().load(gameItem.imageURI).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    toolbar.background = BitmapDrawable(resources, bitmap)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            })
        }
    }

}