package com.adyen.android.assignment

import com.adyen.android.assignment.money.Bill
import com.adyen.android.assignment.money.Change
import com.adyen.android.assignment.money.Coin
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CashRegisterTest {

    @Test
    fun `should have initial value`() {
        val expectedChangeValue = Change().add(Coin.TWO_EURO, 1)

        val cashRegister = CashRegister(expectedChangeValue)
        cashRegister.performTransaction(0L, Change())

        assertEquals(expectedChangeValue, cashRegister.total)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should throw TransactionException when price is negative`() {
        val cashRegister = CashRegister(Change().add(Coin.TWO_EURO, 1))

        cashRegister.performTransaction(-1L, Change())
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should not make a transaction if the change is less than product price`() {
        val cashRegister = CashRegister(Change().add(Coin.TWO_EURO, 1))

        cashRegister.performTransaction(10L, Change().add(Coin.ONE_CENT, 1))
    }

    @Test
    fun `should not return change when money paid is same as price`() {
        val noChange = Change()

        val cashRegister = CashRegister(Change().add(Bill.ONE_HUNDRED_EURO, 1))
        val transactionResult =
            cashRegister.performTransaction(10_00, Change().add(Bill.TEN_EURO, 1))
        assertEquals(noChange, transactionResult)
    }

    @Test
    fun `should return change`() {
        val noChangeForTransaction1 = Change()
        val oneEuroChangeForTransaction2 = Change().add(Coin.ONE_EURO, 1)
        val fiveEuroChangeForTransaction3 = Change().add(Bill.FIVE_EURO, 1)
        val fiftyCentChangeForTransaction4 = Change().add(Coin.FIFTY_CENT, 1)

        val cashRegister = CashRegister(Change().add(Bill.ONE_HUNDRED_EURO, 1))

        val transaction1 = cashRegister.performTransaction(10_00, Change().add(Bill.TEN_EURO, 1))
        val transaction2 = cashRegister.performTransaction(1_00L, Change().add(Coin.ONE_EURO, 2))
        val transaction3 = cashRegister.performTransaction(5_00L, Change().add(Bill.TEN_EURO, 1))
        val transaction4 = cashRegister.performTransaction(50L, Change().add(Coin.ONE_EURO, 1))

        assertEquals(noChangeForTransaction1, transaction1)
        assertEquals(oneEuroChangeForTransaction2, transaction2)
        assertEquals(fiveEuroChangeForTransaction3, transaction3)
        assertEquals(fiftyCentChangeForTransaction4, transaction4)
    }

    @Test(expected = CashRegister.TransactionException::class)
    fun `should not make a transaction when there is no enough change`() {
        fail()
    }
}
