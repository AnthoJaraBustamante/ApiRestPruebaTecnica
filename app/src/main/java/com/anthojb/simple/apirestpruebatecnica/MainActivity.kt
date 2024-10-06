package com.anthojb.simple.apirestpruebatecnica

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.anthojb.simple.apirestpruebatecnica.model.Category
import com.anthojb.simple.apirestpruebatecnica.model.Product
import com.anthojb.simple.apirestpruebatecnica.ui.theme.ApiRestPruebaTecnicaTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApiRestPruebaTecnicaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CustomAppbar()
                    },
                ) { innerPadding ->
                    ProductListScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppbar() {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("Platzi Fake Store API")
        }

    )

}

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val products by viewModel.products.observeAsState(emptyList())

    viewModel.getProducts()

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(products) { product ->
            ProductItem(product = product)
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    product.images.forEach { imageUrl ->
        Log.d("ProductViewModel", "URL de imagen: $imageUrl")
    }
    val imageUrl = product.images.firstOrNull()?.removeSurrounding("[", "]")?.replace("\"", "")
    val typography = MaterialTheme.typography
    Column {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = product.title, style = typography.titleMedium)
                Text(text = "$${product.price}", style = typography.bodyMedium)
            }
            CustomSpacer()
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                ),
            ) {
                AsyncImage(
                    modifier = Modifier.size(80.dp),
                    model = imageUrl,
                    contentDescription = "Imagen de ${product.title}",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.empty),
                    error = painterResource(R.drawable.empty),
                )
            }

            CustomSpacer()

        }
        CustomSpacer()
        HorizontalDivider()
        CustomSpacer()
    }

}

@Composable
fun CustomSpacer(size: Int = 8) = Spacer(modifier = Modifier.size(size.dp))

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    ApiRestPruebaTecnicaTheme {
        ProductItem(
            product = Product(
                id = 1,
                title = "Producto",
                price = 9.99,
                description = "Description",
                images = listOf(),
                category = Category(id = 1, name = "Categoria", image = "")
            )
        )
    }
}
