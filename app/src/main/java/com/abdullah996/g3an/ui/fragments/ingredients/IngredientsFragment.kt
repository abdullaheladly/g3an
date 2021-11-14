package com.abdullah996.g3an.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdullah996.g3an.R
import com.abdullah996.g3an.adapters.IngredientsAdapter
import com.abdullah996.g3an.model.Result
import com.abdullah996.g3an.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    private val ingredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_ingredients, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setupRecycleView(view)
        myBundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }

        return view
    }

    private fun setupRecycleView(view: View) {
        view.ingredients_recyclerview.adapter=ingredientsAdapter
        view.ingredients_recyclerview.layoutManager=LinearLayoutManager(requireContext())
    }


}