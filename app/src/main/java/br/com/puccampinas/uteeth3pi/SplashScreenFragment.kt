package br.com.puccampinas.uteeth3pi

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import android.view.Window;

class SplashScreenFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)


        Handler(Looper.myLooper()!!).postDelayed({

            findNavController().navigate(R.id.action_splashScreenFragment3_to_LoginFragment)

        }, 3000)

        return view
    }





}