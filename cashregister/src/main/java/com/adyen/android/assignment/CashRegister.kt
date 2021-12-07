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

        val totalChangeToBeReturned = amountPaid.total - price
        val changeToBeReturned = getMoney(totalChangeToBeReturned)

        if (haveChangeToBeReturned(amountPaid, changeToBeReturned).not())
            throw TransactionException(
                "There is no enough change for transaction",
                IllegalArgumentException()
            )

        amountPaid.getElements().forEach { monetaryElement ->
            total.add(monetaryElement, amountPaid.getCount(monetaryElement))
        }

        changeToBeReturned.getElements().forEach { monetaryElement ->
            total.remove(monetaryElement, changeToBeReturned.getCount(monetaryElement))
        }

        return changeToBeReturned
    }

    private fun haveChangeToBeReturned(
        amountPaid: Change,
        changeToBeReturned: Change
    ): Boolean {
        var haveWeEnoughChange = true
        val currentMonetaryElementsWithAmountPaid = Change()

        (amountPaid.getElements()).forEach { monetaryElement ->
            currentMonetaryElementsWithAmountPaid.add(
                monetaryElement,
                amountPaid.getCount(monetaryElement)
            )
        }

        (total.getElements()).forEach { monetaryElement ->
            currentMonetaryElementsWithAmountPaid.add(
                monetaryElement,
                total.getCount(monetaryElement)
            )
        }

        changeToBeReturned.getElements().forEach { changeMonetaryElement ->
            if (currentMonetaryElementsWithAmountPaid.getCount(changeMonetaryElement) < changeToBeReturned.getCount(
                    changeMonetaryElement
                )
            )
                haveWeEnoughChange = false
        }
        return haveWeEnoughChange
    }

    private fun getMoney(price: Long): Change {
        var temporaryPriceForTransaction = price
        val change = Change()

        val monetaryElements: Set<MonetaryElement> =
            Bill.values().map { it as MonetaryElement } union Coin.values()
                .map { it as MonetaryElement }

        monetaryElements.sortedWith(descendingMonetaryElementComparator()).forEach { monetaryElement ->
            while ((temporaryPriceForTransaction / monetaryElement.minorValue) != 0L) {
                temporaryPriceForTransaction -= monetaryElement.minorValue
                change.add(monetaryElement, 1)
            }
        }
        return change
    }

    private fun descendingMonetaryElementComparator() =
        compareBy<MonetaryElement> { -it.minorValue }

    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}