package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val customerDao: CustomerDao,
    private val repairDao: RepairDao,
    private val imeiHistoryDao: ImeiHistoryDao
) {
    // Customers
    val allCustomers: Flow<List<Customer>> = customerDao.getAllCustomers()

    fun searchCustomers(query: String): Flow<List<Customer>> {
        return customerDao.searchCustomers("%$query%")
    }

    suspend fun insertCustomer(customer: Customer): Long {
        return customerDao.insertCustomer(customer)
    }

    suspend fun updateCustomer(customer: Customer) {
        customerDao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun getCustomerById(id: Long): Customer? {
        return customerDao.getCustomerById(id)
    }

    // Repairs
    val allRepairs: Flow<List<RepairRecord>> = repairDao.getAllRepairs()

    fun searchRepairs(query: String): Flow<List<RepairRecord>> {
        return repairDao.searchRepairs("%$query%")
    }

    suspend fun insertRepair(record: RepairRecord): Long {
        return repairDao.insertRepair(record)
    }

    suspend fun updateRepair(record: RepairRecord) {
        repairDao.updateRepair(record)
    }

    suspend fun deleteRepair(record: RepairRecord) {
        repairDao.deleteRepair(record)
    }

    suspend fun getRepairById(id: Long): RepairRecord? {
        return repairDao.getRepairById(id)
    }

    fun getRepairsForCustomer(customerId: Long): Flow<List<RepairRecord>> {
        return repairDao.getRepairsForCustomer(customerId)
    }

    // IMEI History
    val allImeiHistory: Flow<List<ImeiHistory>> = imeiHistoryDao.getAllImeiHistory()

    suspend fun insertImeiHistory(history: ImeiHistory) {
        imeiHistoryDao.insertImeiHistory(history)
    }

    suspend fun clearImeiHistory() {
        imeiHistoryDao.clearHistory()
    }
}
