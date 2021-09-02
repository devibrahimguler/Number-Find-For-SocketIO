package com.muntazif.sayibulma.view.fr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.muntazif.sayibulma.R
import com.muntazif.sayibulma.databinding.FragmentHakkimizdaBinding

class HakkimizdaFragment : Fragment() {

    lateinit var binding : FragmentHakkimizdaBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hakkimizda, container, false)

        binding.geriHakkimizdaFr.setOnClickListener {
            val action = HakkimizdaFragmentDirections.actionHakkimizdaFragmentToGorunum()
            Navigation.findNavController(it).navigate(action)
        }

        return binding.root
    }

}