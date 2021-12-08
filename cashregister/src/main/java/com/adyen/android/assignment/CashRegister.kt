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

    private val availableMonetaryUnits =
        Bill.values().map { it as MonetaryElement } union Coin.values()
            .map { it as MonetaryElement }

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

        when {
            price < 0 -> throw TransactionException(
                "Price could not be negative",
                IllegalArgumentException()
            )
            price > amountPaid.total -> throw TransactionException(
                "Amount Paid should be greater or equal the price",
                IllegalArgumentException()
            )
        }

        val totalChangeToBeReturned = amountPaid.total - price
        val changeToBeReturnedInMonetaryElements =
            transformPriceInMonetaryElements(totalChangeToBeReturned, availableMonetaryUnits)

        if (haveChangeToBeReturned(amountPaid, changeToBeReturnedInMonetaryElements).not())
            throw TransactionException(
                "There is no enough change for transaction",
                IllegalArgumentException()
            )

        updateCashRegister(amountPaid, changeToBeReturnedInMonetaryElements)

        return changeToBeReturnedInMonetaryElements
    }

    private fun transformPriceInMonetaryElements(
        price: Long,
        monetaryElements: Set<MonetaryElement>
    ): Change {
        var temporaryPriceForTransaction = price
        val change = Change.none()

        monetaryElements.sortedWith(descendingMonetaryElementComparator())
            .forEach { monetaryElement ->
                while ((temporaryPriceForTransaction / monetaryElement.minorValue) != 0L) {
                    temporaryPriceForTransaction -= monetaryElement.minorValue
                    change.add(monetaryElement, 1)
                }
            }
        return change
    }

    private fun descendingMonetaryElementComparator() =
        compareBy<MonetaryElement> { -it.minorValue }

    private fun haveChangeToBeReturned(
        amountPaid: Change,
        changeToBeReturned: Change
    ): Boolean {
        val totalInCashRegisterWithAmountPaid = Change.none()

        (amountPaid.getElements()).forEach { monetaryElement ->
            totalInCashRegisterWithAmountPaid.add(
                monetaryElement,
                amountPaid.getCount(monetaryElement)
            )
        }

        (total.getElements()).forEach { monetaryElement ->
            totalInCashRegisterWithAmountPaid.add(
                monetaryElement,
                total.getCount(monetaryElement)
            )
        }

        changeToBeReturned.getElements().forEach { changeMonetaryElement ->
            if (totalInCashRegisterWithAmountPaid.getCount(changeMonetaryElement) < changeToBeReturned.getCount(
                    changeMonetaryElement
                )
            )
                return false
        }

        return true
    }

    private fun updateCashRegister(
        amountPaid: Change,
        changeToBeReturnedInMonetaryElements: Change
    ) {
        amountPaid.getElements().forEach { monetaryElement ->
            total.add(monetaryElement, amountPaid.getCount(monetaryElement))
        }

        changeToBeReturnedInMonetaryElements.getElements().forEach { monetaryElement ->
            total.remove(
                monetaryElement,
                changeToBeReturnedInMonetaryElements.getCount(monetaryElement)
            )
        }
    }

    class TransactionException(message: String, cause: Throwable? = null) :
        Exception(message, cause)
}