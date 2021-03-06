package app.by.wildan.efisherystore.pages

import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.by.wildan.efisherystore.EventObserver
import app.by.wildan.efisherystore.R
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product
import app.by.wildan.efisherystore.decorator.DecoratorRecyclerViewHorizontal
import app.by.wildan.efisherystore.decorator.GridDecorator
import app.by.wildan.efisherystore.dialog.LoadingDialog
import app.by.wildan.efisherystore.utils.hideKeyboardFrom
import app.by.wildan.efisherystore.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_layout.*
import kotlinx.android.synthetic.main.filter_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val productViewModel: ProductViewModel by viewModel()
    private val productAdapter by lazy {
        ProductAdapter()
    }

    private val selectionAdapter by lazy {
        SelectedAdapter()
    }

    private var filterBehavior: BottomSheetBehavior<FrameLayout>? = null
    private var addBehavior: BottomSheetBehavior<FrameLayout>? = null

    private var isOnSearch = false

    private val productDataHolder = mutableListOf<Product>()
    private val sizeDataHolder = mutableListOf<OptionSize>()
    private val areaDataHolder = mutableListOf<OptionArea>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupList()

        setupAdd()

        val loading = LoadingDialog(this)
        productViewModel.loadingState.observe(this,EventObserver{
            loading.perform(it)
        })
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

        productViewModel.getProduct {
            it.observe(this, Observer {
                productDataHolder.apply {
                    clear()
                    addAll(it)
                }
                setupFilter()
                setupSearch()
                selectionAdapter.notifyDataSetChanged(it.take(5))
                productAdapter.notifyDataSetChanged(it)
                mainShimmerProduct.visibility = View.GONE
                mainShimmerSelection.visibility = View.GONE

                mainListProduct.visibility = View.VISIBLE
                mainListSelection.visibility = View.VISIBLE
            })
        }

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
            } else {
                if (!mainBtnSearch.isGone)
                    mainBtnSearch.animate()
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction {
                            mainBtnSearch.visibility = View.GONE
                        }
            }
            val search = productDataHolder
            val suggest = search.distinctBy { it.komoditas }.filter {
                it.komoditas.toLowerCase().contains(query.toString().toLowerCase())
            }
                .map {
                    var value =
                        it.komoditas.replace(query.toString(), "<b>${query.toString()}</b>")
                    if (!value.contains("<b>")) {
                        value = it.komoditas.replace(
                            query.toString().capitalize(),
                            "<b>${query.toString().capitalize()}</b>"
                        )
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


    fun setupFilter(){
        filterBehavior = BottomSheetBehavior.from(bottomSheetFilter)

        mainBtnFilter.setOnClickListener {
            filterBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        val area = productDataHolder.distinctBy { it.area_provinsi }
        Timber.i(area.toString())
        area.forEach {
            val chip = layoutInflater.inflate(R.layout.chip_filter,slideUpChipGroupArea,false) as Chip
            chip.text = it.area_provinsi.capitalize()
            chip.tag = it.area_provinsi
            slideUpChipGroupArea.addView(chip)
        }

        val comudity = productDataHolder.distinctBy { it.komoditas }
        comudity.forEach {
            val chip = layoutInflater.inflate(
                R.layout.chip_filter,
                slideUpChipGroupComidity,
                false
            ) as Chip
            chip.text = it.komoditas.capitalize()
            chip.tag = it.komoditas
            slideUpChipGroupComidity.addView(chip)
        }

        slideUpBtnFilter.setOnClickListener {
            filterProduct()
            filterBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        }

    }

    fun setupAdd() {
        addBehavior = BottomSheetBehavior.from(bottomSheetAdd)
        mainBtnAdd.setOnClickListener {
            addBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        productViewModel.getArea {
            it.observe(this, Observer {
                areaDataHolder.apply {
                    clear()
                    addAll(it)
                }

                val provinceAdapter = ArrayAdapter(
                    this,
                    R.layout.list_item,
                    areaDataHolder.distinctBy { it.province }.map { it.province })
                (slideUpAddInputProvince.editText as? AutoCompleteTextView)?.setAdapter(
                    provinceAdapter
                )

                slideUpAddInputProvince.editText?.addTextChangedListener {
                    val city =
                        areaDataHolder.filter { it.province == slideUpAddInputProvince.editText?.text.toString() }
                            .map { it.city?:"" }
                        println(city)
                    val cityAdapter = ArrayAdapter(this, R.layout.list_item, city)
                    (slideUpAddInputCity.editText as? AutoCompleteTextView)?.setAdapter(cityAdapter)

                }
            })

        }
        productViewModel.getSize {
            it.observe(this, Observer {
                sizeDataHolder.apply {
                    clear()
                    addAll(it)
                }
                val sizeAdapter =
                    ArrayAdapter(this, R.layout.list_item, sizeDataHolder.map { it.size })
                (slideUpAddInputSize.editText as? AutoCompleteTextView)?.setAdapter(sizeAdapter)
            })

        }

        slideUpBtnAdd.setOnClickListener {
            productViewModel.postProduct(
                app.by.wildan.mobile.Product(
                    Random(15).toString(),
                    selideUpAddInputComudity.editText?.text.toString(),
                    slideUpAddInputProvince.editText?.text.toString(),
                    slideUpAddInputCity.editText?.text.toString(),
                    slideUpAddInputSize.editText?.text.toString(),
                    slideUpAddInputPrice.editText?.text.toString(),
                    Calendar.getInstance().time.toString(),
                    Calendar.getInstance().timeInMillis.toString()
                    )
            )
        }

        productViewModel.eventPostProduct.observe(this,EventObserver{
            toast(it)

            if(it == "success"){
                addBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                hideKeyboardFrom(this,bottomSheetAdd)
                productViewModel.updateData()
            }
        })

    }


    fun filterProduct() {
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
        productAdapter.notifyDataSetChanged(productDataHolder.filter {
            if (filterArea.toList().isNotEmpty() && filterComudity.toList().isNotEmpty()) {
                filterArea.contains(it.area_provinsi) && filterComudity.contains(it.komoditas)
            } else if (filterArea.toList().isNotEmpty() || filterComudity.toList().isEmpty())
                filterArea.contains(it.area_provinsi)
            else
                filterComudity.contains(it.komoditas)
        })
    }

    fun search() {
        hideKeyboardFrom(this, mainInputSearch)
        if (mainInputSearch.isFocused) {
            mainInputSearch.clearFocus()
        }
        val filter = productDataHolder
        val filtered = filter.filter {
            it.komoditas.toLowerCase().contains(mainInputSearch.text.toString().toLowerCase())
        }
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