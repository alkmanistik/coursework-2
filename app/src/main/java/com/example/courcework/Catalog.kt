package com.example.courcework

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.courcework.databinding.FragmentBasketBinding
import com.example.courcework.databinding.FragmentCatalogBinding
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Catalog.newInstance] factory method to
 * create an instance of this fragment.
 */
class Catalog : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var nowSort: String = sort[0]
    private var nowSortingBy: String = sortingBy[0]
    private var nowCategory: String = ""
    private lateinit var db: ShopDataBaseHelper
    private lateinit var savePreferences: SharedPreferences
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!
    private lateinit var productData: ArrayList<Product>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity
        val context = requireContext()
        savePreferences =
            activity?.getSharedPreferences("savePreferences", Context.MODE_PRIVATE)!!
        db = ShopDataBaseHelper(context, null)

        binding.buttonSearch.setOnClickListener {
            binding.textPage.visibility = View.GONE
            binding.buttonSearch.visibility = View.GONE
            binding.buttonBack.visibility = View.VISIBLE
            binding.searchBar.visibility = View.VISIBLE
        }

        binding.buttonBack.setOnClickListener{
            binding.textPage.visibility = View.VISIBLE
            binding.buttonSearch.visibility = View.VISIBLE
            binding.buttonBack.visibility = View.GONE
            binding.searchBar.visibility = View.GONE
        }

        binding.listProduct.layoutManager = LinearLayoutManager(activity)
        reload()


        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        val filterButton = binding.buttonFilter
        val sortButton = binding.buttonSort

        val popupCategory = PopupMenu(activity, filterButton)
        popupCategory.inflate(R.menu.popup_filter_menu)
        popupCategory.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sofa -> {
                    nowCategory = "Диваны"
                    reload()
                    true
                }

                R.id.chair -> {
                    nowCategory = "Стулья"
                    reload()
                    true
                }

                R.id.table -> {
                    nowCategory = "Столы"
                    reload()
                    true
                }

                R.id.accessories -> {
                    nowCategory = "Аксессуары"
                    reload()
                    true
                }

                R.id.none -> {
                    nowCategory = ""
                    reload()
                    true
                }

                else -> false
            }
        }

        val popupSort = PopupMenu(activity, sortButton)
        popupSort.inflate(R.menu.popup_sort_menu)
        popupSort.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sortNameASC -> {
                    nowSort = sort[1]
                    nowSortingBy = sortingBy[0]
                    reload()
                    true
                }

                R.id.sortNameDESC -> {
                    nowSort = sort[1]
                    nowSortingBy = sortingBy[1]
                    reload()
                    true
                }

                R.id.sortPriceASC -> {
                    nowSort = sort[2]
                    nowSortingBy = sortingBy[0]
                    reload()
                    true
                }

                R.id.sortPriceDESC -> {
                    nowSort = sort[2]
                    nowSortingBy = sortingBy[1]
                    reload()
                    true
                }

                R.id.none -> {
                    nowSort = sort[0]
                    nowSortingBy = sortingBy[0]
                    reload()
                    true
                }

                else -> false
            }
        }

        filterButton.setOnClickListener {
            popupCategory.show()
        }

        sortButton.setOnClickListener {
            popupSort.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun reload(){
        productData = db.getProduct(nowCategory, nowSort, nowSortingBy)
        adapter = ProductAdapter(productData, requireContext(), savePreferences, requireActivity(), db)
        binding.listProduct.adapter = adapter
        binding.searchView.setQuery(binding.searchView.query, true)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Catalog.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Catalog().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = java.util.ArrayList<Product>()
            for (i in productData) {
                if (i.nameFull.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                adapter.setFilteredList(filteredList)
                binding.emptyList.visibility = View.VISIBLE
            } else {
                adapter.setFilteredList(filteredList)
                binding.emptyList.visibility = View.GONE
            }
        }
    }
}