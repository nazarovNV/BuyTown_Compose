package com.example.buytowncompose.data.repository

import com.example.buytowncompose.data.source.demoProducts
import com.example.buytowncompose.domain.repository.ProductRepository

class DemoProductRepository : ProductRepository {
    override val products = demoProducts
}
