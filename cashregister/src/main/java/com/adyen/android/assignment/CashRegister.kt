package com.adyen.android.assignment

import com.adyen.android.assignment.money.*

/**
 * The CashRegister class holds the logic for performing transactions.
 *
 * @param change The change that the CashRegister is holding.
 */
class CashRegister(private val change: Change) {

    var total = Change()
        private set

    init {
        total = change
    }

    /**
     * Performs a transaction for a product/products with a certain price and a given amount.
     *
     * @param price The price of the product(s).
     * @param amountPaid The amount paid by the shopper.
     *
     * @return The change for the transaction.
     *
     * @throws TransactionException If the transaction cannot be performed.
     */
    fun performTransaction(price: Long, amountPaid: Change): Change {
        if (price < 0)
            throw TransactionException("Price could not be negative", IllegalArgumentException())

        if (price > amountPaid.total)
            throw TransactionException("Price could not be negative", IllegalArgumentException())

        if (price == amountPaid.total)
            return Change()

        var temporaryPriceForTransaction = price
        val change = Change()

        val monetaryElements: Set<MonetaryElement> =
            Bill.values().map { it as MonetaryElement } union Coin.values()
                .map { it as MonetaryElement }

        monetaryElements.sortedWith(descendingComparator()).forEach { bill ->
            while ((temporaryPriceForTransaction / bill.minorValue) != 0L) {
                temporaryPriceForTransaction -= bill.minorValue
                change.add(bill, 1)
            }
        }

        return change
    }

    private fun descendingComparator() = compareBy<MonetaryElement> { -it.minorValue }

    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}
