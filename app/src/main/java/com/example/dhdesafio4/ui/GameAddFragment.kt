package com.example.dhdesafio4.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dhdesafio4.data.GameItem
import com.example.dhdesafio4.databinding.FragmentAddGameItemBinding
import com.example.dhdesafio4.utils.behavior.snack.OkAndErrorSnack
import com.example.dhdesafio4.utils.behavior.snack.SnackCreator
import com.example.dhdesafio4.utils.behavior.setMargins
import com.example.dhdesafio4.viewmodel.game.add.GameAddViewModel
import com.example.dhdesafio4.viewmodel.game.GameViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class GameAddFragment : Fragment() {
    private val imageCode = 0x01

    private var _binding: FragmentAddGameItemBinding? = null

    private val binding get() = _binding!!

    private val args: GameAddFragmentArgs by navArgs()

    private val snackCreator: SnackCreator by lazy {
        OkAndErrorSnack(
            requireView(),
            requireContext()
        )
    }

    private val viewModel by viewModels<GameAddViewModel> {
        GameViewModelFactory(
            FirebaseStorage.getInstance(),
            FirebaseAuth.getInstance(),
            FirebaseDatabase.getInstance()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameItemBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.goToAndFinish.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), it)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.gameCreateStatus.observe(viewLifecycleOwner) {
            binding.loading.llLoading.visibility = View.GONE

            if (!it.first) {
                snackCreator.showSnackS(it)
            } else {
                val nav = findNavController()
                nav.popBackStack()
            }
        }

        binding.run {
            // Card view responsavel por deixar a imagem circular. Pegamos o click dela já que preva
            // lece aquele da imagem
            iconCircle.setOnClickListener {
                selectImage()
            }

            btnSaveGame.setOnClickListener {
                loading.llLoading.visibility = View.VISIBLE

                if (args.updateItem) {
                    viewModel.updateGameItem(GameItem(
                        tiName.editText!!.text.toString(),
                        tiCreatedAt.editText!!.text.toString(),
                        tiDescription.editText!!.text.toString(),
                        viewModel.imageUri,
                        args.gameItem!!.uid
                    ))

                    return@setOnClickListener
                }
                viewModel.saveGameItem(GameItem(
                    tiName.editText!!.text.toString(),
                    tiCreatedAt.editText!!.text.toString(),
                    tiDescription.editText!!.text.toString(),
                    viewModel.imageUri
                ))
            }

            // Caso tenhamos que atualizar algum item colocamos os dados deles aqui
            if (args.updateItem && args.gameItem != null) {
                tiName.editText!!.text = Editable.Factory().newEditable(args.gameItem!!.name)
                tiCreatedAt.editText!!.text = Editable.Factory().newEditable(
                    args.gameItem!!.createdAt
                )
                tiDescription.editText!!.text = Editable.Factory().newEditable(
                    args.gameItem!!.description
                )
                Picasso.get().load(args.gameItem!!.imageURI).into(iconSelector)
                setMargins(iconSelector, 0, 0, 0, 0)
                iconSelector.scaleType = ImageView.ScaleType.CENTER_CROP
                viewModel.imageUri = args.gameItem!!.imageURI
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (Activity.RESULT_CANCELED != resultCode && data != null) {
            when(requestCode) {
                imageCode -> {
                    // Carrega a imagem passada da URI (Não precisa ser da internet)
                    Picasso.get().load(data.data).into(binding.iconSelector)
                    // Remove as margens que faziam a imagem estar no meio do circulo
                    setMargins(binding.iconSelector, 0, 0, 0, 0)
                    // Escala a imagem para preencher o circulo
                    binding.iconSelector.scaleType = ImageView.ScaleType.CENTER_CROP

                    // Passa os dados para o viewmodel sobre o caminho da imagem
                    viewModel.imageUri = data.data.toString()
                }
            }

            return
        }


        snackCreator.showSnackS(
            Pair(false, "Não foi possível escolher ou pegar a imagem")
        )
    }

    /**
     * PT-BR
     * Chama o sistema para possibilitar a escolha de uma imagem do dispositivo
     *
     * EN-US
     * Calls the system to able the selection of an image from the device.
     *
     */
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, imageCode)
    }
}