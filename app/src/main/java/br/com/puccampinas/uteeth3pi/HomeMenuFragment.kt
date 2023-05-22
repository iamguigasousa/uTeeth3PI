package br.com.puccampinas.uteeth3pi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.puccampinas.uteeth3pi.databinding.FragmentHomeMenuBinding



class HomeMenuFragment : Fragment() {
    private var _binding: FragmentHomeMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAccount.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_accountDetailsFragment)
        }

        binding.btnEmergencias.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_HomeFragment)
        }

        binding.btnDeslogar.setOnClickListener {
            findNavController().navigate(R.id.action_homeMenuFragment_to_LoginFragment)
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

