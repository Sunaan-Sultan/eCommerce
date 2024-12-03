package com.project.ecommerce.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.repository.R
import com.project.api.product.Product
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
@Composable
fun ProductList(
    products: List<Product>,
    onLoadMore: () -> Unit,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
//        modifier = modifier.padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 0.dp ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products.size) { index ->
            val product = products[index]
            if (index == products.size - 1) {
                onLoadMore() // Trigger load more when reaching the end
            }
            ProductItem(
                product = product,
                onClick = onProductClick,
                onAddToCart = onAddToCart
            )
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(
        product.imageUrl,
        "drawable",
        context.packageName
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) Color.Black else Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable { onClick(product) }
    ) {
        Row(
            modifier = Modifier
//                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                if (resourceId != 0) {
                    Image(
                        painter = painterResource(resourceId),
                        contentDescription = product.productName,
                        modifier = Modifier.size(64.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.football),
                        contentDescription = "Default Image",
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = product.productName, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "${product.productPrice}$", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Column {
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = {
                    onAddToCart(product)
                }) {
                    Icon(
                        imageVector = Icons.Filled.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}





