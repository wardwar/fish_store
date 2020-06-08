package app.by.wildan.efisherystore.pages

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.by.wildan.efisherystore.R
import app.by.wildan.efisherystore.decorator.DecoratorRecyclerViewHorizontal
import app.by.wildan.efisherystore.decorator.GridDecorator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val productAdapter by lazy {
        ProductAdapter()
    }

    private val selectionAdapter by lazy {
        SelectedAdapter()
    }

    private var isOnSearch = false

    val data = listOf(
        Product("Udang", "50000", "100"),
        Product("Lele", "45000", "100"),
        Product("Gurame", "60000", "100"),
        Product("Nila", "80000", "100"),
        Product("Mas", "90000", "100")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupList()

        setupSearch()

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
            val search = data
            val suggest = search.filter { it.komuditi?.toLowerCase()?.contains(query.toString().toLowerCase()) ?: false }
                .map {
                    it.komuditi?.replace(query.toString(), "<b>${query.toString()}</b>",true)?.capitalize()?:""
                }
            suggestAdapter.notifyDataSetChanged(suggest)

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

    override fun onBackPressed() {
        if (isOnSearch) {
            mainInputSearch.clearFocus()
        } else {
            super.onBackPressed()
        }
    }
}