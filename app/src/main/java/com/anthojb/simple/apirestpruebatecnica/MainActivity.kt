package com.anthojb.simple.apirestpruebatecnica

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: ProductViewModel = hiltViewModel(),
) {
    val products by viewModel.products.observeAsState(emptyList())
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    if (selectedProduct != null) {
        ProductDetailScreen(product = selectedProduct!!) {
            selectedProduct = null
        }
    } else {
        ProductsScreen(products = products, onClick = { product ->
            selectedProduct = product
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    products: List<Product>,
    onClick: (Product) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var isAscending by remember { mutableStateOf(true) }

    
    val filteredProducts = products.filter { product ->
        product.title.contains(searchQuery, ignoreCase = true)
    }.sortedBy { if (isAscending) it.price else -it.price }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "Platzi Fake Store API")
                },
                actions = {
                    
                    IconButton(onClick = { isAscending = !isAscending }) {
                        Icon(
                            imageVector = if (isAscending) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (isAscending) "Ordenar ascendente" else "Ordenar descendente"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar producto") },
                modifier = Modifier.fillMaxWidth()
            )

            LazyColumn {
                items(filteredProducts) { product ->
                    ProductItem(product = product, onClick = { onClick(product) })
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    product.images.forEach { imageUrl ->
        Log.d("ProductViewModel", "URL de imagen: $imageUrl")
    }
    val imageUrl = product.images.firstOrNull()?.removeSurrounding("[", "]")?.replace("\"", "")
    val typography = MaterialTheme.typography

    Column {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable(onClick = onClick),
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
                ProductImage(
                    imageUrl = product.images.firstOrNull(),
                    modifier = Modifier.size(80.dp)
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(product: Product, onClick: () -> Unit) {
    val typography = MaterialTheme.typography
    BackHandler {
        onClick()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(product.title)
                },
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },

        ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            ProductImage(
                imageUrl = product.images.firstOrNull(),
                modifier = Modifier
                    .height(350.dp)
                    .fillMaxWidth()
            )

            Text(
                text = product.title,
                modifier = Modifier.padding(16.dp),
                style = typography.headlineLarge
            )
            Text(
                text = "$${product.price}",
                modifier = Modifier.padding(start = 16.dp),
                style = typography.headlineSmall
            )


            Text(text = product.description, modifier = Modifier.padding(16.dp))


            Text(
                text = "Categoría: ${product.category.name}",
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                style = typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProductImage(imageUrl: String?, modifier: Modifier) {
    AsyncImage(
        model = imageUrl?.removeSurrounding("[", "]")?.replace("\"", ""),
        contentDescription = "Imagen del producto",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.empty),
        error = painterResource(R.drawable.empty)
    )
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ApiRestPruebaTecnicaTheme {
        ProductDetailScreen(
            product = Product(
                id = 1,
                title = "Titulo",
                price = 0.00,
                description = "Lorem ipsum dolor sit amet consectetur adipiscing elit, conubia sed tristique nunc ornare elementum, pretium eleifend duis sapien arcu inceptos vulputate, metus dis eros etiam varius. Quam curae vel placerat hac tincidunt velit ad, tellus ullamcorper inceptos dictum quis auctor, hendrerit sapien nibh porttitor urna orci. Sodales dis nam per aliquam volutpat donec non, senectus fringilla libero a primis himenaeos imperdiet, eros vitae curabitur facilisi inceptos laoreet.",
                images = listOf(),
                category = Category(id = 1, name = "Cualquier", image = "")
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListPreview() {
    ApiRestPruebaTecnicaTheme {

        val products = listOf(
            Product(
                id = 1,
                title = "Producto 1",
                price = 9.99,
                description = "",
                images = listOf(""),
                category = Category(id = 1, name = "", image = "")
            ),
            Product(
                id = 2,
                title = "Producto 2",
                price = 19.99,
                description = "",
                images = listOf(""),
                category = Category(id = 1, name = "", image = "")
            ),
            Product(
                id = 3,
                title = "Producto 3",
                price = 29.99,
                description = "",
                images = listOf(""),
                category = Category(id = 1, name = "", image = "")
            )
        )

        ProductsScreen(products = products, onClick = {})
    }
}
