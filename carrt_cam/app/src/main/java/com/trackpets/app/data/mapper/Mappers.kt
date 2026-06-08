package com.trackpets.app.data.mapper

import com.trackpets.app.data.dto.*
import com.trackpets.app.domain.model.*

fun UserDto.toUserDomain(): User = User(
    id = id,
    username = username,
    firstName = first_name,
    lastName = last_name,
    email = email,
    phone = phone,
    role = role
)

fun User.toUserDto(): UserDto = UserDto(
    id = id,
    username = username,
    first_name = firstName,
    last_name = lastName,
    email = email,
    phone = phone,
    role = role,
    password = password
)

fun CategoryDto.toDomain(): Category = Category(
    id = id,
    name = name,
    description = description ?: ""
)

fun Category.toDto(): CategoryDto = CategoryDto(
    id = id,
    name = name,
    description = description
)

fun VehicleDto.toDomain(): Vehicle = Vehicle(
    id = id,
    brand = brand,
    model = model,
    year = year,
    plate = plate,
    pricePerDay = pricePerDay.toDoubleOrNull() ?: 0.0,
    available = available,
    categoryId = category
)

fun Vehicle.toDto(): VehicleDto = VehicleDto(
    id = id,
    brand = brand,
    model = model,
    year = year,
    plate = plate,
    pricePerDay = pricePerDay.toString(),
    available = available,
    category = categoryId
)

fun ReservationDto.toDomain(): Reservation = Reservation(
    id = id,
    userId = user,
    vehicleId = vehicle,
    startDate = startDate,
    endDate = endDate,
    totalPrice = totalPrice.toDoubleOrNull() ?: 0.0,
    status = status,
    createdAt = createdAt ?: ""
)

fun Reservation.toDto(): ReservationDto = ReservationDto(
    id = id,
    user = userId,
    vehicle = vehicleId,
    startDate = startDate,
    endDate = endDate,
    totalPrice = totalPrice.toString(),
    status = status
)

fun ReviewDto.toDomain(): Review = Review(
    id = id,
    vehicleId = vehicle,
    userId = user,
    rating = rating,
    comment = comment,
    createdAt = createdAt
)

fun Review.toDto(): ReviewDto = ReviewDto(
    id = id,
    vehicle = vehicleId,
    user = userId,
    rating = rating,
    comment = comment
)

fun PaymentDto.toDomain(): Payment = Payment(
    id = id,
    reservationId = reservation,
    amount = amount.toDoubleOrNull() ?: 0.0,
    paymentMethod = paymentMethod,
    status = status,
    transactionId = transactionId,
    createdAt = createdAt
)

fun Payment.toDto(): PaymentDto = PaymentDto(
    id = id,
    reservation = reservationId,
    amount = amount.toString(),
    paymentMethod = paymentMethod,
    status = status,
    transactionId = transactionId
)
