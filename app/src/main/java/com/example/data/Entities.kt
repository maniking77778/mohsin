package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String = "",
    val imei: String = "",
    val deviceModel: String = "",
    val visitCount: Int = 1,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "repair_records")
data class RepairRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerId: Long,
    val customerName: String,
    val customerPhone: String,
    val deviceImei: String,
    val deviceModel: String,
    val faultType: String,
    val issueDescription: String,
    val repairCost: Double,
    val status: String, // "Pending", "In Progress", "Completed", "Delivered"
    val invoiceId: String,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "imei_history")
data class ImeiHistory(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imei: String,
    val brand: String = "",
    val model: String = "",
    val isValid: Boolean = true,
    val status: String = "No Records found", // e.g., "Clean (Active)", "Blacklisted"
    val ptaStatus: String = "Approved (Tax Paid)", // "Approved", "Unapproved / Tax Due", "Blocked"
    val taxAmount: Double = 0.0,
    val deviceDetails: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
