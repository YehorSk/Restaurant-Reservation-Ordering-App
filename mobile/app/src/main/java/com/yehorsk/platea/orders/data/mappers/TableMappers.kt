package com.yehorsk.platea.orders.data.mappers

import com.yehorsk.platea.orders.data.remote.dto.TableDto
import com.yehorsk.platea.orders.domain.models.Table

fun TableDto.toTable(): Table{
    return Table(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        number = number,
        capacity = capacity
    )
}