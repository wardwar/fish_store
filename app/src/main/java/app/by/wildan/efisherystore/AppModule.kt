package app.by.wildan.efisherystore

import androidx.room.Room
import app.by.wildan.efisherystore.data.getProductDao
import app.by.wildan.efisherystore.data.getService
import app.by.wildan.efisherystore.data.local.ProductDatabase
import app.by.wildan.efisherystore.data.repository.product.ProductLocalDataSource
import app.by.wildan.efisherystore.data.repository.product.ProductRemoteDataSource
import app.by.wildan.efisherystore.data.repository.product.ProductRepository
import app.by.wildan.efisherystore.data.repository.product.ProductRepositoryImp
import app.by.wildan.efisherystore.pages.ProductViewModel
import app.by.wildan.mobile.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { getService() as ApiClient }
    single { Room.databaseBuilder(
        androidContext(),
        ProductDatabase::class.java,
        "product_database"
    ).build() }
    single { get<ProductDatabase>().productDao() }
    single { ProductRemoteDataSource(get()) }
    single { ProductLocalDataSource(get()) }
    single{ProductRepositoryImp(get(),get()) as ProductRepository}
    viewModel { ProductViewModel(get()) }
}