 package com.example.dhdesafio4.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dhdesafio4.R
import com.example.dhdesafio4.data.GameItem
import com.example.dhdesafio4.data.adapter.GamesAdapter
import com.example.dhdesafio4.databinding.FragmentMainBinding
import com.example.dhdesafio4.utils.behavior.snack.OkAndErrorSnack
import com.example.dhdesafio4.utils.behavior.snack.SnackCreator
import com.example.dhdesafio4.viewmodel.game.GameViewModelFactory
import com.example.dhdesafio4.viewmodel.game.list.GameViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

 class GamesFragment : Fragment(), GamesAdapter.ViewHolderClick {
    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<GameViewModel> {
        GameViewModelFactory(
            FirebaseStorage.getInstance(),
            FirebaseAuth.getInstance(),
            FirebaseDatabase.getInstance()
        )
    }

    private val snackCreator: SnackCreator by lazy {
        OkAndErrorSnack(
            requireView(),
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getGameList()

        viewModel.gameListStatus.observe(viewLifecycleOwner) {
            snackCreator.showSnackS(it)
        }

        viewModel.gameList.observe(viewLifecycleOwner) {
            binding.pbGames.visibility = View.GONE
            binding.tvNoGame.visibility = View.GONE
            binding.rvGames.visibility = View.VISIBLE

            val adapter = binding.rvGames.adapter as GamesAdapter
            adapter.gameItems.clear()
            adapter.gameItems.addAll(it)
            adapter.notifyDataSetChanged()

            if (adapter.gameItems.isEmpty()) {
                binding.rvGames.visibility = View.GONE
                binding.tvNoGame.visibility = View.VISIBLE
            }
        }

        binding.run {
            val layoutManager = GridLayoutManager(requireContext(), 2)
            // Mostra duas views em uma única linha horizontal
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = 1
            }
            rvGames.layoutManager = layoutManager

            rvGames.adapter = GamesAdapter(
                arrayListOf(), this@GamesFragment
            )

            fbtnAddGames.setOnClickListener {
                val nav = findNavController()
                nav.navigate(R.id.gameAddFragment)
            }
        }
    }

     override fun onClick(gameItem: GameItem) {
         val action = GamesFragmentDirections.actionGamesFragmentToGameDetailFragment(gameItem)
         findNavController().navigate(action)
     }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}