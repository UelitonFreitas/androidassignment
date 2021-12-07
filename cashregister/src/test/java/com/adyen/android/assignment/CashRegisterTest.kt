package com.adyen.android.assignment

import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Test

class CashRegisterTest {
    @Test
    fun `should have initial value`() {
        val expectedChangeValue = Change().add(Coin.TWO_EURO, 1)

        val cashRegister = CashRegister(expectedChangeValue)

        assertEquals(expectedChangeValue, cashRegister.performTransaction(0L, Change()))
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should throw TransactionException when price is negative `() {
        val cashRegister = CashRegister(Change().add(Coin.TWO_EURO, 1))

        cashRegister.performTransaction(-1L, Change())
    }


    @Test(expected = CashRegister.TransactionException::class)
    fun `should not make a transaction if the change is less than product price`() {
        val cashRegister = CashRegister(Change().add(Coin.TWO_EURO, 1))

        cashRegister.performTransaction(10L, Change().add(Coin.ONE_CENT, 1))
    }
}
