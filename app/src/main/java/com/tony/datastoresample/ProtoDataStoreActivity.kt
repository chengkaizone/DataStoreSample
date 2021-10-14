package com.tony.datastoresample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

/**
 *
 */
class ProtoDataStoreActivity: AppCompatActivity() {


    lateinit var foodTaste: ChipGroup
    lateinit var foodType: ChipGroup

    lateinit var veg: Chip
    lateinit var nonVeg: Chip
    lateinit var sweet: Chip
    lateinit var spicy: Chip

    lateinit var recyclerView: RecyclerView
    lateinit var foodPreferenceManager: FoodPreferenceManager

    private val foodListAdapter by lazy { FoodListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proto_datastore)

        foodPreferenceManager = FoodPreferenceManager(applicationContext)
        recyclerView = findViewById(R.id.recyclerView)

        veg = findViewById(R.id.veg)
        nonVeg = findViewById(R.id.nonVeg)
        sweet = findViewById(R.id.sweet)
        spicy = findViewById(R.id.spicy)


        observePreferences()
        initFoodList()
        initViews()

        loadData()
    }

    private fun initFoodList() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProtoDataStoreActivity)
            adapter = foodListAdapter
        }

    }

    private fun loadData() {

        foodListAdapter.submitList(getFoodList())
    }

    private fun getFoodList() = listOf(
        Food("Paneer Masala", FoodType.VEG, FoodTaste.SPICY),
        Food("Gulab Jamoon", FoodType.VEG, FoodTaste.SWEET),
        Food("Chicken Lollipop", FoodType.NON_VEG, FoodTaste.SPICY),
        Food("Fish Fry", FoodType.NON_VEG, FoodTaste.SPICY),
        Food("Veg Mix", FoodType.VEG, FoodTaste.SPICY),
        Food("Aloo Sabji", FoodType.VEG, FoodTaste.SWEET),
        Food("Papad", FoodType.VEG, FoodTaste.SPICY),
        Food("Rasgulla", FoodType.VEG, FoodTaste.SWEET),
        Food("Shev Bhaji", FoodType.VEG, FoodTaste.SPICY),
        Food("Puran Poli", FoodType.VEG, FoodTaste.SWEET),
        Food("Pani Puri", FoodType.VEG, FoodTaste.SPICY),
        Food("Veg Manchurian", FoodType.VEG, FoodTaste.SPICY),
        Food("Chicken Manchurian", FoodType.NON_VEG, FoodTaste.SPICY),
        Food("Chhole", FoodType.VEG, FoodTaste.SPICY),
        Food("Biryani", FoodType.NON_VEG, FoodTaste.SPICY)
    )

    private fun observePreferences() {

        foodPreferenceManager.userFoodPreference.asLiveData().observe(this) {
            filterFoodList(it.type, it.taste)
        }
    }

    private fun filterFoodList(type: FoodType?, taste: FoodTaste?) {
        var filteredList = getFoodList()
        type?.let { foodType ->
            filteredList = filteredList.filter { it.type == foodType }
        }
        taste?.let { foodTaste ->
            filteredList = filteredList.filter { it.taste == foodTaste }
        }

        foodListAdapter.submitList(filteredList)

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No results!", Toast.LENGTH_SHORT).show()
        }

        updateViews(type, taste)
    }

    private fun initViews() {

        foodTaste.setOnCheckedChangeListener { group, checkedId ->
            val taste = when (checkedId) {
                R.id.sweet -> FoodTaste.SWEET
                R.id.spicy -> FoodTaste.SPICY
                else -> null
            }

            lifecycleScope.launch {
                foodPreferenceManager.updateUserFoodTastePreference(taste)
            }
        }

        foodType.setOnCheckedChangeListener { group, checkedId ->
            val type = when (checkedId) {
                R.id.veg -> FoodType.VEG
                R.id.nonVeg -> FoodType.NON_VEG
                else -> null
            }

            lifecycleScope.launch {
                foodPreferenceManager.updateUserFoodTypePreference(type)
            }
        }
    }


    private fun updateViews(type: FoodType?, taste: FoodTaste?) {

        when (type) {
            FoodType.VEG -> veg.isChecked = true
            FoodType.NON_VEG -> nonVeg.isChecked = true
        }

        when (taste) {
            FoodTaste.SWEET -> sweet.isChecked = true
            FoodTaste.SPICY -> spicy.isChecked = true
        }
    }

}

class FoodListAdapter : ListAdapter<Food, FoodListAdapter.FoodItemViewHolder>(DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false))

    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(food: Food) {

            itemView.findViewById<TextView>(R.id.textFoodName).text = food.name
            itemView.findViewById<TextView>(R.id.textFoodType).run {
                text = food.type.name
                setTextColor(ContextCompat.getColor(itemView.context, when (food.type) {
                    FoodType.NON_VEG -> android.R.color.holo_red_dark
                    FoodType.VEG -> android.R.color.holo_green_dark
                }))
            }

            itemView.findViewById<TextView>(R.id.textFoodTaste).run {
                text = food.taste.name
                setTextColor(
                    ContextCompat.getColor(itemView.context, when (food.taste) {
                        FoodTaste.SWEET -> android.R.color.holo_blue_light
                        FoodTaste.SPICY -> android.R.color.holo_orange_dark
                    })
                )
            }
        }
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Food>() {

            override fun areItemsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Food, newItem: Food): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }

}