package app.by.wildan.efisherystore.pages

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.by.wildan.efisherystore.R
import app.by.wildan.efisherystore.decorator.DecoratorRecyclerViewHorizontal
import app.by.wildan.efisherystore.decorator.GridDecorator
import app.by.wildan.efisherystore.utils.hideKeyboardFrom
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.filter_layout.*

class MainActivity : AppCompatActivity() {
    private val productAdapter by lazy {
        ProductAdapter()
    }

    private val selectionAdapter by lazy {
        SelectedAdapter()
    }

    private var filterBehavior: BottomSheetBehavior<FrameLayout>? = null

    private var isOnSearch = false

    val data = listOf(
        Product("Udang", "50000", "100","Jakarta Utara"),
        Product("Lele", "45000", "100","Jakarta Utara"),
        Product("Gurame", "60000", "100","Bandung"),
        Product("Nila", "80000", "100","Jawa Tengah"),
        Product("Mas", "90000", "100","Jawa Barat")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupList()

        setupSearch()
        setupFilter()

    }

    fun setupList(){
        mainListSelection.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = selectionAdapter
            val margin16 = resources.getDimension(R.dimen.dp16).toInt()
            val margin4 = resources.getDimension(R.dimen.dp4).toInt()
            val marginDecorator = DecoratorRecyclerViewHorizontal(
                margin16,
                margin16,
                margin16, margin16, margin4, margin4
            )
            addItemDecoration(marginDecorator)
        }

        mainListProduct.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = productAdapter
            val margin16 = resources.getDimension(R.dimen.dp16).toInt()
            val marginDecorator = GridDecorator(
                margin16, 2
            )
            addItemDecoration(marginDecorator)
        }

        generateDummy()
    }

    fun setupSearch() {
        mainInputSearch.setOnFocusChangeListener { view, isFocus ->
            if (isFocus) {
                isOnSearch = true
                mainScroll.setOnTouchListener { _, _ ->
                    true
                }
                mainListSearchSuggestion.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .withStartAction { mainListSearchSuggestion.visibility = View.VISIBLE }
            } else {
                mainListSearchSuggestion.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        mainListSearchSuggestion.visibility = View.GONE
                        isOnSearch = false
                        mainScroll.setOnTouchListener { _, _ ->
                            false
                        }
                    }
            }
        }

        val suggestAdapter = SearchAdapter()

        mainListSearchSuggestion.apply {
            adapter = suggestAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        mainInputSearch.addTextChangedListener { query ->
            if(query?.isNotEmpty() == true){
                if(!mainBtnSearch.isVisible)
                mainBtnSearch.animate()
                    .alpha(1f)
                    .setDuration(500)
                    .withStartAction { mainBtnSearch.visibility = View.VISIBLE }
            }else{
                if(!mainBtnSearch.isGone)
                mainBtnSearch.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        mainBtnSearch.visibility = View.GONE
                    }
            }
            val search = data
            val suggest = search.filter { it.komuditi?.toLowerCase()?.contains(query.toString().toLowerCase()) ?: false }
                .map {
                    var value =  it.komuditi?.replace(query.toString(), "<b>${query.toString()}</b>") ?:""
                   if (!value.contains("<b>")){
                       value = it.komuditi?.replace(query.toString().capitalize(), "<b>${query.toString().capitalize()}</b>") ?:""
                   }
                    value
                }
            suggestAdapter.notifyDataSetChanged(suggest)
        }

        suggestAdapter.addOnItemClickListener {
            mainInputSearch.setText(it.replace("<b>","").replace("</b>",""))
            mainInputSearch.clearFocus()
            search()
        }

        mainBtnSearch.setOnClickListener {
           search()
        }
    }

    fun generateDummy() {
        productAdapter.notifyDataSetChanged(
            data
        )

        selectionAdapter.notifyDataSetChanged(
            data
        )
    }

    fun setupFilter(){
        filterBehavior = BottomSheetBehavior.from(bottomSheetFilter)

        mainBtnFilter.setOnClickListener {
            filterBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val area = data.distinctBy { it.area_provinsi }
        area.forEach {
            val chip = layoutInflater.inflate(R.layout.chip_filter,slideUpChipGroupArea,false) as Chip
            chip.text = it.area_provinsi?.capitalize()
            chip.tag = it.area_provinsi
            slideUpChipGroupArea.addView(chip)
        }

        val comudity = data.distinctBy { it.komuditi }
        comudity.forEach {
            val chip = layoutInflater.inflate(R.layout.chip_filter,slideUpChipGroupComidity,false) as Chip
            chip.text = it.komuditi?.capitalize()
            chip.tag = it.komuditi
            slideUpChipGroupComidity.addView(chip)
        }

        slideUpBtnFilter.setOnClickListener {
            filterProduct()
            filterBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }
    
    fun filterProduct(){
        val filterArea = slideUpChipGroupArea.children.filter {
            val chip = it as Chip
            chip.isChecked
        }.map {
            it.tag as String
        }
        val filterComudity = slideUpChipGroupComidity.children.filter {
            val chip = it as Chip
            chip.isChecked
        }.map {
            it.tag as String
        }
        productAdapter.notifyDataSetChanged(data.filter {
            if(filterArea.toList().isNotEmpty() && filterComudity.toList().isNotEmpty()){
                filterArea.contains(it.area_provinsi) && filterComudity.contains(it.komuditi)
            }else if(filterArea.toList().isNotEmpty() || filterComudity.toList().isEmpty())
                filterArea.contains(it.area_provinsi)
            else
                filterComudity.contains(it.komuditi)
        })
    }

    fun search(){
        hideKeyboardFrom(this,mainInputSearch)
        if(mainInputSearch.isFocused){
            mainInputSearch.clearFocus()
        }
        val filter = data
        val filtered = filter.filter { it.komuditi?.toLowerCase()?.contains( mainInputSearch.text.toString().toLowerCase())?:false}
        productAdapter.notifyDataSetChanged(filtered)
    }

    override fun onBackPressed() {
        if (isOnSearch) {
            mainInputSearch.clearFocus()
        } else {
            super.onBackPressed()
        }
    }

}