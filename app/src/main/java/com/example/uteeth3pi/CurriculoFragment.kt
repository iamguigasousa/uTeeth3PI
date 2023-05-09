package com.example.uteeth3pi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.uteeth3pi.databinding.FragmentCurriculoBinding

class CurriculoFragment : Fragment() {

    private var _binding: FragmentCurriculoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCurriculoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgbArrow.setOnClickListener {
            findNavController().navigate(R.id.action_CurriculoFragment_to_CriarContaFragment)

        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_CurriculoFragment_to_LoginFragment)
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}